package a.gleb.oauth2server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class OAuth2ServerController {

    @GetMapping
    public String helloWorlds() {
        var context = SecurityContextHolder.getContext();
        var name = context.getAuthentication().getName();
        log.info("Context: {}", context);
        return String.format("Hello user, %s", name);
    }
}
