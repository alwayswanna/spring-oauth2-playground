package a.gleb.oauth2server.db.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.AUTO
import java.util.*

@Entity(name = "account_role")
data class AccountRole(
    @Id
    @GeneratedValue(strategy = AUTO)
    val id: UUID?,

    @Column(name = "role_name")
    val roleName: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "account_to_role",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "account_id")]
    )
    private val roles: Set<Account> = HashSet()

)