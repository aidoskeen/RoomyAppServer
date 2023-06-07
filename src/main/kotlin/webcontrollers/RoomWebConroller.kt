package webcontrollers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import enums.RequestStatus
import model.Resident
import model.Room
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import repository.hibernate.DormitoryHibernateController
import repository.hibernate.RoomHibernateController
import repository.hibernate.UserHibernateController
import tools.serializers.RoomSerializer
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.Int
import kotlin.String

@Controller
class RoomWebConroller() {

    private val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    private val roomHibernateController = RoomHibernateController(entityManagerFactory)
    private val dormitoryHibernateController = DormitoryHibernateController(entityManagerFactory)
    private val userHibernateController = UserHibernateController(entityManagerFactory)
    private val jsonSerializer = RoomSerializer()

    @RequestMapping(value = ["/room/allRooms/{dormitoryId}"], method = [RequestMethod.GET])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun getAllRoomsByDormitory(@PathVariable dormitoryId: Int) : String {
        val dormitory = dormitoryHibernateController.getDormitoryById(dormitoryId)
        val roomSerializer = RoomSerializer().getRoomJsonSerializer()

        val rooms: List<Room>? = dormitory?.rooms
        return if (rooms != null) {
            val listSerializer = jsonSerializer.getRoomListSerializer()
            val gson = GsonBuilder()
            val type = object : TypeToken<List<Room>>() {}.type
            gson.registerTypeAdapter(type, listSerializer)
                .registerTypeAdapter(Room::class.java, roomSerializer)
            val parser = gson.create()
            parser.toJson(rooms)
        } else ""
    }

    @RequestMapping(value = ["/room/byResident/{residentId}"], method = [RequestMethod.GET])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun getRoomByResident(@PathVariable residentId: Int) : String {
        val resident = userHibernateController.getResidentById(residentId)
        val room = resident?.roomNumber?.let { roomHibernateController.getRoomById(it) }

        return if (room != null) {
            val gson = GsonBuilder()
                .registerTypeAdapter(Room::class.java, jsonSerializer.getRoomJsonSerializer())
            val parser = gson.create()
            parser.toJson(room)
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
        val resident: Resident? = properties.getProperty("residentId")?.toIntOrNull()
            ?.let { userHibernateController.getResidentById(it) }
        val available: Boolean = properties.getProperty("available", "true").toBoolean()
        val roomNumber = properties.getProperty("roomNumber")?.toIntOrNull()
        if (place != null) {
            resident?.let { place.livingResident = it }
            place.requestStatus = status
            place.available = available
            roomNumber?.let { place.livingResident?.roomNumber = it }
            roomHibernateController.updatePlace(place)
            place.livingResident?.let { userHibernateController.editResident(it) }
        }
        else return "Error"
        return "Success"
    }
}