package de.htwberlin.persistence.repo;

import de.htwberlin.playermanagement.api.model.Player;

public interface PlayerRepositoryCustom {
    Player customFindMethod(Long id);
}
