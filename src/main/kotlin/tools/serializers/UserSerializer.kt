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
            val userJson = JsonObject()
            userJson.addProperty("userId", resident.userId)
            userJson.addProperty("name", resident.name)
            userJson.addProperty("surname", resident.surname)
            userJson.addProperty("username", resident.username)
            userJson.addProperty("password", resident.password)
            userJson
        }
        return serializer
    }
}