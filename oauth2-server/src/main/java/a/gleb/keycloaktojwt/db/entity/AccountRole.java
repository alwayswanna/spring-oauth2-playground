package a.gleb.keycloaktojwt.db.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account_role")
public class AccountRole {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_to_role",
            joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "account_id") })
    private Set<Account> roles = new HashSet<>();
}
