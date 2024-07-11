//package de.htwberlin.persistence.service;
//
//import de.htwberlin.cardsmanagement.api.enums.Suit;
//import de.htwberlin.persistence.repo.RulesRepository;
//import de.htwberlin.rulesmanagement.api.model.Rules;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class RulesService {
//
//    private final RulesRepository rulesRepository;
//
//    @Autowired
//    public RulesService(RulesRepository rulesRepository) {
//        this.rulesRepository = rulesRepository;
//    }
//
//    public List<Rules> findRulesWithMoreCardsToBeDrawnThan(int cards) {
//        return rulesRepository.findRulesWithMoreCardsToBeDrawnThan(cards);
//    }
//
//    public List<Rules> findRulesByWishCard(Suit wishCard) {
//        return rulesRepository.findRulesByWishCard(wishCard);
//    }
//
//    public Rules findRulesByGameId(Long gameId) {
//        return rulesRepository.findRulesByGameId(gameId);
//    }
//
//    public List<Rules> findRulesWhereSkipNextPlayerTurnIsTrue() {
//        return rulesRepository.findRulesWhereSkipNextPlayerTurnIsTrue();
//    }
//
//    public List<Rules> findRulesWhereCanPlayAgain(boolean canPlayAgain) {
//        return rulesRepository.findRulesWhereCanPlayAgain(canPlayAgain);
//    }
//
//    public Rules saveRules(Rules rules) {
//        return rulesRepository.save(rules);
//    }
//
//    public void deleteAllRules() {
//        rulesRepository.deleteAll();
//    }
//}
