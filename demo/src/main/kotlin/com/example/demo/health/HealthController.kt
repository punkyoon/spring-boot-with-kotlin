package com.example.demo.health

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/health")
    fun checkHealth(): HealthResponse {
        logger.info("Checking health..")

        return HealthResponse(status = "OK")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(HealthController::class.java)
    }
}