object ShipmentFactory {
    fun createFromRaw(raw: String): Shipment {
        // Example format: "ID123,Express,2025-07-22,2025-07-25,Shipped"
        val parts = raw.split(",")
        if (parts.size != 5) throw IllegalArgumentException("Invalid input format")

        val (id, type, creationDate, expectedDelivery, status) = parts

        return when (type) {
            "Standard" -> StandardShipment(id, creationDate, expectedDelivery, status)
            "Express" -> ExpressShipment(id, creationDate, expectedDelivery, status)
            "Overnight" -> OvernightShipment(id, creationDate, expectedDelivery, status)
            "Bulk" -> BulkShipment(id, creationDate, expectedDelivery, status)
            else -> throw IllegalArgumentException("Invalid shipment type: $type")
        }
    }
}
