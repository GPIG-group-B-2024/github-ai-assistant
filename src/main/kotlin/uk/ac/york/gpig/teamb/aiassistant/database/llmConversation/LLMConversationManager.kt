package uk.ac.york.gpig.teamb.aiassistant.database.llmConversation

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.conversions.toJooqMessageRole
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.facades.LLMConversationReadFacade
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.facades.LLMConversationWriteFacade
import uk.ac.york.gpig.teamb.aiassistant.llm.client.OpenAIMessage
import java.util.UUID

@Service
class LLMConversationManager(
    @Autowired
    private val llmConversationWriteFacade: LLMConversationWriteFacade,
    @Autowired
    private val llmConversationReadFacade: LLMConversationReadFacade,
    @Autowired
    private val transactionTemplate: TransactionTemplate,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Start a new conversation and add the first message to it.
     * */
    fun initConversationWithFirstMessage(
        repoId: UUID,
        issueId: Int,
        role: OpenAIMessage.Role,
        content: String,
    ) {
        if (llmConversationReadFacade.checkConversationExists(repoId, issueId)) {
            throw Exception("Conversation about issue $issueId in repo $repoId already exists")
        }
        val conversationId = UUID.randomUUID()
        val messageId = UUID.randomUUID()

        transactionTemplate.execute {
            llmConversationWriteFacade.initConversation(conversationId, repoId, issueId)
            llmConversationWriteFacade.storeMessage(messageId, role.toJooqMessageRole(), content)
            llmConversationWriteFacade.linkMessageToConversation(conversationId, messageId)
        }
        logger.info("Stored new conversation with id $conversationId and first message with id $messageId")
    }
}
