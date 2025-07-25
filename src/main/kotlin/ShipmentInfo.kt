import kotlinx.serialization.Serializable

@Serializable
data class ShipmentInfo(
    val id: String,
    val type: String,
    val status: String,
    val location: String,
    val expectedDelivery: String,
    val updates: List<String>,
    val notes: List<String>
)