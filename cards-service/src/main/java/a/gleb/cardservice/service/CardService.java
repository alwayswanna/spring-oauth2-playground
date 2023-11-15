package a.gleb.cardservice.service;

import a.gleb.cardservice.db.repository.CardRepository;
import a.gleb.cardservice.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
}
