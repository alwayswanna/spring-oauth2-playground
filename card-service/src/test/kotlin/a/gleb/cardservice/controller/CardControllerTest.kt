package a.gleb.cardservice.controller

import a.gleb.cardservice.CARD_REQUEST_URI
import a.gleb.cardservice.CardServiceApplicationTests
import a.gleb.cardservice.buildRequest
import a.gleb.cardservice.model.CardResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class CardControllerTest : CardServiceApplicationTests() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `create new card test`() {
        val response = mockMvc.perform(
            buildRequest()
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponse = objectMapper.readValue<CardResponse>(response.contentAsString)
        assertEquals("Card title", cardResponse.cardTitle)
        assertEquals("Card description", cardResponse.cardDescription)
        assertEquals(UUID.fromString("c5a7f1e2-760e-4fb5-961a-00112ec1358c"), cardResponse.userId)
        assertNotNull(cardResponse.cardId)
    }

    @Test
    fun `create new card without description test`() {
        val response = mockMvc.perform(
            post(CARD_REQUEST_URI)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                        {
                          "title": "Card title"
                        }
                    """
                )
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponse = objectMapper.readValue<CardResponse>(response.contentAsString)
        assertEquals("Card title", cardResponse.cardTitle)
        assertEquals(UUID.fromString("c5a7f1e2-760e-4fb5-961a-00112ec1358c"), cardResponse.userId)
        assertNotNull(cardResponse.cardId)
    }

    @Test
    fun `update card test`() {
        val createdCardId = createCard()

        val response = mockMvc.perform(
            put(CARD_REQUEST_URI)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                      {
                        "id": "$createdCardId",
                        "title": "New title",
                        "description": "New description"
                      }
                    """
                )
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponse = objectMapper.readValue<CardResponse>(response.contentAsString)
        assertEquals("New title", cardResponse.cardTitle)
        assertEquals("New description", cardResponse.cardDescription)
        assertEquals(UUID.fromString("c5a7f1e2-760e-4fb5-961a-00112ec1358c"), cardResponse.userId)
        assertEquals(createdCardId, cardResponse.cardId)
    }

    @Test
    fun `get card test`() {
        val createdCardId = createCard()

        val response = mockMvc.perform(
            get(CARD_REQUEST_URI)
                .param("id", "$createdCardId")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponse = objectMapper.readValue<CardResponse>(response.contentAsString)
        assertNotNull(cardResponse.cardTitle)
        assertNotNull(cardResponse.cardDescription)
        assertNotNull(cardResponse.userId)
        assertNotNull(cardResponse.cardId)
    }

    @Test
    fun `get not existing card test`() {
        val createdCardId = UUID.randomUUID()

        mockMvc.perform(
            get(CARD_REQUEST_URI)
                .param("id", "$createdCardId")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `delete card test`() {
        val createdCardId = createCard()

        mockMvc.perform(delete(CARD_REQUEST_URI).param("id", "$createdCardId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `delete not existing card test`() {
        mockMvc.perform(delete(CARD_REQUEST_URI).param("id", "${UUID.randomUUID()}"))
            .andExpect(status().isNotFound)
    }

    /**
     * Method create entity for delete/get/put tests.
     */
    private fun createCard(): UUID {
        val response = mockMvc.perform(
            buildRequest()
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val cardResponse = objectMapper.readValue<CardResponse>(response.contentAsString)
        assertNotNull(cardResponse.cardId)

        return cardResponse.cardId!!
    }
}