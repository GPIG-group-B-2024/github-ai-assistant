package uk.ac.york.gpig.teamb.aiassistant.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.LLMConversationManager

@Controller
@RequestMapping("/admin")
class ConversationAdminController(
    @Autowired
    private val llmConversationManager: LLMConversationManager,
) {
    @GetMapping("/index")
    fun index(model: Model): String {
        val conversations = llmConversationManager.fetchConversations()
        model.addAttribute("conversationCount", conversations.size)
        model.addAttribute("data", conversations)
        return "admin/index"
    }
}
