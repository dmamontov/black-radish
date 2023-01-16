package tech.mamontov.blackradish.core.utils.reflecation

import com.google.gson.Gson
import tech.mamontov.blackradish.core.utils.Logged
import java.lang.reflect.Field
import java.lang.reflect.Method

class Reflecation : Logged {
    companion object {
        fun get(clazz: Any, field: String, parent: Boolean = false): Any {
            val declared = this.field(clazz, field, parent)

            return declared[clazz]
        }

        fun get(clazz: Any, field: Field): Any {
            return field[clazz]
        }

        fun field(clazz: Any, field: String, parent: Boolean = false): Field {
            val declared: Field = getJavaClass(clazz, parent).getDeclaredField(field)
            declared.isAccessible = true

            return declared
        }

        fun method(
            clazz: Any,
            name: String,
            parent: Boolean = false,
            vararg parameterTypes: Class<*>?,
        ): ReflecationMethod {
            val declared: Method = getJavaClass(clazz, parent).getDeclaredMethod(
                name,
                *parameterTypes,
            )
            declared.isAccessible = true

            return ReflecationMethod(clazz, declared)
        }

        fun match(clazz: Any, name: String, parent: Boolean = false): Boolean {
            return getJavaClass(clazz, parent).name == name
        }

        fun clone(clazz: Any): Any {
            return Gson().fromJson(Gson().toJson(clazz), clazz::class.java)
        }

        private fun getJavaClass(clazz: Any, parent: Boolean): Class<out Any> {
            var javaClass = clazz::class.java
            if (parent) {
                javaClass = javaClass.superclass
            }

            return javaClass
        }
    }
}
