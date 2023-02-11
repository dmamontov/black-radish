package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.Background
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Background ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Background
 * @property parent AstNode
 * @constructor
 */
class BackgroundNode(override val node: Background, override val parent: AstNode) : AstNode
