package tech.mamontov.blackradish.core.utils.formatter

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
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.AstNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.BackgroundNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.ExamplesNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.FeatureNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.RuleNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.ScenarioNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.StepNode
import tech.mamontov.blackradish.core.utils.formatter.ast.nodes.TableRowNode
import java.net.URI
import java.util.Optional
import java.util.stream.Stream

internal class TestSources : Logged {
    private val toReadEventMap: MutableMap<URI, TestSourceRead> = HashMap()
    private val toAstMap: MutableMap<URI, GherkinDocument> = HashMap()
    private val toNodeMap: MutableMap<URI, Map<Int, AstNode>> = HashMap()

    fun addReadEvent(path: URI, event: TestSourceRead) {
        toReadEventMap[path] = event
    }

    fun feature(path: URI): Optional<Feature>? {
        if (!toAstMap.containsKey(path)) {
            parse(path)
        }

        return toAstMap[path]?.feature
    }

    fun raw(uri: URI, stepLine: Int): String {
        val feature: Optional<Feature>? = this.feature(uri)
        if (feature !== null && !feature.isEmpty && toReadEventMap.containsKey(uri)) {
            return toReadEventMap[uri]!!.source.split("\n")
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[stepLine].trim()
        }

        return ""
    }

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

    private fun parse(nodeMap: MutableMap<Int, AstNode>, background: Background, parent: AstNode) {
        val node = BackgroundNode(background, parent)

        nodeMap[background.location.line.toInt()] = node

        background.steps.forEach { step: Step ->
            nodeMap[step.location.line.toInt()] = StepNode(step, node)
        }
    }

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
