package tech.mamontov.blackradish.core.runners

import org.assertj.core.api.Assertions
import java.io.BufferedReader
import java.io.Reader
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/**
 * Database script runner
 *
 * @author Dmitry Mamontov
 *
 * @property connection Connection
 * @property timeout Int
 * @property lastResult List<Map<String, Any>>
 * @property delimiter String
 * @constructor
 */
class ScriptRunner(private val connection: Connection, private val timeout: Int) {
    companion object {
        const val DEFAULT_DELIMITER = ";"
        private val DELIMITER_PATTERN = "^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+(\\S+)".toPattern()
    }

    var lastResult: List<Map<String, Any>> = listOf()

    private var delimiter = DEFAULT_DELIMITER

    /**
     * Database script run
     *
     * @param reader Reader
     */
    fun run(reader: Reader) {
        try {
            connection.autoCommit = false
            execute(reader)
        } finally {
            rollback()
            connection.autoCommit = true
        }
    }

    /**
     * Execute command
     *
     * @param reader Reader
     */
    private fun execute(reader: Reader) {
        val command = StringBuilder()
        try {
            val iterator = BufferedReader(reader).lineSequence().iterator()
            while (iterator.hasNext()) {
                handle(command, iterator.next())
            }
            commit()
            checkMissing(command)
        } catch (e: Exception) {
            Assertions.fail<Any?>("Error executing: $command.", e)
        }
    }

    /**
     * Commit
     */
    private fun commit() {
        try {
            connection.commit()
        } catch (e: Throwable) {
            Assertions.fail<Any?>("Could not commit transaction.", e)
        }
    }

    /**
     * Rollback
     */
    private fun rollback() {
        try {
            connection.rollback()
        } catch (_: Throwable) {
        }
    }

    /**
     * Check missing end delimiter
     *
     * @param command StringBuilder
     */
    private fun checkMissing(command: StringBuilder) {
        if (command.toString().trim().isNotEmpty()) {
            Assertions.fail<Any?>("Line missing end-of-line terminator ($delimiter) => $command")
        }
    }

    /**
     * Handle line
     *
     * @param command StringBuilder
     * @param line String
     */
    private fun handle(command: StringBuilder, line: String) {
        val trimmedLine = line.trim()

        if (trimmedLine.startsWith("//") || trimmedLine.startsWith("--")) {
            val matcher = DELIMITER_PATTERN.matcher(trimmedLine)
            if (matcher.find()) {
                delimiter = matcher.group(5)
            }
        } else if (trimmedLine.contains(delimiter)) {
            command.append(line, 0, line.lastIndexOf(delimiter))
            command.append(System.lineSeparator())
            executeStatement(command.toString())
            command.setLength(0)
        } else if (trimmedLine.isNotEmpty()) {
            command.append(line)
            command.append(System.lineSeparator())
        }
    }

    /**
     * Execute by statement
     *
     * @param command String
     */
    private fun executeStatement(command: String) {
        connection.createStatement().use { statement ->
            statement.queryTimeout = timeout
            try {
                statement.setEscapeProcessing(true)
            } catch (_: SQLException) {
            }
            var hasResults = statement.execute(command.replace("\r\n", "\n"))
            while (!(!hasResults && statement.updateCount == -1)) {
                checkWarnings(statement)

                setResults(statement, hasResults)

                hasResults = statement.moreResults
            }
        }
    }

    /**
     * Check warnings
     *
     * @param statement Statement
     */
    private fun checkWarnings(statement: Statement) {
        val warning = statement.warnings
        if (warning != null) {
            throw warning
        }
    }

    /**
     * Set results
     *
     * @param statement Statement
     * @param hasResults Boolean
     */
    private fun setResults(statement: Statement, hasResults: Boolean) {
        if (hasResults) {
            lastResult = statement.resultSet.toList()

            return
        }

        lastResult = listOf(mapOf("updateCount" to statement.updateCount))
    }
}

/**
 * ResultSet to List converter
 *
 * @receiver ResultSet
 * @return List<Map<String, Any>>
 */
private fun ResultSet.toList(): List<Map<String, Any>> {
    val results: MutableList<Map<String, Any>> = mutableListOf()

    while (next()) {
        val row: MutableMap<String, Any> = mutableMapOf()
        for (i in 1..metaData.columnCount) {
            row[metaData.getColumnLabel(i)] = getObject(i)
        }
        results.add(row)
    }

    return results
}
