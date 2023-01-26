plugins {
    `java-library`
}

dependencies {
    implementation(project(":core"))
    implementation("org.apache.commons:commons-csv:1.9.0")

    api(project(":modules:filesystem"))
    api("net.sourceforge.csvjdbc:csvjdbc:1.0.40")
}
