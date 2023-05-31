package tools.serializers

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import model.Place
import model.Room
import java.lang.reflect.Type

class RoomSerializer() {
    fun getRoomJsonSerializer() : JsonSerializer<Room> {
        val placeSerializer = PlaceSerializer().getPlaceJsonSerializer()
        val serializer = JsonSerializer {
                room: Room,
                _: Type,
                _: JsonSerializationContext ->
            val parser = GsonBuilder().registerTypeAdapter(Place::class.java, placeSerializer)
                .create()
            val roomJson = JsonObject()
            val placesArray = JsonArray()
            room.places.forEach {
                placesArray.add(parser.toJsonTree(it))
            }
            roomJson.addProperty("roomNumber", room.roomNumber)
            roomJson.addProperty("roomSize", room.roomSize.toString())
            roomJson.addProperty("description", room.description)
            roomJson.addProperty("roomType", room.roomType.toString())
            roomJson.add("places", placesArray)
            roomJson
        }
        return serializer
    }

    fun getRoomListSerializer() : JsonSerializer<List<Room>> {
        val serializer = JsonSerializer { rooms: List<Room>, _, _ ->
            val jsonArray = JsonArray()
            val parser = GsonBuilder().create()

            for (room in rooms) {
                jsonArray.add(parser.toJson(room))
            }
            jsonArray
        }
        return serializer
    }
}