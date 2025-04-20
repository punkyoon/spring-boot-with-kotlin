package com.example.demo.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/health")
    fun checkHealth(): HealthResponse {
        return HealthResponse(status = "OK")
    }
}