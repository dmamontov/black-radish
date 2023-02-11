package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.TableRow
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Table row ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node TableRow
 * @property parent AstNode
 * @constructor
 */
class TableRowNode(override val node: TableRow, override val parent: AstNode) : AstNode
