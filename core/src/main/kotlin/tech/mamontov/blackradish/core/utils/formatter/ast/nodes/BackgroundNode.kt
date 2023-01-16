package tech.mamontov.blackradish.core.utils.formatter.ast.nodes

import io.cucumber.messages.types.Background

class BackgroundNode(override val node: Background, override val parent: AstNode) : AstNode
