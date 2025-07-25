import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*

val shipmentTracker = ShipmentTracker()

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost() // For local testing only. Do NOT use in production.
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Post)
    }

    routing {
        post("/shipment") {
            val rawInput = call.receive<String>()

            try {
                shipmentTracker.processUpdate(rawInput)
                call.respond(HttpStatusCode.OK, "Update processed: $rawInput")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid data")
            }
        }

        get("/shipment/{id}") {
            val id = call.parameters["id"]
            val shipment = shipmentTracker.getShipment(id!!)
            if (shipment != null) {
                call.respond(shipment.getData())
            } else {
                call.respond(HttpStatusCode.NotFound, "Shipment not found")
            }
        }
    }
}
