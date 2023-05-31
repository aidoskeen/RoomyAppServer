package webcontrollers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import enums.RequestStatus
import model.Request
import model.Resident
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import repository.hibernate.RequestsHibernateController
import tools.serializers.SerializationTool
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

@Controller
class RequestWebController() {
    private val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    private val requestsHibernateController = RequestsHibernateController(entityManagerFactory)
    private val jsonSerializer = SerializationTool()

    @RequestMapping(value = ["/request/allRequests/{dormitoryId}"], method = [RequestMethod.GET])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun getAllRequestsByDormitory(@PathVariable(name = "dormitoryId") id: Int) : String {
        val requests = requestsHibernateController.getRequestsByDormitoryId(id) ?: return ""
        val gson = GsonBuilder()
        val parser = gson.create()
        return parser.toJson(requests)
    }

    @RequestMapping(value = ["/dormitory/new"], method = [RequestMethod.POST])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun addNewRequest(@RequestBody requestBody: String): String {
        val gson = Gson()
        val properties = gson.fromJson(requestBody, Properties::class.java)
        val request = Request()
        request.roomId = properties.getProperty("roomId")
        request.placeId = properties.getProperty("placeId")
        request.dormitoryId = properties.getProperty("dormitoryId")
        request.requester = gson.fromJson(properties.getProperty("resident"), Resident::class.java)
        request.requestStatus = RequestStatus.PENDING

        requestsHibernateController.addNewRequest(request)
        return "Success"
    }
}