package tech.mamontov.blackradish.core.utils.formatter.ast.nodes

import io.cucumber.messages.types.TableRow

class TableRowNode(override val node: TableRow, override val parent: AstNode) : AstNode
