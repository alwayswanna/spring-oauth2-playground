package a.gleb.oauth2server.db.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import jakarta.persistence.GenerationType.AUTO
import java.util.*

@Entity(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = AUTO)
    var id: UUID?,

    @Column(name = "preferred_username")
    var preferredUsername: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    name = "account_to_role",
    joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
    inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    @JsonIgnoreProperties("roles")
    var roles: List<Role> = mutableListOf(),

    @Column(name = "disabled")
    var disabled: Boolean
)
