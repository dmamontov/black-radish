plugins {
    `java-library`
}

dependencies {
    api(project(":core"))
    api(project(":modules:command"))
    implementation("com.github.mwiede:jsch:0.2.6")
}
