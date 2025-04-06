package uk.ac.york.gpig.teamb.aiassistant.controllers.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The data required to associate a github repository to a structurizr workspace
 *
 * @param repoUrl The full URL of the github repository
 * @param rawStructurizr The __full__, raw structurizr code snippet representing the workspace
 * */
data class StructurizrWriteRequestBody(
    @JsonProperty("repo_url")
    val repoUrl: String,
    @JsonProperty("raw_structurizr")
    val rawStructurizr: String,
)
