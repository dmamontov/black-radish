package tech.mamontov.blackradish.core.generators

import io.cucumber.core.gherkin.Feature
import io.cucumber.core.gherkin.Pickle
import io.cucumber.core.gherkin.Step
import io.cucumber.core.resource.ResourceScanner
import io.cucumber.core.stepexpression.Argument
import tech.mamontov.blackradish.core.data.IncludeStep
import tech.mamontov.blackradish.core.exceptions.IncludeDepthException
import tech.mamontov.blackradish.core.exceptions.IncludeException
import tech.mamontov.blackradish.core.exceptions.IncludeSkipException
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.reflecation.Reflecation
import tech.mamontov.blackradish.core.storages.ConfigurationStorage
import java.net.URI
import java.net.URISyntaxException

/**
 * Generator include
 *
 * @author Dmitry Mamontov
 *
 * @property stepsMap MutableMap<String, IncludeStep>
 * @property stepsUri MutableMap<String, URI>
 * @property featureScanner ResourceScanner<Feature>?
 */
@Suppress("UNCHECKED_CAST")
class IncludeGenerator : Logged, StepGenerator() {
    private val stepsMap: MutableMap<String, IncludeStep> = mutableMapOf()

    val stepsUri: MutableMap<String, URI> = mutableMapOf()
    var featureScanner: ResourceScanner<Feature>? = null

    /**
     * Generate
     *
     * @param pickle Any
     * @param steps MutableList<Step>
     * @param depth Int
     * @return MutableList<Step>
     */
    fun generate(pickle: Any, steps: MutableList<Step>, depth: Int = 1): MutableList<Step> {
        this.fillStepsUri(pickle, steps)

        if (this.featureScanner === null) {
            return steps
        }

        val targetSteps: MutableList<Step> = mutableListOf()
        var skippedTo = false

        steps.forEach { step: Step ->
            if (skippedTo) {
                this.skippedSteps += step.id
                targetSteps += step

                return@forEach
            }

            try {
                targetSteps += this.generateStep(step, depth)
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

    /**
     * Generate step
     *
     * @param step Step
     * @param depth Int
     * @return MutableList<Step>
     */
    private fun generateStep(step: Step, depth: Int = 1): MutableList<Step> {
        if (!this.stepsMap.containsKey(step.id)) {
            return mutableListOf(step)
        }

        if (this.stepsMap[step.id]?.includeUri === null) {
            throwStep(IncludeException(step, "feature path is empty"))
        }

        var features: List<Feature> = listOf()

        val path = this.stepsMap[step.id]?.includeUri!!
        try {
            features = this.featureScanner!!.scanForResourcesUri(path)
        } catch (e: IllegalArgumentException) {
            throwStep(IncludeException(step, e.message.toString()))
        }

        if (features.isEmpty()) {
            throwStep(IncludeException(step, "path must exist: $path"))
        }

        val feature: Feature = features.elementAt(0)

        val text: String = this.stepsMap[step.id]?.includeScenario!!

        val pickle: Pickle? =
            (Reflecation.getValue(feature, "pickles") as List<Pickle>)
                .firstOrNull { it.name.trim() == text.trim() }

        if (pickle === null) {
            throwStep(IncludeException(step, "scenario '$text' not found in feature '$path'"))
        }

        val steps = Reflecation.getValue(pickle!!, "steps") as List<Step>

        this.fillStepsMap(pickle, steps, pickle.name)

        val settingDepth = ConfigurationStorage.get(
            ConfigurationStorage.ASPECT_INCLUDE_DEPTH,
            10,
        )
        if (depth > settingDepth) {
            throwStep(IncludeDepthException(step, "Permissible include depth of feature $settingDepth"))
        }

        return this.generate(pickle, steps.toMutableList(), depth + 1)
    }

    /**
     * Fill steps map
     *
     * @param pickle Any
     * @param steps List<Step>
     * @param name String
     */
    fun fillStepsMap(pickle: Any, steps: List<Step>, name: String) {
        steps.forEach { step: Step ->
            if (!this.`is`(pickle, step)) {
                return@forEach
            }

            val match = this.pickleStepDefinitionMatchMethod!!.call(pickle, step)
            val arguments = Reflecation.getValue(match, "arguments", true) as List<Argument>
            val uri = Reflecation.getValue(match, "uri") as URI
            var includeUri: URI? = null

            try {
                includeUri = if (arguments[1].value === null) uri else URI(arguments[1].value.toString())
            } catch (e: URISyntaxException) {
                stepErrors[step.id] = e.message.toString()
            }

            this.stepsMap[step.id] = IncludeStep(
                step.id,
                name,
                uri,
                arguments[0].value.toString(),
                includeUri,
            )
        }
    }

    /**
     * Fill steps URI
     *
     * @param pickle Any
     * @param steps List<Step>
     */
    private fun fillStepsUri(pickle: Any, steps: List<Step>) {
        steps.forEach { step: Step -> this.stepsUri[step.id] = Reflecation.getValue(pickle, "uri") as URI }
    }
}
