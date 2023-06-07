package repository.hibernate

import model.Announcement
import model.Dormitory
import model.Resident
import model.Room
import org.hibernate.Hibernate
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.Query
import javax.persistence.criteria.CriteriaQuery

class DormitoryHibernateController(
    private val entityManagerFactory: EntityManagerFactory
) {

    private fun getEntityManager(): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    fun editDormitory(dormitory: Dormitory) {
        val em = getEntityManager()
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

    fun getAllAnnouncements(): List<Announcement>? {
        val em = getEntityManager()
        try {
            val criteriaQuery: CriteriaQuery<Announcement> = em.criteriaBuilder.createQuery(Announcement::class.java)
            criteriaQuery.select(criteriaQuery.from(Announcement::class.java))
            val query: Query = em.createQuery(criteriaQuery)

            return query.resultList.filterIsInstance(Announcement::class.java)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return null
    }

    fun addNewAnnouncement(announcement: Announcement): Boolean {
        val em = getEntityManager()
        return try {
            em.transaction.begin()
            em.persist(announcement)
            em.transaction.commit()
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        } finally {
            em.close()
        }
    }

    fun addNewDormitory(dormitory: Dormitory?) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.persist(dormitory)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun removeDormitory(id: Int) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            var dormitory: Dormitory? = null
            try {
                dormitory = em.getReference(Dormitory::class.java, id)
            } catch (e: java.lang.Exception) {
                println("No such Dormitory")
            }
            em.remove(dormitory)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun getAllDormitories(): List<Dormitory>? {
        val em = getEntityManager()
        try {
            val criteriaQuery: CriteriaQuery<Dormitory> = em.criteriaBuilder.createQuery(Dormitory::class.java)
            criteriaQuery.select(criteriaQuery.from(Dormitory::class.java))
            val query: Query = em.createQuery(criteriaQuery)

            return query.resultList.filterIsInstance(Dormitory::class.java)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return null
    }

    fun getDormitoryById(id: Int) : Dormitory? {
        var em = getEntityManager()
        val cb = em.criteriaBuilder
        val criteriaQuery = cb.createQuery(Dormitory::class.java)
        val root = criteriaQuery.from(Dormitory::class.java)
        criteriaQuery.select(root).where(cb.equal(root.get<Int>("dormitoryId"), id))
        val query: Query = em.createQuery(criteriaQuery)
        return try {
            return query.singleResult as Dormitory
        } catch (e: NoResultException) {
            println("Could not find such resident")
            null
        }
    }
}