package info.kurozeropb.alcompanion.responses

typealias Ships = List<AllShip>

data class AllShip(
    val name: String?,
    val id: String?,
    val avatar: String?,
    val type: String?,
    val nationality: String?,
    val rarity: String?
)

data class AllShipsResponse(
    val statusCode: Int,
    val statusMessage: String,
    val message: String,
    val ships: Ships
)