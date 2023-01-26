plugins {
    `java-library`
}

dependencies {
    implementation(project(":core"))
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    api(project(":modules:filesystem"))
}
