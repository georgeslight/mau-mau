# Mau-Mau-Spiel

## Teammitglieder

- Debora Mincheva - 586987,
- George Slight -580245,
- Ghazi Nakkash - 587550

## Beschreibung

Das Mau-Mau-Spiel ist ein Kartenspiel, das mit einem speziellen Kartendeck gespielt wird. Ziel des Spiels ist es, als erster Spieler alle Karten in der Hand abzulegen und somit das Spiel zu gewinnen. Die Regeln des Mau-Mau-Spiels sind einfach und leicht zu erlernen, was es zu einem beliebten Kartenspiel für Jung und Alt macht. In unserem Projekt haben wir eine Online-Version des Mau-Mau-Spiels entwickelt, die es den Benutzern ermöglicht, das Spiel zu spielen und gegen andere Spieler anzutreten. Die Anwendung umfasst die folgenden Funktionalitäten:
### 1. Komponentenschnitt


Unser Informationssystem besteht aus mehreren fachlichen und technischen Komponenten, die die Funktionalitäten des Mau-Mau-Spiels implementieren und bereitstellen. Die Komponenten sind in einem Komponentendiagramm dargestellt, das die Beziehungen und Abhängigkeiten zwischen den einzelnen Komponenten veranschaulicht. 

Im Folgenden werden die wichtigsten fachlichen Komponenten und ihre Funktionalitäten beschrieben:
- **PlayerManagement**: Diese Komponente ist für die Verwaltung der Spielerdaten und -aktionen zuständig. Sie ermöglicht das Hinzufügen neuer Spieler, das Abrufen von Spielerinformationen und das Sortieren der Karten in den Händen der Spieler. Die Komponente PlayerManagement stellt die Schnittstelle zur Spielerinteraktion bereit und koordiniert die Spielerdaten innerhalb des Spiels.
- **CardsManagement**: Diese Komponente verwaltet die Kartendaten und -aktionen, wie das Mischen des Kartendecks oder das Ziehen von Karten. Sie stellt die Schnittstelle zur Kartendatenverwaltung bereit und ermöglicht die Interaktion mit den Karten innerhalb des Spiels.
- **GameEngine**: Diese Komponente koordiniert die Spiellogik und -aktionen, wie das Starten neuer Spiele, die Durchführung von Spielzügen und die Berechnung von Punktzahlen. Sie stellt die Schnittstelle zur Spiellogik bereit und orchestriert die Interaktionen zwischen den Spielern und Karten.
- **RulesManagement**: Diese Komponente verwaltet die Spielregeln und -aktionen, wie das Überprüfen der Gültigkeit von Spielzügen oder das Anwenden spezieller Karteneffekte. Sie stellt die Schnittstelle zur Spielregelverwaltung bereit und sorgt dafür, dass die Regeln korrekt angewendet werden.
- **Persistence**: Diese Komponente ist für die Datenpersistenz und -verwaltung zuständig. Sie speichert die Spielstände, Spielerdaten und Kartendaten in der Datenbank und stellt die Schnittstelle zur Datenbankverwaltung bereit.



![Fachliche Komponente](Fachliche Komponente.png)

Die Komponenten sind miteinander verbunden und kommunizieren über definierte Schnittstellen, um die Funktionalitäten des Mau-Mau-Spiels zu implementieren. Jede Komponente hat eine spezifische Aufgabe und Verantwortungsbereich innerhalb des Informationssystems und trägt zur Gesamtfunktionalität des Spiels bei. Das Komponentendiagramm veranschaulicht die Struktur und Architektur des Informationssystems und zeigt die Abhängigkeiten und Beziehungen zwischen den einzelnen Komponenten auf.



Technische Komponenten:
- **Spring Boot**: Verwendet für die Erstellung von RESTful Web Services und die Konfiguration der Anwendung.
- **Spring Data JPA**: Verwandet für die Datenpersistenz und -verwaltung.
- **Spring MVC**: Implementierung des MVC-Musters und Erstellung der Präsentationsschicht.
- **Hibernate**: Verwendet für die Objekt-Relationen-Mapping (ORM) und die Datenbankinteraktion.
- **PostgreSQL**: Relationale Datenbank für die Speicherung der Anwendungsdaten und -informationen.
- **JUnit**: Verwendet für das Testen der Anwendungskomponenten.
- **Swagger**: Dokumentation der RESTful Web Services für die Anwendung.
- **Maven**: Verwendet für das Build-Management und die Abhängigkeitsverwaltung für das Projekt.
- **Lombok**: Vereinfacht der Java-Entwicklung durch Annotationen für Getter, Setter, Konstruktoren und andere Methoden.
- **Docker**: Containerisierung der Anwendung für die Bereitstellung in einer Cloud-Umgebung.

![Technische Komponente](Technische Komponente.png)



### 2. Schnittstellenbeschreibung


In unserem Mau-Mau-Spielprojekt bestehen mehrere Schnittstellen zwischen den verschiedenen Komponenten, die es ermöglichen, Daten und Funktionalitäten zwischen den Modulen auszutauschen. Die Schnittstellen sind als JavaDoc-Kommentare in den entsprechenden Klassen und Methoden dokumentiert und beschrieben. Im Folgenden werden die wichtigsten Schnittstellen und ihre Funktionalitäten erläutert:

- [PlayerService](PlayerManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fplayermanagement%2Fimpl%2FPlayerService.java): Diese Schnittstelle verwaltet alle spielerbezogenen Daten und Aktionen. Dazu gehören das Hinzufügen neuer Spieler, das Abrufen von Spielerinformationen, das Sortieren der Karten in den Händen der Spieler und die Handhabung der "Mau"-Aktion, die auftritt, wenn ein Spieler nur noch eine Karte hat. Sie wird von der Schnittstelle GameManagerInterface verwendet, um Spieler zu verwalten und deren Interaktionen innerhalb des Spiels zu koordinieren. Die Methoden dieser Schnittstelle sind in der Klasse [PlayerService](PlayerManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fplayermanagement%2Fimpl%2FPlayerService.java) beschrieben.
- [CardService](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fimpl%2FCardService.java): Diese Schnittstelle ist für die Verwaltung von Kartendaten und -operationen zuständig, wie etwa das Mischen des Kartendecks oder das Ziehen von Karten. Sie wird von verschiedenen Komponenten verwendet, um auf die Kartendaten zuzugreifen und diese zu manipulieren. Details der Methoden finden Sie in der Klasse [CardService](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fimpl%2FCardService.java). 
- [GameService](GameEngine%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fgameengine%2Fimpl%2FGameService.java): Diese Schnittstelle koordiniert die Spiellogik und -aktionen, einschließlich des Startens neuer Spiele, der Durchführung von Spielzügen, der Berechnung von Punktzahlen und der Bestimmung des Spielgewinners. Die Methodendefinitionen sind in der Klasse [GameService](GameEngine%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fgameengine%2Fimpl%2FGameService.java) dokumentiert.
- [RuleService](RulesManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Frulesmanagement%2Fimpl%2FRuleService.java): Diese Schnittstelle verwaltet die Spielregeln und -aktionen. Sie überprüft die Gültigkeit von Spielzügen, berechnet den nächsten Spieler, wendet spezielle Karteneffekte an und berechnet die Punktzahlen. Die Methoden dieser Schnittstelle sind in der Klasse [RuleService](RulesManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Frulesmanagement%2Fimpl%2FRuleService.java) beschrieben.

Diese Schnittstellen ermöglichen den Datenaustausch und die Interaktion zwischen den verschiedenen Komponenten des Mau-Mau-Spiels und stellen sicher, dass die Spiellogik und -regeln korrekt angewendet werden. Die JavaDoc-Kommentare in den Klassen und Methoden dokumentieren die Funktionalitäten und Methodenparameter der Schnittstellen und erleichtern die Entwicklung und Wartung der Anwendung.


### 3. Konzeptionelles Datenmodell.


Wir haben uns für ein relationales Datenmodell entschieden, das auf PostgreSQL basiert. Ein relationales Datenmodell bietet die geeignete Struktur zur Darstellung der verschiedenen Entitäten und ihrer Beziehungen innerhalb unseres Mau-Mau-Spiels. Diese Struktur ermöglicht uns die notwendige Flexibilität und Performance, um die Anforderungen des Spiels zu erfüllen.

In unserem Modell verwenden wir Tabellen, um die wichtigsten Entitäten wie Karten, Spieler, Spielzustände und Regeln darzustellen. Beziehungen zwischen diesen Entitäten werden durch Fremdschlüssel abgebildet, um eine referenzielle Integrität sicherzustellen.

---
### JPA- und Hibernate-Abbildung

Das konzeptionelle Modell wird mithilfe von JPA (Java Persistence API) und Hibernate ein ORM-Framework) auf das physische Datenmodell abgebildet. JPA erlaubt es uns, die Entitäten in unserem Projekt als Java-Klassen zu modellieren und diese durch JPA-Annotations mit den entsprechenden Tabellen in der Datenbank zu verknüpfen. Hibernate übernimmt die Aufgabe, die Entitäten in der Datenbank zu speichern und die Datenzugriffslogik zu implementieren.

Jede Entität im Modell wird durch eine Java-Klasse repräsentiert, die mit den entsprechenden JPA-Annotations versehen ist, um die Zuordnung zu den Tabellen und Spalten in der Datenbank zu definieren. 

- **Beispiel einer Entität**: [Card](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fapi%2Fmodel%2FCard.java)- Klasse, die die Karten im Spiel repräsentiert. Die Klasse enthält die Attribute `id`, `suit` und `rank`, die durch JPA-Annotations wie `@Entity`, `@Table`, `@Id`, `@Column` und `@Enumerated` mit den entsprechenden Tabellen und Spalten in der Datenbank verknüpft sind.


Die [persistence.properties](Persistence%2Fsrc%2Fmain%2Fresources%2Fpersistence.properties) -Datei oder entsprechende Konfigurationsklassen definieren die Verbindungsdetails zur Datenbank:
```
# DataSource
dataSource.setDriverClassName=org.postgresql.Driver
dataSource.setUrl=jdbc:postgresql://cav8p52l9arddb.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/dfhdlnab3vno9q
dataSource.setUsername=uabs3qn5a7d925
dataSource.setPassword=p396b51e151b4debfa547386ce7ec4d0b8cc7fa08241a2b79fe1d818c9ec1852d

```

---

### Denormalisierung

Nein, unser Datenmodell ist nicht denormalisiert. Wir haben uns bewusst für eine normalisierte Datenbankstruktur entschieden, um Datenkonsistenz und -integrität zu gewährleisten. Eine normalisierte Struktur vermeidet Datenredundanzen und erleichtert die Wartung und Erweiterung des Modells.

Durch die Verwendung von JPA und relationalen Datenbanken können wir sicherstellen, dass unsere Daten konsistent und effizient gespeichert werden. Beispielsweise gibt es separate Tabellen für Spieler, Karten, Spielzustände und Regeln, und die Beziehungen zwischen diesen Tabellen werden durch Fremdschlüssel definiert.

---

### Zustandigkeit und Datenhoheit der Komponenten

- **CardsManagement**:
    - **Zuständigkeit**: Verwaltung der Kartendaten, insbesondere der Attribute  `Suit`  und  `Rank`  der Karten.
    - **Datenhoheit**: Tabelle [Card](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fapi%2Fmodel%2FCard.java). 
        - Das  `CardManagement`  hat die Datenhoheit über die Kartendaten, inklusive Erstellung, Modifikation und Speicherung von Karten. 
    - **Beschreibung**: Das  `CardManagement`  ist verantwortlich für das Erstellen, Lesen, Aktualisieren und Löschen von Kartendaten. Sie stellt sicher, dass die Daten der Karten konsistent und korrekt verwaltet werden. Die Karten werden in der Datenbank gespeichert und können von anderen Komponenten abgerufen und verwendet werden.
    - Abbildungen der Karten auf die Datenbank erfolgen durch entsprechende JPA-Repositorys - [CardRepository.java](Persistence%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fpersistence%2Frepo%2FCardRepository.java)..
  


- **PlayerManagement**:
    - **Zuständigkeit**: Verantwortlich für die Verwaltung der Spielerdaten. 
    - **Datenhoheit**: [Player](PlayerManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fplayermanagement%2Fapi%2Fmodel%2FPlayer.java) -Entität enthält die Spielerinformationen und Statistiken. 
        - Das `PlayerManagement` ist verantwortlich für die Verwaltung und Speicherung von Spielerdaten, wie z.B. Namen und Handkarten der Spieler. 
    - **Beschreibung**: Diese Komponente ist verantwortlich für das Erstellen, Lesen, Aktualisieren und Löschen von Spielerdaten. Sie sorgt dafür, dass die Informationen der Spieler wie Name und Ranking-Punkte konsistent und korrekt gespeichert werden. 



- **GameEngine**:
    - **Zuständigkeit**: Orchestriert die Geschäftslogik des Spiels und verwaltet die laufenden Spiele.
    - Verantwortlich für die Speicherung und Verwaltung der Spielzustände und Spielhistorie. 
    - **Datenhoheit**: [GameState](GameEngine%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fgameengine%2Fapi%2Fmodel%2FGameState.java) -Entität.
        - Die `GameEngine` hat die Datenhoheit über den Spielzustand und verwaltet die aktuelle Situation des Spiels, inklusive der Reihenfolge der Spieler und des Ablagestapels. 
    - **Beschreibung**: Diese Komponente kümmert sich um den aktuellen Zustand des Spiels und die Verwaltung der laufenden Spielzüge. Sie stellt sicher, dass der Status des Spiels und die Beziehungen zu Spielern und Karten richtig gepflegt werden. 


- **RulesManagement**:
    - **Zuständigkeit**: Verwaltet die Regeln des Spiels und deren Implementierung. 
    - **Datenhoheit**: Enthält Entitäten wie [Rules](RulesManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Frulesmanagement%2Fapi%2Fmodel%2FRules.java), die die verschiedenen Spielregeln repräsentieren.
    - **Beschreibung**: Das `RulesManagement` ist zuständig für die Definition und Anwendung der Spielregeln und verwaltet die entsprechenden Regelwerke. Sie speichert Regeln wie die Anzahl der zu ziehenden Karten und andere spielrelevante Vorgaben. Das Modul stellt sicher, dass die Regeln konsistent und korrekt angewendet werden.


- **Persistence**:
    - **Zuständigkeit**: Allgemeine Datenpersistenzschicht, die sich um die Konfiguration der Datenbankverbindungen und allgemeine Persistenzstrategien kümmert.
    - **Datenhoheit**: Verantwortlich für die Datenpersistenz und -verwaltung der gesamten Anwendung.
    - **Beschreibung**: Das `Persistence` Modul ist für die Konfiguration der Datenbankverbindungen und die allgemeine Persistenzstrategie der Anwendung zuständig. Es stellt sicher, dass die Daten konsistent und korrekt gespeichert werden und bietet die notwendigen Methoden für den Datenzugriff.


Jede dieser Komponenten hat die Datenhoheit über ihre jeweiligen Entitäten und stellt sicher, dass die Daten konsistent und korrekt verwaltet werden. Die JPA-Repositories bieten die notwendigen Methoden, um CRUD-Operationen (Create, Read, Update, Delete) auf den jeweiligen Entitäten durchzuführen und sorgen für eine zentrale Verwaltung der Datenzugriffslogik.


### 4. Präsentationsschicht.

Unsere GUI-Architektur basiert auf einem modularen Ansatz, der die Trennung von Präsentation, Geschäftslogik und Datenzugriff ermöglicht. Wir verwenden das MVC (Model-View-Controller)-Muster, um die GUI in drei Hauptkomponenten zu unterteilen:

- **Model**: Enthält die Daten und Geschäftslogik der Anwendung. Im Kontext unserer Anwendung umfasst das Model die Datenstrukturen für Spieler ([Player](PlayerManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fplayermanagement%2Fapi%2Fmodel%2FPlayer.java)), Karten ([Card](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fapi%2Fmodel%2FCard.java)), Spielregeln ([Rules](RulesManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Frulesmanagement%2Fapi%2Fmodel%2FRules.java)) und Spielstände ([GameState](GameEngine%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fgameengine%2Fapi%2Fmodel%2FGameState.java)). 
- **View**: Präsentiert die Daten und ermöglicht die Interaktion mit dem Benutzer. In unserem Fall besteht die View aus einer Benutzeroberfläche, die die Spielerinformationen, Kartenverwaltung und Spielhistorie anzeigt.
- **Controller**: Koordiniert die Interaktion zwischen Model und View. In unserer Anwendung steuern die Controller die Benutzerinteraktionen und rufen die entsprechenden Services auf, um die Daten zu verarbeiten und anzuzeigen.


- **GUI**: Präsentiert die Daten und ermöglicht die Interaktion mit dem Benutzer.


Die GUI besteht aus mehreren Ansichten, die jeweils für einen bestimmten Teil der Anwendung zuständig sind. Beispielsweise gibt es Ansichten für die Spielerinformationen, die Kartenverwaltung und die Spielhistorie. Jede Ansicht wird durch einen entsprechenden Controller gesteuert, der die Interaktion mit dem Benutzer koordiniert. Die Ansichten sind so gestaltet, dass sie die Daten aus dem Model anzeigen und dem Benutzer die Möglichkeit geben, mit der Anwendung zu interagieren. Die GUI-Architektur ermöglicht eine klare Trennung der verschiedenen Komponenten und erleichtert die Wartung und Erweiterung der Anwendung. Durch die Verwendung des MVC-Musters können Änderungen an der Benutzeroberfläche unabhängig von der Geschäftslogik vorgenommen werden, was die Flexibilität und Skalierbarkeit der Anwendung erhöht. Die GUI-Architektur ist so konzipiert, dass sie eine benutzerfreundliche und intuitive Benutzeroberfläche bietet, die es dem Benutzer ermöglicht, die Anwendung effizient zu bedienen und die gewünschten Informationen schnell zu finden.

### Beschreiben Sie Ihre Dialoge! (Skizzen, Screenshots)?

Unsere GUI besteht aus mehreren Dialogen, die jeweils für einen bestimmten Teil der Anwendung zuständig sind. Jeder Dialog enthält die relevanten Informationen und Steuerelemente, die es dem Benutzer ermöglichen, mit der Anwendung zu interagieren. Hier sind einige Beispiele für Dialoge in unserer Anwendung:

![img.png](img.png)




### Services und ihre Rollen
 
- [PlayerService](PlayerManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fplayermanagement%2Fimpl%2FPlayerService.java): Verwaltet die Spielerdaten und -aktionen, wie z.B. das Hinzufügen neuer Spieler oder das Abrufen von Spielerinformationen und das Sortieren der Karten in den Händen der Spieler.
- [CardService](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fimpl%2FCardService.java): Verwaltet die Kartendaten und -aktionen, wie z.B. das Mischen des Kartendecks oder das Ziehen von Karten. [CardComparator](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fimpl%2FCardComparator.java) wird verwendet, um die Karten zu sortieren und zu vergleichen, um die Reihenfolge der Karten zu bestimmen.
- [GameService](GameEngine%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fgameengine%2Fimpl%2FGameService.java): Koordiniert die Spiellogik und -aktionen, wie z.B. das Starten eines neuen Spiels oder das Ausführen von Spielzügen, die Berechnung von Punktzahlen und die Bestimmung des Spielgewinners.
- [RuleService](RulesManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Frulesmanagement%2Fimpl%2FRuleService.java): Verwaltet die Spielregeln und -aktionen, wie z.B. das Festlegen der Regeln oder das Überprüfen der Spielregeln.
- **PersistenceService**([PersistenceJPAConfig](Persistence%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fpersistence%2FPersistenceJPAConfig.java)): Verwaltet die Datenpersistenz und -aktionen, wie z.B. das Speichern von Spielständen oder das Laden von Spielhistorien.

Diese Services werden von den Controllern der GUI aufgerufen, um die entsprechenden Aktionen auszuführen und die Daten anzuzeigen. Die Services stellen die Schnittstelle zwischen der GUI und den anderen Komponenten der Anwendung dar und ermöglichen eine klare Trennung der verschiedenen Schichten. 

### 5. Frameworks

In unserem Projekt kommen mehrere Frameworks und Bibliotheken zum Einsatz, die eine effiziente und skalierbare technische Architektur gewährleisten. Im Folgenden werden die eingesetzten Frameworks detailliert beschrieben und ihre Konfiguration sowie ihre spezifischen Rollen im Projekt erläutert.

- **Spring Boot**: Verwendet für die Erstellung von RESTful Web Services und die Konfiguration der Anwendung. In der Datei [pom.xml](pom.xml) werden die Version und die Abhängigkeiten von Spring Boot definiert.

- **Spring Data JPA**: Verwendet für die Datenpersistenz und -verwaltung. Das Konfigurationsfile [PersistenceJPAConfig](Persistence%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fpersistence%2FPersistenceJPAConfig.java) enthält die Einstellungen für die JPA-Konfiguration auch in [pom.xml](pom.xml) wird die Abhängigkeit definiert.

- **Spring MVC**: Verwendet für die Implementierung des MVC-Musters und die Erstellung der Präsentationsschicht. Die Controller-Klassen in den Modulen der Anwendung verwenden Spring MVC für die Steuerung der Interaktionen zwischen Model und View. 

- **Hibernate**: Verwendet für die Objekt-Relationen-Mapping (ORM) und die Datenbankinteraktion. Konfiguriert durch die JPA-Annotations in den Entitätsklassen. In die Klasse [persistence.properties](Persistence%2Fsrc%2Fmain%2Fresources%2Fpersistence.properties) werden die Datenbankverbindungsdaten konfiguriert, inbesondere Hibernate und auch in [pom.xml](pom.xml) wird die Abhängigkeit definiert.

- **PostgreSQL**: Verwendet als relationale Datenbank für die Speicherung der Anwendungsdaten. Die Datenbankverbindungsdaten werden in der [persistence.properties](Persistence%2Fsrc%2Fmain%2Fresources%2Fpersistence.properties)-Datei konfiguriert. 

- **JUnit**: Verwendet für das Testen der Anwendungskomponenten. Die Testklassen sind in den jeweiligen Modulen der Anwendung enthalten und verwenden JUnit für das Testen der Funktionalitäten. Zum Beispiel: [CardServiceTest](CardsManagement%2Fsrc%2Ftest%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fimpl%2FCardServiceTest.java).

- **Mockito**: Verwendet für das Mocking von Objekten in den Unit-Tests. Die Konfiguration erfolgt durch die Verwendung von Mockito in den Testklassen, um die Abhängigkeiten zu simulieren und die Funktionalitäten der Anwendung zu testen. Zum Beispiel: [CardServiceTest](CardsManagement%2Fsrc%2Ftest%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fimpl%2FCardServiceTest.java). Das Konfigurationsfile [pom.xml](pom.xml) enthält die Abhängigkeiten für Mockito.

- **Apache Maven**: Verwendet für das Build-Management und die Abhängigkeitsverwaltung. Das Konfigurationsfile [pom.xml](pom.xml) in jedem Modul des Projekts enthält die Abhängigkeiten und Plugins für das Projekt.

- **Swagger**: Verwendet für die Dokumentation der RESTful Web Services. Die Konfiguration erfolgt durch die Annotationen in den Controller-Klassen und die Generierung der Swagger-Dokumentation. Zum Beispiel: [CardController](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fapi%2Fcontroller%2FCardController.java).

- **Maven**: Verwendet für das Build-Management und die Abhängigkeitsverwaltung. Das Konfigurationsfile [pom.xml](pom.xml) in jedem Modul des Projekts enthält die Abhängigkeiten und Plugins für das Projekt. 

- **Lombok**: Verwendet für die Vereinfachung der Java-Entwicklung durch Annotationen. Die Konfiguration erfolgt durch die Lombok-Annotationen in den Entitätsklassen und Service-Klassen. Zum Beispiel: [Card](CardsManagement%2Fsrc%2Fmain%2Fjava%2Fde%2Fhtwberlin%2Fcardsmanagement%2Fapi%2Fmodel%2FCard.java) und in [pom.xml](pom.xml).

- **Docker**: Verwendet für die Containerisierung der Anwendung. Die Konfiguration erfolgt durch die Dockerfiles in den jeweiligen Modulen der Anwendung. Zum Beispiel: Die Docker-Konfiguration für CardsManagement sorgt dafür, dass diese Komponente in einem eigenen Container läuft, der von den anderen Komponenten isoliert ist, aber über das Netzwerk mit ihnen kommunizieren kann.

Die Konfiguration dieser Frameworks und Produkte erfolgt durch entsprechende Konfigurationsdateien und Annotationen in den Java-Klassen. Beispielsweise werden die Datenbankverbindungsdaten in der [persistence.properties](Persistence%2Fsrc%2Fmain%2Fresources%2Fpersistence.properties)-Datei konfiguriert, während die RESTful Web Services durch Annotationen in den Controller-Klassen definiert werden. Die Konfiguration der Anwendung erfolgt durch die entsprechenden Konfigurationsklassen und -dateien, die die Einstellungen für die verschiedenen Frameworks und Produkte enthalten. Die Verwendung dieser Frameworks und Produkte ermöglicht es uns, die Anwendung effizient zu entwickeln, zu testen und zu deployen und eine skalierbare und wartbare Architektur zu schaffen.

### 6. Ablaufumgebung


Unser System wird in einer Cloud-Umgebung eingesetzt, die aus mehreren virtuellen Maschinen besteht, die über das Internet miteinander verbunden sind. Die Architektur der technischen Infrastruktur umfasst folgende Komponenten:

- **Frontend-Server**: Dieser Server enthält die Benutzeroberfläche der Anwendung und stellt die Schnittstelle für die Benutzerinteraktion bereit. Der Frontend-Server läuft auf einem separaten virtuellen Server, der über das Internet erreichbar ist. Beispiele Technologien: HTML, CSS, JavaScript, Angular, React.
- **Backend-Server**: Dieser Server beinhaltet die Geschäftslogik und Datenverarbeitung der Anwendung. Der Backend-Server läuft auf einem separaten virtuellen Server, der über das Internet erreichbar ist. Spring Boot dient hier als Framework zur Entwicklung.
- **Datenbank-Server**: Dieser Server hostet die Datenbank, die zur Speicherung der Anwendungsdaten verwendet wird. Der Datenbank-Server läuft auf einem separaten virtuellen Server und ist über das Internet erreichbar. In unserem Fall verwenden wir PostgreSQL als relationale Datenbank.
- **Load Balancer**: Der Load Balancer verteilt die Anfragen der Benutzer auf die verschiedenen Server und sorgt für eine gleichmäßige Auslastung der Ressourcen. Er läuft auf einem separaten virtuellen Server, der über das Internet erreichbar ist und die Anfragen an die entsprechenden Server weiterleitet. Hier kommen beispielsweise Nginx oder Apache zum Einsatz.

Die virtuellen Server sind über das Internet miteinander verbunden und kommunizieren über das Netzwerk miteinander. Die Anwendung wird über das Internet aufgerufen und kann von den Benutzern über einen Webbrowser oder eine spezielle Anwendungssoftware genutzt werden. Alle Anwendungsdaten werden in der Datenbank gespeichert, während die Anwendung selbst über die Frontend- und Backend-Server bereitgestellt wird. Diese technische Infrastruktur ermöglicht eine skalierbare und flexible Bereitstellung der Anwendung und gewährleistet eine hohe Verfügbarkeit und Leistungsfähigkeit.