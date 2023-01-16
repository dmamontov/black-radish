package tech.mamontov.blackradish.core.plugins

import io.cucumber.core.exception.CucumberException
import io.cucumber.core.exception.ExceptionUtils
import io.cucumber.core.stepexpression.DataTableArgument
import io.cucumber.core.stepexpression.DocStringArgument
import io.cucumber.core.stepexpression.ExpressionArgument
import io.cucumber.cucumberexpressions.Group
import io.cucumber.messages.types.Feature
import io.cucumber.plugin.ColorAware
import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.Argument
import io.cucumber.plugin.event.EmbedEvent
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.PickleStepTestStep
import io.cucumber.plugin.event.Status
import io.cucumber.plugin.event.Step
import io.cucumber.plugin.event.TestCase
import io.cucumber.plugin.event.TestCaseStarted
import io.cucumber.plugin.event.TestRunFinished
import io.cucumber.plugin.event.TestRunStarted
import io.cucumber.plugin.event.TestSourceRead
import io.cucumber.plugin.event.TestStep
import io.cucumber.plugin.event.TestStepFinished
import io.cucumber.plugin.event.WriteEvent
import tech.mamontov.blackradish.core.aspects.ReplacementAspect
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.formatter.OutputAppendable
import tech.mamontov.blackradish.core.utils.formatter.OutputUtf8StreamWriter
import tech.mamontov.blackradish.core.utils.formatter.TestSources
import tech.mamontov.blackradish.core.utils.formatter.formats.Format
import tech.mamontov.blackradish.core.utils.formatter.formats.Formats
import tech.mamontov.blackradish.core.utils.property.ThreadConfiguration
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.StringReader
import java.net.URI
import java.net.URISyntaxException
import java.util.Locale
import java.util.Optional
import java.util.UUID
import kotlin.math.max
import io.cucumber.core.stepexpression.Argument as StepArgument

@Suppress("UNUSED_PARAMETER", "UNCHECKED_CAST")
class Formatter(output: OutputStream) : Logged, ConcurrentEventListener, ColorAware {
    private var formats: Formats = Formats.ansi()
    private var file: String? = null
    private val sources: TestSources = TestSources()
    private val output: OutputAppendable = OutputAppendable(OutputUtf8StreamWriter(output))
    private val indexMap: MutableMap<UUID, Int> = HashMap()

    companion object {
        private const val STEP_INDENT = "    "
        private const val STEP_SCENARIO_INDENT = "    "
        private const val SCENARIO_INDENT = "  "
        private const val TABLES_INDENT = "     "
        private const val LOCATION_INDENT = "         "
    }

    override fun setMonochrome(monochrome: Boolean) {
        formats = if (monochrome) Formats.monochrome() else Formats.ansi()
    }

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(
            TestRunStarted::class.java,
        ) { event: TestRunStarted -> this.handle(event) }

        publisher.registerHandlerFor(
            TestSourceRead::class.java,
        ) { event: TestSourceRead -> this.handle(event) }

        publisher.registerHandlerFor(
            TestCaseStarted::class.java,
        ) { event: TestCaseStarted -> this.handle(event) }

        publisher.registerHandlerFor(
            TestStepFinished::class.java,
        ) { event: TestStepFinished -> this.handle(event) }

        publisher.registerHandlerFor(
            WriteEvent::class.java,
        ) { event: WriteEvent -> this.handle(event) }

        publisher.registerHandlerFor(
            EmbedEvent::class.java,
        ) { event: EmbedEvent -> this.handle(event) }

        publisher.registerHandlerFor(
            TestRunFinished::class.java,
        ) { event: TestRunFinished -> this.handle(event) }
    }

    private fun handle(event: TestRunStarted) {
        this.output.dividing()
        // TODO Print ascii logotype
    }

    private fun handle(event: TestSourceRead) {
        sources.addReadEvent(event.uri, event)
    }

    private fun handle(event: TestCaseStarted) {
        val case = event.testCase

        this.printFeature(case)
        this.output.println()

        this.calculateIndent(case)

        this.printTags(case)
        this.printScenario(case)
    }

    private fun handle(event: TestStepFinished) {
        if (event.testStep is PickleStepTestStep) {
            this.printComments(event.testStep as PickleStepTestStep)
            this.printStep(event.testStep as PickleStepTestStep, event)

            if (ThreadConfiguration.get(ThreadConfiguration.DEBUG_SHOW_TRACE, false)) {
                this.printStack(event.testStep as PickleStepTestStep)
            }
        }

        this.printError(event)
    }

    private fun handle(event: WriteEvent) {
        output.println()

        try {
            BufferedReader(StringReader(event.text)).use { lines ->
                var line: String
                while ((lines.readLine().also { line = it }) !== null) {
                    output.println(STEP_SCENARIO_INDENT + line)
                }
            }
        } catch (e: IOException) {
            throw CucumberException(e)
        }

        output.println()
    }

    private fun handle(event: EmbedEvent) {
        output.println()
        output.println(
            STEP_SCENARIO_INDENT + "Embedding " + event.getName() + " [" + event.mediaType + " " + event.data.size + " bytes]",
        )
        output.println()
    }

    private fun handle(event: TestRunFinished) {
        this.output.dividing()
        // TODO Print help text
        output.close()
    }

    private fun printFeature(case: TestCase) {
        if (this.file !== null && this.file == case.uri.toString()) {
            return
        }

        if (this.file !== null) {
            output.println()
        }

        this.file = case.uri.toString()

        var featureOptional: Optional<Feature>? = null
        try {
            featureOptional = this.sources.feature(URI(this.file.toString()))
        } catch (e: URISyntaxException) {
            logger.error(
                "Error getting feature document from " + this.file + ". " + e.message,
            )
        }

        output.println(this.format(this.relativize(case.uri).schemeSpecificPart))

        if (featureOptional === null || featureOptional.isEmpty) {
            return
        }

        val feature: Feature = featureOptional.get()
        output.println(
            formats.get("pending_arg").text(feature.keyword) + ": " + feature.name,
        )

        if (feature.description.isNotEmpty()) {
            output.println(feature.description)
        }
    }

    private fun printTags(case: TestCase) {
        if (case.tags.isNotEmpty()) {
            output.println(
                formats.get("tag").text(
                    SCENARIO_INDENT + case.tags.joinToString(" "),
                ),
            )
        }
    }

    private fun printScenario(case: TestCase) {
        val definition = this.format(case)

        output.println(
            SCENARIO_INDENT + definition + indent(case, SCENARIO_INDENT + definition).trimEnd() + " " + this.format(
                this.relativize(case.uri).schemeSpecificPart + ":" + case.location.line,
            ),
        )
    }

    private fun printComments(step: PickleStepTestStep) {
        val comment: String = sources.raw(step.uri, step.step.line - 2)
        val upperCaseComment = comment.uppercase(Locale.getDefault())

        try {
            if (upperCaseComment.startsWith("#LOG") || upperCaseComment.startsWith("#NOTE")) {
                output.println(
                    STEP_INDENT + formats.get("output").text(ReplacementAspect.replace(comment)),
                )
            }
        } catch (e: Exception) {
            output.println(
                STEP_INDENT + formats.get("failed").text(e.message.toString()),
            )
        }
    }

    private fun printStep(step: PickleStepTestStep, event: TestStepFinished) {
        val statusName = event.result.status.name.lowercase(Locale.ROOT)

        val keywordFormat = when (event.result.status) {
            Status.SKIPPED -> statusName
            else -> "pending_arg"
        }

        val formattedStepText = this.format(
            step.step.keyword,
            step.step.text,
            step.definitionArgument,
            formats.get(keywordFormat),
            formats.get(statusName),
            formats.get(statusName + "_arg"),
        )

        var location = ""
        if (event.result.status === Status.FAILED) {
            location = " # " + this.relativize(step.uri).schemeSpecificPart + ":" + step.step.location.line
        }

        output.println(
            STEP_INDENT + formattedStepText + location + indent(
                event.testCase,
                this.format(step.step.keyword, step.step.text),
            ),
        )
        try {
            this.arguments(step).forEach { argument: StepArgument ->
                when (argument) {
                    is DocStringArgument -> {
                        output.println(TABLES_INDENT + formats.get(statusName).text("\"\"\""))

                        try {
                            output.println(
                                TABLES_INDENT + formats.get(statusName)
                                    .text(Reflecation.get(argument, "content") as String),
                            )
                        } catch (e: Exception) {
                            output.println(TABLES_INDENT + formats.get(statusName).text(argument.value.toString()))
                        }

                        output.println(TABLES_INDENT + formats.get(statusName).text("\"\"\""))
                    }

                    is DataTableArgument -> {
                        argument.value.toString().split("\n").dropLastWhile { it.isEmpty() }.toTypedArray()
                            .forEach { row: String ->
                                output.println(TABLES_INDENT + formats.get(statusName).text(row.trim()))
                            }
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun printStack(step: PickleStepTestStep) {
        output.println(LOCATION_INDENT + this.format(step.codeLocation))
        try {
            this.arguments(step).forEachIndexed { index: Int, argument: StepArgument ->
                val prefix = "Argument " + (index + 1) + ": "
                when (argument) {
                    is ExpressionArgument -> {
                        val group = argument.group as Group
                        val content = if (group.children.isNotEmpty()) {
                            group.children.joinToString { it.value }
                        } else {
                            group.value
                        }

                        output.println(
                            LOCATION_INDENT + this.format(prefix + content),
                        )
                    }

                    is DocStringArgument -> {
                        output.println(
                            LOCATION_INDENT + this.format(
                                prefix + argument.value.toString(),
                            ),
                        )
                    }

                    is DataTableArgument -> {
                        val tableArguments = Reflecation.get(argument, "argument") as List<*>

                        output.println(
                            LOCATION_INDENT + this.format(
                                prefix + tableArguments.toTypedArray().contentToString(),
                            ),
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun printError(event: TestStepFinished) {
        val result = event.result
        if (result.error === null) {
            return
        }

        try {
            val status = result.status.name.lowercase(Locale.ROOT)
            var message = result.error.message!!.replace("\n", " ")

            if (ThreadConfiguration.get(ThreadConfiguration.DEBUG_SHOW_STACKTRACE, false)) {
                message = ExceptionUtils.printStackTrace(result.error)
            }

            output.println(STEP_INDENT + formats.get(status).text(message))
        } catch (e: Exception) {
            output.println(
                STEP_INDENT + formats.get("failed").text(
                    "Unexpected exception retrieving error message: " + e.message + ". Check the stacktrace for more details or enable debug.show.stacktrace in settings.properties",
                ),
            )
        }
    }

    private fun format(keyword: String, text: String): String {
        return STEP_INDENT + keyword + text
    }

    private fun format(case: TestCase): String {
        return formats.get("pending_arg").text(case.keyword) + ": " + case.name
    }

    private fun format(location: String): String {
        return formats.get("comment").text("# $location")
    }

    private fun format(
        keyword: String,
        text: String,
        arguments: List<Argument>,
        keywordFormat: Format,
        textFormat: Format,
        argFormat: Format,
    ): String {
        var index = 0
        val result: StringBuilder = StringBuilder(
            keywordFormat.text(keyword),
        )

        arguments.forEach { argument: Argument ->
            if (argument.value !== null) {
                val offset = argument.start
                if (offset < index) {
                    return@forEach
                }

                result.append(textFormat.text(text.substring(index, offset)))

                var value = argument.value
                try {
                    value = ReplacementAspect.replace(argument.value)
                } catch (_: AssertionError) {
                }
                result.append(argFormat.text(value))

                index = argument.end
            }
        }

        if (index != text.length) {
            result.append(textFormat.text(text.substring(index)))
        }

        return result.toString()
    }

    private fun indent(step: TestCase, prefix: String): String {
        val padding = this.indexMap.getOrDefault(step.id, 0) - prefix.length
        return " ".repeat(if (padding >= 0) padding else 1)
    }

    private fun calculateIndent(case: TestCase) {
        val longestStep = case.testSteps.stream().filter { obj: TestStep? -> obj is PickleStepTestStep }
            .map { obj: TestStep? -> obj as PickleStepTestStep }.map { obj: PickleStepTestStep -> obj.step }
            .map { step: Step ->
                this.format(
                    step.keyword,
                    step.text,
                ).length
            }.max(Comparator.naturalOrder()).orElse(0)

        indexMap[case.id] = max(longestStep, this.format(case).length) + 1
    }

    private fun relativize(uri: URI): URI {
        if ("file" != uri.scheme || !uri.isAbsolute) {
            return uri
        }

        try {
            val relative = File("").toURI().relativize(uri)

            return URI("file", relative.schemeSpecificPart, relative.fragment)
        } catch (e: URISyntaxException) {
            throw IllegalArgumentException(e.message, e)
        }
    }

    private fun arguments(testStep: TestStep): List<StepArgument> {
        return Reflecation.get(
            Reflecation.get(testStep, "definitionMatch"),
            "arguments",
            true,
        ) as List<StepArgument>
    }
}
