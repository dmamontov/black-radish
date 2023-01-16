package tech.mamontov.blackradish.core.utils.data

import tech.mamontov.blackradish.core.enumerated.Token

data class ConditionStatus(
    val token: Token,
    val successful: Boolean = false,
    val proceed: Boolean = false,
    val parentSuccessful: Boolean = true,
    val parentProceed: Boolean = true,
)
