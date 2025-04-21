dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = false
    }
}