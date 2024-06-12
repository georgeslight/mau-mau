package de.htwberlin.impl.service;

import de.htwberlin.impl.service.GameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GameUIController {

    private static final Logger LOGGER = LogManager.getLogger(GameUIController.class);

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
