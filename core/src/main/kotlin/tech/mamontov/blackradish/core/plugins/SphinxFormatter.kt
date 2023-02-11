package tech.mamontov.blackradish.core.plugins

import io.cucumber.messages.types.Feature
import io.cucumber.messages.types.FeatureChild
import io.cucumber.messages.types.RuleChild
import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestCaseFinished
import io.cucumber.plugin.event.TestRunStarted
import io.cucumber.plugin.event.TestSourceRead
import org.apache.commons.io.FileUtils
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.output.RstWriter
import tech.mamontov.blackradish.core.sources.TestSources
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.nio.charset.Charset
import java.util.Optional

/**
 * Plugin for generate sphinx rst
 *
 * @author Dmitry Mamontov
 *
 * @property sources TestSources
 * @property fileName String?
 */
@Suppress("UNUSED_PARAMETER")
class SphinxFormatter : Logged, ConcurrentEventListener {
    companion object {
        private const val MAX_COMMENT_LENGTH = 10
    }

    private val sources: TestSources = TestSources()
    private var fileName: String? = null

    /**
     * Subscribe to events
     *
     * @param publisher EventPublisher
     */
    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(
            TestRunStarted::class.java,
        ) { event: TestRunStarted -> this.handle(event) }

        publisher.registerHandlerFor(
            TestSourceRead::class.java,
        ) { event: TestSourceRead -> this.handle(event) }

        publisher.registerHandlerFor(
            TestCaseFinished::class.java,
        ) { event: TestCaseFinished -> this.handle(event) }
    }

    /**
     * Add event to sources
     *
     * @param event TestSourceRead
     */
    private fun handle(event: TestSourceRead) {
        sources.addReadEvent(event.uri, event)
    }

    /**
     * Create directory on run
     *
     * @param event TestCaseStarted
     */
    private fun handle(event: TestRunStarted) {
        val attachments = File(
            Filesystem.current(RstWriter.BUILD_DIRECTORY).asFile.toString() + File.separator + "attachments",
        )

        FileUtils.forceMkdir(attachments)
        FileUtils.cleanDirectory(attachments)
    }

    /**
     * Write feature
     *
     * @param event TestCaseStarted
     */
    private fun handle(event: TestCaseFinished) {
        val case = event.testCase

        if (this.fileName !== null && this.fileName == case.uri.toString()) {
            return
        }

        this.fileName = case.uri.path ?: case.uri.toString()

        var featureOptional: Optional<Feature>? = null
        try {
            featureOptional = this.sources.feature(Filesystem.that(this.fileName!!).absolute().uri)
        } catch (_: Exception) {
        }

        if (featureOptional === null || featureOptional.isEmpty) {
            return
        }

        val feature: Feature = featureOptional.get()
        val moduleName = feature.name.lowercase().replace(" ", "_")

        val rst = Filesystem.current(
            RstWriter.BUILD_DIRECTORY,
        ).asFile.toString() + File.separator + moduleName + "." + feature.language.lowercase() + ".rst"

        val output = RstWriter(
            FileOutputStream(File(rst)).bufferedWriter(Charset.defaultCharset()),
        )

        output.writeTags(case.tags)
        output.writeHeader(feature)

        feature.children.filter { it.rule !== null }.forEach { featureChild: FeatureChild ->
            val rule = featureChild.rule.get()

            output.writeH(rule.name, "-")
            writeComment(output, case.uri, rule.location.line.toInt(), moduleName)

            rule.children.filter { it.scenario !== null }.forEach { ruleChild: RuleChild ->
                val scenario = ruleChild.scenario.get()

                output.writeH(scenario.name, "~")
                writeComment(output, case.uri, scenario.location.line.toInt(), moduleName)
                output.writeCode(scenario)
            }
        }

        output.writeTree()
        output.close()
    }

    private fun writeComment(output: RstWriter, uri: URI, startIndex: Int, moduleName: String) {
        for (index in 0..MAX_COMMENT_LENGTH) {
            val raw = sources.raw(uri, startIndex + index).trim()
            if (!raw.startsWith("#", true)) {
                return
            }

            output.writeComment(raw, moduleName)
        }
    }
}
