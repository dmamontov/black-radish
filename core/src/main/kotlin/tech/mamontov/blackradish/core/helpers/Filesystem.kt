package tech.mamontov.blackradish.core.helpers

import io.cucumber.core.resource.ClassLoaders
import io.cucumber.core.resource.ClasspathSupport
import io.cucumber.core.resource.Resource
import io.cucumber.core.resource.ResourceScanner
import org.apache.commons.io.FilenameUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.reflecation.Reflecation
import java.io.File
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.Path
import java.util.Optional

/**
 * Filesystem
 *
 * @author Dmitry Mamontov
 *
 * @property scanner ResourceScanner<(Any..Any?)>
 * @property schema String
 * @property path String
 * @property uri URI
 * @property asFile File
 */
class Filesystem : Logged {
    private val scanner = ResourceScanner(
        { ClassLoaders.getDefaultClassLoader() },
        { path: Path -> path.toString().isNotEmpty() },
        { resource: Resource -> Optional.of(Reflecation.getValue(resource, "resource") as Path) },
    )
    private val schema: String
        get() = if (path.first() == File.separatorChar) {
            FILE_SCHEME
        } else {
            ClasspathSupport.CLASSPATH_SCHEME
        }

    val path: String
    val uri: URI

    /**
     * Constructor
     *
     * @param path String
     * @constructor
     */
    constructor(path: String) {
        var realPath = path
        if (path.startsWith(ClasspathSupport.CLASSPATH_SCHEME_PREFIX)) {
            realPath = realPath.replace(ClasspathSupport.CLASSPATH_SCHEME_PREFIX, "")
        }

        this.path = realPath
        this.uri = URI("$schema:$realPath")
    }

    /**
     * Constructor
     *
     * @param path String
     * @param fragment String
     * @constructor
     */
    constructor(path: String, fragment: String) {
        this.path = path
        this.uri = URI(schema, path, fragment)
    }

    companion object {
        private const val FILE_SCHEME = "file"

        /**
         * Static constructor
         *
         * @param path String
         * @return Filesystem
         */
        fun that(path: String): Filesystem {
            return Filesystem(path)
        }

        /**
         * Static constructor
         * Whit fragment
         *
         * @param path String
         * @param fragment String
         * @return Filesystem
         */
        fun that(path: String, fragment: String): Filesystem {
            return Filesystem(path, fragment)
        }

        /**
         * Static constructor
         * Project directory
         *
         * @return Filesystem
         */
        fun current(): Filesystem {
            return Filesystem(File("").absolutePath.trimEnd(File.separatorChar))
        }

        /**
         * Static constructor
         * Project directory with end string
         *
         * @param end String
         * @return Filesystem
         */
        fun current(end: String): Filesystem {
            return Filesystem(current().uri.path + File.separator + end.trimStart(File.separatorChar))
        }

        /**
         * Relativize URI
         *
         * @param uri URI
         * @return URI
         */
        fun relativize(uri: URI): URI {
            if ("file" != uri.scheme || !uri.isAbsolute) {
                return uri
            }

            try {
                val relative = File("").toURI().relativize(uri)

                return if (relative.fragment === null) {
                    Filesystem(relative.schemeSpecificPart).uri
                } else {
                    Filesystem(relative.schemeSpecificPart, relative.fragment).uri
                }
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException(e.message, e)
            }
        }
    }

    val asFile: File
        get() = File(path)

    /**
     * Find absolute path
     *
     * @return Filesystem
     */
    fun absolute(): Filesystem {
        if (this.uri.scheme == FILE_SCHEME) {
            if (!File(this.uri.path).exists()) {
                throwNotExist(this.path)
            }

            return this
        }

        val resourcePath = scanner.scanForResourcesUri(this.uri).firstOrNull {
            it.toString().contains(toName(this.uri))
        }

        if (resourcePath === null) {
            throwNotExist(path)
        }

        return Filesystem(resourcePath.toString().substringBeforeLast(path) + path)
    }

    /**
     * Find directory path
     *
     * @return Filesystem
     */
    fun directory(): Filesystem {
        if (FilenameUtils.getFullPath(path).isEmpty()) {
            return this
        }

        if (FilenameUtils.getExtension(path).isNotEmpty()) {
            return Filesystem(FilenameUtils.getFullPath(path)).absolute()
        }

        return absolute()
    }

    /**
     * Find file path
     *
     * @return Filesystem
     */
    fun file(): Filesystem {
        return Filesystem(directory().path + FilenameUtils.getName(path))
    }

    /**
     * Safe find path
     *
     * @return Filesystem
     */
    fun safe(): Filesystem {
        try {
            return try {
                absolute()
            } catch (_: AssertionError) {
                current(path).absolute()
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        return this
    }

    /**
     * Find sub path for new file or directory
     *
     * @return Filesystem
     */
    fun new(): Filesystem {
        var temp = path
        var helper: Filesystem? = null

        while (temp.isNotEmpty()) {
            try {
                helper = that(temp).directory()
                break
            } catch (_: AssertionError) {
                temp = FilenameUtils.getFullPath(temp.trimEnd(File.separatorChar))
            }
        }

        if (helper === null || helper.path == File.separator) {
            helper = current()
        }

        val newPath = helper.path.trimEnd(File.separatorChar) + File.separator + path.replaceFirst(temp, "")
            .trimStart(File.separatorChar)

        return that(newPath.trimEnd(File.separatorChar))
    }

    /**
     * Convert resource uri to name
     *
     * @param uri URI
     * @return String
     */
    private fun toName(uri: URI): String {
        if (uri.scheme == ClasspathSupport.CLASSPATH_SCHEME) {
            return ClasspathSupport.resourceName(uri)
        }

        if (uri.path !== null) {
            return uri.path
        }

        return uri.schemeSpecificPart
    }

    /**
     * Throw not exists
     *
     * @param path String
     */
    private fun throwNotExist(path: String) {
        Assertions.fail<Any>("$path does not exist")
    }
}
