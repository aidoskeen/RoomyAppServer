package webcontrollers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import enums.RequestStatus
import model.Dormitory
import model.Place
import model.Resident
import model.Room
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import repository.hibernate.RoomHibernateController
import repository.hibernate.UserHibernateController
import tools.serializers.RoomSerializer
import java.lang.Boolean
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.Int
import kotlin.String

@Controller
class RoomWebConroller() {

    private val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    private val roomHibernateController = RoomHibernateController(entityManagerFactory)
    private val userHibernateController = UserHibernateController(entityManagerFactory)
    private val jsonSerializer = RoomSerializer()

    @RequestMapping(value = ["/room/allRooms/{dormitoryId}"], method = [RequestMethod.GET])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun getAllRoomsByDormitory(@PathVariable dormitoryId: Int) : String {
        val rooms: List<Room>? = roomHibernateController.getRoomsByDormitoryId(dormitoryId)
        return if (rooms != null) {
            val listSerializer = jsonSerializer.getRoomListSerializer()
            val gson = GsonBuilder()
            val type = object : TypeToken<List<Dormitory?>>() {}.type
            gson.registerTypeAdapter(type, listSerializer)
            val parser = gson.create()
            parser.toJson(rooms)
        } else ""
    }


    @RequestMapping(value = ["/place/updatePlace/{id}"], method = [RequestMethod.PUT])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun updatePlace(@RequestBody request: String?, @PathVariable(name = "id") id: Int): String? {
        val gson = Gson()
        val properties = gson.fromJson(request, Properties::class.java)
        val place = roomHibernateController.getPlaceById(id)
        val status = when(properties.getProperty("requestStatus")) {
            "PENDING" -> RequestStatus.PENDING
            "ACCEPTED" -> RequestStatus.ACCEPTED
            "REJECTED" -> RequestStatus.REJECTED
            else -> RequestStatus.NONE
        }
        val resident: Resident? = userHibernateController.getResidentById(properties.getProperty("residentId").toInt())
        val available: kotlin.Boolean = properties.getProperty("available", "true").toBoolean()

        if (place != null) {
            place.livingResident = resident
            place.requestStatus = status
            place.available = available
            roomHibernateController.updatePlace(place)
        }
        else return "Error"
        return "Success"
    }
}