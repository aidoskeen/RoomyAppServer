package webcontrollers

import enums.PaymentStatus
import model.MonthlyPayment
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import repository.hibernate.DormitoryHibernateController
import repository.hibernate.PaymentHibernateController
import repository.hibernate.RoomHibernateController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class PaymentWebController() {
    private val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    private val dormitoryHibernateController = DormitoryHibernateController(entityManagerFactory)
    private val placeHibernateController = RoomHibernateController(entityManagerFactory)
    private val paymentHibernateController = PaymentHibernateController(entityManagerFactory)

    @RequestMapping(value = ["/dormitory/createPayments/{id}"], method = [RequestMethod.PUT])
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun createPayments(@RequestBody request: String?, @PathVariable(name = "id") id: Int): String? {
        val month = LocalDate.now().month.toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dueDate = LocalDate.now().plusDays(14).format(formatter)
        val dormitory = dormitoryHibernateController.getDormitoryById(id)
        if (dormitory != null) {
            dormitory.rooms.forEach { room ->
                room.places.forEach { place ->
                    val payment = MonthlyPayment()
                    payment.month = month
                    payment.paymentStatus = PaymentStatus.NONE
                    payment.paymentId = place.placeId
                    payment.dueDate = dueDate
                    payment.paymentAmount = place.price
                    place.monthlyPayment = payment
                    payment.place = place
                    paymentHibernateController.editPayment(payment)
                    placeHibernateController.updatePlace(place)
                }
            }
            return "Success"
        }
        else return "Fail"
    }
}