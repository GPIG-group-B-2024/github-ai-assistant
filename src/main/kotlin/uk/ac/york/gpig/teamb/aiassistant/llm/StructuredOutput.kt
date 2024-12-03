package uk.ac.york.gpig.teamb.aiassistant.llm

data class StructuredOutput(
    val name: String,
    val age: Int,
    val cars: List<Car>,
) {
    data class Car(
        val make: String,
        val model: String,
    )
}
