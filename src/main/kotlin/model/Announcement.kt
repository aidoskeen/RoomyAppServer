package model

import javax.persistence.*

@Entity
class Announcement() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var announcementId: Int? = null
    open var title: String = ""
    open var text: String = ""
    @ManyToOne(fetch = FetchType.LAZY)
    open var dormitory: Dormitory? = null

    constructor(title: String, text: String): this() {
        this.title = title
        this.text = text
    }
}