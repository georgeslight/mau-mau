package htw.berlin.de.service;


import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.PlayerManagerInterface;

@SpringBootTest
public class PlayerUIControllerTest {

    private PlayerManagerInterface playerManager;
    private PlayerUIController playerUIController;

    @BeforeEach
    public void setUp() {
        playerManager = mock(PlayerManagerInterface.class);
        playerUIController = new PlayerUIController(playerManager);
    }

    @Test
    public void testRun() {
        Player player = new Player("John Doe", null);
        when(playerManager.createPlayer(Mockito.anyString(), Mockito.anyList())).thenReturn(player);

        playerUIController.run();

    }
}
