package repository.hibernate

import model.Dormitory
import model.Place
import model.Room
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.Query

class RoomHibernateController(
    val entityManagerFactory: EntityManagerFactory
) {
    private fun getEntityManager(): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    fun addRoom(room: Room?) {
        var em: EntityManager? = null
        try {
            em = getEntityManager()
            em.transaction.begin()
            em.persist(room)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em?.close()
        }
    }

    fun removeRoom(dormitory: Dormitory, room: Room) {
        val em = getEntityManager()
        if (dormitory.rooms.contains(room))
            dormitory.rooms.remove(room)
        else {
            println("There is no such room in this dormitory")
            return
        }
        try {
            em.transaction.begin()
            em.merge<Any>(dormitory)
            em.transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun editRoom(room: Room) {
        var em: EntityManager? = null
        try {
            em = getEntityManager()
            em.transaction.begin()
            em.merge<Any>(room)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em?.close()
        }
    }
    fun getPlaceById(id: Int) : Place? {
        val em = getEntityManager()
        var place: Place? = null
        try {
            em.transaction.begin()
            place = em.find(Place::class.java, id)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            println("Place doesn't exist")
        }
        return place
    }

    fun updatePlace(place: Place) {
        var em: EntityManager? = null
        try {
            em = getEntityManager()
            em.transaction.begin()
            em.merge<Any>(place)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em?.close()
        }
    }

    fun getRoomsByDormitoryId(id: Int): List<Room>? {
        val entityManager = getEntityManager()
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Room::class.java)
        val root = criteriaQuery.from(Room::class.java)
        val inClause = criteriaBuilder.`in`(root.get<Int>("dormitory_dormitoryId"))
        val dormitory: Dormitory = entityManager.getReference(Dormitory::class.java, id)
        dormitory.rooms.forEach { room ->
            inClause.value(room.roomNumber)
        }
        criteriaQuery.select(root).where(inClause)
        val query: Query
        return try {
            query = entityManager.createQuery(criteriaQuery)
            query.resultList
        } catch (e: NoResultException) {
            null
        }
    }

    fun getRoomById(id: Int) : Room? {
        val em = getEntityManager()
        var room: Room? = null
        try {
            em.transaction.begin()
            room = em.find(Room::class.java, id)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            println("Place doesn't exist")
        }
        return room
    }
}