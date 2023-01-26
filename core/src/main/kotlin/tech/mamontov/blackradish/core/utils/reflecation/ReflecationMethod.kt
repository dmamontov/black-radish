package tech.mamontov.blackradish.core.utils.reflecation

import tech.mamontov.blackradish.core.interfaces.Logged
import java.lang.reflect.Method

class ReflecationMethod(private val clazz: Any, private val method: Method) : Logged {
    fun call(vararg args: Any?): Any {
        return method.invoke(clazz, *args)
    }
}
