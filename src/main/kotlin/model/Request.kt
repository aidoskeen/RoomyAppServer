package model

import enums.RequestStatus
import javax.persistence.*

@Entity
open class Request() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var requestId: Int? = null
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    open var requester: Resident? = null
    open var placeId: String = ""
    open var roomId: String? = ""
    open var dormitoryId: String = ""
    open var requestStatus: RequestStatus = RequestStatus.NONE
}