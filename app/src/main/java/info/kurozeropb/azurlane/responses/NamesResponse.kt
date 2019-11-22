package info.kurozeropb.azurlane.responses

data class NamesResponse(
    val statusCode: Int,
    val statusMessage: String,
    val message: String,
    val ships: List<String>
)