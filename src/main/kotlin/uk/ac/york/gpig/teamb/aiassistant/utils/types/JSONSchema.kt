package uk.ac.york.gpig.teamb.aiassistant.utils.types

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchemaGenerator
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.toJsonSchema(): String  {
    val mapper = ObjectMapper()
    val schemaGenerator = JsonSchemaGenerator(mapper)
    val schema = schemaGenerator.generateSchema(this.java).asObjectSchema()
    return mapper.writeValueAsString(schema)
}
