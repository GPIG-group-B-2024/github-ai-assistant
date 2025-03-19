package uk.ac.york.gpig.teamb.aiassistant.utils.web

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * GitHub seems to send duplicate webhook payloads which can only be differentiated by their header.
 * For this project, we are only interested in "repository" level events. No-op for others.
 *
 * This component also checks the signature attached to the webhook payload to ensure that the webhook is being sent by github.
 * */
@Component
class WebhookValidationFilter(
    @Value("\${app_settings.github_webhook_secret}")
    val githubSecret: String,
) : OncePerRequestFilter() {
    companion object {
        /**
         * Algorithm used by github to encrypt their signature
         * */
        const val ALGORITHM = "HmacSHA256"
    }

    /**
     * Only apply filter to webhook endpoint, others will use oauth
     * */
    override fun shouldNotFilter(request: HttpServletRequest): Boolean = !request.requestURI.contains("/webhooks")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val cachedRequest = ContentCachingRequestWrapper(request)
        filterChain.doFilter(
            cachedRequest,
            response,
        ) // we need to do this in order to cache this request and read its body several times (once here, once in the controller)
        val targetHeader: String? = cachedRequest.getHeader("x-github-hook-installation-target-type")
        // get bytes of the webhook signature without the prefix (if missing, stop. The cachedRequest is not passing the filter)
        val payloadHashHex =
            cachedRequest.getHeader("x-hub-signature-256")?.replace("sha256=", "")
        if (payloadHashHex.isNullOrEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value())
            return
        }
        // Following github instructions here. Use the secret as the key and encrypt the cachedRequest body to check the hashes
        val hmacSha256 = Mac.getInstance(ALGORITHM).apply { init(SecretKeySpec(githubSecret.toByteArray(), ALGORITHM)) }
        val cachedRequestBody = cachedRequest.inputStream.readAllBytes()
        // convert the encrypted bytes to hex string
        val expectedHashHex = hmacSha256.doFinal(cachedRequestBody).joinToString("") { "%02x".format(it) }
        when {
            targetHeader == "repository" &&
                constantTimeCompare(
                    payloadHashHex,
                    expectedHashHex,
                )
            -> filterChain.doFilter(cachedRequest, response) // we have received a request from github itself (and validated it)
            else -> response.sendError(HttpStatus.FORBIDDEN.value())
        } // request has the github header but the value is wrong. This is a duplicate request from smee. Do not let through.
    }

    /**
     * Compare two hash strings **completely** before returning a decision.
     *
     * Recommended by [github](https://docs.github.com/en/webhooks/using-webhooks/validating-webhook-deliveries#validating-webhook-deliveries) to avoid timing attacks
     * */
    private fun constantTimeCompare(
        left: String,
        right: String,
    ): Boolean = left.length == right.length && left.zip(right).fold(true) { acc, (left, right) -> acc && (left == right) }
}
