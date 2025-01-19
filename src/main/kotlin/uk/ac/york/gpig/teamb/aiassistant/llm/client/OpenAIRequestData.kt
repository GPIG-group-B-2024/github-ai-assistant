package uk.ac.york.gpig.teamb.aiassistant.llm.client

data class OpenAIMessage(
    val role: Role,
    val message: String,
) {
    /**
     * What kind of prompt this message is: system vs user.
     * */
    enum class Role {
        USER,
        SYSTEM,
    }
}

sealed class OpenAIRequestData {
    abstract val model: String
    abstract val messages: List<OpenAIMessage>
}

data class OpenAIStructuredRequestData(
    val responseFormat: String,
    override val model: String,
    override val messages: List<OpenAIMessage>,
) : OpenAIRequestData()

data class OpenAIUnstructuredRequestData(
    override val model: String,
    override val messages: List<OpenAIMessage>,
) : OpenAIRequestData()
