package tech.mamontov.blackradish.core.utils.formatter

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

internal class OutputUtf8StreamWriter(output: OutputStream) : OutputStreamWriter(output, StandardCharsets.UTF_8)
