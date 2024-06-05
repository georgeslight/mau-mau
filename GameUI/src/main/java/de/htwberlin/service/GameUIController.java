package de.htwberlin.service;

import de.htwberlin.impl.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GameUIController {

    private GameService gameService;

    @Autowired
    public GameUIController(GameService gameService) {
        super();
        this.gameService = gameService;
    }

    public GameUIController() {
        super();
    }

    public GameService getGameService() {
        return gameService;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public void run() {
        System.out.println("GameUI is running");
    }
}
