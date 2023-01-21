plugins {
    `java-library`
}

dependencies {
    api(project(":core"))
    api(project(":modules:filesystem"))
    api(project(":modules:command"))
    implementation("com.sshtools:maverick-synergy-client:3.0.10")
    implementation("com.github.mwiede:jsch:0.2.6")
}

dockerCompose {
    isRequiredBy(tasks.test)
}
