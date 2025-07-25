import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
open class Shipment(
    val id: String,
    val type: String,
    val creationDate: String,
    val expectedDelivery: String,
    var status: String,
    var abnormalMessage: String? = null
)

class StandardShipment(id: String, creationDate: String, expectedDelivery: String, status: String) :
    Shipment(id, "Standard", creationDate, expectedDelivery, status)

class ExpressShipment(id: String, creationDate: String, expectedDelivery: String, status: String) :
    Shipment(id, "Express", creationDate, expectedDelivery, status) {
    init {
        if (daysBetween(creationDate, expectedDelivery) > 3)
            abnormalMessage = "Express shipment delivery is more than 3 days"
    }
}

class OvernightShipment(id: String, creationDate: String, expectedDelivery: String, status: String) :
    Shipment(id, "Overnight", creationDate, expectedDelivery, status) {
    init {
        if (daysBetween(creationDate, expectedDelivery) > 1)
            abnormalMessage = "Overnight shipment expected after 24 hours"
    }
}

class BulkShipment(id: String, creationDate: String, expectedDelivery: String, status: String) :
    Shipment(id, "Bulk", creationDate, expectedDelivery, status) {
    init {
        if (daysBetween(creationDate, expectedDelivery) < 3)
            abnormalMessage = "Bulk shipment expected too soon (< 3 days)"
    }
}

fun daysBetween(start: String, end: String): Long {
    val formatter = DateTimeFormatter.ISO_DATE
    val startDate = LocalDate.parse(start, formatter)
    val endDate = LocalDate.parse(end, formatter)
    return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate)
}
