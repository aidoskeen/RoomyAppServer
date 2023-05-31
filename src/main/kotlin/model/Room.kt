package model

import enums.RoomSize
import enums.RoomType
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
open class Room() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var roomNumber: Int? = null
    open var roomType: RoomType = RoomType.SINGLE
    open var roomSize: RoomSize = RoomSize.SMALL
    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL], orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    open var places: MutableList<Place> = mutableListOf()
    open var description: String = ""
    @ManyToOne(fetch = FetchType.LAZY)
    open var dormitory: Dormitory? = null

}