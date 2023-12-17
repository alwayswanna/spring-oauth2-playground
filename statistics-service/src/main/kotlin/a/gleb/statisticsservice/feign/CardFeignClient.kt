package a.gleb.statisticsservice.feign

import a.gleb.statisticsservice.model.CardResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "card-service",
    url = "\${statistics-service.card-service.url}"
)
interface CardFeignClient {

    @GetMapping("/api/v1/statistics")
    fun statistics(): List<CardResponse>
}