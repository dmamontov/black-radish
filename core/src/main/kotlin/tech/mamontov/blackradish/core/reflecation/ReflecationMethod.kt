package tech.mamontov.blackradish.core.reflecation

import tech.mamontov.blackradish.core.interfaces.Logged
import java.lang.reflect.Method

/**
 * Reflecation method
 *
 * @author Dmitry Mamontov
 *
 * @property clazz Any
 * @property method Method
 * @constructor
 */
class ReflecationMethod(private val clazz: Any, private val method: Method) : Logged {
    /**
     * Call method
     *
     * @param args Array<out Any?>
     * @return Any
     */
    fun call(vararg args: Any?): Any {
        return method.invoke(clazz, *args)
    }
}
