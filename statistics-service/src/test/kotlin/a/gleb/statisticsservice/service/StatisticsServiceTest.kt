package a.gleb.statisticsservice.service

import a.gleb.statisticsservice.StatisticsServiceApplicationTests
import a.gleb.statisticsservice.WIREMOCK_PORT
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils

@WireMockTest(httpPort = WIREMOCK_PORT)
class StatisticsServiceTest : StatisticsServiceApplicationTests() {

    @Autowired
    private lateinit var statisticsService: StatisticsService
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `load statistic test`() {
        statisticsService.saveStatistic()
        val count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "user_statistics")
        assertNotNull(count)
        assertEquals(1, count)
    }
}