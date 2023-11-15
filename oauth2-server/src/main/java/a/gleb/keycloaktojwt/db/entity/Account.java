package a.gleb.keycloaktojwt.db.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "accounts_user")
public class Account {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_to_role",
            joinColumns = { @JoinColumn(name = "account_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<AccountRole> roles = new HashSet<>();

    @Column(name = "email")
    private String email;

    @Column(name = "disabled")
    private boolean disabled = false;

    @Column(name = "birth_date")
    private LocalDate birthData;
}
