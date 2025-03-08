package uk.ac.york.gpig.teamb.aiassistant.llm.responseSchemas

data class LLMPullRequestData(
    val pullRequestBody: String,
    val pullRequestTitle: String,
    val gitPath: String, // idk if this belongs here
    val branchName: String, // idk if this belongs here
    val changeList: List<Change>,
) {
    data class Change(
        val type: String, // TODO: make enum
        val filePath: String, // full file path (not sure if can be enforced)
        val contents: String,
    )
}
