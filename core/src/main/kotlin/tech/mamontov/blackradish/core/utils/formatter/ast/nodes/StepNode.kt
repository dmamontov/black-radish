package tech.mamontov.blackradish.core.utils.formatter.ast.nodes

import io.cucumber.messages.types.Step

class StepNode(override val node: Step, override val parent: AstNode) : AstNode
