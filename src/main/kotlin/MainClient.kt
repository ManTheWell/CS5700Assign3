import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.*

@Composable
@Preview
fun app() {
    val trackedShipments = remember { mutableStateMapOf<String, ShipmentInfo?>() }
    val trackingID = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Background poller to refresh shipment data every second
    LaunchedEffect(trackedShipments.keys) {
        while (true) {
            delay(1000)
            trackedShipments.keys.forEach { id ->
                scope.launch {
                    val updated = fetchShipmentById(id)
                    trackedShipments[id] = updated
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        Row {
            TextField(
                value = trackingID.value,
                onValueChange = { trackingID.value = it },
                placeholder = { Text("Enter Tracking ID") },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black)
            )

            Button(onClick = {
                scope.launch {
                    val shipment = fetchShipmentById(trackingID.value)
                    if (shipment != null) {
                        trackedShipments[shipment.id] = shipment
                    }
                }
            }) {
                Text("Track")
            }
        }

        Spacer(Modifier.height(16.dp))

        trackedShipments.forEach { (shipmentID, shipment) ->
            if (shipment != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Text("Shipment ID: ${shipment.id.uppercase(Locale.getDefault())}")
                    Text("Status: ${shipment.status}")

                    Spacer(Modifier.height(4.dp))

                    Text("Updates:")
                    shipment.updates.forEach { update ->
                        Text("- $update")
                    }

                    Spacer(Modifier.height(4.dp))

                    Text("Notes:")
                    shipment.notes.forEach { note ->
                        Text("* $note")
                    }


                    Spacer(Modifier.height(4.dp))

                    Button(onClick = {
                        trackedShipments.remove(shipment.id)
                    }) {
                        Text("Stop Tracking ${shipment.id}")
                    }
                }
            }
        }
    }
}

suspend fun fetchShipmentById(id: String): ShipmentInfo? {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    return try {
        client.get("http://localhost:8080/shipment/$id").body()
    } catch (e: Exception) {
        println("Error: ${e.message}")
        null
    }
}


fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Shipment Tracker") {
        app()
    }
}
