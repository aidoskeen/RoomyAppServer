package model

import enums.RequestStatus
import javax.persistence.*

@Entity
open class Place() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var placeId: Int? = null
    open var available: Boolean = true
    open var price: Long = 0
    @ManyToOne(fetch = FetchType.LAZY)
    open var room: Room? = null
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    open var livingResident: Resident? = null
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentId")
    open var monthlyPayment: MonthlyPayment? = null
    open var requestStatus: RequestStatus = RequestStatus.NONE


}