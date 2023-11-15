package a.gleb.keycloaktojwt.service;

import a.gleb.keycloaktojwt.db.entity.Account;
import a.gleb.keycloaktojwt.db.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @NonNull
    public Account findAccountByUsername(@NonNull String username) {
        return accountRepository.findAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("User with username=%s does not exists", username)
                        )
                );
    }
}
