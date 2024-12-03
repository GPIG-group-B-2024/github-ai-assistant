package uk.ac.york.gpig.teamb.aiassistant.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.ac.york.gpig.teamb.aiassistant.llm.StructuredOutput
import uk.ac.york.gpig.teamb.aiassistant.utils.types.toJsonSchema

class JSONSchemaTest {
    @Test
    fun `converts object to schema`()  {
        expectThat(StructuredOutput::class.toJsonSchema()).isEqualTo(
            """
            {
              "type": "object",
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
