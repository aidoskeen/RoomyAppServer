package model

import javax.persistence.*

@Entity
@DiscriminatorValue("resident")
class Resident : User {
    @OneToOne(mappedBy = "livingResident", cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY, optional = true)
    open var place: Place? = null
    @OneToOne(mappedBy = "requester", cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY, optional = true)
    open var request: Request? = null
    open var roomNumber: Int? = null

    constructor(name: String, surname: String, username: String, password: String) : super(name, surname, username, password)
    constructor()
}