package a.gleb.cardservice.controller;

import a.gleb.cardservice.model.CardResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    @GetMapping
    public CardResponse getRandomCard(){
        return CardResponse.builder().build();
    }
}
