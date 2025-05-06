dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.auth0:java-jwt:4.5.0")

    testImplementation(kotlin("test"))
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}