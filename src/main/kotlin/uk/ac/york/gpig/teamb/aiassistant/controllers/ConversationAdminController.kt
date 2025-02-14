package uk.ac.york.gpig.teamb.aiassistant.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.LLMConversationManager
import java.util.UUID

@Controller
@RequestMapping("/admin")
class ConversationAdminController(
    @Autowired
    private val llmConversationManager: LLMConversationManager,
) {
    @GetMapping("/conversations")
    fun index(model: Model): String {
        val conversations = llmConversationManager.fetchConversations()
        model.addAttribute("conversationCount", conversations.size)
        model.addAttribute("data", conversations)
        return "admin/index"
    }

    @GetMapping("/conversations/{conversationId}")
    fun conversationPage(
        model: Model,
        @PathVariable conversationId: UUID,
    ): String {
        val messages = llmConversationManager.fetchConversationMessages(conversationId)
        model.addAttribute("conversationId", conversationId)
        model.addAttribute("messageCount", messages.size)
        model.addAttribute("data", messages)
        return "admin/conversation"
    }
}
