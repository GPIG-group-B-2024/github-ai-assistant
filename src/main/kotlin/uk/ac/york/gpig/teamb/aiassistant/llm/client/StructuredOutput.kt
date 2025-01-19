package uk.ac.york.gpig.teamb.aiassistant.llm.client

data class StructuredOutput(
    val pullRequestBody: String,
    val pullRequestTitle: String,
    val updatedFiles: List<UpdatedFile>,
) {
    data class UpdatedFile(
        val fullName: String,
        val newContents: String,
    )
}
