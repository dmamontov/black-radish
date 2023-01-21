package tech.mamontov.blackradish.core.utils

import io.cucumber.core.resource.ClassLoaders
import io.cucumber.core.resource.ClasspathSupport
import io.cucumber.core.resource.Resource
import io.cucumber.core.resource.ResourceScanner
import org.apache.commons.io.FilenameUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import java.io.File
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.Path
import java.util.Optional

class UriHelper : Logged {
    companion object {
        private const val FILE_SCHEME = "file"

        private val scanner = ResourceScanner(
            { ClassLoaders.getDefaultClassLoader() },
            { path: Path -> path.toString().isNotEmpty() },
            { resource: Resource -> resourcePath(resource) },
        )

        fun create(path: String): URI {
            return URI("$FILE_SCHEME:$path")
        }

        private fun create(path: String, fragment: String?): URI {
            return URI(FILE_SCHEME, path, fragment)
        }

        fun uri(path: String): URI {
            var uri = URI(path)

            if (uri.scheme === null) {
                val scheme = if (path.first() == File.separatorChar) FILE_SCHEME else ClasspathSupport.CLASSPATH_SCHEME
                uri = URI("$scheme:$path")
            }

            if (uri.scheme == FILE_SCHEME) {
                if (!File(uri.path).exists()) {
                    throwNotExist(path)
                }

                return uri
            }

            val resourcePath = scanner.scanForResourcesUri(uri).firstOrNull {
                it.toString().contains(resourceName(uri))
            }
            if (resourcePath === null) {
                throwNotExist(path)
            }

            return create(resourcePath.toString().substringBeforeLast(path) + path)
        }

        fun uri(path: String, excludeName: Boolean = false): URI {
            if (!excludeName) {
                return create(path)
            }

            return create(path(path).path + FilenameUtils.getName(path))
        }

        fun current(): URI {
            return create(File("").absolutePath.trimEnd(File.separatorChar))
        }

        fun current(postfix: String): URI {
            return uri(create(current().path + File.separator + postfix.trimStart(File.separatorChar)).path)
        }

        fun path(path: String): URI {
            if (FilenameUtils.getFullPath(path).isEmpty()) {
                return create(path)
            }

            if (FilenameUtils.getExtension(path).isNotEmpty()) {
                return create(uri(FilenameUtils.getFullPath(path)).path)
            }

            return create(uri(path).path)
        }

        fun relativize(uri: URI): URI {
            if ("file" != uri.scheme || !uri.isAbsolute) {
                return uri
            }

            try {
                val relative = File("").toURI().relativize(uri)

                return create(relative.schemeSpecificPart, relative.fragment)
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException(e.message, e)
            }
        }

        private fun resourcePath(resource: Resource): Optional<Any> {
            return Optional.of(Reflecation.get(resource, "resource") as Path)
        }

        private fun resourceName(uri: URI): String {
            if (uri.scheme == ClasspathSupport.CLASSPATH_SCHEME) {
                return ClasspathSupport.resourceName(uri)
            }

            if (uri.path !== null) {
                return uri.path
            }

            return uri.schemeSpecificPart
        }

        private fun throwNotExist(path: String) {
            Assertions.fail<Any>("$path does not exist")
        }
    }
}
