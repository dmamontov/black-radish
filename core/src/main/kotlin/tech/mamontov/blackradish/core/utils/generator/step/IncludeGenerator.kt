package tech.mamontov.blackradish.core.utils.generator.step

import io.cucumber.core.gherkin.Feature
import io.cucumber.core.gherkin.Pickle
import io.cucumber.core.gherkin.Step
import io.cucumber.core.resource.ResourceScanner
import io.cucumber.core.stepexpression.Argument
import tech.mamontov.blackradish.core.data.StepInclude
import tech.mamontov.blackradish.core.exceptions.IncludeDepthException
import tech.mamontov.blackradish.core.exceptions.IncludeException
import tech.mamontov.blackradish.core.exceptions.IncludeSkipException
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import java.net.URI
import java.net.URISyntaxException

@Suppress("UNCHECKED_CAST")
class IncludeGenerator : Logged, StepGenerator() {
    private val map: MutableMap<String, StepInclude> = mutableMapOf()

    val uris: MutableMap<String, URI> = mutableMapOf()
    var scanner: ResourceScanner<Feature>? = null

    fun generate(pickle: Any, steps: MutableList<Step>, depth: Int = 1): MutableList<Step> {
        this.fillUri(pickle, steps)

        if (this.scanner === null) {
            return steps
        }

        val targetSteps: MutableList<Step> = mutableListOf()
        var skippedTo = false

        steps.forEach { step: Step ->
            if (skippedTo) {
                this.skipped += step.id
                targetSteps += step

                return@forEach
            }

            try {
                targetSteps += this.generate(step, depth)
            } catch (e: IncludeDepthException) {
                targetSteps += e.step
                return if (depth == 1) targetSteps else throw e
            } catch (e: IncludeException) {
                targetSteps += e.step
                skippedTo = true
            } catch (e: IncludeSkipException) {
                targetSteps += e.steps
                skippedTo = true
            }
        }

        if (skippedTo) {
            if (depth > 1) {
                throw IncludeSkipException(targetSteps)
            }

            return targetSteps
        }

        return targetSteps
    }

    private fun generate(step: Step, depth: Int = 1): MutableList<Step> {
        if (!this.map.containsKey(step.id)) {
            return mutableListOf(step)
        }

        if (this.map[step.id]?.includeUri === null) {
            throwStep(IncludeException(step, "feature path is empty"))
        }

        var features: List<Feature> = listOf()

        val path = this.map[step.id]?.includeUri!!
        try {
            features = this.scanner!!.scanForResourcesUri(path)
        } catch (e: IllegalArgumentException) {
            throwStep(IncludeException(step, e.message.toString()))
        }

        if (features.isEmpty()) {
            throwStep(IncludeException(step, "path must exist: $path"))
        }

        val feature: Feature = features.elementAt(0)

        val text: String = this.map[step.id]?.includeScenario!!

        val pickle: Pickle? =
            (Reflecation.get(feature, "pickles") as List<Pickle>)
                .firstOrNull { it.name.trim() == text.trim() }

        if (pickle === null) {
            throwStep(IncludeException(step, "scenario '$text' not found in feature '$path'"))
        }

        val steps = Reflecation.get(pickle!!, "steps") as List<Step>

        this.fill(pickle, steps, pickle.name)

        val settingDepth = ConfigurationProperty.get(
            ConfigurationProperty.ASPECT_INCLUDE_DEPTH,
            10,
        )
        if (depth > settingDepth) {
            throwStep(IncludeDepthException(step, "Permissible include depth of feature $settingDepth"))
        }

        return this.generate(pickle, steps.toMutableList(), depth + 1)
    }

    fun fill(pickle: Any, steps: List<Step>, name: String) {
        steps.forEach { step: Step ->
            if (!this.match(pickle, step)) {
                return@forEach
            }

            val match = this.matcher!!.call(pickle, step)
            val arguments = Reflecation.get(match, "arguments", true) as List<Argument>
            val uri = Reflecation.get(match, "uri") as URI
            var includeUri: URI? = null

            try {
                includeUri = if (arguments[1].value === null) uri else URI(arguments[1].value.toString())
            } catch (e: URISyntaxException) {
                errors[step.id] = e.message.toString()
            }

            this.map[step.id] = StepInclude(
                step.id,
                name,
                uri,
                arguments[0].value.toString(),
                includeUri,
            )
        }
    }

    private fun fillUri(pickle: Any, steps: List<Step>) {
        steps.forEach { step: Step -> this.uris[step.id] = Reflecation.get(pickle, "uri") as URI }
    }
}
