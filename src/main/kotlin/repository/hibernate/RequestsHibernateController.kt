package repository.hibernate

import model.Dormitory
import model.Request
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.Query
import javax.persistence.criteria.CriteriaQuery

class RequestsHibernateController(
    val entityManagerFactory: EntityManagerFactory
) {
    private fun getEntityManager(): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    fun editRequest(request: Request) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.merge<Any>(request)
            em.transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            em?.close()
        }
    }

    fun addNewRequest(request: Request) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.persist(request)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun removeRequest(id: Int) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            var request: Request? = null
            try {
                request = em.getReference(Request::class.java, id)
            } catch (e: java.lang.Exception) {
                println("No such Dormitory")
            }
            em.remove(request)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }
    fun getRequestsByDormitoryId(dormitoryId: Int): List<Dormitory?>? {
        val entityManager = getEntityManager()
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Dormitory::class.java)
        val root = criteriaQuery.from(Dormitory::class.java)
        val inClause = criteriaBuilder.`in`(root.get<Int>("dorm_num"))
        val requests = getAllRequests() ?: return null
        for (request in requests) {
            if (request != null) {
                //inClause.value(request.dormitoryId)
            }
        }
        criteriaQuery.select(root).where(inClause)
        return try {
            val query = entityManager.createQuery(criteriaQuery)
            query.resultList
        } catch (e: NoResultException) {
            null
        }
    }

    fun getAllRequests(): List<Request?>? {
        val em = getEntityManager()
        try {
            val criteriaQuery: CriteriaQuery<Request> = em.criteriaBuilder.createQuery(Request::class.java)
            criteriaQuery.select(criteriaQuery.from(Request::class.java))
            val query: Query = em.createQuery(criteriaQuery)

            return query.resultList.filterIsInstance(Request::class.java)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return null
    }

    fun getRequestById(id: Int) : Request? {
        val em = getEntityManager()
        var request: Request? = null
        try {
            em.transaction.begin()
            request = em.find(Request::class.java, id)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            println("Request does not exist")
        }
        return request
    }
}