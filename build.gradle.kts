plugins {
    id("org.jetbrains.kotlin.jvm") version embeddedKotlinVersion
    id("org.jetbrains.dokka") version "1.7.20" apply false
    id("com.avast.gradle.docker-compose") version "0.16.11" apply false
    id("io.qameta.allure") version "2.11.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    gradlePluginPortal()
}

val globalAspectjVersion = "1.9.19"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.avast.gradle.docker-compose")
    apply(plugin = "io.qameta.allure")

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        api("org.aspectj:aspectjrt:$globalAspectjVersion")
        api("org.aspectj:aspectjweaver:$globalAspectjVersion")
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
}

val downloadOnly: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    downloadOnly("org.aspectj:aspectjweaver:$globalAspectjVersion")
}

tasks.register<Copy>("download") {
    from(downloadOnly)
    into("runtime/")
    rename {fileName: String ->
        fileName.replace("aspectjweaver-$globalAspectjVersion", "aspectj")
    }
}
