package tech.mamontov.blackradish.core.enumerated

/**
 * Enum step token
 *
 * @author Dmitry Mamontov
 */
enum class StepToken {
    UNDEFINED,

    IF, ELSEIF, ELSE, ENDIF,

    LOOP, ENDLOOP,

    INCLUDE,
}
