package a.gleb.oauth2server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginPageController {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping
    fun authenticated(): String {
        return "authorized"
    }
}