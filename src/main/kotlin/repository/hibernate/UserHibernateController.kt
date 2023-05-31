package repository.hibernate

import model.Dormitory
import model.Resident
import model.User
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.Query

class UserHibernateController(
    private val entityManagerFactory: EntityManagerFactory
) {
    private fun getEntityManager(): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    fun editUsers(user: User) {
        var em = getEntityManager()
        try {
            em = getEntityManager()
            em.transaction.begin()
            em.merge<Any>(user)
            em.transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun addNewUser(user: User?): Boolean {
        var em = getEntityManager()
        try {
            em = getEntityManager()
            em.transaction.begin()
            em.persist(user)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        } finally {
            em.close()
        }
        return true
    }

    fun removeUser(id: Int) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            var user: User? = null
            try {
                user = em.getReference(User::class.java, id)
            } catch (e: java.lang.Exception) {
                println("No such Dormitory")
            }
            em.remove(user)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            println("Could not perform removal")
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun getUserByLoginData(login: String?, password: String?): User? {
        val em = getEntityManager()
        val cb = em.criteriaBuilder
        val criteriaQuery = cb.createQuery(User::class.java)
        val root = criteriaQuery.from(User::class.java)
        criteriaQuery.select(root).where(cb.like(root.get("username"), login))
        criteriaQuery.select(root).where(cb.like(root.get("password"), password))
        val query: Query = em.createQuery(criteriaQuery)
        return try {
            query.singleResult as User
        } catch (e: NoResultException) {
            println("Could not find user with these credentials")
            null
        }
    }

    fun getResidentById(id: Int) : Resident? {
        var em = getEntityManager()
        var resident: Resident? = null
        try {
            em = getEntityManager()
            em.transaction.begin()
            resident = em.find(Resident::class.java, id)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            println("No such Resident")
        }
        return resident
    }

}