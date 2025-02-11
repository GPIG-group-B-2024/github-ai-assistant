package uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.facades

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.entities.LLMConversationEntity
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.entities.LLMMessageEntity
import uk.ac.york.gpig.teamb.aiassistant.tables.references.CONVERSATION_MESSAGE
import uk.ac.york.gpig.teamb.aiassistant.tables.references.LLM_CONVERSATION
import uk.ac.york.gpig.teamb.aiassistant.tables.references.LLM_MESSAGE
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class LLMConversationReadFacade(
    @Autowired val ctx: DSLContext,
) {
    fun listConversationMessages(conversationId: UUID): List<LLMMessageEntity> =
        ctx.select(
            LLM_MESSAGE.ID,
            LLM_MESSAGE.CONTENT,
            LLM_MESSAGE.CREATED_AT,
            LLM_MESSAGE.ROLE,
        )
            .from(LLM_MESSAGE)
            .leftJoin(CONVERSATION_MESSAGE)
            .on(LLM_MESSAGE.ID.eq(CONVERSATION_MESSAGE.MESSAGE_ID))
            .where(CONVERSATION_MESSAGE.CONVERSATION_ID.eq(conversationId))
            .orderBy(LLM_MESSAGE.CREATED_AT)
            .fetch(LLMMessageEntity::fromJooq)

    fun fetchConversation(
        repoId: UUID,
        issueId: Int,
        createdAt: OffsetDateTime,
    ): LLMConversationEntity? =
        ctx.selectFrom(LLM_CONVERSATION)
            .where(
                LLM_CONVERSATION.REPO_ID.eq(
                    repoId,
                )
                    .and(
                        LLM_CONVERSATION.ISSUE_ID.eq(issueId)
                            .and(LLM_CONVERSATION.CREATED_AT.eq(createdAt)),
                    ),
            )
            .fetchOne(LLMConversationEntity::fromJooq)
}
