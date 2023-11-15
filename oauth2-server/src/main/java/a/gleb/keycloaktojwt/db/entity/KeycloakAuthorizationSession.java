package a.gleb.keycloaktojwt.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "keycloak_authorization_session")
public class KeycloakAuthorizationSession {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "session_state")
    private String sessionState;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "access_token_expire_in")
    private LocalDateTime accessTokenExpireIn;

    @Column(name = "username")
    private String username;

    @Column(name = "create_at")
    private LocalDateTime createAt;
}
