package a.gleb.keycloaktojwt.db.repository;

import a.gleb.keycloaktojwt.db.entity.KeycloakAuthorizationSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface KeycloakAuthorizationSessionRepository extends JpaRepository<KeycloakAuthorizationSession, UUID> {

    List<KeycloakAuthorizationSession> findAllByUsername(@NonNull String username);
}
