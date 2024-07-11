//package de.htwberlin.persistence.service;
//
//import de.htwberlin.cardsmanagement.api.model.Card;
//import de.htwberlin.persistence.repo.CardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CardService {
//
//    private final CardRepository cardRepository;
//
//    @Autowired
//    public CardService(CardRepository cardRepository) {
//        this.cardRepository = cardRepository;
//    }
//
//    public Card saveCard(Card card) {
//        return cardRepository.save(card);
//    }
//
//    public Optional<Card> findCardById(Long id) {
//        return cardRepository.findById(id);
//    }
//
//    public List<Card> findAllCards() {
//        return cardRepository.findAll();
//    }
//
//    public void deleteCardById(Long id) {
//        cardRepository.deleteById(id);
//    }
//
//    public void deleteAllCards() {
//        cardRepository.deleteAll();
//    }
//
//    public long countCards() {
//        return cardRepository.count();
//    }
//
//    public boolean existsCardById(Long id) {
//        return cardRepository.existsById(id);
//    }
//
//    public List<Card> findPlayerCardsByGameId(Long gameId) {
//        return cardRepository.findPlayerCardsByGameId(gameId);
//    }
//
//    public List<Card> findPileCardsByGameId(Long gameId) {
//        return cardRepository.findPileCardsByGameId(gameId);
//    }
//
//    public List<Card> findDeckCardsByGameId(Long gameId) {
//        return cardRepository.findDeckCardsByGameId(gameId);
//    }
//}
