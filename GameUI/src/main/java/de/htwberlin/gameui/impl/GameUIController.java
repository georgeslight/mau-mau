package de.htwberlin.gameui.impl;

import de.htwberlin.gameui.api.GameUIInterface;
import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
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

            Card topCard = gameState.getDiscardPile().get(gameState.getDiscardPile().size() - 1);
            view.showTopCard(topCard);

            // If 7 was played
            int accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
            if (accumulatedDrawCount > 0) {
                view.showAccumulatedDrawCount(accumulatedDrawCount);
            }

            String input = this.getPlayerInput(currentPlayer, topCard, gameState);

            // ends game
            if (!gameState.isGameRunning()) {
                Player winner = gameService.getWinner(gameState);
                view.showEndGame(gameState, winner);
                break;
            }

            Card playedCard = null;
            if (isNumeric(input)) playedCard = currentPlayer.getHand().get(Integer.parseInt(input) - 1);

            if (playedCard != null) {
                gameService.playCard(currentPlayer, playedCard, gameState);
                view.showPlayedCard(currentPlayer, playedCard);
                // Set special card effects
                ruleService.applySpecialCardsEffect(playedCard, gameState.getRules());

                // Jack played
                if (playedCard.getRank().equals(Rank.JACK)) {
                    Suit wishedSuit = view.getPlayerWishedSuit(currentPlayer);
                    ruleService.applyJackSpecialEffect(playedCard,wishedSuit, gameState.getRules());
                    view.showWishedSuit(currentPlayer, wishedSuit);
                }


            } else {
                // If player chooses to draw cards
                this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
                // if player saidMau last round and didn't win, mau is reset
                if (currentPlayer.isSaidMau()) {
                    currentPlayer.setSaidMau(false);
                }
            }

            if (gameService.checkEmptyHand(currentPlayer)) {
                if (currentPlayer.isSaidMau()) {
                    gameService.endRound(gameState);
                    view.showWinner(currentPlayer);
                    view.showRankingPoints(gameState);
                } else {
                    view.showMauFailureMessage(currentPlayer);
                    this.drawCards(2, gameState, currentPlayer);
                }
            }
            // If player has no cards but hasn't said mau, draws cards
            gameService.nextPlayer(gameState);
        }
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public GameState init() {
        view.showWelcomeMessage();
        int numberOfPlayers = view.getNumberOfPlayers();
        String playerName = view.getPlayerName();
        GameState gameState = gameService.initializeGame(playerName, numberOfPlayers);
        view.showPlayers(gameState.getPlayers());
        return gameState;
    }

    private String getPlayerInput(Player player, Card topCard, GameState gameState) {
        while (true) {
            String input = view.promptCardChoice();

            // End game
            if (input.equalsIgnoreCase("end")) {
                gameState.setGameRunning(false);
                gameService.calcRankingPoints(gameState);
                return input;
            }

            // Handle "mau" input
            if (input.equalsIgnoreCase("mau")) {
                player.setSaidMau(true);
                view.showMauMessage(player);
                // Continue to prompt for another input
                input = view.promptCardChoice();
            }

            if (input.equalsIgnoreCase("draw")) {
                return input;
            }

            try {
                if (ruleService.isValidMove(player.getHand().get(Integer.parseInt(input) - 1), topCard, gameState.getRules())) {
                    return input;
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
