package uk.ac.york.gpig.teamb.aiassistant.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import uk.ac.york.gpig.teamb.aiassistant.llm.client.StructuredOutput
import uk.ac.york.gpig.teamb.aiassistant.testutils.assertions.JsonAssertions.Companion.isEqualToJson
import uk.ac.york.gpig.teamb.aiassistant.utils.types.toJsonSchema

class JSONSchemaTest {
    @Test
    fun `converts object to schema`() {
        expectThat(StructuredOutput::class.toJsonSchema()).isEqualToJson(
            """
            {
              "type": "object",
              "additionalProperties": false,
              "id": "urn:jsonschema:uk:ac:york:gpig:teamb:aiassistant:llm:StructuredOutput",
              "properties": {
                "name": {
                  "type": "string"
                },
                "age": {
                  "type": "integer"
                },
                "cars": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "additionalProperties": false,
                    "id": "urn:jsonschema:uk:ac:york:gpig:teamb:aiassistant:llm:StructuredOutput:Car",
                    "properties": {
                      "make": {
                        "type": "string"
                      },
                      "model": {
                        "type": "string"
                      }
                    }
                  }
                }
              }
            }
        """.replace("[\\s]".toRegex(), ""),
        )
    }
}
