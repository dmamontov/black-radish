package tech.mamontov.blackradish.core.converters

import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ToJsonConverter

/**
 * Database converter
 *
 * @author Dmitry Mamontov
 *
 * @property result List<Map<String, Any>>
 * @constructor
 */
class DatabaseConverter(val result: List<Map<String, Any>>) : Logged, ToJsonConverter
