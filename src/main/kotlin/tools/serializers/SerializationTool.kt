package tools.serializers

import com.google.gson.*
import model.Dormitory
import model.Place
import model.Room
import java.lang.reflect.Type

class SerializationTool() {
    private val gson = Gson()
    fun getDormitoryJsonSerializer() : JsonSerializer<Dormitory> {
        val serializer = JsonSerializer {
                dormitory: Dormitory,
                _: Type,
                _: JsonSerializationContext ->
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(Room::class.java, getRoomJsonSerializer())
            val roomsArray = JsonArray()
            val parser = gsonBuilder.create()
            dormitory.rooms.forEach {
                roomsArray.add(parser.toJson(it))
            }
            val dormitoryJson = JsonObject()
            dormitoryJson.addProperty("dormitoryId", dormitory.dormitoryId.toString())
            dormitoryJson.addProperty("address", dormitory.address)
            dormitoryJson.addProperty("university", dormitory.university)
            dormitoryJson.add("rooms", roomsArray)
            dormitoryJson
        }
        return serializer
    }

    fun getDormitoryListSerializer() : JsonSerializer<List<Dormitory>> {
        val serializer = JsonSerializer { dormitories: List<Dormitory>, _, _ ->
            val jsonArray = JsonArray()
            val parser = GsonBuilder().create()

            for (dorm in dormitories) {
                jsonArray.add(parser.toJson(dorm))
            }
            jsonArray
        }
        return serializer
    }

    fun getRoomJsonSerializer() : JsonSerializer<Room> {
        val serializer = JsonSerializer {
                room: Room,
                _: Type,
                _: JsonSerializationContext ->
            val parser = GsonBuilder().create()
            val roomJson = JsonObject()
            val placesArray = JsonArray()
            room.places.forEach {
                placesArray.add(parser.toJson(it))
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
            placeJson
        }
        return serializer
    }






}