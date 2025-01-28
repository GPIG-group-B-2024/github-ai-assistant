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
        // step 1 : check if the top-level schema is an object (most likely yes) schema
        if (this.isObjectSchema) {
            this.id = null // remove the id field (not needed)
            // set `additionalProperties` to false (to prevent chatGPT from putting random fields into its answer)
            this.asObjectSchema().rejectAdditionalProperties()
        }
        // if the schema has any nested objects/arrays, visit them all and perform the above changes
        when {
            this.isObjectSchema -> {
                // we found a nested object schema. We need to remove the `id` field and set `additionalProperties` to false
                this.asObjectSchema().properties.forEach { (_, childSchema) ->
                    childSchema.cleanupForStructuredOutput()
                }
            }
            this.isArraySchema -> {
                // we found a nested array schema, check if its items are object schemas and if so, make the changes
                // NOTE: assumes that arrays can only contain data of one type (fine for strongly typed languages)
                this.asArraySchema().items.asSingleItems().schema.cleanupForStructuredOutput()
            }
            else -> Unit // the schema is neither, stop.
        }
    }
