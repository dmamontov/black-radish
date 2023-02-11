package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.Feature
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Feature ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Feature
 * @constructor
 */
class FeatureNode(override val node: Feature) : AstNode
