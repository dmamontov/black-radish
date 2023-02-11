package tech.mamontov.blackradish.core.sources

import io.cucumber.gherkin.GherkinParser
import io.cucumber.messages.types.Background
import io.cucumber.messages.types.Envelope
import io.cucumber.messages.types.Examples
import io.cucumber.messages.types.Feature
import io.cucumber.messages.types.FeatureChild
import io.cucumber.messages.types.GherkinDocument
import io.cucumber.messages.types.Rule
import io.cucumber.messages.types.RuleChild
import io.cucumber.messages.types.Scenario
import io.cucumber.messages.types.Step
import io.cucumber.messages.types.TableRow
import io.cucumber.plugin.event.TestSourceRead
import tech.mamontov.blackradish.core.data.ast.BackgroundNode
import tech.mamontov.blackradish.core.data.ast.ExamplesNode
import tech.mamontov.blackradish.core.data.ast.FeatureNode
import tech.mamontov.blackradish.core.data.ast.RuleNode
import tech.mamontov.blackradish.core.data.ast.ScenarioNode
import tech.mamontov.blackradish.core.data.ast.StepNode
import tech.mamontov.blackradish.core.data.ast.TableRowNode
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.AstNode
import tech.mamontov.blackradish.core.interfaces.Logged
import java.net.URI
import java.util.Optional
import java.util.stream.Stream

/**
 * Test sources
 *
 * @author Dmitry Mamontov
 *
 * @property toReadEventMap MutableMap<URI, TestSourceRead>
 * @property toAstMap MutableMap<URI, GherkinDocument>
 * @property toNodeMap MutableMap<URI, Map<Int, AstNode>>
 */
internal class TestSources : Logged {
    val toReadEventMap: MutableMap<URI, TestSourceRead> = HashMap()
    private val toAstMap: MutableMap<URI, GherkinDocument> = HashMap()
    private val toNodeMap: MutableMap<URI, Map<Int, AstNode>> = HashMap()

    /**
     * Add event to read
     *
     * @param path URI
     * @param event TestSourceRead
     */
    fun addReadEvent(path: URI, event: TestSourceRead) {
        val realPath = try {
            Filesystem.that(path.path ?: path.toString()).absolute().uri
        } catch (_: AssertionError) {
            path
        }

        toReadEventMap[realPath] = event
    }

    /**
     * Get future by uri
     *
     * @param path URI
     * @return Optional<Feature>?
     */
    fun feature(path: URI): Optional<Feature>? {
        if (!toAstMap.containsKey(path)) {
            parse(path)
        }

        return toAstMap[path]?.feature
    }

    /**
     * Get raw step
     *
     * @param uri URI
     * @param stepLine Int
     * @return String
     */
    fun raw(uri: URI, stepLine: Int): String {
        val realUri = try {
            Filesystem.that(uri.toString()).absolute().uri
        } catch (_: AssertionError) {
            uri
        }

        val feature: Optional<Feature>? = this.feature(realUri)
        if (feature !== null && !feature.isEmpty && toReadEventMap.containsKey(realUri)) {
            try {
                return toReadEventMap[realUri]!!.source.split("\n")
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[stepLine].trim()
            } catch (_: ArrayIndexOutOfBoundsException) {
            }
        }

        return ""
    }

    /**
     * Parse URI
     *
     * @param path URI
     */
    private fun parse(path: URI) {
        if (!toReadEventMap.containsKey(path)) {
            return
        }

        val source = toReadEventMap[path]!!.source
        val envelopes: Stream<Envelope> = GherkinParser
            .builder().build().parse(path.toString(), source.toByteArray())

        val document: Optional<GherkinDocument> = envelopes
            .filter { envelope -> !envelope.gherkinDocument.isEmpty }
            .map<Optional<GherkinDocument>>(Envelope::getGherkinDocument)
            .findFirst()
            .orElse(null)

        if (document.isEmpty) {
            return
        }

        toAstMap[path] = document.get()

        val nodeMap: MutableMap<Int, AstNode> = HashMap()
        val feature: Optional<Feature> = document.get().feature

        if (!feature.isEmpty) {
            val parent = FeatureNode(feature.get())

            feature.get().children.forEach { child: FeatureChild ->
                if (!child.background.isEmpty) {
                    this.parse(nodeMap, child.background.get(), parent)
                } else if (!child.scenario.isEmpty) {
                    this.parse(nodeMap, child.scenario.get(), parent)
                } else if (!child.rule.isEmpty) {
                    this.parse(nodeMap, child.rule.get(), parent)
                }
            }
        }

        toNodeMap[path] = nodeMap
    }

    /**
     * Parse background
     *
     * @param nodeMap MutableMap<Int, AstNode>
     * @param background Background
     * @param parent AstNode
     */
    private fun parse(nodeMap: MutableMap<Int, AstNode>, background: Background, parent: AstNode) {
        val node = BackgroundNode(background, parent)

        nodeMap[background.location.line.toInt()] = node

        background.steps.forEach { step: Step ->
            nodeMap[step.location.line.toInt()] = StepNode(step, node)
        }
    }

    /**
     * Parse scenario
     *
     * @param nodeMap MutableMap<Int, AstNode>
     * @param scenario Scenario
     * @param parent AstNode
     */
    private fun parse(nodeMap: MutableMap<Int, AstNode>, scenario: Scenario, parent: AstNode) {
        val node = ScenarioNode(scenario, parent)

        nodeMap[scenario.location.line.toInt()] = node

        scenario.steps.forEach { step: Step ->
            nodeMap[step.location.line.toInt()] = StepNode(step, node)
        }

        if (scenario.examples.isNotEmpty()) {
            scenario.examples.forEach { example: Examples ->
                this.parse(nodeMap, example, node)
            }
        }
    }

    /**
     * Parse examples
     *
     * @param nodeMap MutableMap<Int, AstNode>
     * @param examples Examples
     * @param parent AstNode
     */
    private fun parse(nodeMap: MutableMap<Int, AstNode>, examples: Examples, parent: AstNode) {
        val node = ExamplesNode(examples, parent)

        val header: Optional<TableRow> = examples.tableHeader
        if (!header.isEmpty) {
            nodeMap[header.get().location.line.toInt()] = TableRowNode(header.get(), node)
        }

        examples.tableBody.forEach { row: TableRow ->
            nodeMap[row.location.line.toInt()] = TableRowNode(row, node)
        }
    }

    /**
     * Parse rule
     *
     * @param nodeMap MutableMap<Int, AstNode>
     * @param rule Rule
     * @param parent AstNode
     */
    private fun parse(nodeMap: MutableMap<Int, AstNode>, rule: Rule, parent: AstNode) {
        val node = RuleNode(rule, parent)

        nodeMap[rule.location.line.toInt()] = node

        rule.children.forEach { child: RuleChild ->
            if (!child.background.isEmpty) {
                this.parse(nodeMap, child.background.get(), node)
            } else if (!child.scenario.isEmpty) {
                this.parse(nodeMap, child.scenario.get(), node)
            }
        }
    }
}
