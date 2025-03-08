package uk.ac.york.gpig.teamb.aiassistant.llm.responseSchemas

data class ChangesResponseSchema(
    val changeList: List<Change>,
) {
    data class Change(
        val type: String, // TODO: make enum
        val fileName: String,
        val contents: String,
    )
}