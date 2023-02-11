package tech.mamontov.blackradish.core.data

import java.net.URI

/**
 * Include step data
 *
 * @author Dmitry Mamontov
 *
 * @property id String
 * @property name String
 * @property uri URI
 * @property includeScenario String?
 * @property includeUri URI?
 * @constructor
 */
data class IncludeStep(
    val id: String,
    val name: String,
    val uri: URI,
    val includeScenario: String? = null,
    val includeUri: URI? = null,
)
