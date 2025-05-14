plugins {
    id("com.google.cloud.tools.jib") version "3.4.5"
}

springBoot {
    mainClass.set("com.example.demo.DemoApplicationKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.auth0:java-jwt:4.5.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.security:spring-security-test")
}

jib {
    from {
        image = "amazoncorretto:21-alpine3.21"

        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }

    to {
        image = "demo"
        tags = mutableSetOf("1.0", "latest")
    }

    container {
        mainClass = "com.example.demo.DemoApplicationKt"
        ports = listOf("8080")

        jvmFlags = listOf(
            "-Dspring.profiles.active=prod",
            "-XX:+UseContainerSupport",
            "-Dserver.port=8080",
            "-Dfile.encoding=UTF-8"
        )

        environment = mapOf("SPRING_OUTPUT_ANSI_ENABLED" to "ALWAYS")
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}