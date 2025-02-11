﻿# Mau-Mau Game

## Description
Mau-Mau is a traditional German card game similar to Uno. The goal is to be the first player to discard all your cards and win the game. Known for its simple and easy-to-learn rules, Mau-Mau is a favorite among players of all ages.

In this project, we have developed a digital version of the Mau-Mau game, allowing ***up to four players*** to compete in a ***multiplayer*** setting, either against each other or virtual opponents, using a ***command-line interface***. The project employs ***component-based development*** to ensure that each module functions independently and can be easily maintained or replaced. 

## Components
### Player Management
- Functionality: Manages player data and actions. Allows for adding new players, retrieving player information, and sorting cards in players hands.
- Role: Provides an interface for player interaction and coordinates player data within the game.
### Cards Management
- Functionality: Manages card data and actions such as shuffling the deck and drawing cards.
- Role: Facilitates interaction with cards within the game and ensures consistent management of card data.
### Game Engine
- Functionality: Coordinates game logic and actions, such as starting new games, executing moves, and calculating scores.
- Role: Orchestrates interactions between players and cards, maintaining the game state and history.
### Rules Management
- Functionality: Manages game rules and actions like validating moves and applying special card effects.
 -Role: Ensures rules are consistently applied and provides an interface for game rule management.
### Persistence
- Functionality: Responsible for data persistence and management, storing game states, player data, and card data in the database.
- Role: Provides database management interfaces, ensuring consistent data storage and retrieval.
### Virtual Player
- Functionality: Controls the logic and behavior of virtual players in the game.
- Role: Simulates computer-controlled opponents that make strategic decisions.
### Technical Components
- Spring Framework: Supports configuration and management of application components using Dependency Injection.
- Spring Data JPA: Simplifies database access with an abstraction layer over JPA.
- Hibernate: An ORM framework for mapping Java classes to database tables.
- JUnit & Mockito: Used for unit testing to validate classes and methods.
- Log4j: Provides logging and monitoring of the application.
