package tools.serializers

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import model.Dormitory
import model.Room
import java.lang.reflect.Type

class DormitorySerializer() {

    fun getDormitoryJsonSerializer() : JsonSerializer<Dormitory> {
        val roomSerializer = RoomSerializer().getRoomJsonSerializer()
        val serializer = JsonSerializer {
                dormitory: Dormitory,
                _: Type,
                _: JsonSerializationContext ->
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(Room::class.java, roomSerializer)
            val roomsArray = JsonArray()
            val parser = gsonBuilder.create()
            dormitory.rooms.forEach {
                roomsArray.add(parser.toJsonTree(it))
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
}