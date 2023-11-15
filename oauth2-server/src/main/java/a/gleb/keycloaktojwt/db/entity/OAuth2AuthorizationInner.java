package a.gleb.keycloaktojwt.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oauth2_authorization")
public class OAuth2AuthorizationInner {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "principal_name")
    private String principalName;

    @Column(name = "access_token_issued_at")
    private Instant accessTokenIssuedAt;
}
