package de.htwberlin.gameui.api;

/**
 * Die GameUIInterface-Schnittstelle definiert die Methode zur Steuerung der Spielbenutzeroberfläche.
 * Sie ermöglicht die Initialisierung und Ausführung der UI-Komponente, die Benutzereingaben aufnimmt
 * und Spielausgaben anzeigt.
 */
public interface GameUIInterface {

        /**
         * Startet die Benutzeroberfläche des Spiels und koordiniert die Interaktion mit dem Benutzer.
         */
        void run();
}
