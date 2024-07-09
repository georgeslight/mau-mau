package de.htwberlin.cardsmanagement.repo;

import de.htwberlin.cardsmanagement.api.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
