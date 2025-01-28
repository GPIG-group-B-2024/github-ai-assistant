package uk.ac.york.gpig.teamb.aiassistant.utils.types

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchemaGenerator
import kotlin.reflect.KClass

/**
 * Generate a JSON schema from a given Kotlin type
 * */
fun <T : Any> KClass<T>.toJsonSchema(): String {
    val mapper = ObjectMapper()
    val schemaGenerator = JsonSchemaGenerator(mapper)
    val schema = schemaGenerator.generateSchema(this.java).cleanupForStructuredOutput()
    return mapper.writeValueAsString(schema)
}

/**
 * Remove the `id` field and reject additional properties for this schema and any nested object schemas.
 *
 * This prepares it for use with the structured output feature of ChatGPT and ensures the least possible chances of
 * unwanted data making it into the response.
 *
 * */
internal fun JsonSchema.cleanupForStructuredOutput(): JsonSchema =
    this.also {
        when {
            this.isObjectSchema -> {
                this.id = null // remove the id field (not needed)
                this.asObjectSchema().rejectAdditionalProperties()
                // ^ set `additionalProperties` to false (to prevent chatGPT from putting random fields into its answer)
                this.asObjectSchema().properties.forEach { (_, childSchema) -> childSchema.cleanupForStructuredOutput() }
                // ^ visit nested properties and check if any of them are objects/arrays too
            }
            this.isArraySchema -> this.asArraySchema().items.asSingleItems().schema.cleanupForStructuredOutput()
            // ^ we found a nested array schema, check if its items are object schemas and if so, make the changes
            // NOTE: assumes that arrays can only contain data of one type (fine for strongly typed languages)
            else -> Unit
            // ^ the schema is neither, stop
        }
    }
