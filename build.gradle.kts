plugins {
    id("org.jetbrains.kotlin.jvm") version embeddedKotlinVersion
    id("com.avast.gradle.docker-compose") version "0.16.11" apply false
    id("io.qameta.allure") version "2.11.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.avast.gradle.docker-compose")
    apply(plugin = "io.qameta.allure")

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        api("org.aspectj:aspectjrt:1.9.19")
        api("org.aspectj:aspectjweaver:1.9.19")
    }

    configurations {
        implementation {
            resolutionStrategy.failOnVersionConflict()
        }
    }

    allure {
        adapter {
            autoconfigureListeners.set(false)
            aspectjVersion.set("1.9.19")
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
