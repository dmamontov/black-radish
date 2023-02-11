package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.Examples
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Examples ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Examples
 * @property parent AstNode
 * @constructor
 */
class ExamplesNode(override val node: Examples, override val parent: AstNode) : AstNode
