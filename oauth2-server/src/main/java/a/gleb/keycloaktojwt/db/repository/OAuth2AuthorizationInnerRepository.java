package a.gleb.keycloaktojwt.db.repository;

import a.gleb.keycloaktojwt.db.entity.OAuth2AuthorizationInner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface OAuth2AuthorizationInnerRepository extends JpaRepository<OAuth2AuthorizationInner, UUID> {

    @Query(
            value = """
                    SELECT * FROM oauth2_authorization 
                        WHERE principal_name = :principalName AND access_token_issued_at IS NOT NULL 
                            ORDER BY access_token_issued_at DESC LIMIT 1;
                    """,
            nativeQuery = true
    )
    OAuth2AuthorizationInner findByPrincipalName(@Param("principalName") @NonNull String principalName);
}
