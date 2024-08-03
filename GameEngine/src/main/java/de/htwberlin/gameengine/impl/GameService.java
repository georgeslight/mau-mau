package de.htwberlin.gameengine.impl;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.api.service.CardManagerInterface;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.gameengine.exception.EmptyPileException;
import de.htwberlin.playermanagement.api.exception.EmptyHandException;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GameService implements GameManagerInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameService.class);

    private PlayerManagerInterface playerManagerInterface;
    private CardManagerInterface cardManagerInterface;
    private RuleEngineInterface ruleEngineInterface;
    private VirtualPlayerInterface virtualPlayerInterface;

    @Autowired
    public GameService(PlayerManagerInterface playerManagerInterface, CardManagerInterface cardManagerInterface, RuleEngineInterface ruleEngineInterface, VirtualPlayerInterface virtualPlayerInterface) {
        this.playerManagerInterface = playerManagerInterface;
        this.cardManagerInterface = cardManagerInterface;
        this.ruleEngineInterface = ruleEngineInterface;
        this.virtualPlayerInterface = virtualPlayerInterface;
    }

    public GameService() {
    }

    @Override
    @Transactional
    public GameState initializeGame(String playerName, int numberOfPlayers) {
        GameState game = new GameState();
        List<Card> deck = cardManagerInterface.shuffle(cardManagerInterface.createDeck());

        // Create main player
        List<Card> firstPlayerHand = IntStream.range(0, 5)
                .mapToObj(i -> deck.remove(deck.size() - 1))
                .collect(Collectors.toList());
        Player firstPlayer = playerManagerInterface.createPlayer(playerName, firstPlayerHand, false);

        // Create remaining players with default names, including virtual players
        List<Player> players = IntStream.range(1, numberOfPlayers)
                .mapToObj(i -> {
                    List<Card> hand = IntStream.range(0, 5)
                            .mapToObj(j -> deck.remove(deck.size() - 1))
                            .collect(Collectors.toList());
                    return playerManagerInterface.createPlayer("Player " + (i + 1), hand, true);
                })
                .collect(Collectors.toList());

        players.add(0, firstPlayer);
        game.setPlayers(players);
        List<Card> discardPile = new ArrayList<>();
        discardPile.add(deck.remove(deck.size() - 1));
        game.setDiscardPile(discardPile);
        game.setRules(new Rules());
        game.setGameRunning(true);
        game.setDeck(deck);
        game.setCurrentPlayerIndex(0);
        game.setTopCard(discardPile.get(0));

        return game;
    }

    @Override
    @Transactional
    public Player nextPlayer(GameState gameState) {
        int nextPlayerIndex = ruleEngineInterface.calculateNextPlayerIndex(gameState.getCurrentPlayerIndex(), gameState.getPlayers().size(), gameState.getRules());
        gameState.setCurrentPlayerIndex(nextPlayerIndex);
        return gameState.getPlayers().get(nextPlayerIndex);
    }

    @Override
    @Transactional
    public void calcRankingPoints(GameState game) {
        game.getPlayers().forEach(player -> {
            // Updated rankingPoints with the sum of all scores
            player.setRankingPoints(player.getScore().stream().reduce(0, Integer::sum));
        });
    }

    @Override
    @Transactional
    public Player getWinner(GameState game) {
        return game.getPlayers().stream().max(Comparator.comparingInt(Player::getRankingPoints))
                .orElse(null);
    }

    @Override
    @Transactional
    public void endRound(GameState game) {
        game.getPlayers().forEach(player -> {
            // Updated score
            player.getScore().add(ruleEngineInterface.calculateScore(player.getHand()));
            // Updated rankingPoints with the sum of all scores
            player.setRankingPoints(player.getScore().stream().reduce(0, Integer::sum));
        });
        // Clear the discard pile
        game.getDiscardPile().clear();
        // Shuffle the deck
        List<Card> deck = cardManagerInterface.shuffle(cardManagerInterface.createDeck());

        // Distribute new cards to the players
        game.setDeck(deck);
        game.getPlayers().forEach(player -> {
            List<Card> hand = IntStream.range(0, 5)
                    .mapToObj(j -> game.getDeck().remove(game.getDeck().size() - 1))
                    .collect(Collectors.toList());
            player.setHand(hand);
        });

        List<Card> discardPile = new ArrayList<>();
        discardPile.add(deck.remove(deck.size() - 1));
        game.setDiscardPile(discardPile);

        // Set the current player index to 0
        game.setCurrentPlayerIndex(0);
    }

    @Override
    @Transactional
    public Card drawCard(GameState game, Player player) {
        // Check if deck is empty
        if (game.getDeck().isEmpty()) {
            this.reshuffleDeck(game);
        }

        Card drawCard = game.getDeck().remove(game.getDeck().size() - 1);
        player.getHand().add(drawCard);
        return drawCard;
    }

    @Override
    @Transactional
    public void reshuffleDeck(GameState game) {
        LOGGER.info("Deck is empty, reshuffling Deck");

        // Save the last card from the discard pile
        Card lastCard = game.getDiscardPile().remove(game.getDiscardPile().size() - 1);

        // Create new discard pile with only last card
        List<Card> newDiscard = new ArrayList<>();
        newDiscard.add(lastCard);

        // Move all cards from the discard pile to deck
        List<Card> newDeck = game.getDiscardPile();

        // Set and shuffle the new deck
        game.setDeck(cardManagerInterface.shuffle(newDeck));

        // Set the new discard pile
        game.setDiscardPile(newDiscard);
    }

    @Override
    @Transactional
    public void playCard(Player player, Card card, GameState gameState) {
        if (player.getHand().remove(card)) {
            gameState.getDiscardPile().add(card);
            gameState.getRules().setWishCard(null);
            gameState.setTopCard(card);
            if (player.isSaidMau()&&player.getHand().size()>1) {
                player.setSaidMau(false);
            }
        } else {
            throw new IllegalArgumentException("The player does not have the specified card.");
        }
    }

    @Override
    public boolean checkEmptyHand(Player player) {
        return player.getHand().isEmpty();
    }


    // New methods to handle player turns and other game logic
    @Override
    @Transactional
    public void handleVirtualPlayerTurn(Player currentPlayer, GameState gameState) {
        Card topCard = gameState.getTopCard();
        int accumulatedDrawCount = 0;
        try {
            if(virtualPlayerInterface.shouldSayMau(currentPlayer)){
                currentPlayer.setSaidMau(true);
            }
            LOGGER.debug("Top card on the discard pile: {}", topCard);

            accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
            Card playedCard = virtualPlayerInterface.decideCardToPlay(currentPlayer, topCard, ruleEngineInterface, gameState.getRules());
            if (playedCard != null) {
                playVirtualCard(currentPlayer, gameState, playedCard);
            } else {
                LOGGER.info("Virtual player {} chose to draw cards", currentPlayer.getName());
                this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
            }
        } catch (EmptyHandException e) {
            LOGGER.warn("Player has no cards to sort: {}", e.getMessage());
            this.handleEndOfTurnTasks(currentPlayer, gameState);
        } catch (EmptyPileException e) {
            LOGGER.warn("The discard pile is empty: {}", e.getMessage());
            reshuffleDeck(gameState);
        }
        handleEndOfTurnTasks(currentPlayer, gameState);
    }
    @Override
    @Transactional
    public void handleHumanPlayerTurn(Player currentPlayer, GameState gameState, String input) {
        Card topCard = gameState.getTopCard();
        int accumulatedDrawCount = 0;
        try {
            playerManagerInterface.sortPlayersCards(currentPlayer);

            LOGGER.debug("Top card on the discard pile: {}", topCard);

            accumulatedDrawCount = gameState.getRules().getCardsToBeDrawn();
        } catch (EmptyHandException e) {
            LOGGER.warn("Player has no cards to sort: {}", e.getMessage());
            this.handleEndOfTurnTasks(currentPlayer, gameState);
        } catch (EmptyPileException e) {
            LOGGER.warn("The discard pile is empty: {}", e.getMessage());
            reshuffleDeck(gameState);
        }

        if (isNumeric(input)) {
            Card playedCard = currentPlayer.getHand().get(Integer.parseInt(input) - 1);
            LOGGER.debug("Played card: {}", playedCard);
            playHumanCard(currentPlayer, playedCard, gameState);
        } else {
            LOGGER.info("Player chose to draw cards");
            this.drawCards(accumulatedDrawCount, gameState, currentPlayer);
        }

        handleEndOfTurnTasks(currentPlayer, gameState);
    }

    private void playVirtualCard(Player currentPlayer, GameState gameState, Card cardToPlay) {
        LOGGER.debug("Playing virtual card: {}", cardToPlay);
        this.playCard(currentPlayer, cardToPlay, gameState);

        if (cardToPlay.getRank().equals(Rank.JACK)) {
            Suit wishedSuit = virtualPlayerInterface.decideSuit(currentPlayer, ruleEngineInterface);
            ruleEngineInterface.applyJackSpecialEffect(cardToPlay, wishedSuit, gameState.getRules());
            LOGGER.info("Virtual player {} wished suit: {}", currentPlayer.getName(), wishedSuit);
        }
    }

    private void playHumanCard(Player currentPlayer, Card playedCard, GameState gameState) {
        if (playedCard != null) {
            this.playCard(currentPlayer, playedCard, gameState);
            ruleEngineInterface.applySpecialCardsEffect(playedCard, gameState.getRules());
        }
    }

    private void handleEndOfTurnTasks(Player currentPlayer, GameState gameState) {
        if (this.checkEmptyHand(currentPlayer)) {
            if (currentPlayer.isSaidMau()) {
                LOGGER.info("Player {} has won the round", currentPlayer.getName());
                this.endRound(gameState);
            } else {
                LOGGER.warn("Player {} failed to say 'mau'", currentPlayer.getName());
                this.drawCards(2, gameState, currentPlayer);
            }
        }
        nextPlayer(gameState);
    }
    @Transactional
    public void drawCards(int accumulatedDrawCount, GameState gameState, Player currentPlayer) {
        IntStream.range(0, Math.max(accumulatedDrawCount, 1))
                .forEach(i -> {
                    Card drawnCard = this.drawCard(gameState, currentPlayer);
                    LOGGER.debug("Player {} drew card {}", currentPlayer.getName(), drawnCard);
                });
        gameState.getRules().setCardsToBeDrawn(0);
        LOGGER.info("Reset accumulated draw count to 0");
    }
    @Transactional
    public boolean isNumeric(String str) {
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
    @Override
    @Transactional
    public List<Player> getSortedPlayersList(GameState gameState) {
        return gameState.getPlayers().stream()
                .sorted(Comparator.comparingLong(Player::getId))
                .collect(Collectors.toList());
    }
}