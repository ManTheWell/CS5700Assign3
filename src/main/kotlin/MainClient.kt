import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

//val tracker = TrackingShipmentSimulator()

@Composable
@Preview
fun app() {
//    val numUpdates by tracker.getNumUpdates()

    val trackedShipments = remember { mutableStateListOf<String>() }
    val trackingID = remember { mutableStateOf("") }

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
            Button(
                onClick = {
//                    if (!trackedShipments.contains(trackingID.value) && tracker.getShipments().containsKey(trackingID.value)) {
//                        trackedShipments.add(0, trackingID.value)
//                        trackingID.value = ""
//                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Track")
            }
        }

        Spacer(Modifier.height(16.dp))

        // TODO
//        tracker.getShipments().values.forEach { shipment ->
//            if (trackedShipments.contains(shipment.getID())) {
//                shipment.createBox()
//
//                Button(onClick = {
//                    trackedShipments.remove(shipment.getID())
//                }) {
//                    Text("Stop Tracking Shipment ${shipment.getID()}")
//                }
//            }
//        }
//
//        Text("Total number of tracking updates: $numUpdates")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Shipment Tracker") {
        app()
    }
}