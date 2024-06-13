package de.htwberlin.impl.service;

import de.htwberlin.api.GameUIInterface;
import de.htwberlin.api.service.GameManagerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GameUIController implements GameUIInterface {

    private static final Logger LOGGER = LogManager.getLogger(GameUIController.class);

    private GameManagerInterface gameManagerInterface;
    private GameUIView view;

    @Autowired
    public GameUIController(GameManagerInterface gameManagerInterface, GameUIView view) {
        super();
        this.view = view;
        this.gameManagerInterface = gameManagerInterface;
    }

    public GameUIController() {
        super();
    }

    public GameManagerInterface getGameService() {
        return gameManagerInterface;
    }

    public void setGameService(GameManagerInterface gameManagerInterface) {
        this.gameManagerInterface = gameManagerInterface;
    }

    /**
     * here view and service
     */
    @Override
    public void run() {
        view.print();

    }
}
