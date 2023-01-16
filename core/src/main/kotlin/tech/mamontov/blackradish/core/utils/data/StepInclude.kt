package tech.mamontov.blackradish.core.utils.data

import java.net.URI

data class StepInclude(
    val id: String,
    val name: String,
    val uri: URI,
    val includeScenario: String? = null,
    val includeUri: URI? = null,
)
