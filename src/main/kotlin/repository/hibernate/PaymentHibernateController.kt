package repository.hibernate

import model.Dormitory
import model.MonthlyPayment
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Query
import javax.persistence.criteria.CriteriaQuery

class PaymentHibernateController(
    private val entityManagerFactory: EntityManagerFactory
) {

    private fun getEntityManager(): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    fun editPayment(payment: MonthlyPayment) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.merge(payment)
            em.transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun addNewPayment(payment: MonthlyPayment?) {
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.persist(payment)
            em.transaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun getAllPayments(): List<MonthlyPayment>? {
        val em = getEntityManager()
        try {
            val criteriaQuery: CriteriaQuery<MonthlyPayment> = em.criteriaBuilder.createQuery(MonthlyPayment::class.java)
            criteriaQuery.select(criteriaQuery.from(MonthlyPayment::class.java))
            val query: Query = em.createQuery(criteriaQuery)

            return query.resultList.filterIsInstance(MonthlyPayment::class.java)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return null
    }
}