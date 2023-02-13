dependencies {
    implementation(project(":core"))
    implementation("com.sshtools:maverick-synergy-client:3.0.10")
    implementation("com.github.mwiede:jsch:0.2.6")

    api(project(":modules:filesystem"))
    api(project(":modules:command"))
}

dockerCompose {
    isRequiredBy(tasks.test)
}
