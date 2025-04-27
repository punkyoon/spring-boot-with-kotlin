springBoot {
    mainClass.set("com.example.demo.DemoApplicationKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.security:spring-security-test")

    testImplementation(kotlin("test"))
}
