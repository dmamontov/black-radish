plugins {
    id("org.jetbrains.kotlin.jvm") version embeddedKotlinVersion
    id("org.jetbrains.dokka") version "1.7.20"
    id("io.qameta.allure") version "2.11.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://nexus.nuiton.org/nexus/content/groups/public/")
    gradlePluginPortal()
}

val globalAspectjVersion = "1.9.19"

val downloadOnly: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    implementation("org.aspectj:aspectjrt:$globalAspectjVersion")
    implementation("org.aspectj:aspectjweaver:$globalAspectjVersion")
    implementation("tech.mamontov.blackradish:core:1.0.0-alpha-1")
    {% if cookiecutter['module: filesystem'] == 'y' %}
    implementation("tech.mamontov.blackradish:filesystem:1.0.0-alpha-1")
    {% endif %}
    {% if cookiecutter['module: command'] == 'y' %}
    implementation("tech.mamontov.blackradish:command:1.0.0-alpha-1")
    {% endif %}
    {% if cookiecutter['module: ssh'] == 'y' %}
    implementation("tech.mamontov.blackradish:ssh:1.0.0-alpha-1")
    {% endif %}
    {% if cookiecutter['module: csv'] == 'y' %}
    implementation("tech.mamontov.blackradish:csv:1.0.0-alpha-1")
    {% endif %}
    {% if cookiecutter['module: csvdb'] == 'y' %}
    implementation("tech.mamontov.blackradish:csvdb:1.0.0-alpha-1")
    {% endif %}
    {% if cookiecutter['module: dbf'] == 'y' %}
    implementation("tech.mamontov.blackradish:dbf:1.0.0-alpha-1")
    {% endif %}

    downloadOnly("org.aspectj:aspectjweaver:$globalAspectjVersion")
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

allure {
    adapter {
        autoconfigureListeners.set(false)
        aspectjVersion.set(globalAspectjVersion)
        aspectjWeaver.set(true)
    }
}

tasks.test {
    filter {
        includeTestsMatching("tech.mamontov.blackradish.*")
    }

    jvmArgs = listOf(
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.io=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.util=ALL-UNNAMED",
        "--add-opens",
        "java.base/java.base=ALL-UNNAMED",
    )

    useTestNG()

    isScanForTestClasses = false

    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.register<Copy>("download") {
    from(downloadOnly)
    into("runtime/")
    rename {fileName: String ->
        fileName.replace("aspectjweaver-$globalAspectjVersion", "aspectj")
    }
}
