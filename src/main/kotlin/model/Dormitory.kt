package model

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
open class Dormitory() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var dormitoryId: Int? = null
    open var address: String = ""
    @OneToMany(mappedBy = "dormitory", cascade = [CascadeType.ALL], orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    open var rooms: MutableList<Room> = mutableListOf()
    @OneToMany(mappedBy = "dormitory", cascade = [CascadeType.ALL], orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    open var announcements: MutableList<Announcement> = mutableListOf()
    open var university: String = ""
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    open var administrator: Administrator? = null


    fun getRoomByNumber(number: Int): Room? {
         return rooms.find { it.roomNumber == number }
    }
}