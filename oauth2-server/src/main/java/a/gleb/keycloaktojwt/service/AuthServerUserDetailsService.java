package a.gleb.keycloaktojwt.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServerUserDetailsService implements UserDetailsService {

    private static final String DEFAULT_ROLE = "uma_authorization";
    private static final String DEFAULT_EMPTY_PASSWORD = "#PASSWORD#";

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.trace("Load user from database, [username={}, timestamp={}]", username, LocalDateTime.now());
        var account = accountService.findAccountByUsername(username);

        return User.builder()
                .username(account.getUsername())
                .password(DEFAULT_EMPTY_PASSWORD)
                .disabled(account.isDisabled())
                .authorities(DEFAULT_ROLE)
                .build();
    }
}
