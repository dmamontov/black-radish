plugins {
    `java-library`
}

dependencies {
    api(project(":core"))
    implementation("com.sshtools:forker-client:1.6.4")
}
