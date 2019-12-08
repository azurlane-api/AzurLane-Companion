package info.kurozeropb.alcompanion.responses

data class Equipment(
        val name: String? = null,
        val icon: String? = null,
        val rarity: String? = null
)

typealias Equipments = List<Equipment>

data class EquipmentsResponse(
        val statusCode: Int,
        val statusMessage: String,
        val message: String,
        val equipments: Equipments
)

data class EquipmentType(
        val name: String? = null,
        val url: String? = null
)

typealias EquipmentTypes = List<EquipmentType>

data class EquipmentTypesResponse(
        val statusCode: Int,
        val statusMessage: String,
        val message: String,
        val equipments: EquipmentTypes
)