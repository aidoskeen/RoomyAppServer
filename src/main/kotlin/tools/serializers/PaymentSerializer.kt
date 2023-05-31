package tools.serializers

import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import model.MonthlyPayment
import java.lang.reflect.Type

class PaymentSerializer() {

    fun getPaymentJsonSerializer() : JsonSerializer<MonthlyPayment> {
        val serializer = JsonSerializer {
                payment: MonthlyPayment,
                _: Type,
                _: JsonSerializationContext ->
            val paymentJson = JsonObject()
            paymentJson.addProperty("paymentId", payment.paymentId)
            paymentJson.addProperty("month", payment.month)
            paymentJson.addProperty("dueDate", payment.dueDate)
            paymentJson.addProperty("paymentStatus", payment.paymentStatus.toString())
            paymentJson
        }
        return serializer
    }
}