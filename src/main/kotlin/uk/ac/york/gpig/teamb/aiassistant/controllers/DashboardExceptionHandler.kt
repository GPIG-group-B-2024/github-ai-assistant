package uk.ac.york.gpig.teamb.aiassistant.controllers

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice(assignableTypes = [ConversationAdminController::class])
class DashboardExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleAccessDeniedException(
        ex: ResponseStatusException,
        @AuthenticationPrincipal principal: OidcUser?,
        model: Model,
        resp: HttpServletResponse,
    ): String {
        return when (ex.statusCode) {
            // populate the "unauthorized" template with user data
            HttpStatus.FORBIDDEN ->
                model.addAttribute("profile", principal?.claims).let {
                    resp.status = HttpStatus.FORBIDDEN.value()
                    "error/403"
                }
            // some other error, return the generic error page
            else -> "error"
        }
    }
}
