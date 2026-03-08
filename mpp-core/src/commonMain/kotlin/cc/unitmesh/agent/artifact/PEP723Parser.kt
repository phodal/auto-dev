package cc.unitmesh.agent.artifact

/**
 * PEP 723 Inline Script Metadata Parser & Generator.
 *
 * Parses and generates PEP 723 compliant inline metadata blocks in Python scripts.
 *
 * PEP 723 format example:
 * ```python
 * # /// script
 * # requires-python = ">=3.11"
 * # dependencies = [
 * #   "requests>=2.28.0",
 * #   "pandas>=1.5.0",
 * # ]
 * # ///
 * ```
 *
 * @see <a href="https://peps.python.org/pep-0723/">PEP 723</a>
 */
object PEP723Parser {

    /**
     * Parsed result from a PEP 723 metadata block.
     */
    data class PEP723Metadata(
        /** Required Python version constraint, e.g. ">=3.11" */
        val requiresPython: String? = null,
        /** List of dependency specifiers, e.g. ["requests>=2.28.0", "pandas>=1.5.0"] */
        val dependencies: List<String> = emptyList(),
        /** AutoDev Unit custom metadata embedded in [tool.autodev-unit] */
        val autodevContext: Map<String, String> = emptyMap(),
        /** The raw text of the entire metadata block (including comment prefixes) */
        val rawBlock: String? = null
    )

    // ---- Parsing ----

    private val PEP723_BLOCK_PATTERN = Regex(
        """(?s)#\s*///\s*script\s*\n(.*?)#\s*///"""
    )

    private val REQUIRES_PYTHON_PATTERN = Regex(
        """requires-python\s*=\s*"([^"]*)""""
    )

    private val DEPENDENCIES_PATTERN = Regex(
        """(?s)dependencies\s*=\s*\[(.*?)\]"""
    )

    private val DEP_ITEM_PATTERN = Regex("""["']([^"']+)["']""")

    private val AUTODEV_SECTION_PATTERN = Regex(
        """(?s)\[tool\.autodev-unit\]\s*\n(.*?)(?=#\s*///|\[tool\.|$)"""
    )

    private val AUTODEV_KV_PATTERN = Regex(
        """#\s*(\S+)\s*=\s*"([^"]*)""""
    )

    /**
     * Parse PEP 723 inline metadata from a Python script.
     *
     * @param pythonContent Full text of the Python script.
     * @return Parsed metadata, or a default empty metadata if no block is found.
     */
    fun parse(pythonContent: String): PEP723Metadata {
        val blockMatch = PEP723_BLOCK_PATTERN.find(pythonContent)
            ?: return PEP723Metadata()

        val metadataBlock = blockMatch.groupValues[1]
        val rawBlock = blockMatch.value

        // Parse requires-python
        val requiresPython = REQUIRES_PYTHON_PATTERN.find(metadataBlock)?.groupValues?.get(1)

        // Parse dependencies
        val dependencies = parseDependencies(metadataBlock)

        // Parse [tool.autodev-unit] section
        val autodevContext = parseAutodevContext(metadataBlock)

        return PEP723Metadata(
            requiresPython = requiresPython,
            dependencies = dependencies,
            autodevContext = autodevContext,
            rawBlock = rawBlock
        )
    }

    /**
     * Extract only the dependency list from a Python script (convenience method).
     */
    fun parseDependencies(pythonContent: String): List<String> {
        val depsMatch = DEPENDENCIES_PATTERN.find(pythonContent) ?: return emptyList()
        val depsContent = depsMatch.groupValues[1]

        return DEP_ITEM_PATTERN.findAll(depsContent)
            .map { it.groupValues[1] }
            .toList()
    }

    private fun parseAutodevContext(metadataBlock: String): Map<String, String> {
        val sectionMatch = AUTODEV_SECTION_PATTERN.find(metadataBlock)
            ?: return emptyMap()

        val sectionContent = sectionMatch.groupValues[1]
        return AUTODEV_KV_PATTERN.findAll(sectionContent)
            .associate { it.groupValues[1] to it.groupValues[2] }
    }

    // ---- Generation ----

    /**
     * Generate a PEP 723 metadata header block.
     *
     * @param dependencies List of dependency specifiers (e.g. "requests>=2.28.0").
     * @param requiresPython Python version constraint (default ">=3.11").
     * @param autodevContext Optional AutoDev Unit context key-value pairs to embed.
     * @return The generated metadata block as a string, ready to prepend to a script.
     */
    fun generate(
        dependencies: List<String> = emptyList(),
        requiresPython: String = ">=3.11",
        autodevContext: Map<String, String> = emptyMap()
    ): String = buildString {
        appendLine("# /// script")
        appendLine("# requires-python = \"$requiresPython\"")

        if (dependencies.isNotEmpty()) {
            appendLine("# dependencies = [")
            dependencies.forEach { dep ->
                appendLine("#   \"$dep\",")
            }
            appendLine("# ]")
        }

        if (autodevContext.isNotEmpty()) {
            appendLine("# [tool.autodev-unit]")
            autodevContext.forEach { (key, value) ->
                appendLine("# $key = \"$value\"")
            }
        }

        appendLine("# ///")
    }

    /**
     * Inject or replace a PEP 723 metadata block in a Python script.
     *
     * If the script already contains a PEP 723 block, it is replaced.
     * Otherwise the new block is prepended.
     *
     * @param pythonContent The original script content.
     * @param dependencies Dependency list.
     * @param requiresPython Python version constraint.
     * @param autodevContext Optional AutoDev context map.
     * @return The script with the metadata block injected/replaced.
     */
    fun injectMetadata(
        pythonContent: String,
        dependencies: List<String> = emptyList(),
        requiresPython: String = ">=3.11",
        autodevContext: Map<String, String> = emptyMap()
    ): String {
        val newBlock = generate(dependencies, requiresPython, autodevContext)

        return if (PEP723_BLOCK_PATTERN.containsMatchIn(pythonContent)) {
            PEP723_BLOCK_PATTERN.replace(pythonContent, newBlock.trimEnd())
        } else {
            newBlock + "\n" + pythonContent
        }
    }

    /**
     * Strip the PEP 723 metadata block from a Python script, returning only the code body.
     */
    fun stripMetadata(pythonContent: String): String {
        return PEP723_BLOCK_PATTERN.replace(pythonContent, "").trimStart('\r', '\n')
    }
}
