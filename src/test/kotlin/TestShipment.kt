import kotlin.test.*

class ShipmentTest {

    @Test
    fun `test created express shipment within allowed time`() {
        val shipment = Shipment("abc123")
        // Format: id,timestamp,status,type,expectedDeliveryTime
        shipment.parse("abc123,1000000000000,created,express,1000000250000")

        val data = shipment.getData()

        assertEquals("abc123", data.id)
        assertEquals("express", data.type)
        assertEquals("created", data.status)
        assertTrue(data.updates.any { it.contains("Created on") })
        assertTrue(data.updates.any { it.contains("Expected delivery on") })
        assertTrue(data.notes.isEmpty())
    }

    @Test
    fun `test express shipment with too long delivery time triggers note`() {
        val shipment = Shipment("xyz789")
        // 4 days difference → triggers note (4 * 86400 * 1000 = 345_600_000 ms)
        shipment.parse("xyz789,1000000000000,created,express,1000345600000")

        val data = shipment.getData()
        assertEquals("express", data.type)
        assertTrue(data.notes.any { it.contains("greater than 3 day expected maximum") })
    }

    @Test
    fun `test created overnight shipment within allowed time`() {
        val shipment = Shipment("abc123")
        // Format: id,timestamp,status,type,expectedDeliveryTime
        shipment.parse("abc123,1000000000000,created,overnight,1000000000025")

        val data = shipment.getData()

        assertEquals("abc123", data.id)
        assertEquals("overnight", data.type)
        assertEquals("created", data.status)
        assertTrue(data.updates.any { it.contains("Created on") })
        assertTrue(data.updates.any { it.contains("Expected delivery on") })
        assertTrue(data.notes.isEmpty())
    }

    @Test
    fun `test overnight shipment with too long delivery time triggers note`() {
        val shipment = Shipment("xyz789")
        shipment.parse("xyz789,1000000000000,created,overnight,1000345600000")

        val data = shipment.getData()
        assertEquals("overnight", data.type)
        assertTrue(data.notes.any { it.contains("greater than 1 day expected maximum") })
    }

    @Test
    fun `test location update`() {
        val shipment = Shipment("loc001")
        shipment.parse("loc001,1000000000000,created,standard,1000000250000")
        shipment.parse("loc001,1000000000000,location,Denver")

        val data = shipment.getData()
        assertEquals("new location", data.status)
        assertEquals("Denver", data.location)
        assertTrue(data.updates.first().contains("Denver"))
    }

    @Test
    fun `test addnote adds note with timestamp`() {
        val shipment = Shipment("note001")
        shipment.parse("note001,1000000000000,created,standard,1000000250000")
        shipment.parse("note001,1000000000000,addnote,This is a note")

        val data = shipment.getData()
        assertEquals("new note", data.status)
        assertTrue(data.notes.first().contains("This is a note"))
    }

    @Test
    fun `test canceled shipment`() {
        val shipment = Shipment("cancel001")
        shipment.parse("cancel001,1000000000000,created,standard,1000000250000")
        shipment.parse("cancel001,1000000000000,canceled")

        val data = shipment.getData()
        assertEquals("canceled", data.status)
        assertEquals("none", data.location)
        assertEquals("none", data.expectedDelivery)
        assertTrue(data.updates.first().contains("Canceled"))
    }

    @Test
    fun `test delayed shipment updates expected delivery`() {
        val shipment = Shipment("delay001")
        shipment.parse("delay001,1000000000000,created,standard,1000000250000")
        shipment.parse("delay001,1000000000000,delayed,1000123456789")

        val data = shipment.getData()
        assertEquals("delayed", data.status)
        assertTrue(data.updates.first().contains("Delayed"))
        assertTrue(data.updates.first().contains("new expected delivery"))
    }
}
