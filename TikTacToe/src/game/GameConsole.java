package game;

import java.util.ArrayList;
import java.util.Scanner;

public class GameConsole {
	static String zeichen1 = "1";
	static String zeichen2 = "2";
	static ArrayList<Integer> a = new ArrayList<Integer>();
	static String[][] spielfeld = new String[3][3];

	public void startConsoleGame() {
		System.out.println("Schreib CVP f�r Computer VS Player oder PVP f�r Player VS Player");
		//Hier wird �berpr�ft ob man Player VS Player oder Computer VS Player spielen m�chte
		while (true) {
			Scanner sc = new Scanner(System.in);
			String s = sc.next();
			if (s.toUpperCase().equals("CVP")) {
				System.out.println("Computer VS Player wurde ausgew�hlt.");
				int i;
				while (true) {
					System.out.println("Bitte gib ein wie Stark der Computer sein soll. 1-Easy 2-Normal");
					i = sc.nextInt();
					if (i == 1 || i == 2) {
						break;
					}
				}
				cvp(i);
				break;
			} else if (s.toUpperCase().equals("PVP")) {
				System.out.println("Player VS Player wurde ausgew�hlt.");
				pvp();
				break;
			} else {
				System.out.println("Bitte gib PVP oder CVP ein.");
			}
		}

	}

	public static void pvp() {
		// Ablauf f�r Spieler VS Spieler
		//Variablen erzeugen
		int gewinnZ�hler = 0;
		Person spieler1 = new Person(zeichen1);
		Person spieler2 = new Person(zeichen2);
		Schiedsrichter schiedsrichter = new Schiedsrichter(spieler1.getZeichen(), spieler2.getZeichen());
		//Spielfeld zeichnen
		ConsoleVisualizer.zeichneSpielfeld(spielfeld);
		//Es gibt maximal 9 Z�ge bei Tic Tac Toe, deswegen nur 9 durchl�ufe
		for (int i = 0; i < 5; i++) {
			//Die Schleife l�uft solange bis der Spieler g�ltige Koordinaten eingibt
			while (true) {
				a = spieler1.spielzug(1);
				if (�berpr�feSpielzugSpieler(spieler1, a)) {
					spielfeld[a.get(0)][a.get(1)] = spieler1.getZeichen();
					//Erst nach dem 5. Zug kann man gewinnen, deswegen wird nach jedem Zug der gewinnZ�hler erh�ht
					gewinnZ�hler = gewinnZ�hler +1;
					break;
				}
			}
			ConsoleVisualizer.zeichneSpielfeld(spielfeld);
			if(gewinnZ�hler > 4) {
				//Hier wird �berpr�ft, ob der Spieler gewonnen hat und ggf. die Schleife abgebrochen
				if(�berpr�feGewinner(schiedsrichter, spieler1)) {
					System.out.println("Spieler 1 hat gewonnen!!!");
					break;
				}
				if(i ==4) {
					System.out.println("Unentschieden!!!");
					break;
				}
			}
			while (true) {
				//Genau das gleiche wie bei Spieler1
				a = spieler2.spielzug(2);
				if (�berpr�feSpielzugSpieler(spieler2, a)) {
					spielfeld[a.get(0)][a.get(1)] = spieler2.getZeichen();
					gewinnZ�hler = gewinnZ�hler +1;
					break;
				}
			}
			ConsoleVisualizer.zeichneSpielfeld(spielfeld);
			if(gewinnZ�hler > 4) {
				if(�berpr�feGewinner(schiedsrichter, spieler2)) {
					System.out.println("Spieler 2 hat gewonnen!!!");
					break;
				}
			}
		}
	}
	
	public static void cvp (int schwierigkeit) {
		//Computer vs Player
		int gewinnZ�hler = 0;
		Person spieler = new Person(zeichen1);
		Computer computer = new Computer(spieler.getZeichen());
		Schiedsrichter schiedsrichter = new Schiedsrichter(spieler.getZeichen(), computer.getZeichen());
		ConsoleVisualizer.zeichneSpielfeld(spielfeld);
		for (int i = 0; i < 8; i++) {
			while (true) {
				a = spieler.spielzug(1);
				if (�berpr�feSpielzugSpieler(spieler, a)) {
					spielfeld[a.get(0)][a.get(1)] = spieler.getZeichen();
					gewinnZ�hler = gewinnZ�hler +1;
					break;
				}
			}
			ConsoleVisualizer.zeichneSpielfeld(spielfeld);
			if(gewinnZ�hler > 4) {
				if(�berpr�feGewinner(schiedsrichter, spieler)) {
					System.out.println("Spieler 1 hat gewonnen!!!");
					break;
				}
				if(i ==4) {
					System.out.println("Unentschieden!!!");
					break;
				}
			}
			
			while (true) {
				a = computer.spielzug(schwierigkeit, spielfeld);
				if (�berpr�feSpielzugComputer(computer, a)) {
					spielfeld[a.get(0)][a.get(1)] = computer.getZeichen();
					gewinnZ�hler = gewinnZ�hler +1;
					System.out.println("Computer:");
					break;
				}
			}
			ConsoleVisualizer.zeichneSpielfeld(spielfeld);
			if(gewinnZ�hler > 4) {
				if(�berpr�feGewinner(schiedsrichter, computer)) {
					System.out.println("Der Computer hat gewonnen!!!");
					break;
				}
			}
		}
		
	}

	public static boolean �berpr�feSpielzugSpieler(Person p, ArrayList koordinaten) {
		try {
		if (spielfeld[a.get(0)][a.get(1)] != null) {
			System.out.println("Das Feld ist schon belegt.");
			return false;
		} else {
			spielfeld[a.get(0)][a.get(1)] = p.getZeichen();
			return true;
		}
		} catch (ArrayIndexOutOfBoundsException  e) {
			System.out.println("Bitte nur Werte von 1-3");
			return false;
		}
	}
	
	public static boolean �berpr�feSpielzugComputer(Computer p, ArrayList koordinaten) {
		try {
		if (spielfeld[a.get(0)][a.get(1)] != null) {
				p.fehler=p.fehler +1;
				System.out.println("Fehler"+p.fehler);
				System.out.println(a.get(0)+""+a.get(1));
				return false;
		} else {
			spielfeld[a.get(0)][a.get(1)] = p.getZeichen();
			if(p.getZeichen().equals("C")) {
				p.fehler=0;
			}
			return true;
		}
		} catch (ArrayIndexOutOfBoundsException  e) {
			System.out.println("Bitte nur Werte von 1-3");
			return false;
		}
	}
	public static boolean �berpr�feGewinner(Schiedsrichter schiedsrichter, Spieler p) {
		if(schiedsrichter.werHatGewonnen(spielfeld).equals(p.getZeichen())) {
			return true;
		}
		else if(schiedsrichter.werHatGewonnen(spielfeld).equals(p.getZeichen())) {
			return true;
		} else {
			return false;
		}
		
	}
}