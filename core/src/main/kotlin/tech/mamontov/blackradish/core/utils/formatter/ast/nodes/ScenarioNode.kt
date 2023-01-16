package tech.mamontov.blackradish.core.utils.formatter.ast.nodes

import io.cucumber.messages.types.Scenario

class ScenarioNode(override val node: Scenario, override val parent: AstNode) : AstNode
