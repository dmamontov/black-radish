package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.Rule
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Rule ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Rule
 * @property parent AstNode
 * @constructor
 */
class RuleNode(override val node: Rule, override val parent: AstNode) : AstNode
