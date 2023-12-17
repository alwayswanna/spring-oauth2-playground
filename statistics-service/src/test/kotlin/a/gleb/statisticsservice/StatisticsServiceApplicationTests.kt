package a.gleb.statisticsservice

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

const val WIREMOCK_PORT = 8090

@SpringBootTest
@ExtendWith(SpringExtension::class)
abstract class StatisticsServiceApplicationTests
