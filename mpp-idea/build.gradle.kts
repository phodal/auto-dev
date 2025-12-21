import groovy.util.Node
import groovy.xml.XmlParser
import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaUltimate
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformDependenciesExtension
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformTestingExtension
import org.jetbrains.intellij.platform.gradle.tasks.RunIdeTask
import org.jetbrains.intellij.platform.gradle.utils.extensionProvider
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// The same as `--stacktrace` param
gradle.startParameter.showStacktrace = ShowStacktrace.ALWAYS

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java") // Java support
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.compose") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("org.jetbrains.intellij.platform") version "2.10.2"
    id("org.jetbrains.changelog") version "2.2.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

val basePluginArchiveName = "autodev-jetbrains"

val javaScriptPlugins = listOf("JavaScript")
val pycharmPlugins = listOf(prop("pythonPlugin"))
val javaPlugins = listOf("com.intellij.java", "org.jetbrains.kotlin")

val rustPlugins = listOf(
    prop("rustPlugin"),
    "org.toml.lang"
)

val platformVersion = prop("platformVersion").toInt()
val ideaPlugins = listOf(
    "com.intellij.java",
    "org.jetbrains.plugins.gradle",
    "org.jetbrains.idea.maven",
    "org.jetbrains.kotlin",
    "JavaScript"
)

var lang = extra.properties["lang"] ?: "java"

changelog {
    version.set(properties("pluginVersion"))
    groups.empty()
    path.set(rootProject.file("CHANGELOG.md").toString())
    repositoryUrl.set(properties("pluginRepositoryUrl"))
    itemPrefix.set("*")
}

repositories {
    mavenLocal()  // For locally published mpp-ui and mpp-core artifacts
    mavenCentral()
    google()
    // Required for mpp-ui's webview dependencies (jogamp)
    maven("https://jogamp.org/deployment/maven")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")

    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

// Global exclusions for heavy dependencies not needed in IntelliJ plugin
// These exclusions apply to ALL configurations including transitive dependencies from includeBuild projects
configurations.all {
    exclude(group = "aws.sdk.kotlin")           // AWS SDK (~30MB) - from ai.koog:prompt-executor-bedrock-client
    exclude(group = "aws.smithy.kotlin")        // AWS Smithy runtime
    exclude(group = "org.apache.tika")          // Apache Tika (~100MB) - document parsing
    exclude(group = "org.apache.poi")           // Apache POI - Office document parsing (from Tika)
    exclude(group = "org.apache.pdfbox")        // PDFBox (~10MB) - PDF parsing
    exclude(group = "net.sourceforge.plantuml") // PlantUML (~20MB) - from dev.snipme:highlights
    exclude(group = "org.jsoup")                // Jsoup HTML parser
    exclude(group = "ai.koog", module = "prompt-executor-bedrock-client")  // Bedrock executor
    // Redis/Lettuce - not needed in IDEA plugin (~5MB)
    exclude(group = "io.lettuce")
    // RSyntaxTextArea - IDEA has its own editor (~1.3MB)
    exclude(group = "com.fifesoft")
    // Netty - not needed for IDEA plugin (~3MB)
    exclude(group = "io.netty")
    // pty4j/jediterm - IDEA has its own terminal (~3MB)
    exclude(group = "org.jetbrains.pty4j")
    exclude(group = "org.jetbrains.jediterm")
    // Note: ktor-serialization-kotlinx-json is NOT excluded globally because it's required
    // by ai.koog:prompt-executor-llms-all (AbstractOpenAILLMClient) at runtime.
    // It's included as an explicit dependency with coroutines excluded below.
}


kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
            )
        )
    }
}

configure(subprojects) {
    apply {
        plugin("idea")
        plugin("kotlin")
        plugin("org.jetbrains.intellij.platform.module")
    }

    repositories {
        mavenCentral()

        intellijPlatform {
            defaultRepositories()
            jetbrainsRuntime()
        }
    }

    intellijPlatform {
        instrumentCode = false
        buildSearchableOptions = false
    }

    idea {
        module {
            generatedSourceDirs.add(file("src/gen"))
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    tasks {
        prepareSandbox { enabled = false }
    }

    val testOutput = configurations.create("testOutput")

    sourceSets {
        main {
            java.srcDirs("src/gen")
        }
    }

    dependencies {
        // Use compileOnly for kotlinx libraries - IntelliJ provides these at runtime
        compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
        implementation("com.knuddels:jtokkit:1.1.0")

        testOutput(sourceSets.test.get().output.classesDirs)

        testImplementation("junit:junit:4.13.2")
        testImplementation("org.opentest4j:opentest4j:1.3.0")
        testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.9.3")
        testImplementation("org.assertj:assertj-core:3.24.2")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:1.7.0") {
            exclude(group = "net.java.dev.jna", module = "jna-platform")
            exclude(group = "net.java.dev.jna", module = "jna")
        }

        // Note: testFramework is configured in each individual project's dependencies block
        // because it requires IntelliJ Platform IDE to be resolved first
    }
}


project(":") {
    apply {
        plugin("org.jetbrains.changelog")
        plugin("org.jetbrains.intellij.platform")
    }

    // Kotlin compiler options for Compose
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
                )
            )
        }
    }

    // Handle duplicate resources from dependencies
    tasks.named<Copy>("processResources") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        // Copy all resources from submodules to main plugin JAR
        // These are required for IntelliJ to load the plugin modules correctly
        from(project(":mpp-idea-core").file("src/main/resources"))
        from(project(":mpp-idea-lang:pycharm").file("src/main/resources"))
        from(project(":mpp-idea-lang:java").file("src/main/resources"))
        from(project(":mpp-idea-lang:kotlin").file("src/main/resources"))
        from(project(":mpp-idea-lang:javascript").file("src/main/resources"))
        from(project(":mpp-idea-lang:goland").file("src/main/resources"))
        from(project(":mpp-idea-lang:rust").file("src/main/resources"))
        from(project(":mpp-idea-exts:ext-database").file("src/main/resources"))
        from(project(":mpp-idea-exts:ext-git").file("src/main/resources"))
        from(project(":mpp-idea-exts:ext-terminal").file("src/main/resources"))
        from(project(":mpp-idea-exts:devins-lang").file("src/main/resources"))
        from(project(":mpp-idea-exts:nanodsl-lang").file("src/main/resources"))
    }

    repositories {
        mavenCentral()
        mavenLocal()  // For locally published mpp-ui and mpp-core artifacts
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

        intellijPlatform {
            defaultRepositories()
            jetbrainsRuntime()
        }
    }

    intellijPlatform {
        projectName = basePluginArchiveName
        pluginConfiguration {
            id = "cc.unitmesh.devins.idea"
            name = "AutoDev Xiuper - One Platform. All Phases. Every Device."
            version = prop("pluginVersion")

            ideaVersion {
                sinceBuild = prop("pluginSinceBuild")
                untilBuild = prop("pluginUntilBuild")
            }

            vendor {
                name = "Phodal Huang"
            }
        }

        pluginVerification {
            freeArgs = listOf("-mute", "TemplateWordInPluginId,ForbiddenPluginIdPrefix")
            ides {
                ide(IntellijIdeaUltimate, "2025.2")
                select {
                    types = listOf(IntellijIdeaUltimate)
                    sinceBuild = "252"
                    untilBuild = "253.*"
                }
            }
        }

        instrumentCode = false
        buildSearchableOptions = false
    }

    dependencies {
        intellijPlatform {
            pluginVerifier()
            intellijIde(prop("ideaVersion"))
            if (hasProp("jbrVersion")) {
                jetbrainsRuntime(prop("jbrVersion"))
            } else {
                jetbrainsRuntime()
            }

            val pluginList: MutableList<String> = mutableListOf(
                "Git4Idea",
                "org.jetbrains.plugins.terminal",
                "org.intellij.plugins.markdown",
                "com.jetbrains.sh"
            )
            when (lang) {
                "idea" -> {
                    pluginList += javaPlugins
                }

                "python" -> {
                    pluginList += pycharmPlugins
                }

                "go" -> {
                    pluginList += listOf("org.jetbrains.plugins.go")
                }

                "rust" -> {
                    pluginList += rustPlugins
                }
            }
            intellijPlugins(pluginList)

            // Compose support dependencies (bundled in IDEA 252+)
            // Note: Jewel modules are not available in IU-252, only use platform.compose
            // Compose support dependencies (bundled in IDEA 252+)
            bundledModules(
                "intellij.libraries.skiko",
                "intellij.libraries.compose.foundation.desktop",
                "intellij.platform.jewel.foundation",
                "intellij.platform.jewel.ui",
                "intellij.platform.jewel.ideLafBridge",
                "intellij.platform.compose"
            )

            testFramework(TestFrameworkType.Bundled)
            testFramework(TestFrameworkType.Platform)
        }

        // Required by IntelliJ's bundled JUnit 5 session listener (uses junit.framework.TestCase)
        testImplementation("junit:junit:4.13.2")

        implementation(project(":mpp-idea-core"))
        implementation(project(":mpp-idea-lang:java"))
        implementation(project(":mpp-idea-lang:kotlin"))
        implementation(project(":mpp-idea-lang:pycharm"))
        implementation(project(":mpp-idea-lang:javascript"))
        implementation(project(":mpp-idea-lang:goland"))
        implementation(project(":mpp-idea-lang:rust"))

        implementation(project(":mpp-idea-exts:ext-database"))
        implementation(project(":mpp-idea-exts:ext-git"))
        implementation(project(":mpp-idea-exts:ext-terminal"))
        implementation(project(":mpp-idea-exts:devins-lang"))
        implementation(project(":mpp-idea-exts:nanodsl-lang"))

        // Ktor dependencies - required at runtime by ai.koog:prompt-executor-llms-all
        // (AbstractOpenAILLMClient uses ContentNegotiation and JsonSupport for HTTP client)
        // Must exclude kotlinx dependencies to use IntelliJ's bundled versions
        implementation("io.ktor:ktor-client-core:3.2.2") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }
        implementation("io.ktor:ktor-client-cio:3.2.2") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }
        implementation("io.ktor:ktor-client-content-negotiation:3.2.2") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }
        implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.2") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }
        compileOnly("io.ktor:ktor-client-logging:3.2.2")

        // Use compileOnly for coroutines - IntelliJ provides these at runtime
        // This ensures we compile against the same API but use IntelliJ's ClassLoader at runtime
        // Note: We use Dispatchers.EDT from IntelliJ Platform instead of Dispatchers.Swing
        compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

        // mpp-core dependency for root project - use published artifact
        implementation("cc.unitmesh:mpp-core:${prop("mppVersion")}") {
            // Exclude Compose dependencies from mpp-core as well
            exclude(group = "org.jetbrains.compose")
            exclude(group = "org.jetbrains.compose.runtime")
            exclude(group = "org.jetbrains.compose.foundation")
            exclude(group = "org.jetbrains.compose.material3")
            exclude(group = "org.jetbrains.compose.material")
            exclude(group = "org.jetbrains.compose.ui")
            exclude(group = "org.jetbrains.skiko")
            // Exclude kotlinx libraries - IntelliJ provides its own
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
            // Note: Do NOT exclude ktor-client-content-negotiation - it's required at runtime by AbstractOpenAILLMClient
            // We provide it explicitly above with kotlinx dependencies excluded
            // Exclude only ktor-serialization-kotlinx-json from mpp-core (we provide it above)
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json-jvm")
            // Note: Heavy dependencies (AWS, Tika, POI, PDFBox, PlantUML, Jsoup) are excluded globally above
        }

        // Note: mpp-ui dependency removed - configuration management moved to mpp-core
        // Color definitions moved to IdeaAutoDevColors in mpp-idea
        // Note: ComposeCharts library cannot be used due to ClassLoader conflicts with IntelliJ's Compose runtime
        // Chart rendering is implemented manually using Compose Canvas API

        testImplementation(kotlin("test"))
    }

    tasks {
        val projectName = project.extensionProvider.flatMap { it.projectName }

        composedJar {
            archiveBaseName.convention(projectName)
        }

        withType<RunIdeTask> {
            // Default args for IDEA installation
            jvmArgs("-Xmx768m", "-XX:+UseG1GC", "-XX:SoftRefLRUPolicyMSPerMB=50")
            // Disable plugin auto reloading. See `com.intellij.ide.plugins.DynamicPluginVfsListener`
            jvmArgs("-Didea.auto.reload.plugins=false")
            // Don't show "Tip of the Day" at startup
            jvmArgs("-Dide.show.tips.on.startup.default.value=false")
            // uncomment if `unexpected exception ProcessCanceledException` prevents you from debugging a running IDE
            // jvmArgs("-Didea.ProcessCanceledException=disabled")
        }

        patchPluginXml {
            pluginDescription.set(provider { file("../src/description.html").readText() })

            changelog {
                version.set(properties("pluginVersion"))
                groups.empty()
                path.set(rootProject.file("CHANGELOG.md").toString())
                repositoryUrl.set(properties("pluginRepositoryUrl"))
            }

            val changelog = project.changelog
            // Get the latest available change notes from the changelog file
            changeNotes.set(properties("pluginVersion").map { pluginVersion ->
                with(changelog) {
                    renderItem(
                        (getOrNull(pluginVersion) ?: getUnreleased())
                            .withHeader(false)
                            .withEmptySections(false),

                        Changelog.OutputType.HTML,
                    )
                }
            })
        }

        buildPlugin {
            archiveBaseName.set(basePluginArchiveName)
            archiveVersion.set(prop("pluginVersion") + "-" + prop("platformVersion"))
        }

        publishPlugin {
            dependsOn("patchChangelog")
            token.set(environment("PUBLISH_TOKEN"))
            channels.set(properties("pluginVersion").map {
                listOf(it.split('-').getOrElse(1) { "default" }.split('.').first())
            })
        }

        // Exclude large font files from mpp-ui that are not needed in IDEA plugin
        // These fonts are for Desktop/WASM apps, IDEA has its own fonts
        named<org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask>("prepareSandbox") {
            // Exclude font files from the plugin distribution
            // NotoSansSC-Regular.ttf (~18MB), NotoColorEmoji.ttf (~11MB x2), FiraCode fonts (~1MB)
            exclude("**/fonts/**")
            exclude("**/composeResources/**/font/**")
            exclude("**/*.ttf")
            exclude("**/*.otf")
            // Also exclude icon files meant for desktop app
            exclude("**/icon.icns")
            exclude("**/icon.ico")
            exclude("**/icon-512.png")
        }

        // Task to verify no conflicting dependencies are included
        register("verifyNoDuplicateDependencies") {
            group = "verification"
            description = "Verifies that no Compose/Kotlinx dependencies are included that would conflict with IntelliJ's bundled versions"

            val forbiddenPatterns = listOf(
                "org.jetbrains.compose",
                "org.jetbrains.skiko",
                "kotlinx-coroutines-core",
                "kotlinx-coroutines-swing",
                "kotlinx-serialization-json",
                "kotlinx-serialization-core"
            )

            // Capture dependency info at configuration time to avoid serializing Project
            val runtimeClasspath = configurations.getByName("runtimeClasspath")
            val dependencyNames = runtimeClasspath.incoming.artifacts.resolvedArtifacts.map { artifacts ->
                artifacts.map { artifact ->
                    val id = artifact.id.componentIdentifier.displayName
                    id
                }
            }

            inputs.property("dependencyNames", dependencyNames)

            doLast {
                val deps = dependencyNames.get()
                val violations = mutableListOf<String>()

                deps.forEach { fullName ->
                    forbiddenPatterns.forEach { pattern ->
                        if (fullName.contains(pattern)) {
                            violations.add(fullName)
                        }
                    }
                }

                if (violations.isNotEmpty()) {
                    throw GradleException("""
                        |DEPENDENCY CONFLICT DETECTED!
                        |The following dependencies will conflict with IntelliJ's bundled libraries:
                        |${violations.joinToString("\n") { "  - $it" }}
                        |
                        |These dependencies must be excluded from mpp-ui and mpp-core.
                    """.trimMargin())
                } else {
                    println("✓ No conflicting dependencies found in runtime classpath")
                }
            }
        }

        // Run verification before build
        named("buildPlugin") {
            dependsOn("verifyNoDuplicateDependencies")
        }
    }
}

project(":mpp-idea-core") {
    apply {
        plugin("org.jetbrains.kotlin.plugin.serialization")
    }

    dependencies {
        implementation("cc.unitmesh:mpp-core:${prop("mppVersion")}") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
            // Note: Ktor client dependencies (content-negotiation, etc.) are NOT excluded here
            // They are provided by root project dependencies with kotlinx exclusions
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json-jvm")
        }

        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins)
            bundledPlugin("com.jetbrains.sh")
            testFramework(TestFrameworkType.Bundled)
            testFramework(TestFrameworkType.Platform)
        }

        implementation("org.jetbrains.xodus:xodus-openAPI:2.0.1")
        implementation("org.jetbrains.xodus:xodus-environment:2.0.1")
        implementation("org.jetbrains.xodus:xodus-entity-store:2.0.1")
        implementation("org.jetbrains.xodus:xodus-vfs:2.0.1")

        implementation("io.modelcontextprotocol:kotlin-sdk:0.7.2") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }
        /// # Ktor - exclude kotlinx dependencies to use IntelliJ's bundled versions
        implementation("io.ktor:ktor-client-cio:3.2.3") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }
        implementation("io.ktor:ktor-server-sse:3.2.3") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-io-core-jvm")
        }

        implementation("io.github.a2asdk:a2a-java-sdk-client:0.3.0.Beta1")

        implementation("io.reactivex.rxjava3:rxjava:3.1.10")

        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
        testImplementation(kotlin("test"))
        testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0") {
            excludeKotlinDeps()
        }
        implementation("com.squareup.okhttp3:okhttp:4.12.0") {
            excludeKotlinDeps()
        }
        implementation("com.squareup.okhttp3:okhttp-sse:4.12.0") {
            excludeKotlinDeps()
        }

        implementation("org.reflections:reflections:0.10.2") {
            exclude(group = "org.slf4j", module = "slf4j-api")
        }

        implementation("com.squareup.retrofit2:converter-jackson:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:2.11.0")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")

        implementation("org.commonmark:commonmark:0.21.0")
        implementation("org.commonmark:commonmark-ext-gfm-tables:0.21.0")

        implementation("com.jayway.jsonpath:json-path:2.9.0")
        implementation("com.jsoizo:kotlin-csv-jvm:1.10.0") {
            excludeKotlinDeps()
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
        }

        // kanban
        implementation("org.kohsuke:github-api:1.326")
        implementation("org.gitlab4j:gitlab4j-api:5.8.0")

        // template engine
        implementation("org.apache.velocity:velocity-engine-core:2.4.1")

        // token count
        implementation("com.knuddels:jtokkit:1.1.0")

        // gitignore parsing library for fallback engine
        implementation("nl.basjes.gitignore:gitignore-reader:1.6.0")

        // Use compileOnly for kotlinx libraries - IntelliJ provides these at runtime
        compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    }

    task("resolveDependencies") {
        doLast {
            rootProject.allprojects
                .map { it.configurations }
                .flatMap { it.filter { c -> c.isCanBeResolved } }
                .forEach { it.resolve() }
        }
    }
}

project(":mpp-idea-lang:pycharm") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins + pycharmPlugins)
        }

        implementation(project(":mpp-idea-core"))
    }
}


project(":mpp-idea-lang:java") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins)
            testFramework(TestFrameworkType.Plugin.Java)
        }

        implementation(project(":mpp-idea-core"))
        implementation(project(":mpp-idea-exts:devins-lang"))
    }
}


val cssPlugins = listOf(
    "com.intellij.css",
    "org.jetbrains.plugins.sass",
    "org.jetbrains.plugins.less",
)

project(":mpp-idea-lang:javascript") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins)
            intellijPlugins(javaScriptPlugins)
            intellijPlugins(cssPlugins)
            testFramework(TestFrameworkType.Plugin.JavaScript)
        }

        implementation(project(":mpp-idea-core"))
    }
}

project(":mpp-idea-lang:kotlin") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins)
            testFramework(TestFrameworkType.Plugin.Java)
        }

        implementation(project(":mpp-idea-core"))
        implementation(project(":mpp-idea-lang:java"))
    }
}

project(":mpp-idea-lang:rust") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins + rustPlugins)
        }

        implementation(project(":mpp-idea-core"))
    }
}

project(":mpp-idea-lang:goland") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(prop("goPlugin").split(',').map(String::trim).filter(String::isNotEmpty))
        }

        implementation(project(":mpp-idea-core"))
    }
}

project(":mpp-idea-exts:ext-database") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins + "com.intellij.database")
        }

        implementation("cc.unitmesh:mpp-core:${prop("mppVersion")}") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json-jvm")
        }
        implementation(project(":mpp-idea-core"))
        implementation(project(":mpp-idea-exts:devins-lang"))
    }
}

project(":mpp-idea-exts:ext-git") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins + "Git4Idea")
        }

        implementation("cc.unitmesh:mpp-core:${prop("mppVersion")}") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json-jvm")
        }
        implementation(project(":mpp-idea-core"))
        implementation(project(":mpp-idea-exts:devins-lang"))

        // kanban
        implementation("org.kohsuke:github-api:1.326")
        implementation("org.gitlab4j:gitlab4j-api:5.8.0")

        implementation("cc.unitmesh:git-commit-message:0.4.6") {
            excludeKotlinDeps()
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
        }
    }
}

project(":mpp-idea-exts:ext-terminal") {
    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins + "org.jetbrains.plugins.terminal")
        }

        implementation(project(":mpp-idea-core"))
    }
}

project(":mpp-idea-exts:devins-lang") {
    apply {
        plugin("org.jetbrains.grammarkit")
        plugin("org.jetbrains.kotlin.plugin.serialization")
    }

    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins + "org.intellij.plugins.markdown" + "com.jetbrains.sh" + "Git4Idea")

            testFramework(TestFrameworkType.Plugin.Java)
        }

        implementation("com.jayway.jsonpath:json-path:2.9.0")
        // Use compileOnly for kotlinx libraries - IntelliJ provides these at runtime
        compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
        implementation("cc.unitmesh:mpp-core:${prop("mppVersion")}") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json-jvm")
        }
        implementation(project(":mpp-idea-core"))
    }

    tasks {
        generateLexer {
            sourceFile.set(file("src/grammar/DevInLexer.flex"))
            targetOutputDir.set(file("src/gen/cc/unitmesh/devti/language/lexer"))
            purgeOldFiles.set(true)
        }

        generateParser {
            sourceFile.set(file("src/grammar/DevInParser.bnf"))
            targetRootOutputDir.set(file("src/gen"))
            pathToParser.set("cc/unitmesh/devti/language/parser/DevInParser.java")
            pathToPsiRoot.set("cc/unitmesh/devti/language/psi")
            purgeOldFiles.set(true)
        }

        withType<KotlinCompile> {
            dependsOn(generateLexer, generateParser)
        }
    }
}

project(":mpp-idea-exts:nanodsl-lang") {
    apply {
        plugin("org.jetbrains.grammarkit")
        plugin("org.jetbrains.kotlin.plugin.serialization")
    }

    dependencies {
        intellijPlatform {
            intellijIde(prop("ideaVersion"))
            intellijPlugins(ideaPlugins)

            testFramework(TestFrameworkType.Plugin.Java)
        }

        implementation("cc.unitmesh:mpp-core:${prop("mppVersion")}") {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core")
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json")
            exclude(group = "io.ktor", module = "ktor-serialization-kotlinx-json-jvm")
        }
        implementation(project(":mpp-idea-core"))
    }

    tasks {
        generateLexer {
            sourceFile.set(file("src/grammar/NanoDSLLexer.flex"))
            targetOutputDir.set(file("src/gen/cc/unitmesh/nanodsl/language/lexer"))
            purgeOldFiles.set(true)
        }

        generateParser {
            sourceFile.set(file("src/grammar/NanoDSLParser.bnf"))
            targetRootOutputDir.set(file("src/gen"))
            pathToParser.set("cc/unitmesh/nanodsl/language/parser/NanoDSLParser.java")
            pathToPsiRoot.set("cc/unitmesh/nanodsl/language/psi")
            purgeOldFiles.set(true)
        }

        withType<KotlinCompile> {
            dependsOn(generateLexer, generateParser)
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

fun File.isPluginJar(): Boolean {
    if (!isFile) return false
    if (extension != "jar") return false
    return zipTree(this).files.any { it.isManifestFile() }
}

fun File.isManifestFile(): Boolean {
    if (extension != "xml") return false
    val rootNode = try {
        val parser = XmlParser()
        parser.parse(this)
    } catch (e: Exception) {
        logger.error("Failed to parse $path", e)
        return false
    }
    return rootNode.name() == "idea-plugin"
}

data class TypeWithVersion(val type: IntelliJPlatformType, val version: String)

fun String.toTypeWithVersion(): TypeWithVersion {
    val (code, version) = split("-", limit = 2)
    return TypeWithVersion(IntelliJPlatformType.fromCode(code), version)
}


fun IntelliJPlatformDependenciesExtension.intellijIde(versionWithCode: String) {
    val (type, version) = versionWithCode.toTypeWithVersion()
    create(type, version, useInstaller = false)
}

fun IntelliJPlatformDependenciesExtension.intellijPlugins(vararg notations: String) {
    for (notation in notations) {
        if (notation.contains(":")) {
            plugin(notation)
        } else {
            bundledPlugin(notation)
        }
    }
}

fun IntelliJPlatformDependenciesExtension.intellijPlugins(notations: List<String>) {
    intellijPlugins(*notations.toTypedArray())
}

fun hasProp(name: String): Boolean = extra.has(name)

fun prop(name: String, fallbackResult: String? = null): String {
    // Try local properties first
    val localProp = extra.properties[name] as? String
    if (localProp != null) return localProp

    // Try parent project properties
    val parentProp = rootProject.parent?.extra?.properties?.get(name) as? String
    if (parentProp != null) return parentProp

    // Try gradle.properties from parent
    val parentGradleProp = rootProject.parent?.findProperty(name) as? String
    if (parentGradleProp != null) return parentGradleProp

    return fallbackResult ?: error("Property `$name` is not defined in gradle.properties")
}

fun withProp(name: String, action: (String) -> Unit) {
    if (hasProp(name)) {
        action(prop(name))
    }
}

fun buildDir(): String {
    return project.layout.buildDirectory.get().asFile.absolutePath
}

fun <T : ModuleDependency> T.excludeKotlinDeps() {
    exclude(module = "kotlin-runtime")
    exclude(module = "kotlin-reflect")
    exclude(module = "kotlin-stdlib")
    exclude(module = "kotlin-stdlib-common")
    exclude(module = "kotlin-stdlib-jdk8")
}


fun parseManifest(file: File): Node {
    val node = XmlParser().parse(file)
    check(node.name() == "idea-plugin") {
        "Manifest file `$file` doesn't contain top-level `idea-plugin` attribute"
    }
    return node
}

fun manifestFile(project: Project): File? {
    var filePath: String? = null

    val mainOutput = project.sourceSets.main.get().output
    val resourcesDir = mainOutput.resourcesDir ?: error("Failed to find resources dir for ${project.name}")

    if (filePath != null) {
        return resourcesDir.resolve(filePath).takeIf { it.exists() }
            ?: error("Failed to find manifest file for ${project.name} module")
    }
    val rootManifestFile =
        manifestFile(project(":intellij-plugin")) ?: error("Failed to find manifest file for :intellij-plugin module")
    val rootManifest = parseManifest(rootManifestFile)
    val children = ((rootManifest["content"] as? List<*>)?.single() as? Node)?.children()
        ?: error("Failed to find module declarations in root manifest")
    return children.filterIsInstance<Node>()
        .flatMap { node ->
            if (node.name() != "module") return@flatMap emptyList()
            val name = node.attribute("name") as? String ?: return@flatMap emptyList()
            listOfNotNull(resourcesDir.resolve("$name.xml").takeIf { it.exists() })
        }.firstOrNull() ?: error("Failed to find manifest file for ${project.name} module")
}

fun findModulePackage(project: Project): String? {
    val moduleManifest = manifestFile(project) ?: return null
    val node = parseManifest(moduleManifest)
    return node.attribute("package") as? String ?: error("Failed to find package for ${project.name}")
}
