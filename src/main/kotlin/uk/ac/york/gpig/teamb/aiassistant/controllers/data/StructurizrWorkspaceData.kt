package uk.ac.york.gpig.teamb.aiassistant.controllers.data

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

/**
 * Represents the data submitted through the form in the admin dashboard.
 * */
data class StructurizrWorkspaceData(
    // repo name in the owner/repo format e.g. some-text-with-no-slashes/more-text-no-slashes
    @field:NotBlank
    @field:Pattern("[^\\/]*\\/[^\\/]*")
    val repoName: String? = null,
    @field:NotBlank
    val repoUrl: String? = null,
    @field:NotBlank
    val rawStructurizr: String? = null,
)
