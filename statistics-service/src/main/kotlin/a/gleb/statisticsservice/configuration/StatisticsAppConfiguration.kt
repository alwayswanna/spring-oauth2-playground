package a.gleb.statisticsservice.configuration

import a.gleb.statisticsservice.feign.CardFeignClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableFeignClients(
    clients = [CardFeignClient::class]
)
class StatisticsAppConfiguration