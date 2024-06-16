package de.htwberlin.impl.service;

import de.htwberlin.api.GameUIInterface;
import de.htwberlin.api.enums.Rank;
import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.api.service.RuleEngineInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.stream.IntStream;

@Controller
public class GameUIController implements GameUIInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameUIController.class);

    private GameManagerInterface gameService;
    private RuleEngineInterface ruleService;
    private GameUIView view;

    @Autowired
    public GameUIController(GameManagerInterface gameManagerInterface, RuleEngineInterface ruleService, GameUIView view) {
        this.view = view;
        this.ruleService = ruleService;
        this.gameService = gameManagerInterface;
    }

    public GameUIController() {
    }

    public GameManagerInterface getGameService() {
        return gameService;
    }

    public void setGameService(GameManagerInterface gameManagerInterface) {
        this.gameService = gameManagerInterface;
    }

    /**
     * here view and service
     */
    @Override
    public void run() {
        GameState gameState = view.init();
        while (true) {
            System.out.println();
            Player currentPlayer = gameState.getPlayers().get(gameState.getCurrentPlayerIndex());
            System.out.println(currentPlayer.getName() + "'s turn. Your hand: ");
            view.displayHand(currentPlayer);

            Card topCard = gameState.getDiscardPile().peek();
            System.out.println("Top card on the discard pile: " + topCard);

            // If 7 was played
            int accumulatedDrawCount = ruleService.getRules().getCardsToBeDrawn();
            if (accumulatedDrawCount > 0) {
                System.out.println("You need to draw " + accumulatedDrawCount + " cards, or play another 7.");
            }

            Card playedCard = view.getPlayerCardChoice(currentPlayer, topCard);

            if (playedCard != null) {
                gameService.playCard(currentPlayer, playedCard, gameState);
                System.out.println(currentPlayer.getName() + " played: " + playedCard);
                // Set special card effects
                ruleService.applySpecialCardsEffect(playedCard);

                // Handle drawing cards if the player plays a 7
                if (playedCard.getRank().equals(Rank.SEVEN)) {
                    gameService.nextPlayer(gameState);
                    continue;
                }

                // Jack played
                if (playedCard.getRank().equals(Rank.JACK)) {
                    Suit wishedSuit = view.getPlayerWishedSuit(currentPlayer);
                    ruleService.applyJackSpecialEffect(playedCard,wishedSuit);
                    System.out.println(currentPlayer.getName() + " wishes for " + wishedSuit);
                }
            } else {
                // If player chooses to draw cards
                IntStream.range(0, Math.max(accumulatedDrawCount, 1))
                        .forEach(i -> {
                            Card drawnCard = gameService.drawCard(gameState, currentPlayer);
                            System.out.println(currentPlayer.getName() + " drew a card: " + drawnCard);
                        });
                // reset state
                ruleService.getRules().setCardsTObeDrawn(0);
            }

            if (gameService.checkWinner(currentPlayer)) {
                System.out.println(currentPlayer.getName() + " wins the game!");
                break;
            }
            gameService.nextPlayer(gameState);
        }
    }
}
