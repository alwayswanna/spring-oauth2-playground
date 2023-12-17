package a.gleb.cardservice

import a.gleb.cardservice.security.WithJwtUser
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

fun buildRequest() = MockMvcRequestBuilders.post(CARD_REQUEST_URI)
    .contentType(MediaType.APPLICATION_JSON)
    .content(
        """
                            {
                              "title": "Card title",
                              "description": "Card description"
                            }
                        """
    )

const val CARD_REQUEST_URI = "/api/v1/card"
const val STATISTIC_REQUEST_URI = "/api/v1/statistics"

@WithJwtUser
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
abstract class CardServiceApplicationTests
