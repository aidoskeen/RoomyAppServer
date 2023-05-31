package tools.serializers

import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import model.Resident
import java.lang.reflect.Type

class UserSerializer {
    fun getResidentJsonSerializer() : JsonSerializer<Resident> {
        val serializer = JsonSerializer {
                resident: Resident,
                _: Type,
                _: JsonSerializationContext ->
            val roomJson = JsonObject()
            roomJson.addProperty("usedId", resident.userId)
            roomJson.addProperty("name", resident.name)
            roomJson.addProperty("surname", resident.surname)
            roomJson.addProperty("username", resident.username)
            roomJson.addProperty("password", resident.password)
            roomJson.addProperty("roomNumber", resident.roomNumber)
            roomJson
        }
        return serializer
    }
}