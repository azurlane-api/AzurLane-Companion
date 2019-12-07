package info.kurozeropb.alcompanion.responses

data class Nation(
        val name: String? = null,
        val prefix: String? = null,
        val icon: String? = null
)

typealias Nations = List<Nation>

data class NationsResponse(
        val statusCode: Int,
        val statusMessage: String,
        val message: String,
        val nations: Nations
)