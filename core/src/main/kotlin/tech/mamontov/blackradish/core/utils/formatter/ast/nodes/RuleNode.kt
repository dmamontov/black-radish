package tech.mamontov.blackradish.core.utils.formatter.ast.nodes

import io.cucumber.messages.types.Rule

class RuleNode(override val node: Rule, override val parent: AstNode) : AstNode
