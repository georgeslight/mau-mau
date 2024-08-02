package de.htwberlin.gameui.impl;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.persistence.repo.GameRepository;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import de.htwberlin.gameui.api.GameUIInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.stream.IntStream;

@Controller
public class GameUIController implements GameUIInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameUIController.class);

    private final GameManagerInterface gameService;
    private final RuleEngineInterface ruleService;
    private final PlayerManagerInterface playerService;
    private final GameUIView view;
    private final GameRepository gameRepository;
    private final VirtualPlayerInterface virtualPlayerInterface;

    @Autowired
    public GameUIController(GameManagerInterface gameManagerInterface, PlayerManagerInterface playerManagerInterface,
                            RuleEngineInterface ruleService, GameUIView view, GameRepository gameRepository,
                            VirtualPlayerInterface virtualPlayerInterface) {
        this.view = view;
        this.playerService = playerManagerInterface;
        this.ruleService = ruleService;
        this.gameService = gameManagerInterface;
        this.gameRepository = gameRepository;
        this.virtualPlayerInterface = virtualPlayerInterface;
    }

    @Override
    public void run() {
        LOGGER.info("Game started");
        GameState gameState = this.init();
        // Save initialized Game
        gameRepository.save(gameState);
        boolean isRunning = true;
        while (isRunning) {
            Player currentPlayer = gameState.getPlayers().get(gameState.getCurrentPlayerIndex());
            LOGGER.debug("Current player: {}", currentPlayer.getName());

            if (currentPlayer.isVirtual()) {
                handleVirtualPlayerTurn(currentPlayer, gameState);
            } else {
                handleHumanPlayerTurn(currentPlayer, gameState);
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
                    drawCards(2, gameState, currentPlayer);
                }
            }

            if (!gameState.isGameRunning()) {
                Player winner = gameService.getWinner(gameState);
                LOGGER.info("Game ended. Winner: {}", winner.getName());
                view.showEndGame(gameState, winner);
                isRunning = false;
                gameRepository.save(gameState);
                continue;
            }

            gameService.nextPlayer(gameState);
            gameRepository.save(gameState);
            LOGGER.debug("Next player: {}", gameState.getPlayers().get(gameState.getCurrentPlayerIndex()).getName());
        }
        LOGGER.info("Game ended");
    }

    private void handleVirtualPlayerTurn(Player currentPlayer, GameState gameState) {
        Card topCard = gameState.getDiscardPile().get(gameState.getDiscardPile().size() - 1);
        LOGGER.debug("Top card on the discard pile: {}", topCard);

        Card playedCard = virtualPlayerInterface.decideCardToPlay(currentPlayer, topCard, ruleService);
        if (playedCard != null) {
            LOGGER.debug("Virtual player {} played card: {}", currentPlayer.getName(), playedCard);
            gameService.playCard(currentPlayer, playedCard, gameState);
            view.showPlayedCard(currentPlayer, playedCard);
            ruleService.applySpecialCardsEffect(playedCard, gameState.getRules());

            if (playedCard.getRank().equals(Rank.JACK)) {
                Suit wishedSuit = virtualPlayerInterface.decideSuit(currentPlayer, ruleService);
                ruleService.applyJackSpecialEffect(playedCard, wishedSuit, gameState.getRules());
                view.showWishedSuit(currentPlayer, wishedSuit);
                LOGGER.info("Virtual player {} wished suit: {}", currentPlayer.getName(), wishedSuit);
            }
        } else {
            LOGGER.info("Virtual player {} chose to draw cards", currentPlayer.getName());
            drawCards(gameState.getRules().getCardsToBeDrawn(), gameState, currentPlayer);
        }
    }

    private void handleHumanPlayerTurn(Player currentPlayer, GameState gameState) {
        playerService.sortPlayersCards(currentPlayer);
        view.showCurrentPlayerInfo(currentPlayer);

        Card topCard = gameState.getDiscardPile().get(gameState.getDiscardPile().size() - 1);
        LOGGER.debug("Top card on the discard pile: {}", topCard);
        view.showTopCard(topCard);

        int accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
        if (accumulatedDrawCount > 0) {
            LOGGER.info("Accumulated draw count: {}", accumulatedDrawCount);
            view.showAccumulatedDrawCount(accumulatedDrawCount);
        }

        String input = this.getPlayerInput(currentPlayer, topCard, gameState);

        Card playedCard = null;
        if (isNumeric(input)) {
            playedCard = currentPlayer.getHand().get(Integer.parseInt(input) - 1);
            LOGGER.debug("Played card: {}", playedCard);
        }

        if (playedCard != null) {
            gameService.playCard(currentPlayer, playedCard, gameState);
            view.showPlayedCard(currentPlayer, playedCard);
            ruleService.applySpecialCardsEffect(playedCard, gameState.getRules());

            if (playedCard.getRank().equals(Rank.JACK)) {
                Suit wishedSuit = view.getPlayerWishedSuit(currentPlayer);
                ruleService.applyJackSpecialEffect(playedCard, wishedSuit, gameState.getRules());
                view.showWishedSuit(currentPlayer, wishedSuit);
                LOGGER.info("Player wished suit: {}", wishedSuit);
            }
        } else {
            LOGGER.info("Player chose to draw cards");
            drawCards(accumulatedDrawCount, gameState, currentPlayer);
            if (currentPlayer.isSaidMau()) {
                currentPlayer.setSaidMau(false);
                LOGGER.debug("Player {} reset 'mau'", currentPlayer.getName());
            }
        }
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

            if (input.equalsIgnoreCase("end")) {
                gameState.setGameRunning(false);
                gameService.calcRankingPoints(gameState);
                return input;
            }

            if (input.equalsIgnoreCase("m")) {
                player.setSaidMau(true);
                view.showMauMessage(player);
                LOGGER.info("Player {} said 'mau'", player.getName());
                continue;
            }

            if (input.equalsIgnoreCase("d")) {
                return input;
            }

            try {
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
        gameState.getRules().setCardsToBeDrawn(0);
        LOGGER.info("Reset accumulated draw count to 0");
    }
}
