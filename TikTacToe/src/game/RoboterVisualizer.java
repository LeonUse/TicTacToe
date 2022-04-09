package game;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import plott3r_V1.geometrie.Linie;
import plott3r_V1.geometrie.X;
import plott3r_V1_solved.Roboter;
import positions.Position2D;
import positions.Position3D;

public class RoboterVisualizer {
	private int[][] koordinaten = { { 35, 125 }, { 90, 125 }, { 140, 125 }, { 35, 75 }, { 90, 75 }, { 140, 75 },
			{ 35, 25 }, { 90, 25 }, { 140, 25 } };
	private Roboter roboter;

	RoboterVisualizer(Roboter r) {
		roboter = r;
	}

	public void zeichneSymbol(String zeichen, int position) {
		if (zeichen == "1") {
			Linie linie = new Linie(new Position2D(koordinaten[position][0], koordinaten[position][1]), 30);
			linie.zeichneLinie(roboter);
		}
		if (zeichen == "2") {
			X x = new X(new Position2D(koordinaten[position][0], koordinaten[position][1]), 30);
			x.zeichneX(roboter);
		}
		if (zeichen == "C") {
			X x = new X(new Position2D(koordinaten[position][0], koordinaten[position][1]), 30);
			x.zeichneX(roboter);
		}
	}

	public void zeichneHintergrund() {
		try {
			roboter.bereitePapierVor();
			roboter.moveToHomePosition();
			roboter.moveToPosition(new Position3D(10, 50, false), 30);
			roboter.moveToPosition(new Position3D(160, 50, true), 30);
			roboter.moveToPosition(new Position3D(160, 100, false), 30);
			roboter.moveToPosition(new Position3D(10, 100, true), 30);
			roboter.moveToPosition(new Position3D(60, 150, false), 30);
			roboter.moveToPosition(new Position3D(60, 0, true), 30);
			roboter.moveToPosition(new Position3D(120, 0, false), 30);
			roboter.moveToPosition(new Position3D(120, 150, true), 30);
			//Wir haben das Zero nach oben verschoben, damit der Roboter das Spielfeld nicht überdeckt.
			roboter.moveToZero();
		} catch (InterruptedException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
	}
	
	//eingabe der XY Werte über den Brick
	public ArrayList<Integer> playerZugBrick() {
		ArrayList<Integer> i  = new ArrayList<Integer>();
		i.add(0);
		i.add(0);
		int xOderY = 0;
		boolean weiter = true;
		//Die Schleife wird solange ausgeführt, bis der Spieler "Enter" drückt.
			while (weiter) {
				brickAnzeigen("Waehle XY:", "X:" + (i.get(0)+1),"Y:" + (i.get(1)+1));			
				int button = Button.waitForAnyPress();
				switch (button) {
				//Drückt der Spieler nach Rechts dann wird die Eingabe um 1 erhöht, bei Links um 1 verringert. Der aktuelle Wert wird dann oben ausgegeben.
				case Button.ID_RIGHT:
					i.set(xOderY, i.get(xOderY)+1);
					break;
				case Button.ID_LEFT:
					i.set(xOderY, i.get(xOderY)-1);
					break;
				case Button.ID_ENTER:
					weiter = false;
					break;
					//Mit Up und Down wird zwischen dem X- und Y-Wert gewechselt.
				case Button.ID_DOWN:
					xOderY=1;
					break;
				case Button.ID_UP:
					xOderY=0;
					break;
				}
			}
		return i;
	}
	
	//Eingabe für PVP/CVP und Stärke (lange Fragen)
	public int eingabe(String teil1, String teil2, String teil3) {
		int a = 1;
		boolean weiter = true;
		while (weiter) {
			brickAnzeigen(teil1, teil2, teil3+":" + a);
			int button = Button.waitForAnyPress();
			switch (button) {
			case Button.ID_RIGHT:
				a = a + 1;
				break;
			case Button.ID_LEFT:
				a = a - 1;
				break;
			case Button.ID_ENTER:
				weiter = false;
				break;
			}
		}
		return a;
		
	}
	//Eingabe für kurze Fragen
	public int eingabe(String teil1, String teil2) {
		int a = 1;
		boolean weiter = true;
		while (weiter) {
			brickAnzeigen(teil1, teil2+":" + a);
			int button = Button.waitForAnyPress();
			switch (button) {
			case Button.ID_RIGHT:
				a = a + 1;
				break;
			case Button.ID_LEFT:
				a = a - 1;
				break;
			case Button.ID_ENTER:
				weiter = false;
				break;
			}
		}
		return a;
	}
	//Eingabe für Ja/Nein
	public boolean jaNein(String teil1, String teil2,String teil3) {
		boolean a = false;
		boolean weiter = true;
		while (weiter) {
			if(a==false) {
				brickAnzeigen(teil1, teil2, teil3+": Nein");
			}else {
				brickAnzeigen(teil1, teil2, teil3+": Ja");
			}
			
			int button = Button.waitForAnyPress();
			switch (button) {
			case Button.ID_RIGHT:
				a=true;
				break;
			case Button.ID_LEFT:
				a=false;
				break;
			case Button.ID_ENTER:
				weiter = false;
				break;
			}
		}
		return a;
	}
	
	//Zweizeiler auf dem Brick anzeigen
	public static void brickAnzeigen(String displayTop, String displayMiddle) {
		LCD.refresh();
		LCD.clear();
		LCD.drawString(displayTop, 0, 1);
		LCD.drawString(displayMiddle, 0, 5);
	}
	//3 Zeilen auf dem Brick anzeigen für längere Texte
	public static void brickAnzeigen(String displayTop, String displayMiddle,String displayBottom) {
		LCD.refresh();
		LCD.clear();
		LCD.drawString(displayTop, 0, 1);
		LCD.drawString(displayMiddle, 0, 3);
		LCD.drawString(displayBottom, 0, 5);
	}
}
