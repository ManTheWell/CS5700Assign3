import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class ShipmentTracker {
    private val shipments = mutableMapOf<String, Shipment>()
    private var numUpdates = mutableStateOf(0)

    fun processUpdate(update: String) {
        numUpdates.value++

        val parts = update.split(",")
        val id = parts[0]
        val shipment = shipments.getOrPut(id) { Shipment(id) }

        shipment.parse(update)
    }

    fun getShipment(id: String): Shipment? {
        return shipments[id]
    }
}
