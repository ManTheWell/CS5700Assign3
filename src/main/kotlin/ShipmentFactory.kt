//object ShipmentFactory {
//    fun createFromRaw(raw: String): Shipment {
//        // created,s10000,1652712855468
//        val parts = raw.split(",")
//        if (parts.size != 5) throw IllegalArgumentException("Invalid input format")
//
//        val (id, type, creationDate, expectedDelivery, status) = parts
//
//        return when (type) {
//            "Standard" -> StandardShipment(id, creationDate, expectedDelivery, status)
//            "Express" -> ExpressShipment(id, creationDate, expectedDelivery, status)
//            "Overnight" -> OvernightShipment(id, creationDate, expectedDelivery, status)
//            "Bulk" -> BulkShipment(id, creationDate, expectedDelivery, status)
//            else -> throw IllegalArgumentException("Invalid shipment type: $type")
//        }
//    }
//}
