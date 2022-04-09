package game;

import java.util.ArrayList;
import java.util.Scanner;

public class Person extends Spieler {
	private String zeichen;

	public Person(String zeichen) {
		this.zeichen = zeichen;
	}

	public ArrayList spielzug(int spieler) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		System.out.println(
				"Spieler"+spieler+", sag die X-Y-Koordinaten von deinem gewünschtem Feld. Bitte gib zuerst die X-Koordiante und dann die Y-Koordinate ein.");
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			while (true) {
				int koordinate = sc.nextInt();
				koordinate = koordinate -1;
				if (koordinate < 3 & koordinate >= 0) {
					a.add(koordinate);
					break;
				} else {
					System.out.println("Bitte gib nur Zahlen von 1 bis 3 ein.");
				}
			}
		}
		return a;
	}
	
	public String getZeichen() {
		return zeichen;
	}

}
