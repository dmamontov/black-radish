package tech.mamontov.blackradish.core.interfaces

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Logged {
    val logger: Logger
        get() = LoggerFactory.getLogger(this::class.java.canonicalName)
}
