plugins {
    id("org.jetbrains.kotlin.jvm") version embeddedKotlinVersion
    id("io.qameta.allure") version "2.11.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.qameta.allure")

    repositories {
        mavenLocal()
        mavenCentral()

        gradlePluginPortal()
    }

    dependencies {
        implementation("org.aspectj:aspectjrt:1.9.19")
        implementation("org.aspectj:aspectjweaver:1.9.19")
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
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.io=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/java.base=ALL-UNNAMED",
        )

        useTestNG()

        isScanForTestClasses = false
        testLogging.showStandardStreams = true
    }
}