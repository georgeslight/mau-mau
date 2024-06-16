package de.htwberlin.impl.service;

import de.htwberlin.api.service.RuleEngineInterface;
import de.htwberlin.api.enums.Rank;
import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Rules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService implements RuleEngineInterface {

    private static final Logger LOGGER = LogManager.getLogger(RuleService.class);

    private Rules rules;
//     todo: why need of cardService, its not used
    private CardService cardService;

    public RuleService() {
        this.rules = new Rules();
        this.cardService = new CardService();
    }


//    todo: why?
    @Override
    public Integer getStartingCards() {
        return 5;
    }

    @Override
    public boolean isValidMove(Card card, Card topCard) {
        // If 9 was played, Nine can be played on any card
        if (card.getRank().equals(Rank.NINE)) return true;

        // Jack rules
        if (card.getRank().equals(Rank.JACK)) {
            // J can be played on any card except another Jack
            if (topCard.getRank().equals(Rank.JACK)) {
                LOGGER.warn("Cannot play Jack on Jack");
                return false;
            }
            return true;
        }

        // If Jack was played
        if (topCard.getRank().equals(Rank.JACK)) {
            if (card.getSuit().equals(rules.getWishCard())) {
                rules.setWishCard(null);
                return true;
            } else {
                System.out.println("You must play a card of the wished suit: " + rules.getWishCard());
                return false;
            }
        }

        // normal rules
        return card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank());

    }

    @Override
    public Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount) {
        int counts = 1;
        // play 8 (skips next player)
        if (rules.isSkipNextPlayerTurn()) {
            counts++;
            LOGGER.debug("Skipped player at index: {}", (currentPlayerIndex + 1) % playerCount);
            // reset state
            rules.setSkipNextPlayerTurn(false);
        }

        // play A (can play again)
        if (rules.isCanPlayAgain()) {
            counts = 0;
            // reset state
            rules.setCanPlayAgain(false);
        }
//        if (!rules.isGameDirection()) counts = -counts;

        int nextPlayerIndex = (currentPlayerIndex + counts) % playerCount;

        if (nextPlayerIndex < 0) {
            nextPlayerIndex += playerCount;
        }
        LOGGER.debug("nextPlayerIndex: {}", nextPlayerIndex);
        return nextPlayerIndex;
    }

    @Override
    public void applySpecialCardsEffect(Card card) {
        switch (card.getRank()) {
            case SEVEN:
                rules.setCardsTObeDrawn(rules.getCardsToBeDrawn() + 2);
                break;
            case EIGHT:
                rules.setSkipNextPlayerTurn(true);
                rules.setCardsTObeDrawn(0);
                break;
            case ACE:
                rules.setCanPlayAgain(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void applyJackSpecialEffect(Card card, Suit wishedSuit) {
        rules.setWishCard(wishedSuit);
    }


    @Override
    public Integer calculateScore(List<Card> cards) {
        int score = 0;
        for (Card card : cards) {
            switch (card.getRank()) {
                case SEVEN:
                    score -= 7;
                    break;
                case EIGHT:
                    score -= 8;
                    break;
                case NINE:
                    score -= 9;
                    break;
                case TEN:
                    score -= 10;
                    break;
                case JACK:
                    score -= 2;
                    break;
                case QUEEN:
                    score -= 3;
                    break;
                case KING:
                    score -= 4;
                    break;
                case ACE:
                    score -= 11;
                    break;
            }
        }
        return score;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

//    public CardService getCardService() {
//        return cardService;
//    }
//
//    public void setCardService(CardService cardService) {
//        this.cardService = cardService;
//    }
}
