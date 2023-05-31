package webcontrollers

import com.google.gson.Gson
import enums.RequestStatus
import model.Resident
import model.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import repository.hibernate.RoomHibernateController
import repository.hibernate.UserHibernateController
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

@Controller
class UserWebController() {

    private val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    private val userHibernateController = UserHibernateController(entityManagerFactory)
    private val roomHibernateController = RoomHibernateController(entityManagerFactory)

    @RequestMapping(value = ["/user/authentication"], method = [RequestMethod.POST])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun getUserByCredentials(@RequestBody credentials: String) : String {
        val gson = Gson()
        val properties = gson.fromJson(credentials, Properties::class.java)
        val user = userHibernateController.getUserByLoginData(
            properties.getProperty("login"),
            properties.getProperty("password")
        )
        return gson.toJson(user)
    }

    @RequestMapping(value = ["/user/registration"], method = [RequestMethod.POST])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun createNewResident(@RequestBody request: String?): String {
        val gson = Gson()
        val properties = gson.fromJson(request, Properties::class.java)
        val resident = User(
            username = properties.getProperty("username"),
            password = properties.getProperty("password"),
            name = properties.getProperty("name"),
            surname = properties.getProperty("surname"),
        )

        return if (userHibernateController.addNewUser(resident))
            "Success"
        else "Error"
    }
}