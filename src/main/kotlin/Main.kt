import enums.PaymentStatus
import enums.RoomSize
import enums.RoomType
import model.*
import repository.hibernate.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.Persistence
import kotlin.coroutines.CoroutineContext

fun main(args: Array<String>) {
    println("Hibernate test")
    val entityManagerFactory = Persistence.createEntityManagerFactory("roomy")
    val dormitoryHibernateController = DormitoryHibernateController(entityManagerFactory)
    val roomHibernateController = RoomHibernateController(entityManagerFactory)
    val userHibernateController = UserHibernateController(entityManagerFactory)
    val paymentHibernateController = PaymentHibernateController(entityManagerFactory)

    var administrator: Administrator = Administrator("testName", "testSurname", "123", "123")
    var administrator2: Administrator = Administrator("testName2", "testSurname2", "1234", "1234")


    var dormitory = Dormitory()

    dormitory.address = "Sauletekio 25"
    dormitory.university = "VGTU"
    var dormitory2 = Dormitory()
    dormitory2.address = "Sauletekio 33"
    dormitory2.university = "VU"
    administrator.dormitory = dormitory
    administrator.dormitory?.administrator = administrator
    administrator2.dormitory = dormitory2
    administrator2.dormitory?.administrator = administrator2

    userHibernateController.addNewUser(administrator)
    userHibernateController.addNewUser(administrator2)

    val smallRooms = createSmallSingleRooms(10, dormitory, roomHibernateController).toMutableList()
    val mediumRooms = createMediumDoubleRooms(10, dormitory, roomHibernateController).toMutableList()
    val singlePlaces = createPlaces(10, RoomType.SINGLE, 200L)
    val doublePlaces = createPlaces(20, RoomType.DOUBLE, 150L)

    val doublePlaces2 = createPlaces(10, RoomType.DOUBLE, 150L)
    val mediumRooms2 = createMediumDoubleRooms(5, dormitory2, roomHibernateController).toMutableList()

    doublePlaces2.forEachIndexed { index, places ->
        assignRoomsToPlaces(places, mediumRooms2[index], roomHibernateController)
    }

    singlePlaces.forEachIndexed { index, places ->
        assignRoomsToPlaces(places, smallRooms[index], roomHibernateController)
    }

    doublePlaces.forEachIndexed { index, places ->
        assignRoomsToPlaces(places, mediumRooms[index], roomHibernateController)
    }
    //val roomsWithPlaces = addPlacesToRooms(singlePlaces, smallRooms, roomHibernateController)
    //val mediumRoomsWithPlaces = addPlacesToRooms(doublePlaces, mediumRooms, roomHibernateController)
    dormitory.rooms.addAll(smallRooms)
    dormitory.rooms.addAll(mediumRooms)
    dormitory2.rooms.addAll(mediumRooms2)
    dormitoryHibernateController.editDormitory(dormitory)
    val newDormitory = dormitory.dormitoryId?.let { dormitoryHibernateController.getDormitoryById(it) }
    if (newDormitory != null) {
        addPaymentsToPlaces(newDormitory, roomHibernateController, paymentHibernateController)
    }
    val payments = paymentHibernateController.getAllPayments()


}

fun createMediumDoubleRooms(
    amount: Int,
    dormitory: Dormitory,
    hibernateController: RoomHibernateController,
): List<Room> {
    val rooms = mutableListOf<Room>()
    for (i in 0..amount) {
        val room = Room()
        room.roomSize = RoomSize.MEDIUM
        room.roomType = RoomType.DOUBLE
        room.description = "Medium size, double rooms"
        room.dormitory = dormitory
        hibernateController.addRoom(room)
        rooms.add(room)
    }
    return rooms
}

fun createSmallSingleRooms(
    amount: Int,
    dormitory: Dormitory,
    hibernateController: RoomHibernateController
): List<Room> {
    val rooms = mutableListOf<Room>()
    for (i in 0..amount) {
        val room = Room()
        room.roomSize = RoomSize.SMALL
        room.roomType = RoomType.SINGLE
        room.description = "Small and single room"
        room.dormitory = dormitory
        hibernateController.addRoom(room)
        rooms.add(room)
    }
    return rooms
}
fun createPlaces(
    amount: Int,
    roomType: RoomType,
    price: Long
) : List<List<Place>> {
    val places = mutableListOf<Place>()
     for (i in 0..amount) {
         val place = Place()
         place.price = price
         places.add(place)
     }
     return when (roomType){
         RoomType.NONE -> listOf<MutableList<Place>>()
         RoomType.SINGLE -> places.chunked(1)
         RoomType.DOUBLE -> places.chunked(2)
         RoomType.TRIPLE -> places.chunked(3)
     }
}

fun addPlacesToRooms(
    listOfPlaces: List<List<Place>>,
    rooms: MutableList<Room>,
    hibernateController: RoomHibernateController
) : MutableList<Room> {
    rooms.forEachIndexed { i, room ->
        room.places = listOfPlaces.getOrElse(i) { listOf() }.toMutableList()
        hibernateController.editRoom(room)
    }
    return rooms
}

fun assignRoomsToPlaces(places: List<Place>, room: Room, hibernateController: RoomHibernateController) {
    places.forEach {
        it.room = room
        hibernateController.updatePlace(it)
    }
}

fun addPaymentsToPlaces(
    dormitory: Dormitory,
    placeHibernateController: RoomHibernateController,
    paymentHibernateController: PaymentHibernateController
) {
    val month = LocalDate.now().month.toString()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dueDate = LocalDate.now().plusDays(14).format(formatter)
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
}


