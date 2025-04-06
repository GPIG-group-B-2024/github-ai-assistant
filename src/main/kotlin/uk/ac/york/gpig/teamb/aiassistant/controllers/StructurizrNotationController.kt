package uk.ac.york.gpig.teamb.aiassistant.controllers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import uk.ac.york.gpig.teamb.aiassistant.controllers.data.StructurizrWriteRequestBody
import uk.ac.york.gpig.teamb.aiassistant.database.c4.C4Manager

@Controller
class StructurizrNotationController(
    val c4Manager: C4Manager,
) {
    /**
     * An endpoint for manually storing a structurizr diagram and associating it with a github repo.
     *
     * @param repoName The name of the github repo in the `owner/repo` format
     * @param
     * */
    @ResponseBody
    @PostMapping("/admin/structurizr/{repoName}")
    fun writeStructurizrRepresentation(
        @PathVariable repoName: String,
        @RequestBody workspaceData: StructurizrWriteRequestBody,
    ) = c4Manager.initializeWorkspace(repoName, workspaceData.repoUrl, workspaceData.rawStructurizr)

    @GetMapping("/admin/structurizr")
    fun writeStructurizrForm(
        model: Model,
        @AuthenticationPrincipal principal: OidcUser,
    ): String{
        model.run{
            addAttribute("profile", principal.claims)
        }
        return "/admin/structurizr/structurizr_input_form"
    }
}
