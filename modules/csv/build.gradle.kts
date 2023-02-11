plugins {
    `java-library`
}

dependencies {
    implementation(project(":core"))
    implementation(project(":modules:filesystem"))
    implementation("org.apache.commons:commons-csv:1.9.0")
}
