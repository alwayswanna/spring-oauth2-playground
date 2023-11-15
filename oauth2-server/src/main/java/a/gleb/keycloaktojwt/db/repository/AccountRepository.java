package a.gleb.keycloaktojwt.db.repository;

import a.gleb.keycloaktojwt.db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findAccountByUsername(@NonNull String username);
}
