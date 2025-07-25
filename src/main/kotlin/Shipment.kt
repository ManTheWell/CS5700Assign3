import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

open class Shipment(creationInfo: String) {
    private lateinit var id: String
    private lateinit var type: String

    init {
        parse(creationInfo)
    }

    private var status: String = ""
    private var location: String = ""
    private var expDeliveryTime: String = ""

    private val updates = mutableListOf<String>()
    private val notes = mutableListOf<String>()

    fun getID(): String {
        return id
    }

    private fun parse(update: String) {
        val parts = update.split(",")

        val time = parts[1]
        val readableTimestamp = convertTime(parts[1])

        status = parts[2]

        when (status) {
            "created" -> {
                updates.add(0, "Created on $readableTimestamp")

                type = parts[3]

                when (type) {
                    "express" -> {}

                    "overnight" -> {}

                    "bulk" -> {}
                }
            }

            "shipped" -> {
                expDeliveryTime = parts.getOrNull(3) ?: ""
                updates.add(0, "Shipped on $readableTimestamp")
            }

            "location" -> {
                status = "new location"
                location = parts.getOrNull(3) ?: ""

                updates.add(0, "New shipment location: $location on $readableTimestamp")
            }

            "delivered" -> {
                updates.add(0, "Shipment delivered at $readableTimestamp}")
            }

            "delayed" -> {
                expDeliveryTime = convertTime(parts.getOrNull(3) ?: "")

                updates.add(0, "Delayed on $readableTimestamp, new expected delivery date $expDeliveryTime")
            }

            "lost" -> {
                expDeliveryTime = "none"
                updates.add(0, "Lost on ${convertTime(readableTimestamp)}, last known location: $location")

                location = "unknown"
            }

            "canceled" -> {
                location = "none"
                expDeliveryTime = "none"

                updates.add(0, "Canceled on $readableTimestamp")
            }

            "noteadded" -> {
                status = "new note"
                notes.add(0, "($readableTimestamp): " + (parts.getOrNull(3) ?: ""))
            }
        }
    }

    private fun convertTime(epocString : String): String {
        val epochMillis: Long = epocString.toLongOrNull() ?: return ""

        val instant = Instant.ofEpochMilli(epochMillis)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' HH:mm").withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    @Composable
    fun createBox() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text("ID: ${id.uppercase()}")
            Text("Type: ${type.lowercase()}")
            Text("Status: $status")
            Text("")

            Text("Updates:")
            updates.forEach { Text("- $it") }
            Text("")

            Text("Notes:")
            notes.forEach { Text("- $it") }
            Text("")
        }
    }
}