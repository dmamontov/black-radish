package tech.mamontov.blackradish.core.data.ast

import io.cucumber.messages.types.Scenario
import tech.mamontov.blackradish.core.interfaces.AstNode

/**
 * Scenario ast node
 *
 * @author Dmitry Mamontov
 *
 * @property node Scenario
 * @property parent AstNode
 * @constructor
 */
class ScenarioNode(override val node: Scenario, override val parent: AstNode) : AstNode
