plugins {
    `java-library`
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-scripting-jsr223")

    api("org.apache.logging.log4j:log4j-api:2.19.0")
    api("org.apache.logging.log4j:log4j-core:2.19.0")

    api("org.slf4j:slf4j-api:2.0.5")
    api("org.slf4j:slf4j-simple:2.0.5")

    api("org.assertj:assertj-core:3.24.1")

    api("io.cucumber:cucumber-java:7.10.1")
    api("io.cucumber:cucumber-picocontainer:7.10.1")
    api("io.cucumber:cucumber-testng:7.10.1")

    api("io.qameta.allure:allure-cucumber7-jvm:2.20.0")

    api("org.apache.commons:commons-lang3:3.12.0")
    api("org.apache.commons:commons-text:1.10.0")
    api("org.apache.commons:commons-configuration2:2.8.0")

    api("commons-beanutils:commons-beanutils:1.9.4")
    api("commons-codec:commons-codec:1.15")
    api("commons-io:commons-io:2.11.0")

    api("org.reflections:reflections:0.10.2")

    api("com.github.javafaker:javafaker:1.0.2")

    api("net.objecthunter:exp4j:0.4.8")

    api("com.google.code.gson:gson:2.10.1")
}
