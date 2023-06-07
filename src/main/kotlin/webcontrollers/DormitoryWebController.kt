package webcontrollers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import enums.PaymentStatus
import model.Announcement
import model.Dormitory
import model.MonthlyPayment
import model.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import repository.hibernate.DormitoryHibernateController
import repository.hibernate.RoomHibernateController
import tools.serializers.DormitorySerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence


@Controller
class DormitoryWebController() {

    private val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    private val dormitoryHibernateController = DormitoryHibernateController(entityManagerFactory)
    private val placeHibernateController = RoomHibernateController(entityManagerFactory)
    private val jsonSerializer = DormitorySerializer()

    @RequestMapping(value = ["/dormitory/allDormitories"], method = [RequestMethod.GET])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun getAllDormitories() : String {
        val dormitories: List<Dormitory>? = dormitoryHibernateController.getAllDormitories()
        return if (dormitories != null) {
            val dormitoryListSerializer = jsonSerializer.getDormitoryListSerializer()
            val dormitorySerializer = jsonSerializer.getDormitoryJsonSerializer()
            val gson = GsonBuilder()
            val type = object : TypeToken<List<Dormitory>>() {}.type
            gson.registerTypeAdapter(type, dormitoryListSerializer)
                .registerTypeAdapter(Dormitory::class.java, dormitorySerializer)
            val parser = gson.create()
            parser.toJson(dormitories)
        }
        else ""
    }

    @RequestMapping(value = ["/dormitory/newAnnouncement"], method = [RequestMethod.POST])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun createNewAnnouncement(@RequestBody request: String?): String {
        val gson = Gson()
        val properties = gson.fromJson(request, Properties::class.java)
        val announcement = Announcement(
            title = properties.getProperty("title"),
            text = properties.getProperty("text")
        )

        return if (dormitoryHibernateController.addNewAnnouncement(announcement))
            "Success"
        else "Error"
    }

}