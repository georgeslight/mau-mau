package de.htwberlin.impl.service;

import de.htwberlin.api.GameUIInterface;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.GameManagerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GameUIController implements GameUIInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameUIController.class);

    private GameManagerInterface gameService;
    private GameUIView view;

    @Autowired
    public GameUIController(GameManagerInterface gameManagerInterface, GameUIView view) {
        this.view = view;
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

            Card playedCard = view.getPlayerCardChoice(currentPlayer, topCard);
            if (playedCard != null) {
                gameService.playCard(currentPlayer, playedCard, gameState);
                System.out.println(currentPlayer.getName() + " played: " + playedCard);
            } else {
                Card drawnCard = gameService.drawCard(gameState, currentPlayer);
                System.out.println(currentPlayer.getName() + " drew a card: " + drawnCard);
            }

            if (gameService.checkWinner(currentPlayer)) {
                System.out.println(currentPlayer.getName() + " wins the game!");
                break;
            }
            gameService.nextPlayer(gameState);
        }
    }
}
