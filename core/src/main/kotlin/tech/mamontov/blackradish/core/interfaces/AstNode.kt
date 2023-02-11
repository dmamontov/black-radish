package tech.mamontov.blackradish.core.interfaces

/**
 * Ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Any
 * @property parent AstNode?
 */
interface AstNode {
    val node: Any
    val parent: AstNode?
        get() = null
}
