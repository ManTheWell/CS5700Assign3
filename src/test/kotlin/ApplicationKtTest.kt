import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class ApplicationKtTest {

    @Test
    fun testPostShipment() = testApplication {
        application {
            module()
        }
        client.post("/shipment").apply {
            TODO("Please write your test here")
        }
    }
}