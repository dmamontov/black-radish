package tech.mamontov.blackradish.csv.parsers

import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ToJsonConverter

/**
 * Csv converter
 *
 * @author Dmitry Mamontov
 *
 * @property list List<List<Any>>
 * @constructor
 */
open class CsvConverter(val list: List<List<Any>>) : Logged, ToJsonConverter
