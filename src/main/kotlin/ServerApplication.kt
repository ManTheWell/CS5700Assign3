import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*


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
//                val shipment = ShipmentFactory.createFromRaw(rawInput)
//                ShipmentRepository.addOrUpdate(shipment)
                call.respond(HttpStatusCode.OK, "Shipment processed") //${shipment.id} processed")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid data")
            }
        }

        get("/shipment/{id}") {
            val id = call.parameters["id"]
//            val shipment = ShipmentRepository.get(id!!)
//            if (shipment != null) {
//                call.respond(shipment)
//            } else {
//                call.respond(HttpStatusCode.NotFound, "Shipment not found")
//            }
        }
    }
}
