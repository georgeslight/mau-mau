package de.htwberlin.rulesmanagement.impl;

import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.rulesmanagement.api.model.Rules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService implements RuleEngineInterface {

    private static final Logger LOGGER = LogManager.getLogger(RuleService.class);

    @Override
    public boolean isValidMove(Card card, Card topCard, Rules rules) {
        LOGGER.debug("Validating move: Playing {} on top card {}", card, topCard);

        // 7 was played
        if (topCard.getRank().equals(Rank.SEVEN)) {
            LOGGER.debug("Top card is a SEVEN");
            if (card.getRank().equals(Rank.SEVEN)) {
                LOGGER.debug("Player is playing another SEVEN");
                return true;
            }
            if (rules.getCardsToBeDrawn() != 0) {
                LOGGER.debug("Invalid move: Player must draw cards or play another SEVEN");
                return false;
            }
        }

        // If 9 was played, Nine can be played on any card
        if (card.getRank().equals(Rank.NINE)) {
            LOGGER.debug("NINE can be played on any card");
            return true;
        }

        // If Jack was played
        if (topCard.getRank().equals(Rank.JACK) && rules.getWishCard() != null) {
            LOGGER.debug("Top card is a JACK with wished suit: {}", rules.getWishCard());
            if (card.getSuit().equals(rules.getWishCard()) && !card.getRank().equals(Rank.JACK)) {
                LOGGER.debug("Playing card of the wished suit: {}", card.getSuit());
                rules.setWishCard(null);
                return true;
            } else {
                LOGGER.debug("Invalid move: Must play a card of the wished suit: {}", rules.getWishCard());
                return false;
            }
        }

        // Jack rules
        if (card.getRank().equals(Rank.JACK)) {
            LOGGER.debug("Player is playing a JACK");
            // J can be played on any card except another Jack
            if (topCard.getRank().equals(Rank.JACK)) {
                LOGGER.warn("Cannot play JACK on JACK");
                return false;
            }
            return true;
        }

        // normal rules
        boolean validMove = card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank());
        LOGGER.debug("Move is {} based on matching rank or suit", validMove ? "valid" : "invalid");
        return validMove;
    }

    @Override
    public Integer calculateNextPlayerIndex(Integer currentPlayerIndex, Integer playerCount, Rules rules) {
        int counts = 1;
        // play 8 (skips next player)
        if (rules.isSkipNextPlayerTurn()) {
            counts++;
            LOGGER.debug("Skipping next player due to EIGHT");
            // reset state
            rules.setSkipNextPlayerTurn(false);
        }
        // play A (can play again)
        if (rules.isCanPlayAgain()) {
            counts = 0;
            LOGGER.debug("Player can play again due to ACE");
            // reset state
            rules.setCanPlayAgain(false);
        }

        int nextPlayerIndex = (currentPlayerIndex + counts) % playerCount;

        if (nextPlayerIndex < 0) {
            nextPlayerIndex += playerCount;
        }
        LOGGER.debug("Next player index calculated: {}", nextPlayerIndex);
        return nextPlayerIndex;
    }

    @Override
    public void applySpecialCardsEffect(Card card, Rules rules) {
        LOGGER.debug("Applying special effect for card: {}", card);
        switch (card.getRank()) {
            case SEVEN:
                int newDrawCount = rules.getCardsToBeDrawn() + 2;
                rules.setCardsToBeDrawn(newDrawCount);
                LOGGER.debug("SEVEN played: cards to be drawn increased to {}", newDrawCount);
                break;
            case EIGHT:
                rules.setSkipNextPlayerTurn(true);
                rules.setCardsToBeDrawn(0);//playing 8 cancels the draw count
                LOGGER.debug("EIGHT played: next player will be skipped");
                break;
            case ACE:
                rules.setCanPlayAgain(true);
                LOGGER.debug("ACE played: player can play again");
                break;
            default:
                LOGGER.debug("No special effect for card: {}", card);
                break;
        }
    }

    @Override
    public void applyJackSpecialEffect(Card card, Suit wishedSuit, Rules rules) {
        LOGGER.debug("Applying JACK special effect: wished suit set to {}", wishedSuit);
        rules.setWishCard(wishedSuit);
    }

    @Override
    public Integer calculateScore(List<Card> cards) {
        int score = 0;
        LOGGER.debug("Calculating score for cards: {}", cards);
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
        LOGGER.debug("Total score calculated: {}", score);
        return score;
    }
}
