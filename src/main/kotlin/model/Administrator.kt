package model

import javax.persistence.*

@Entity
@DiscriminatorValue("admin")
open class Administrator : User {
    @OneToOne(mappedBy = "administrator", cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER, optional = false)
    open var dormitory: Dormitory? = null

    fun assingDormotiry(dormitory: Dormitory) {
        this.dormitory = dormitory
        dormitory.administrator = this
    }

    constructor(name: String, surname: String, username: String, password: String) : super(name, surname, username, password)
    constructor()
}