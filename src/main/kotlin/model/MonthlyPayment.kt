package model

import enums.PaymentStatus
import javax.persistence.*

@Entity
open class MonthlyPayment() {
    @Id
    open var paymentId: Int? = null
    open var month: String = ""
    open var dueDate: String = ""
    open var paymentAmount: Long? = null
    open var paymentStatus: PaymentStatus = PaymentStatus.NONE
    @OneToOne(mappedBy = "monthlyPayment", cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY, optional = true)
    open var place: Place? = null
}