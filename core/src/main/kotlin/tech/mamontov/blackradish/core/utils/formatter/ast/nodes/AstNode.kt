package tech.mamontov.blackradish.core.utils.formatter.ast.nodes

interface AstNode {
    val node: Any
    val parent: AstNode?
        get() = null
}
