package model

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "userType",
    discriminatorType = DiscriminatorType.STRING)
open class User() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var userId: Int? = null
    open var name: String = ""
    open var surname: String = ""
    open var username: String = ""
    open var password: String = ""
    @Column(insertable = false, updatable = false)
    open var userType: String? = null
    fun getFullName(): String {
        return this.name + " " + this.surname
    }

    constructor(name: String, surname: String, username: String, password: String) : this() {
        this.name = name
        this.surname = surname
        this.username = username
        this.password = password
    }
}
