package a.gleb.cardservice.controller

import a.gleb.cardservice.CARD_REQUEST_URI
import a.gleb.cardservice.CardServiceApplicationTests
import a.gleb.cardservice.STATISTIC_REQUEST_URI
import a.gleb.cardservice.buildRequest
import a.gleb.cardservice.model.CardResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class StatisticsControllerTest : CardServiceApplicationTests() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `load statistic test`() {
        val response = mockMvc.perform(
            get(STATISTIC_REQUEST_URI)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponse = objectMapper.readValue<List<CardResponse>>(response.contentAsString)
        cardResponse.forEach {
            mockMvc.perform(delete(CARD_REQUEST_URI).param("id", "${it.cardId}"))
                .andExpect(status().isOk)
        }

        mockMvc.perform(buildRequest()).andExpect(status().isOk)
        val responseAssert = mockMvc.perform(
            get(STATISTIC_REQUEST_URI)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponseAssert = objectMapper.readValue<List<CardResponse>>(responseAssert.contentAsString)
        assertEquals(1, cardResponseAssert.size)
    }
}