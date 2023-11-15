package a.gleb.cardservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/card")
public class CardUserController {

    @GetMapping
    public String getCard() {
        var context = SecurityContextHolder.getContext();
        log.info("SecurityContext: {}", context);
        return String.format("Hi user, %s", context.getAuthentication().getName());
    }
}
