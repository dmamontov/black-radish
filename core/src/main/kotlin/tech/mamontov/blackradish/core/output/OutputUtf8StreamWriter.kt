package tech.mamontov.blackradish.core.output

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

/**
 * Output UTF-8 stream writer
 *
 * @author Dmitry Mamontov
 *
 * @constructor
 */
internal class OutputUtf8StreamWriter(output: OutputStream) : OutputStreamWriter(output, StandardCharsets.UTF_8)
