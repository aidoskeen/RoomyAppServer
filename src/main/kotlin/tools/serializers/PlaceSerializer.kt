package tools.serializers

import com.google.gson.*
import model.MonthlyPayment
import model.Place
import model.Resident
import java.lang.reflect.Type

class PlaceSerializer() {
    val residentSerializer = UserSerializer().getResidentJsonSerializer()
    val paymentSerializer = PaymentSerializer().getPaymentJsonSerializer()
    val gson = GsonBuilder()
        .registerTypeAdapter(Resident::class.java, residentSerializer)
        .registerTypeAdapter(MonthlyPayment::class.java, paymentSerializer)
        .create()
    fun getPlaceJsonSerializer() : JsonSerializer<Place> {
        val serializer = JsonSerializer{
                place: Place,
                _: Type,
                _: JsonSerializationContext ->

            val placeJson = JsonObject()
            placeJson.addProperty("placeId", place.placeId.toString())
            placeJson.addProperty("available", place.available.toString())
            placeJson.addProperty("price", place.price.toString())
            placeJson.add("livingResident", gson.toJsonTree(place.livingResident))
            placeJson.add("monthlyPayment", gson.toJsonTree(place.monthlyPayment))
            placeJson.addProperty("requestStatus", place.requestStatus.toString())
            placeJson
        }
        return serializer
    }

    fun getPlaceListSerializer() : JsonSerializer<List<Place>> {
        val serializer = JsonSerializer { places: List<Place>, _, _ ->
            val jsonArray = JsonArray()
            val parser = GsonBuilder()
                .registerTypeAdapter(Place::class.java, getPlaceJsonSerializer())
                .create()

            for (place in places) {
                jsonArray.add(parser.toJson(place))
            }
            jsonArray
        }
        return serializer
    }
}