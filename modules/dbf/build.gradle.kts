repositories {
    maven("https://nexus.nuiton.org/nexus/content/groups/public/")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":modules:csvdb"))
    implementation("nl.knaw.dans.common:dans-dbf-lib:1.0.0-beta-10")
}
