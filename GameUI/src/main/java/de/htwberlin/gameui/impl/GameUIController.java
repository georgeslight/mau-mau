package de.htwberlin.gameui.impl;

import de.htwberlin.gameui.api.GameUIInterface;
import de.htwberlin.cardmanagement.api.enums.Rank;
import de.htwberlin.cardmanagement.api.enums.Suit;
import de.htwberlin.cardmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
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
    private PlayerManagerInterface playerService;
    private GameUIView view;

    @Autowired
    public GameUIController(GameManagerInterface gameManagerInterface, PlayerManagerInterface playerManagerInterface, RuleEngineInterface ruleService, GameUIView view) {
        this.view = view;
        this.playerService = playerManagerInterface;
        this.ruleService = ruleService;
        this.gameService = gameManagerInterface;
    }

    public GameUIController() {
    }

    @Override
    public void run() {
        GameState gameState = this.init();
        while (true) {
            Player currentPlayer = gameState.getPlayers().get(gameState.getCurrentPlayerIndex());
            playerService.sortPlayersCards(currentPlayer);
            view.showCurrentPlayerInfo(currentPlayer);

            Card topCard = gameState.getDiscardPile().peek();
            view.showTopCard(topCard);

            // If 7 was played
            int accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
            if (accumulatedDrawCount > 0) {
                view.showAccumulatedDrawCount(accumulatedDrawCount);
            }

            Card playedCard = this.getPlayerCardChoice(currentPlayer, topCard, gameState);

            if (playedCard != null) {
                gameService.playCard(currentPlayer, playedCard, gameState);
                view.showPlayedCard(currentPlayer, playedCard);
                // Set special card effects
                ruleService.applySpecialCardsEffect(playedCard, gameState.getRules());

                // Handle drawing cards if the player plays a 7
                if (playedCard.getRank().equals(Rank.SEVEN)) {
                    gameService.nextPlayer(gameState);
                    continue;
                }

                // Jack played
                if (playedCard.getRank().equals(Rank.JACK)) {
                    Suit wishedSuit = view.getPlayerWishedSuit(currentPlayer);
                    ruleService.applyJackSpecialEffect(playedCard,wishedSuit, gameState.getRules());
                    view.showWishedSuit(currentPlayer, wishedSuit);
                }
            } else {
                // If player chooses to draw cards
                this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
            }

            if (gameService.checkWinner(currentPlayer)) {
                view.showWinner(currentPlayer);
                break;
            }
            gameService.nextPlayer(gameState);
        }
    }

    public GameState init() {
        view.showWelcomeMessage();
        int numberOfPlayers = view.getNumberOfPlayers();
        String playerName = view.getPlayerName();
        GameState gameState = gameService.initializeGame(playerName, numberOfPlayers);
        view.showPlayers(gameState.getPlayers());
        gameState.getPlayers().forEach(player -> System.out.println(player.getName()));
        return gameState;
    }

    private Card getPlayerCardChoice(Player player, Card topCard, GameState gameState) {
        while (true) {
            String input = view.promptCardChoice();
            if (input.equalsIgnoreCase("draw")) {
                return null;
            }
            try {
                int cardIndex = Integer.parseInt(input);
                Card chosenCard = player.getHand().get(cardIndex);
                if (ruleService.isValidMove(chosenCard, topCard, gameState.getRules())) {
                    return chosenCard;
                } else {
                    view.showInvalidMoveMessage();
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                view.showInvalidInputMessage();
            }
        }
    }

    private void drawCards(int accumulatedDrawCount, GameState gameState, Player currentPlayer) {
        IntStream.range(0, Math.max(accumulatedDrawCount, 1))
                .forEach(i -> {
                    Card drawnCard = gameService.drawCard(gameState, currentPlayer);
                    view.showDrawnCard(currentPlayer, drawnCard);
                });
        // reset state
        gameState.getRules().setCardsTObeDrawn(0);
    }
}
