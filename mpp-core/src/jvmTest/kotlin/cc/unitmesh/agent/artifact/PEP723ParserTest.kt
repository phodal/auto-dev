package cc.unitmesh.agent.artifact

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PEP723ParserTest {

    // ===== parse() tests =====

    @Test
    fun `parse should extract requires-python`() {
        val script = """
            # /// script
            # requires-python = ">=3.11"
            # dependencies = [
            #   "requests>=2.28.0",
            # ]
            # ///
            
            import requests
            print("hello")
        """.trimIndent()

        val meta = PEP723Parser.parse(script)

        assertEquals(">=3.11", meta.requiresPython)
    }

    @Test
    fun `parse should extract dependencies list`() {
        val script = """
            # /// script
            # requires-python = ">=3.11"
            # dependencies = [
            #   "requests>=2.28.0",
            #   "pandas>=2.0.0",
            #   "numpy",
            # ]
            # ///
            
            import requests
        """.trimIndent()

        val meta = PEP723Parser.parse(script)

        assertEquals(3, meta.dependencies.size)
        assertEquals("requests>=2.28.0", meta.dependencies[0])
        assertEquals("pandas>=2.0.0", meta.dependencies[1])
        assertEquals("numpy", meta.dependencies[2])
    }

    @Test
    fun `parse should extract autodev-unit context`() {
        val script = """
            # /// script
            # requires-python = ">=3.11"
            # dependencies = [
            #   "requests>=2.28.0",
            # ]
            # [tool.autodev-unit]
            # version = "1.0"
            # session-id = "abc123"
            # ///
            
            import requests
        """.trimIndent()

        val meta = PEP723Parser.parse(script)

        assertEquals("1.0", meta.autodevContext["version"])
        assertEquals("abc123", meta.autodevContext["session-id"])
    }

    @Test
    fun `parse should return empty metadata when no block exists`() {
        val script = """
            import os
            print("hello")
        """.trimIndent()

        val meta = PEP723Parser.parse(script)

        assertNull(meta.requiresPython)
        assertTrue(meta.dependencies.isEmpty())
        assertNull(meta.rawBlock)
    }

    @Test
    fun `parse should capture rawBlock`() {
        val script = """
            # /// script
            # requires-python = ">=3.11"
            # ///
            
            print("test")
        """.trimIndent()

        val meta = PEP723Parser.parse(script)
        assertNotNull(meta.rawBlock)
        assertTrue(meta.rawBlock!!.contains("requires-python"))
    }

    // ===== parseDependencies() tests =====

    @Test
    fun `parseDependencies should return empty list for no deps block`() {
        val script = """
            # /// script
            # requires-python = ">=3.11"
            # ///
        """.trimIndent()

        val deps = PEP723Parser.parseDependencies(script)
        assertTrue(deps.isEmpty())
    }

    // ===== generate() tests =====

    @Test
    fun `generate should produce valid PEP 723 block`() {
        val block = PEP723Parser.generate(
            dependencies = listOf("requests>=2.28.0", "pandas>=2.0.0"),
            requiresPython = ">=3.12"
        )

        assertTrue(block.contains("# /// script"))
        assertTrue(block.contains("# ///"))
        assertTrue(block.contains("""requires-python = ">=3.12""""))
        assertTrue(block.contains(""""requests>=2.28.0""""))
        assertTrue(block.contains(""""pandas>=2.0.0""""))
    }

    @Test
    fun `generate should include autodev context`() {
        val block = PEP723Parser.generate(
            dependencies = listOf("flask"),
            autodevContext = mapOf("version" to "1.0", "session-id" to "xyz")
        )

        assertTrue(block.contains("[tool.autodev-unit]"))
        assertTrue(block.contains("""version = "1.0""""))
        assertTrue(block.contains("""session-id = "xyz""""))
    }

    // ===== injectMetadata() tests =====

    @Test
    fun `injectMetadata should prepend block when none exists`() {
        val script = """
            import os
            print("hello")
        """.trimIndent()

        val result = PEP723Parser.injectMetadata(
            pythonContent = script,
            dependencies = listOf("requests"),
            requiresPython = ">=3.11"
        )

        assertTrue(result.startsWith("# /// script"))
        assertTrue(result.contains("import os"))
        assertTrue(result.contains(""""requests""""))
    }

    @Test
    fun `injectMetadata should replace existing block`() {
        val script = """
            # /// script
            # requires-python = ">=3.10"
            # dependencies = [
            #   "old-package",
            # ]
            # ///
            
            import os
        """.trimIndent()

        val result = PEP723Parser.injectMetadata(
            pythonContent = script,
            dependencies = listOf("new-package>=1.0"),
            requiresPython = ">=3.12"
        )

        assertTrue(result.contains(""""new-package>=1.0""""))
        assertTrue(result.contains("""requires-python = ">=3.12""""))
        // old package should be gone
        assertTrue(!result.contains("old-package"))
    }

    // ===== stripMetadata() tests =====

    @Test
    fun `stripMetadata should remove PEP 723 block`() {
        val script = """
            # /// script
            # requires-python = ">=3.11"
            # dependencies = [
            #   "requests",
            # ]
            # ///
            
            import requests
            print("done")
        """.trimIndent()

        val stripped = PEP723Parser.stripMetadata(script)

        assertTrue(!stripped.contains("# /// script"))
        assertTrue(!stripped.contains("# ///"))
        assertTrue(stripped.contains("import requests"))
    }
}
