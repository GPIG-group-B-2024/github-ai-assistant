package uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.conversions

import uk.ac.york.gpig.teamb.aiassistant.enums.LlmMessageRole
import uk.ac.york.gpig.teamb.aiassistant.llm.client.OpenAIMessage.Role

fun LlmMessageRole.toOpenAIMessageRole(): Role =
    when (this) {
        LlmMessageRole.system -> Role.SYSTEM
        LlmMessageRole.user -> Role.USER
        LlmMessageRole.assistant -> Role.ASSISTANT
    }

fun Role.toJooqMessageRole(): LlmMessageRole =
    when (this) {
        Role.USER -> LlmMessageRole.user
        Role.SYSTEM -> LlmMessageRole.system
        Role.ASSISTANT -> LlmMessageRole.assistant
    }
