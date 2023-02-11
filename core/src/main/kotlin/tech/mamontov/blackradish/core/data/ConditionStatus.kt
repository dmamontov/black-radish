package tech.mamontov.blackradish.core.data

import tech.mamontov.blackradish.core.enumerated.StepToken

/**
 * Condition status data
 *
 * @author Dmitry Mamontov
 *
 * @property stepToken StepToken
 * @property successful Boolean
 * @property proceed Boolean
 * @property parentSuccessful Boolean
 * @property parentProceed Boolean
 * @constructor
 */
data class ConditionStatus(
    val stepToken: StepToken,
    val successful: Boolean = false,
    val proceed: Boolean = false,
    val parentSuccessful: Boolean = true,
    val parentProceed: Boolean = true,
)
