package de.htwberlin.gameui.impl;

import de.htwberlin.gameui.api.GameUIInterface;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
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

    @Override
    public void run() {
        LOGGER.info("Game started");
        GameState gameState = this.init();
        boolean isRunning = true;
        while (isRunning) {
            Player currentPlayer = gameState.getPlayers().get(gameState.getCurrentPlayerIndex());
            LOGGER.debug("Current player: {}", currentPlayer.getName());

            if (currentPlayer.isVirtual()) {
                gameService.nextPlayer(gameState);
                continue;
            }

            playerService.sortPlayersCards(currentPlayer);
            view.showCurrentPlayerInfo(currentPlayer);

            Card topCard = gameState.getDiscardPile().get(gameState.getDiscardPile().size() - 1);
            LOGGER.debug("Top card on the discard pile: {}", topCard);
            view.showTopCard(topCard);

            // If 7 was played
            int accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
            if (accumulatedDrawCount > 0) {
                LOGGER.info("Accumulated draw count: {}", accumulatedDrawCount);
                view.showAccumulatedDrawCount(accumulatedDrawCount);
            }

            String input = this.getPlayerInput(currentPlayer, topCard, gameState);

            // ends game
            if (!gameState.isGameRunning()) {
                Player winner = gameService.getWinner(gameState);
                LOGGER.info("Game ended. Winner: {}", winner.getName());
                view.showEndGame(gameState, winner);
                isRunning = false;
                continue;
            }

            Card playedCard = null;
            if (isNumeric(input)) {
                playedCard = currentPlayer.getHand().get(Integer.parseInt(input) - 1);
                LOGGER.debug("Played card: {}", playedCard);
            }

            if (playedCard != null) {
                gameService.playCard(currentPlayer, playedCard, gameState);
                view.showPlayedCard(currentPlayer, playedCard);
                // Set special card effects
                ruleService.applySpecialCardsEffect(playedCard, gameState.getRules());

                // Jack played
                if (playedCard.getRank().equals(Rank.JACK)) {
                    Suit wishedSuit = view.getPlayerWishedSuit(currentPlayer);
                    ruleService.applyJackSpecialEffect(playedCard, wishedSuit, gameState.getRules());
                    view.showWishedSuit(currentPlayer, wishedSuit);
                    LOGGER.info("Player wished suit: {}", wishedSuit);
                }
            } else {
                // If player chooses to draw cards
                LOGGER.info("Player chose to draw cards");
                this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
                // if player saidMau last round and didn't win, mau is reset
                if (currentPlayer.isSaidMau()) {
                    currentPlayer.setSaidMau(false);
                    LOGGER.debug("Player {} reset 'mau'", currentPlayer.getName());
                }
            }

            if (gameService.checkEmptyHand(currentPlayer)) {
                if (currentPlayer.isSaidMau()) {
                    LOGGER.info("Player {} has won the round", currentPlayer.getName());
                    gameService.endRound(gameState);
                    view.showWinner(currentPlayer);
                    view.showRankingPoints(gameState);
                } else {
                    LOGGER.warn("Player {} failed to say 'mau'", currentPlayer.getName());
                    view.showMauFailureMessage(currentPlayer);
                    this.drawCards(2, gameState, currentPlayer);
                }
            }
            // If player has no cards but hasn't said mau, draws cards
            gameService.nextPlayer(gameState);
            LOGGER.debug("Next player: {}", gameState.getPlayers().get(gameState.getCurrentPlayerIndex()).getName());
        }
        LOGGER.info("Game ended");
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid numeric input: {}", str);
            return false;
        }
        return true;
    }

    public GameState init() {
        view.showWelcomeMessage();
        int numberOfPlayers = view.getNumberOfPlayers();
        String playerName = view.getPlayerName();
        LOGGER.info("Initializing game for {} players with first player named {}", numberOfPlayers, playerName);
        GameState gameState = gameService.initializeGame(playerName, numberOfPlayers);
        view.showPlayers(gameState.getPlayers());
        return gameState;
    }

    private String getPlayerInput(Player player, Card topCard, GameState gameState) {
        while (true) {
            String input = view.promptCardChoice();
            LOGGER.debug("Player input: {}", input);

            // End game
            if (input.equalsIgnoreCase("end")) {
                gameState.setGameRunning(false);
                gameService.calcRankingPoints(gameState);
                return input;
            }

            // Handle "m" input
            if (input.equalsIgnoreCase("m")) {
                player.setSaidMau(true);
                view.showMauMessage(player);
                LOGGER.info("Player {} said 'mau'", player.getName());
                // Continue to prompt for another input
                continue;
            }

            if (input.equalsIgnoreCase("d")) {
                return input;
            }

            try {
                // Added an explicit check for valid card index range
                int cardIndex = Integer.parseInt(input) - 1;
                if (cardIndex < 0 || cardIndex >= player.getHand().size()) {
                    throw new IndexOutOfBoundsException();
                }
                if (ruleService.isValidMove(player.getHand().get(cardIndex), topCard, gameState.getRules())) {
                    return input;
                } else {
                    view.showInvalidMoveMessage();
                    LOGGER.warn("Invalid move: card {} on top card {}", player.getHand().get(cardIndex), topCard);
                }
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid input: {}", input);
                view.showInvalidInputMessage();
            } catch (IndexOutOfBoundsException e) {
                LOGGER.warn("Invalid card index: {}", input);
                view.showInvalidInputMessage();
            }
        }
    }

    private void drawCards(int accumulatedDrawCount, GameState gameState, Player currentPlayer) {
        IntStream.range(0, Math.max(accumulatedDrawCount, 1))
                .forEach(i -> {
                    Card drawnCard = gameService.drawCard(gameState, currentPlayer);
                    view.showDrawnCard(currentPlayer, drawnCard);
                    LOGGER.debug("Player {} drew card {}", currentPlayer.getName(), drawnCard);
                });
        // reset state
        gameState.getRules().setCardsTObeDrawn(0);
        LOGGER.info("Reset accumulated draw count to 0");
    }
}