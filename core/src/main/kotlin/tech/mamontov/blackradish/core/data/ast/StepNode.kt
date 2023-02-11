package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.Step
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Step ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Step
 * @property parent AstNode
 * @constructor
 */
class StepNode(override val node: Step, override val parent: AstNode) : AstNode
