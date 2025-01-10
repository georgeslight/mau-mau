package de.htwberlin.gameui.impl;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.gameengine.exception.EmptyPileException;
import de.htwberlin.gameui.api.GameUIInterface;
import de.htwberlin.persistence.repo.GameRepository;
import de.htwberlin.playermanagement.api.exception.EmptyHandException;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class GameUIController implements GameUIInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameUIController.class);
    private static final int POLLING_DELAY_MS = 3000;

    private final GameManagerInterface gameService;
    private final RuleEngineInterface ruleService;
    private final PlayerManagerInterface playerService;
    private final GameUIView view;
    private final GameRepository gameRepository;
    private final VirtualPlayerInterface virtualPlayerInterface;

    @Autowired
    public GameUIController(GameManagerInterface gameService, PlayerManagerInterface playerService,
                            RuleEngineInterface ruleService, GameUIView view, GameRepository gameRepository,
                            VirtualPlayerInterface virtualPlayerInterface) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.ruleService = ruleService;
        this.view = view;
        this.gameRepository = gameRepository;
        this.virtualPlayerInterface = virtualPlayerInterface;
    }

    private GameState handleCreatingNewGame(String playerName) {
        GameState gameState = this.init(playerName);
        gameState.setJoiningAllowed(true);
        gameRepository.saveAndFlush(gameState);
        boolean isWaitingForPlayers = true;
        while (isWaitingForPlayers) {
            view.showPlayers(gameState.getPlayers());
        if (view.getWaitingForPlayersToJoin()==1) {
            try {
                Thread.sleep(5000);
                gameState = refreshState(gameState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            isWaitingForPlayers = false;
        }
        }
        gameState = refreshState(gameState);
        gameState.setJoiningAllowed(false);
        gameRepository.saveAndFlush(gameState);
        debugger(gameState);
        return gameState;
    }

    private GameState joinExistingGame(GameState gameState, String playerName) {
        Optional<Player> player = replaceVirtualPlayer(gameState, playerName);
        if (player.isEmpty()) {
            LOGGER.error("Failed to replace virtual player");
            view.showFailedToJoinGame();
            return null;
        }
        gameRepository.saveAndFlush(gameState);
        debugger(gameRepository.findById(gameState.getId()).orElse(null));
        return gameState;
    }

    @Override
    public void run() throws InterruptedException {
            Player thisPlayer;
            view.showWelcomeMessage();
            String playerName = view.getPlayerName();

            Integer wantToCreateGame = view.showCreateOrJoinGameMessage();
            GameState gameState = null;
            if (wantToCreateGame == 1) {//handle creating new Game
                gameState = handleCreatingNewGame(playerName);
                thisPlayer = gameState.getPlayers().stream().filter(player -> player.getName().equals(playerName)).findFirst().orElse(null);
            } else if (wantToCreateGame == 2) {//handle joining existing game


                List<GameState> games = gameRepository.findAll().stream().filter(GameState::isJoiningAllowed).collect(Collectors.toList()); //get all join able games
                GameState gameState1 = view.getGame(games);
                gameState = joinExistingGame(gameState1, playerName); //join the selected game and save the player
                thisPlayer = gameState.getPlayers().stream().filter(player -> player.getName().equals(playerName)).findFirst().orElse(null);
                waiterOtherPlayersToPlay(gameState1, thisPlayer); //wait till turn comes
            } else {
                LOGGER.error("Invalid input. please enter 1 or 2");
                return;//todo: check if this is correct
            }
        boolean isRunning = true;
        while (isRunning) {
            LOGGER.debug("------------------------------------- New turn -------------------------------------");
            gameState = refreshState(gameState);
            debugger(gameState);
            List<Player> players = gameService.getSortedPlayersList(gameState);
            Player currentPlayer = players.get(gameState.getCurrentPlayerIndex());
            LOGGER.debug("PlayerNamesList: {}",players.stream().map(Player::getName).reduce((p1, p2) -> p1 + ", " + p2).orElse(""));
            for (Card card : gameState.getDiscardPile()){
                LOGGER.debug("DiscardPile: {}", card);
            }
            LOGGER.debug("Current player: {}", currentPlayer.getName());

            if (currentPlayer.getId().equals(thisPlayer.getId())) {
                handleHumanPlayerTurn(currentPlayer, gameState);
            } else if (currentPlayer.isVirtual()) {
                handleVirtualPlayerTurn(currentPlayer, gameState);
            }else {//wait for other players to play
                try {
                    waiterOtherPlayersToPlay(gameState, thisPlayer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!gameState.isGameRunning()) {
                Player winner = gameService.getWinner(gameState);
                LOGGER.info("Game ended. Winner: {}", winner.getName());
                view.showEndGame(gameState, winner);
                isRunning = false;
                gameRepository.saveAndFlush(gameState);
            }
        }
        LOGGER.info("Game ended");
    }

    private boolean waiterOtherPlayersToPlay(GameState gameState, Player player) throws InterruptedException {
        boolean isWaitingForPlayers = true;
        while (isWaitingForPlayers) {
            gameState = refreshState(gameState);
            if (player.getId().equals(gameState.getPlayers().get(gameState.getCurrentPlayerIndex()).getId())) {
                isWaitingForPlayers = false;
            } else {
                //current player
                view.showCurrentPlayerInfo(gameState.getPlayers().get(gameState.getCurrentPlayerIndex()));
                //top card
                view.showTopCard(gameState.getTopCard());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return isWaitingForPlayers;
    }

    private void showCurrentPlayerWaiting(Player currentPlayer, GameState gameState) {
        LOGGER.debug("Showing waiting message for player: {}", currentPlayer.getName());
        view.showCurrentPlayerInfo(currentPlayer);
        view.showTopCard(gameState.getTopCard());
        view.showWaitingForPlayer(currentPlayer);
        delayPolling();
    }

    private void delayPolling() {
        try {
            Thread.sleep(POLLING_DELAY_MS);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private Optional<Player> replaceVirtualPlayer(GameState gameState, String playerName) {
        LOGGER.debug("Replacing virtual player with name: {}", playerName);
        for (Player p : gameState.getPlayers()) {
            if (p.isVirtual()) {
                p.setName(playerName);
                p.setVirtual(false);
                LOGGER.debug("Virtual player replaced: {}", p);
                return Optional.of(p);
            }
        }
        LOGGER.error("No virtual player found for replacement");
        return Optional.empty();
    }


    private void handleVirtualPlayerTurn(Player currentPlayer, GameState gameState) {
        Card topCard = gameState.getTopCard();
        int accumulatedDrawCount = 0;
        try {
            view.showTopCard(topCard);
            LOGGER.debug("Top card on the discard pile: {}", topCard);
            if(virtualPlayerInterface.shouldSayMau(currentPlayer)){
                currentPlayer.setSaidMau(true);
                view.showMauMessage(currentPlayer);
            }

            accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
            Card playedCard = virtualPlayerInterface.decideCardToPlay(currentPlayer, topCard, ruleService, gameState.getRules());
            if (playedCard != null) {
                playVirtualCard(currentPlayer, gameState, playedCard);
            } else {
                LOGGER.info("Virtual player {} chose to draw cards", currentPlayer.getName());
                this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
            }

            if (accumulatedDrawCount > 0) {
                LOGGER.info("Accumulated draw count: {}", accumulatedDrawCount);
                view.showAccumulatedDrawCount(accumulatedDrawCount);
            }
        } catch (EmptyHandException e) {
            LOGGER.warn("Player has no cards to sort: {}", e.getMessage());
            this.handleEndOfTurnTasks(currentPlayer, gameState);
        } catch (EmptyPileException e) {
            LOGGER.warn("The discard pile is empty: {}", e.getMessage());
            gameService.reshuffleDeck(gameState);
        }
        handleEndOfTurnTasks(currentPlayer, gameState);
    }

    private void playVirtualCard(Player currentPlayer, GameState gameState, Card cardToPlay) {
        LOGGER.debug("Playing virtual card: {}", cardToPlay);
        gameService.playCard(currentPlayer, cardToPlay, gameState);
        ruleService.applySpecialCardsEffect(cardToPlay, gameState.getRules());
        view.showPlayedCard(currentPlayer, cardToPlay);

        if (cardToPlay.getRank().equals(Rank.JACK)) {
            Suit wishedSuit = virtualPlayerInterface.decideSuit(currentPlayer, ruleService);
            ruleService.applyJackSpecialEffect(cardToPlay, wishedSuit, gameState.getRules());
            view.showWishedSuit(currentPlayer, wishedSuit);
            LOGGER.info("Virtual player {} wished suit: {}", currentPlayer.getName(), wishedSuit);
        }
    }

    private void handleHumanPlayerTurn(Player currentPlayer, GameState gameState) {
        Card topCard = gameState.getTopCard();
        int accumulatedDrawCount = 0;
        try {
            playerService.sortPlayersCards(currentPlayer);
            view.showActivePlayersHand(currentPlayer);

            LOGGER.debug("Top card on the discard pile: {}", topCard);
            view.showTopCard(topCard);

            accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
            if (accumulatedDrawCount > 0) {
                LOGGER.info("Accumulated draw count: {}", accumulatedDrawCount);
                view.showAccumulatedDrawCount(accumulatedDrawCount);
            }
        } catch (EmptyHandException e) {
            LOGGER.warn("Player has no cards to sort: {}", e.getMessage());
            this.handleEndOfTurnTasks(currentPlayer, gameState);
        } catch (EmptyPileException e) {
            LOGGER.warn("The discard pile is empty: {}", e.getMessage());
            gameService.reshuffleDeck(gameState);
        }

        String input = this.getPlayerInput(currentPlayer, topCard, gameState);

        if (isNumeric(input)) {
            try {
                int cardIndex = Integer.parseInt(input) - 1;
                if (cardIndex >= 0 && cardIndex < currentPlayer.getHand().size()) {
                    Card playedCard = currentPlayer.getHand().get(cardIndex);
                    LOGGER.debug("Played card: {}", playedCard);
                    playHumanCard(currentPlayer, playedCard, gameState);
                } else {
                    LOGGER.warn("Invalid card index: {}", input);
                    view.showInvalidInputMessage();
                }
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid numeric input: {}", input);
                view.showInvalidInputMessage();
            }
        } else {
            LOGGER.info("Player chose to draw cards");
            this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
        }

        handleEndOfTurnTasks(currentPlayer, gameState);
    }

    private void playHumanCard(Player currentPlayer, Card playedCard, GameState gameState) {
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
            if (currentPlayer.isSaidMau()) {
                currentPlayer.setSaidMau(false);
                LOGGER.debug("Player {} reset 'mau'", currentPlayer.getName());
            }
        }
    }

    private void handleEndOfTurnTasks(Player currentPlayer, GameState gameState) {
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
        gameService.nextPlayer(gameState);
        LOGGER.debug("Saving this game state");
        debugger(gameState);
        gameRepository.saveAndFlush(gameState);
        LOGGER.debug("Data in Database:");
        debugger(gameRepository.findById(gameState.getId()).orElse(null));
        LOGGER.debug("Next player: {}", gameState.getPlayers().get(gameState.getCurrentPlayerIndex()).getName());
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

    public GameState init(String playerName) {
        GameState gameState = null;
        try {
            int numberOfPlayers = view.getNumberOfPlayers();
            LOGGER.info("Initializing game for {} players with first player named {}", numberOfPlayers, playerName);
            gameState = gameService.initializeGame(playerName, numberOfPlayers);
            view.showPlayers(gameState.getPlayers());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred initializing the game: {}", e.getMessage());
            return init(playerName); // Retry initialization
        }
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

    private GameState refreshState(GameState gameState) {
        LOGGER.debug("Refreshing game state");
        GameState refreshedGameState = gameRepository.findById(gameState.getId()).orElse(null);
        return refreshedGameState != null ? refreshedGameState : gameState;
    }

    private void debugger(GameState gameState) {
        LOGGER.debug("GameState: ID={}, \n" +
                        "CurrentPlayerIndex={}, CardsToBeDrawn={}, WishCard={}, \n" +
                        "Players=[{}], TopCard={}, GameRunning={}",
                gameState.getId(),
                gameState.getCurrentPlayerIndex(),
                gameState.getRules().getCardsToBeDrawn(),
                gameState.getRules().getWishCard(),
                gameState.getPlayers().stream()
                        .map(player -> String.format("ID=%d, Name=%s, Hand=%s, SaidMau=%s, IsVirtual=%s",
                                player.getId(),
                                player.getName(),
                                player.getHand(),
                                player.isSaidMau(),
                                player.isVirtual()))
                        .reduce((p1, p2) -> p1 + "; " + p2)
                        .orElse(""),
                gameState.getTopCard(),
                gameState.isGameRunning()
        );
    }
}
