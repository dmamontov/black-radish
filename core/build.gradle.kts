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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223")

    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")

    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.5")

    implementation("org.assertj:assertj-core:3.24.1")

    implementation("io.cucumber:cucumber-java:7.10.1")
    implementation("io.cucumber:cucumber-picocontainer:7.10.1")
    implementation("io.cucumber:cucumber-testng:7.10.1")

    implementation("io.qameta.allure:allure-cucumber7-jvm:2.20.0")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.apache.commons:commons-configuration2:2.8.0")

    implementation("commons-beanutils:commons-beanutils:1.9.4")
    implementation("commons-codec:commons-codec:1.15")

    implementation("org.reflections:reflections:0.10.2")

    implementation("com.github.javafaker:javafaker:1.0.2")

    implementation("net.objecthunter:exp4j:0.4.8")

    implementation("com.google.code.gson:gson:2.10.1")
}
