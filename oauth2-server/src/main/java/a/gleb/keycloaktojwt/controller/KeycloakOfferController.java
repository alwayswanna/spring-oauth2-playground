package a.gleb.keycloaktojwt.controller;

import a.gleb.keycloaktojwt.service.TokenOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/offer")
public class KeycloakOfferController {

    private final TokenOfferService tokenOfferService;

    @GetMapping
    private Map<String, String> offerJwtToken() {
        return tokenOfferService.offer();
    }
}
