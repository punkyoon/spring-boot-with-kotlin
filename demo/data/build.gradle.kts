plugins {
    kotlin("plugin.jpa") version "1.9.25"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}