package uk.ac.york.gpig.teamb.aiassistant.utils.types

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchemaGenerator
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.toJsonSchema(): String {
    val mapper = ObjectMapper()
    val schemaGenerator = JsonSchemaGenerator(mapper)
    val schema = schemaGenerator.generateSchema(this.java).asObjectSchema()
    return mapper.writeValueAsString(schema).replace(
        """"type":"object",""",
        """"type":"object","additionalProperties":false,""",
        // ^ disable additional properties for all objects. NOTE: quick and dirty.
        // Will need to investigate if this is good enough for the actual structured output type we will end up using.
    )
}
