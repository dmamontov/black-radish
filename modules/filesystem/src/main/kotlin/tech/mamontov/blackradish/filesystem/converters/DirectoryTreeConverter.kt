package tech.mamontov.blackradish.filesystem.converters

import tech.mamontov.blackradish.core.enumerated.ConvertedResultOperation
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ToJsonConverter

/**
 * Directory tree converter
 *
 * @author Dmitry Mamontov
 *
 * @property tree List<String>
 * @property operations List<ConvertedResultOperation>
 * @constructor
 */
class DirectoryTreeConverter(val tree: List<String>) : Logged, ToJsonConverter {
    override val operations: List<ConvertedResultOperation> = listOf(
        ConvertedResultOperation.SIZE,
        ConvertedResultOperation.MATCH,
        ConvertedResultOperation.SAVE,
    )
}
