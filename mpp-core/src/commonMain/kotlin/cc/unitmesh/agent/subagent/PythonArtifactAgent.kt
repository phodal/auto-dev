package cc.unitmesh.agent.subagent

import cc.unitmesh.agent.artifact.ArtifactContext
import cc.unitmesh.agent.artifact.ConversationMessage
import cc.unitmesh.agent.artifact.ModelInfo
import cc.unitmesh.agent.artifact.PEP723Parser
import cc.unitmesh.agent.core.SubAgent
import cc.unitmesh.agent.model.AgentDefinition
import cc.unitmesh.agent.model.PromptConfig
import cc.unitmesh.agent.model.RunConfig
import cc.unitmesh.agent.tool.ToolResult
import cc.unitmesh.llm.LLMService
import cc.unitmesh.devins.llm.Message
import cc.unitmesh.devins.llm.MessageRole
import cc.unitmesh.llm.ModelConfig
import kotlinx.serialization.Serializable

/**
 * PythonArtifactAgent – Sub-agent responsible for generating
 * complete, self-contained Python scripts with PEP 723 inline metadata.
 *
 * The generated scripts follow the AutoDev Artifact convention and include
 * dependency declarations so that they can be executed with `uv run` or
 * after a simple `pip install`.
 *
 * @see <a href="https://github.com/phodal/auto-dev/issues/526">Issue #526</a>
 */
class PythonArtifactAgent(
    private val llmService: LLMService
) : SubAgent<PythonArtifactInput, ToolResult.AgentResult>(
    AgentDefinition(
        name = "PythonArtifactAgent",
        displayName = "Python Artifact Agent",
        description = "Generates self-contained Python scripts with PEP 723 metadata for the AutoDev Unit system",
        promptConfig = PromptConfig(
            systemPrompt = SYSTEM_PROMPT,
            queryTemplate = null,
            initialMessages = emptyList()
        ),
        modelConfig = ModelConfig.default(),
        runConfig = RunConfig(
            maxTurns = 1,
            maxTimeMinutes = 5,
            terminateOnError = true
        )
    )
) {

    override fun validateInput(input: Map<String, Any>): PythonArtifactInput {
        val prompt = input["prompt"] as? String
            ?: throw IllegalArgumentException("'prompt' is required")
        val dependencies = (input["dependencies"] as? List<*>)
            ?.filterIsInstance<String>()
            ?: emptyList()

        return PythonArtifactInput(
            prompt = prompt,
            dependencies = dependencies,
            requiresPython = input["requiresPython"] as? String ?: ">=3.11"
        )
    }

    override suspend fun execute(
        input: PythonArtifactInput,
        onProgress: (String) -> Unit
    ): ToolResult.AgentResult {
        onProgress("[Python] Generating script...")

        val responseBuilder = StringBuilder()

        val historyMessages = listOf(
            Message(role = MessageRole.SYSTEM, content = SYSTEM_PROMPT)
        )

        return try {
            llmService.streamPrompt(
                userPrompt = buildUserPrompt(input),
                historyMessages = historyMessages,
                compileDevIns = false
            ).collect { chunk ->
                responseBuilder.append(chunk)
                onProgress(chunk)
            }

            val rawResponse = responseBuilder.toString()
            val scriptContent = extractPythonCode(rawResponse)

            if (scriptContent.isNullOrBlank()) {
                return ToolResult.AgentResult(
                    success = false,
                    content = "Failed to extract Python code from LLM response."
                )
            }

            // Validate PEP 723 metadata is present; inject if missing
            val meta = PEP723Parser.parse(scriptContent)
            val finalScript = if (meta.rawBlock == null) {
                PEP723Parser.injectMetadata(
                    pythonContent = scriptContent,
                    dependencies = input.dependencies,
                    requiresPython = input.requiresPython
                )
            } else {
                scriptContent
            }

            onProgress("\n[OK] Python script generated successfully.")

            ToolResult.AgentResult(
                success = true,
                content = finalScript,
                metadata = mapOf(
                    "type" to "python",
                    "dependencies" to PEP723Parser.parseDependencies(finalScript).joinToString(","),
                    "requiresPython" to (PEP723Parser.parse(finalScript).requiresPython ?: ">=3.11")
                )
            )
        } catch (e: Exception) {
            ToolResult.AgentResult(
                success = false,
                content = "Generation failed: ${e.message}"
            )
        }
    }

    override fun formatOutput(output: ToolResult.AgentResult): String = output.content

    // ---- helpers ----

    private fun buildUserPrompt(input: PythonArtifactInput): String = buildString {
        appendLine(input.prompt)
        if (input.dependencies.isNotEmpty()) {
            appendLine()
            appendLine("Required dependencies: ${input.dependencies.joinToString(", ")}")
        }
    }

    /**
     * Extract the Python code block from an LLM response.
     * Supports fenced code blocks (```python ... ```) and raw artifact XML.
     */
    private fun extractPythonCode(response: String): String? {
        // Try autodev-artifact XML tag first
        val artifactPattern = Regex(
            """(?s)<autodev-artifact[^>]*type="application/autodev\.artifacts\.python"[^>]*>(.*?)</autodev-artifact>"""
        )
        artifactPattern.find(response)?.let { return it.groupValues[1].trim() }

        // Try fenced python code block
        val fencedPattern = Regex(
            """(?s)```python\s*\n(.*?)```"""
        )
        fencedPattern.find(response)?.let { return it.groupValues[1].trim() }

        // Fallback: if the whole response looks like Python code
        if (response.trimStart().startsWith("#") || response.trimStart().startsWith("import ") || response.trimStart().startsWith("from ")) {
            return response.trim()
        }

        return null
    }

    companion object {
        /**
         * System prompt guiding the LLM to generate PEP 723 compliant Python scripts.
         */
        const val SYSTEM_PROMPT = """You are an expert Python developer specializing in creating self-contained, executable Python scripts.

## Rules

1. **PEP 723 Metadata** – Every script MUST begin with an inline metadata block:
```python
# /// script
# requires-python = ">=3.11"
# dependencies = [
#   "some-package>=1.0",
# ]
# ///
```

2. **Self-Contained** – The script must run independently. All logic resides in a single file.

3. **Main Guard** – Always include:
```python
if __name__ == "__main__":
    main()
```

4. **Clear Output** – Use `print()` to provide meaningful output to stdout.

5. **Error Handling** – Include basic try/except blocks for I/O, network, or file operations.

6. **No External Config** – Avoid reading from external config files. Use environment variables via `os.environ.get()` when necessary.

7. **Output Format** – Wrap the script in `<autodev-artifact identifier="..." type="application/autodev.artifacts.python" title="...">` tags.
"""
    }
}

/**
 * Input for PythonArtifactAgent
 */
@Serializable
data class PythonArtifactInput(
    /** Natural-language description of what the script should do */
    val prompt: String,
    /** Pre-declared dependencies (may be empty) */
    val dependencies: List<String> = emptyList(),
    /** Python version constraint */
    val requiresPython: String = ">=3.11"
)
