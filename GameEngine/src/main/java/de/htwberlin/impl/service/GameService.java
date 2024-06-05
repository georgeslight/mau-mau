package de.htwberlin.impl.service;

import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GameService implements GameManagerInterface {

    private PlayerService playerService;
    private CardService cardService;
    private RuleService ruleService;

    public GameService(PlayerService playerService, CardService cardService, RuleService ruleService) {
        this.playerService = playerService;
        this.cardService = cardService;
        this.ruleService = ruleService;
    }

    @Override
    public GameState initializeGame(int numberOfPlayers) {
        GameState game = new GameState();
        Stack<Card> deck = cardService.shuffle(cardService.createDeck());
        List<Player> players = IntStream.range(0, numberOfPlayers)
                .mapToObj(i -> {
                    List<Card> hand = IntStream.range(0, 5)
                            .mapToObj(j -> deck.pop())
                            .collect(Collectors.toList());
                    return playerService.createPlayer("Player " + i, hand);
                })
                .collect(Collectors.toList());

        game.setPlayers(players);
        Stack<Card> discardPile = new Stack<>();
        discardPile.push(deck.pop());
        game.setDiscardPile(discardPile);

        game.setDeck(deck);

        game.setCurrentPlayerIndex(0);
        game.setNextPlayerIndex(1);

        return game;
    }

    @Override
    public Player nextPlayer(GameState gameState) {
        int nextPlayerIndex = ruleService.calculateNextPlayerIndex(gameState.getCurrentPlayerIndex(), gameState.getPlayers().size());
        gameState.setCurrentPlayerIndex(nextPlayerIndex);
        return gameState.getPlayers().get(nextPlayerIndex);
    }

    @Override
    public Player endGame(GameState game) {
        game.getPlayers().forEach( player -> {
            player.getScore(). ruleService.calculateScore(player.getHand());
        });
        return null;
    }

    @Override
    public Card drawCard(GameState game, Player player) {
        if (game.getDeck().empty()) throw new IllegalStateException("Cannot draw from an empty deck");

        Card drawCard = game.getDeck().pop();
        player.getHand().add(drawCard);
        return drawCard;
    }

    @Override
    public void playCard(Player player, Card card, GameState gameState) {
        if (player.getHand().remove(card)) {
            gameState.getDiscardPile().push(card);
        } else {
            throw new IllegalArgumentException("The player does not have the specified card.");
        }
    }

    @Override
    public boolean checkWinner(Player player) {
        return player.getHand().isEmpty();
    }
}
