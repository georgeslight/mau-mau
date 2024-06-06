package htw.berlin.de.service;

import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.PlayerManagerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerUIController {

    private final PlayerManagerInterface playerManager;

    @Autowired
    public PlayerUIController(PlayerManagerInterface playerManager) {
        super();
        this.playerManager = playerManager;
    }

    public void run() {
        System.out.println("PlayerUI is running");
        Player player = playerManager.createPlayer("John Doe", null);
        System.out.println("Created player: " + player.getName());
    }
}
