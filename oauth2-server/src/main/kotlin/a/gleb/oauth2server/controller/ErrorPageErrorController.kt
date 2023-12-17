package a.gleb.oauth2server.controller

import a.gleb.oauth2server.constants.EMPTY
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ErrorPageErrorController : ErrorController {

    @RequestMapping("/error")
    fun error(model: Model, request: HttpServletRequest): String {
        val errorMessage: String = getErrorMessage(request)
        if (errorMessage.startsWith("[access_denied]")) {
            model.addAttribute("errorTitle", "Access Denied")
            model.addAttribute("errorMessage", "You have denied access.")
        } else {
            model.addAttribute("errorTitle", "Error")
            model.addAttribute("errorMessage", errorMessage)
        }

        return "error"
    }

    private fun getErrorMessage(request: HttpServletRequest): String {
        val errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE)
        if (errorMessage is String && errorMessage.toString().isNotEmpty()) {
            return errorMessage.toString()
        }

        return EMPTY
    }
}