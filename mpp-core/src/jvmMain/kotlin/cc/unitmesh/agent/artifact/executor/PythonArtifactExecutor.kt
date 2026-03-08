package cc.unitmesh.agent.artifact.executor

import cc.unitmesh.agent.artifact.ArtifactType
import cc.unitmesh.agent.artifact.PEP723Parser
import cc.unitmesh.agent.logging.AutoDevLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Executor for Python artifacts
 * 
 * Handles:
 * - PEP 723 inline metadata parsing for dependencies
 * - pip install for dependencies
 * - python index.py execution
 */
class PythonArtifactExecutor : ArtifactExecutor {
    private val logger = AutoDevLogger

    override val supportedTypes: Set<ArtifactType> = setOf(ArtifactType.PYTHON)

    override suspend fun validate(extractDir: File, bundleType: ArtifactType): ValidationResult = withContext(Dispatchers.IO) {
        val errors = mutableListOf<String>()

        val mainFile = File(extractDir, "index.py")
        if (!mainFile.exists()) {
            errors.add("index.py not found")
        } else {
            // Verify it's Python code
            val content = mainFile.readText()
            if (content.trim().startsWith("{") && content.contains("\"name\"")) {
                errors.add("index.py contains invalid content (appears to be JSON)")
            }
        }

        if (errors.isEmpty()) {
            ValidationResult.Valid()
        } else {
            ValidationResult.Invalid(errors)
        }
    }

    override suspend fun execute(
        extractDir: File,
        bundleType: ArtifactType,
        onOutput: ((String) -> Unit)?
    ): ExecutionResult = withContext(Dispatchers.IO) {
        try {
            logger.info("PythonArtifactExecutor") { "🚀 Executing Python artifact in: ${extractDir.absolutePath}" }

            // Validate first
            when (val validation = validate(extractDir, bundleType)) {
                is ValidationResult.Invalid -> {
                    return@withContext ExecutionResult.Error("Validation failed: ${validation.errors.joinToString(", ")}")
                }
                is ValidationResult.Valid -> {
                    logger.info("PythonArtifactExecutor") { "✅ Validation passed" }
                }
            }

            // Step 1: Parse PEP 723 metadata and install dependencies
            val mainFile = File(extractDir, "index.py")
            val pythonContent = mainFile.readText()
            val dependencies = parsePep723Dependencies(pythonContent)

            if (dependencies.isNotEmpty()) {
                logger.info("PythonArtifactExecutor") { "📦 Installing dependencies: $dependencies" }
                onOutput?.invoke("Installing dependencies: ${dependencies.joinToString(", ")}...\n")

                // Create requirements.txt if needed
                val requirementsFile = File(extractDir, "requirements.txt")
                if (!requirementsFile.exists()) {
                    requirementsFile.writeText(dependencies.joinToString("\n"))
                }

                val installResult = executeCommand(
                    command = "pip install -r requirements.txt",
                    workingDirectory = extractDir.absolutePath,
                    onOutput = onOutput
                )

                if (installResult.exitCode != 0) {
                    logger.warn("PythonArtifactExecutor") { "⚠️ pip install failed with exit code ${installResult.exitCode}" }
                    onOutput?.invoke("Warning: pip install failed. Continuing anyway...\n")
                } else {
                    logger.info("PythonArtifactExecutor") { "✅ Dependencies installed" }
                    onOutput?.invoke("Dependencies installed successfully.\n")
                }
            } else {
                logger.info("PythonArtifactExecutor") { "ℹ️ No dependencies to install" }
                onOutput?.invoke("No dependencies to install.\n")
            }

            // Step 2: Execute the Python script
            logger.info("PythonArtifactExecutor") { "▶️ Executing: python index.py" }
            onOutput?.invoke("Starting Python script...\n")
            onOutput?.invoke("=".repeat(50) + "\n")

            val executeResult = executeCommand(
                command = "python3 index.py",
                workingDirectory = extractDir.absolutePath,
                onOutput = onOutput
            )

            val output = if (executeResult.exitCode == 0) {
                "Script executed successfully.\n${executeResult.stdout}"
            } else {
                "Script exited with code ${executeResult.exitCode}.\n${executeResult.stdout}\n${executeResult.stderr}"
            }

            ExecutionResult.Success(
                output = output,
                workingDirectory = extractDir.absolutePath
            )
        } catch (e: Exception) {
            logger.error("PythonArtifactExecutor") { "❌ Execution failed: ${e.message}" }
            ExecutionResult.Error("Execution failed: ${e.message}", e)
        }
    }

    /**
     * Parse PEP 723 inline metadata from Python script.
     * Delegates to the shared [PEP723Parser].
     */
    private fun parsePep723Dependencies(pythonContent: String): List<String> {
        return PEP723Parser.parseDependencies(pythonContent)
    }

    private suspend fun executeCommand(
        command: String,
        workingDirectory: String,
        onOutput: ((String) -> Unit)? = null
    ): CommandResult = withContext(Dispatchers.IO) {
        try {
            val processBuilder = ProcessBuilder()
                .command("sh", "-c", command)
                .directory(File(workingDirectory))
                .redirectErrorStream(true)

            val process = processBuilder.start()
            val outputBuilder = StringBuilder()

            coroutineScope {
                val outputJob = launch(Dispatchers.IO) {
                    process.inputStream.bufferedReader().use { reader ->
                        reader.lineSequence().forEach { line ->
                            outputBuilder.appendLine(line)
                            onOutput?.invoke("$line\n")
                        }
                    }
                }

                val exitCode = process.waitFor()
                outputJob.join()

                CommandResult(
                    exitCode = exitCode,
                    stdout = outputBuilder.toString(),
                    stderr = ""
                )
            }
        } catch (e: Exception) {
            CommandResult(
                exitCode = -1,
                stdout = "",
                stderr = "Error executing command: ${e.message}"
            )
        }
    }

    private data class CommandResult(
        val exitCode: Int,
        val stdout: String,
        val stderr: String
    )
}

