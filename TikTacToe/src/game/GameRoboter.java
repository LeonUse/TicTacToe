package game;

import java.util.ArrayList;
import java.util.Scanner;

import plott3r_V1_solved.Roboter;

public class GameRoboter {
	private static String zeichen1 = "1";
	private static String zeichen2 = "2";
	private static ArrayList<Integer> a = new ArrayList<Integer>();
	private static String[][] spielfeld = new String[3][3];
	private Roboter roboter = new Roboter();
	private final RoboterVisualizer roboterVisualizer = new RoboterVisualizer(roboter);
	private boolean weiterspielen;

	public void startRoboterGame() {
		// Der Hintergrund wird in einem neuen Thread gezeichnet, damit der Spieler
		// schon auswählen kann was er spielen möchte.
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				roboterVisualizer.zeichneHintergrund();
			}
		});
		t1.start();
		// Hier wird überprüft, ob man Player VS Player oder Computer VS Player spielen
		// möchte.
		while (true) {

			int eingabe = roboterVisualizer.eingabe("1 fuer CVP", "2 fuer PVP", "Auswahl");
			if (eingabe == 1) {
				int i;
				while (true) {
					// Schwierigkeitsstufe wird abgefragt
					i = roboterVisualizer.eingabe("Nimm Schwierigkeit", "von 1-2", "Schwierigkeit");
					if (i == 1 || i == 2) {
						break;
					}
					roboterVisualizer.eingabe("Bitte 1-2", "auswaehlen");
					// Das "sleep" wird benötigt, damit die Ausgabe für einen Moment angezeigt wird
					// und nicht direkt überschrieben wird von der Ausgabe in Zeile 33.
					sleep();
				}
				// Computer vs Player wird mit der gewählten Schwierigkeit gestartet.
				cvp(i, roboter, roboterVisualizer);
				break;
			} else if (eingabe == 2) {
				roboterVisualizer.brickAnzeigen("PVP ausgewaehlt.", "");
				pvp(roboter, roboterVisualizer);
				break;
			} else {
				roboterVisualizer.brickAnzeigen("Bitte waehle", "1 fuer PVP", "2 fuer CVP");
				sleep();
			}
		}
		// Hier wird nach einem Spiel überprüft, ob der Spieler noch eine Runde spielen
		// möchte.
		weiterspielen = neuesSpiel(roboterVisualizer);
		if (weiterspielen == true) {
			try {
				roboter.entfernePapier();
			} catch (InterruptedException e) {
				roboterVisualizer.brickAnzeigen("UPS, ein Fehler", "ist aufgetreten.", "Bitte neustarten");
			}
			roboterVisualizer.brickAnzeigen("Bitte lege ein", "neues Papier", "ein");
			try {
				roboter.bereitePapierVor();
			} catch (InterruptedException e) {
				roboterVisualizer.brickAnzeigen("UPS, ein Fehler", "ist aufgetreten.", "Bitte neustarten");
			}
			// Das Spielfeld wird wieder neu erzeugt, damit keine alten Werte mehr drin
			// stehen.
			spielfeld = null;
			spielfeld = new String[3][3];
			startRoboterGame();

		} else {
			roboterVisualizer.brickAnzeigen("Bis bald", ":)");
			try {
				roboter.entfernePapier();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sleep();
		}
	}

	public static void pvp(Roboter roboter, RoboterVisualizer roboterVisualizer) {
		// Ablauf für Spieler VS Spieler
		// Variablen erzeugen
		int gewinnZähler = 0;
		Person spieler1 = new Person(zeichen1);
		Person spieler2 = new Person(zeichen2);
		Schiedsrichter schiedsrichter = new Schiedsrichter(spieler1.getZeichen(), spieler2.getZeichen());
		// Es gibt maximal 9 Züge bei Tic Tac Toe, deswegen nur 5 durchläufe (Pro
		// durchlauf 2 Züge und beim 5. Durchlauf nur 1 Zug)
		for (int i = 0; i < 5; i++) {
			// Die Schleife läuft solange bis der Spieler gültige Koordinaten eingibt
			while (true) {
				a = roboterVisualizer.playerZugBrick();
				if (überprüfeSpielzug(spieler1, a, roboterVisualizer)) {
					spielfeld[a.get(0)][a.get(1)] = spieler1.getZeichen();
					// Nummer des Feldes ermittlen und dann von dem Roboter zeichnen lassen
					roboterVisualizer.zeichneSymbol(spieler1.getZeichen(), findeFeldnummer(a));
					// Erst nach dem 5. Zug kann man gewinnen, deswegen wird nach jedem Zug der
					// gewinnZähler erhöht
					gewinnZähler = gewinnZähler + 1;
					break;
				}
			}
			if (gewinnZähler > 4) {
				// Hier wird überprüft, ob der Spieler gewonnen hat und ggf. die Schleife
				// abgebrochen
				if (überprüfeGewinner(schiedsrichter, spieler1)) {
					// System.out.println("Spieler 1 hat gewonnen!!!");
					roboterVisualizer.brickAnzeigen("Spieler 1 hat", "GEWONNEN!!!");
					sleep();
					break;
				}
				if (i == 4) {
					System.out.println("Unentschieden!!!");
					break;
				}
			}
			while (true) {
				// Genau das gleiche wie bei Spieler1
				a = roboterVisualizer.playerZugBrick();
				if (überprüfeSpielzug(spieler2, a, roboterVisualizer)) {
					spielfeld[a.get(0)][a.get(1)] = spieler2.getZeichen();
					roboterVisualizer.zeichneSymbol(spieler2.getZeichen(), findeFeldnummer(a));
					gewinnZähler = gewinnZähler + 1;
					break;
				}
			}
			// ConsoleVisualizer.zeichneSpielfeld(spielfeld);
			if (gewinnZähler > 4) {
				if (überprüfeGewinner(schiedsrichter, spieler2)) {
					// System.out.println("Spieler 2 hat gewonnen!!!");
					roboterVisualizer.brickAnzeigen("Spieler 2 hat", "GEWONNEN!!!");
					sleep();
					break;
				}
			}
		}
	}

	public static void cvp(int schwierigkeit, Roboter roboter, RoboterVisualizer roboterVisualizer) {
		// Computer vs Player
		int gewinnZähler = 0;
		Person spieler = new Person(zeichen1);
		Computer computer = new Computer(spieler.getZeichen());
		Schiedsrichter schiedsrichter = new Schiedsrichter(spieler.getZeichen(), computer.getZeichen());
		for (int i = 0; i < 8; i++) {
			while (true) {
				a = roboterVisualizer.playerZugBrick();
				if (überprüfeSpielzug(spieler, a, roboterVisualizer)) {
					spielfeld[a.get(0)][a.get(1)] = spieler.getZeichen();
					roboterVisualizer.zeichneSymbol(spieler.getZeichen(), findeFeldnummer(a));
					gewinnZähler = gewinnZähler + 1;
					break;
				}
			}
			if (gewinnZähler > 4) {
				if (überprüfeGewinner(schiedsrichter, spieler)) {
					roboterVisualizer.brickAnzeigen("Du hast", "GEWONNEN!!!");
					sleep();
					break;
				}
			}
			while (true) {
				a = computer.spielzug(schwierigkeit, spielfeld);
				if (überprüfeSpielzug(computer, a, roboterVisualizer)) {
					spielfeld[a.get(0)][a.get(1)] = computer.getZeichen();
					gewinnZähler = gewinnZähler + 1;
					roboterVisualizer.zeichneSymbol(computer.getZeichen(), findeFeldnummer(a));
					break;
				}
			}
			if (gewinnZähler > 4) {
				if (überprüfeGewinner(schiedsrichter, computer)) {
					roboterVisualizer.brickAnzeigen("Der Computer", "hat gewonnen!");
					sleep();
					break;
				}
			}
		}

	}

	// Hier wird geschaut, ob der gewählte Zug durchgeführt werden kann oder ob das
	// Feld schon belegt ist.
	public static boolean überprüfeSpielzug(Spieler p, ArrayList koordinaten, RoboterVisualizer roboterVisualizer) {
		//Try-Catch falls der SPieler Werte eingibt, die größer als 3 sind.
		try {
			if (spielfeld[a.get(0)][a.get(1)] != null) {
				if (p.getZeichen().equals("C")) {
					return false;
				}
				roboterVisualizer.brickAnzeigen("Das Feld", "ist schon belegt.");
				sleep();
				return false;
			} else {
				spielfeld[a.get(0)][a.get(1)] = p.getZeichen();
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			roboterVisualizer.brickAnzeigen("Bitte nur Werte", "von 1-3");
			sleep();
			return false;
		}
	}

	// Das Gleiche für den Computer mit einem Eingebauten Fehler. Spielt man gegen
	// den Computer auf der Stufe 2 und baut eine Zwickmühle dann will er ein Feld
	// belgen, dass schon belegt ist. Passiert das 3x dann wird ein Backup Code
	// ausgeführt und das Spiel geht weiter.
	public static boolean überprüfeSpielzugComputer(Computer p, ArrayList koordinaten,
			RoboterVisualizer roboterVisualizer) {
		try {
			if (spielfeld[a.get(0)][a.get(1)] != null) {
				p.fehler = p.fehler + 1;
				return false;
			} else {
				spielfeld[a.get(0)][a.get(1)] = p.getZeichen();
				p.fehler = 0;
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			roboterVisualizer.brickAnzeigen("Bitte nur Werte", "von 1-3");
			sleep();
			return false;
		}
	}

	// Hier wird anhand des Rückgabewertes geschaut, ob es einen Gewinner gibt.
	public static boolean überprüfeGewinner(Schiedsrichter schiedsrichter, Spieler p) {
		if (schiedsrichter.werHatGewonnen(spielfeld).equals(p.getZeichen())) {
			return true;
		} else if (schiedsrichter.werHatGewonnen(spielfeld).equals(p.getZeichen())) {
			return true;
		} else {
			return false;
		}

	}

	// Damit das Zeichnen leichter ist suchen
	public static int findeFeldnummer(ArrayList<Integer> a) {
		int position = -1;
		switch (a.get(0)) {
		case 0:
			switch (a.get(1)) {
			case 0:
				position = 0;
				break;
			case 1:
				position = 3;
				break;
			case 2:
				position = 6;
				break;
			}
			break;
		case 1:
			switch (a.get(1)) {
			case 0:
				position = 1;
				break;
			case 1:
				position = 4;
				break;
			case 2:
				position = 7;
				break;
			}
			break;
		case 2:
			switch (a.get(1)) {
			case 0:
				position = 2;
				break;
			case 1:
				position = 5;
				break;
			case 2:
				position = 8;
				break;
			}
			break;
		}
		return position;

	}

	// Ein einfaches Sleep, damit der Code nicht überall doppelt steht.
	public static void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Abfrage, ob der Spieler noch eine Runde spielen möchte.
	public static boolean neuesSpiel(RoboterVisualizer roboterVisualizer) {
		boolean weiter = roboterVisualizer.jaNein("Moechtest du", "weiterspielen?", "JA/NEIN");
		return weiter;
	}
}
