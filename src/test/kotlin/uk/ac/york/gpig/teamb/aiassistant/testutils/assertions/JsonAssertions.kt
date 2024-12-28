package uk.ac.york.gpig.teamb.aiassistant.testutils.assertions
import com.google.gson.Gson
import com.google.gson.JsonElement
import strikt.api.Assertion.Builder
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class JsonAssertions {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> Builder<T>.isEqualToJson(jsonString: String): Builder<T> {
            val first: JsonElement = Gson().toJsonTree(this.subject)
            val second: JsonElement = Gson().toJsonTree(jsonString)
            return expectThat(first).isEqualTo(second) as Builder<T>
        }
    }
}
