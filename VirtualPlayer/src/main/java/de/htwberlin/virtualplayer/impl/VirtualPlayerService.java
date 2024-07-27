package de.htwberlin.virtualplayer.impl;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.impl.CardComparator;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VirtualPlayerService implements VirtualPlayerInterface {

    private static final Logger LOGGER = LogManager.getLogger(VirtualPlayerService.class);


    @Override
    public void makeMove() {
        LOGGER.info("Virtual player makes a move");
    }
}

