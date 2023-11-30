package a.gleb.oauth2server.db.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.AUTO
import java.time.LocalDate
import java.util.*

@Entity(name = "accounts_user")
data class Account(

    @Id
    @GeneratedValue(strategy = AUTO)
    var id: UUID?,

    @Column(name = "username")
    var username: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "first_name")
    var firstName: String,

    @Column(name = "middle_name")
    var middleName: String,

    @Column(name = "last_name")
    var lastName: String?,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "account_to_role",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<AccountRole> = HashSet(),

    @Column(name = "email")
    var email: String,

    @Column(name = "disabled")
    var disabled: Boolean,

    @Column(name = "birth_date")
    var birthDate: LocalDate
)
