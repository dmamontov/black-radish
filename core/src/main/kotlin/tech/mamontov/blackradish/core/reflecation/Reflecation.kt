package tech.mamontov.blackradish.core.reflecation

import com.google.gson.Gson
import tech.mamontov.blackradish.core.interfaces.Logged
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Reflecation
 *
 * @author Dmitry Mamontov
 */
class Reflecation : Logged {
    companion object {

        /**
         * Get value from class field
         *
         * @param clazz Any
         * @param field String
         * @param fromSuperClass Boolean
         * @return Any
         */
        fun getValue(clazz: Any, field: String, fromSuperClass: Boolean = false): Any {
            val declared = field(clazz, field, fromSuperClass)

            return declared[clazz]
        }

        /**
         * Get value from class field with field
         *
         * @param clazz Any
         * @param field Field
         * @return Any
         */
        fun getValue(clazz: Any, field: Field): Any {
            return field[clazz]
        }

        /**
         * Get class field
         *
         * @param clazz Any
         * @param field String
         * @param parent Boolean
         * @return Field
         */
        fun field(clazz: Any, field: String, parent: Boolean = false): Field {
            val declared: Field = getJavaClass(clazz, parent).getDeclaredField(field)
            declared.isAccessible = true

            return declared
        }

        /**
         * Get class method
         *
         * @param clazz Any
         * @param name String
         * @param parent Boolean
         * @param parameterTypes Array<out Class<*>?>
         * @return ReflecationMethod
         */
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

        /**
         * Class instance of
         *
         * @param clazz Any
         * @param name String
         * @param parent Boolean
         * @return Boolean
         */
        fun instanceOf(clazz: Any, name: String, parent: Boolean = false): Boolean {
            return getJavaClass(clazz, parent).name == name
        }

        /**
         * Clone class
         *
         * @param clazz Any
         * @return Any
         */
        fun clone(clazz: Any): Any {
            return Gson().fromJson(Gson().toJson(clazz), clazz::class.java)
        }

        /**
         * Get real Java class
         *
         * @param clazz Any
         * @param fromSuperClass Boolean
         * @return Class<out Any>
         */
        private fun getJavaClass(clazz: Any, fromSuperClass: Boolean): Class<out Any> {
            var javaClass = clazz::class.java
            if (fromSuperClass) {
                javaClass = javaClass.superclass
            }

            return javaClass
        }
    }
}
