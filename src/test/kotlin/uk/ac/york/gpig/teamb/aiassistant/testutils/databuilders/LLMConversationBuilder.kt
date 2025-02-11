package uk.ac.york.gpig.teamb.aiassistant.testutils.databuilders

import org.jooq.DSLContext
import java.util.UUID

class LLMConversationBuilder : TestDataWithIdBuilder<LLMConversationBuilder, UUID?>() {
    override var id: UUID? = UUID.randomUUID()

    override fun create(ctx: DSLContext): LLMConversationBuilder {
        TODO("Not yet implemented")
    }
}
