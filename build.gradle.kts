plugins {
    id("org.jetbrains.kotlin.jvm") version embeddedKotlinVersion
    id("com.avast.gradle.docker-compose") version "0.16.11" apply false
    id("org.jetbrains.dokka") version "1.7.20"
    id("io.qameta.allure") version "2.11.2"
    `maven-publish`
    signing
}

val globalAspectjVersion = "1.9.19"

allprojects {
    group = "tech.mamontov.blackradish"
    version = findProperty("TAG") ?: "0.0.0-SNAPSHOT"

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.avast.gradle.docker-compose")
    apply(plugin = "io.qameta.allure")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }

    dependencies {
        api("org.aspectj:aspectjrt:$globalAspectjVersion")
        api("org.aspectj:aspectjweaver:$globalAspectjVersion")
        dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
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

subprojects {
    java {
        withSourcesJar()
    }

    val kDocJar by tasks.creating(Jar::class) {
        archiveClassifier.set("kdoc")
        from(tasks.findByName("dokkaHtml"))
    }

    val javaDocJar by tasks.creating(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.findByName("dokkaJavadoc"))
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])

                artifact(kDocJar)
                artifact(javaDocJar)

                artifactId = tasks.jar.get().archiveBaseName.get()

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }

                val artifactName = artifactId.capitalize()

                pom {
                    name.set("Module $artifactName")
                    description.set("Black Radish is a general purpose test automation framework.")
                    url.set("https://blackradish.mamontov.tech")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/mit-license.php")
                        }
                    }
                    issueManagement {
                        system.set("Github")
                        url.set("https://github.com/dmamontov/black-radish/issues")
                    }
                    developers {
                        developer {
                            id.set("dmamontov")
                            name.set("Dmitry Mamontov")
                            email.set("d@mamontov.tech")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/dmamontov/black-radish.git")
                        developerConnection.set("scm:git:ssh://github.com/dmamontov/black-radish.git")
                        url.set("https://github.com/dmamontov/black-radish")
                    }
                }
            }
        }

        repositories {
            maven {
                val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                credentials {
                    username = (findProperty("MAVEN_USERNAME") ?: "").toString()
                    password = (findProperty("MAVEN_PASSWORD") ?: "").toString()
                }
            }
        }
    }

    signing {
        sign(publishing.publications["maven"])
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
