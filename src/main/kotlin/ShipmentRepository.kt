object ShipmentRepository {
    private val shipments = mutableMapOf<String, Shipment>()

    fun addOrUpdate(shipment: Shipment) {
        shipments[shipment.id] = shipment
    }

    fun get(id: String): Shipment? = shipments[id]
}
