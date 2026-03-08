package cc.unitmesh.agent.artifact

import cc.unitmesh.agent.logging.AutoDevLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Packages a Python artifact into a self-contained executable or ready-to-run bundle.
 *
 * Supports two strategies:
 * 1. **UV (preferred)** – uses `uv run` for zero-config execution with PEP 723 metadata.
 * 2. **pip fallback** – generates a `requirements.txt` and calls `pip install`.
 *
 * The packager can also embed AutoDev Unit context inside the PEP 723 metadata block
 * so that the script remains fully reversible (load-back support).
 */
class PythonArtifactPackager {

    private val logger = AutoDevLogger

    /**
     * Result of a packaging operation.
     */
    sealed class PackageResult {
        data class Success(
            val scriptFile: File,
            val strategy: Strategy,
            val message: String
        ) : PackageResult()

        data class Error(
            val message: String,
            val cause: Exception? = null
        ) : PackageResult()
    }

    enum class Strategy { UV, PIP }

    /**
     * Package a Python script with dependencies resolved.
     *
     * 1. Injects AutoDev context into the PEP 723 header.
     * 2. Writes the enriched script to [outputDir].
     * 3. Attempts to install dependencies via `uv` or falls back to `pip`.
     *
     * @param scriptContent   Raw Python script (may or may not contain PEP 723 block).
     * @param context         AutoDev artifact context to embed.
     * @param outputDir       Directory where the enriched script and artifacts are written.
     * @param onOutput        Optional callback for streaming console output.
     * @return [PackageResult] indicating success or error.
     */
    suspend fun packageScript(
        scriptContent: String,
        context: ArtifactContext,
        outputDir: File,
        onOutput: ((String) -> Unit)? = null
    ): PackageResult = withContext(Dispatchers.IO) {
        try {
            // 1. Parse existing metadata (if any)
            val existingMeta = PEP723Parser.parse(scriptContent)
            val dependencies = existingMeta.dependencies

            // 2. Build AutoDev context map
            val autodevMap = buildMap {
                put("version", "1.0")
                put("fingerprint", context.fingerprint)
                context.model?.let { put("model", it.name) }
            }

            // 3. Inject / replace PEP 723 header
            val enrichedScript = PEP723Parser.injectMetadata(
                pythonContent = scriptContent,
                dependencies = dependencies,
                requiresPython = existingMeta.requiresPython ?: ">=3.11",
                autodevContext = autodevMap
            )

            // 4. Write enriched script
            outputDir.mkdirs()
            val scriptFile = File(outputDir, "index.py")
            scriptFile.writeText(enrichedScript)
            logger.info("PythonArtifactPackager") { "📝 Enriched script written to ${scriptFile.absolutePath}" }

            // 5. Write requirements.txt (for pip fallback)
            if (dependencies.isNotEmpty()) {
                val reqFile = File(outputDir, "requirements.txt")
                reqFile.writeText(dependencies.joinToString("\n"))
            }

            // 6. Resolve dependencies
            val strategy = resolveDependencies(outputDir, dependencies, onOutput)

            PackageResult.Success(
                scriptFile = scriptFile,
                strategy = strategy,
                message = "Python script packaged successfully with $strategy strategy."
            )
        } catch (e: Exception) {
            logger.error("PythonArtifactPackager") { "❌ Packaging failed: ${e.message}" }
            PackageResult.Error("Packaging failed: ${e.message}", e)
        }
    }

    // ---- internals ----

    /**
     * Try to resolve dependencies: prefer `uv`, fall back to `pip`.
     */
    private suspend fun resolveDependencies(
        workDir: File,
        dependencies: List<String>,
        onOutput: ((String) -> Unit)?
    ): Strategy = withContext(Dispatchers.IO) {
        if (dependencies.isEmpty()) {
            onOutput?.invoke("No dependencies to install.\n")
            return@withContext Strategy.UV // doesn't matter
        }

        // Try uv first
        if (isCommandAvailable("uv")) {
            onOutput?.invoke("📦 Installing dependencies via uv...\n")
            val args = listOf("uv", "pip", "install") + dependencies
            val result = executeCommandArray(args, workDir.absolutePath, onOutput)
            if (result.exitCode == 0) {
                onOutput?.invoke("✅ Dependencies installed via uv.\n")
                return@withContext Strategy.UV
            }
            onOutput?.invoke("⚠️ uv install failed, falling back to pip.\n")
        }

        // Fallback: pip
        onOutput?.invoke("📦 Installing dependencies via pip...\n")
        val pipResult = executeCommand("pip install -r requirements.txt", workDir.absolutePath, onOutput)
        if (pipResult.exitCode != 0) {
            onOutput?.invoke("⚠️ pip install failed (exit code ${pipResult.exitCode}). Continuing anyway.\n")
        } else {
            onOutput?.invoke("✅ Dependencies installed via pip.\n")
        }
        Strategy.PIP
    }

    private suspend fun isCommandAvailable(cmd: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val isWindows = System.getProperty("os.name").lowercase().contains("win")
            val checkCmd = if (isWindows) listOf("where", cmd) else listOf("which", cmd)
            val process = ProcessBuilder(checkCmd)
                .redirectErrorStream(true)
                .start()
            process.waitFor() == 0
        } catch (_: Exception) {
            false
        }
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
                CommandResult(exitCode, outputBuilder.toString(), "")
            }
        } catch (e: Exception) {
            CommandResult(-1, "", "Error: ${e.message}")
        }
    }

    private suspend fun executeCommandArray(
        command: List<String>,
        workingDirectory: String,
        onOutput: ((String) -> Unit)? = null
    ): CommandResult = withContext(Dispatchers.IO) {
        try {
            val processBuilder = ProcessBuilder()
                .command(command)
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
                CommandResult(exitCode, outputBuilder.toString(), "")
            }
        } catch (e: Exception) {
            CommandResult(-1, "", "Error: ${e.message}")
        }
    }

    private data class CommandResult(val exitCode: Int, val stdout: String, val stderr: String)
}
