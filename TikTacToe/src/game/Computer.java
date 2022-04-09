package game;

import java.util.ArrayList;

public class Computer extends Spieler {
	private String zeichen = "C";
	private String spielerZeichen;
	public static int fehler = 0;
	public ArrayList spielzug(int schwierigkeit, String[][] input) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		int randomZahl;
		int ersterZug = 0;
		if (schwierigkeit == 1) {
			// easy; hier wird zuf�llig eine Zahl als Koordinate generiert
			for (int i = 0; i < 2; i++) {
				randomZahl = (int) (Math.random() * 3);
				a.add(randomZahl);
			}
			return a;
		}
		//Test
		// normal; hier schaut der Computer erst, ob er schon 2 Felder nebeneinander
		// belegt hat und das 3. frei ist damit er gewinnt. Wenn das m�glich ist macht
		// er es.
		// Danach schaut der Computer ob der Spieler schon 2 Felder nebeneinander belegt
		// hat und blockiert wenn m�glich das 3.
		// Wenn das auch nicht der Fall ist dann belegt er Zuf�llig ein Feld.
		if (schwierigkeit == 2) {
			//Wenn man eine Zwickm�hle aufbaut wei� der Computer nicht was er machen soll und h�ngt in einer Endlosschleife. Deswegen wird der Feld dann zuf�llig ausgew�hlt, da in dem Fall der Spieler eh gewonnen hat.
			if (fehler>2) {
				for (int z = 0; z < 2; z++) {
					int zahl1 = (int) (Math.random() * 3);
					a.add(zahl1);
				}
				return a;
			}
			for (int i = 0; i < 8; i++) {
				String line = null;
				switch (i) {
				case 0:
					line = input[0][0] + input[0][1] + input[0][2];
					break;
				case 1:
					line = input[1][0] + input[1][1] + input[1][2];
					break;
				case 2:
					line = input[2][0] + input[2][1] + input[2][2];
					break;
				case 3:
					line = input[0][0] + input[1][0] + input[2][0];
					break;
				case 4:
					line = input[0][1] + input[1][1] + input[2][1];
					break;
				case 5:
					line = input[0][2] + input[1][2] + input[2][2];
					break;
				case 6:
					line = input[0][0] + input[1][1] + input[2][2];
					break;
				case 7:
					line = input[2][0] + input[1][1] + input[0][2];
					break;
				}
				// Hier wird geschaut, ob er 2 Felder nebeneinander belegt hat und ob das 3.
				// frei ist.
				if (line.equals(null + zeichen + zeichen) | line.equals(zeichen + zeichen + null)
						| line.equals(zeichen + null + zeichen)) {
					int z�hler = 0;
					// Hier wird geschaut welches von den 3 Feldern er belegen muss um zu Gewinnen.
					switch (i) {
					case 0:
						if (input[0][0] == null) {
							a.add(0);
							a.add(0);
							return a;
						}
						if (input[0][1] == null) {
							a.add(0);
							a.add(1);
							return a;
						}
						if (input[0][2] == null) {
							a.add(0);
							a.add(2);
							return a;
						}
						break;
					case 1:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[1][z�hler] == null) {
								a.add(1);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 2:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[2][z�hler] == null) {
								a.add(1);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 3:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][0] == null) {
								a.add(z�hler);
								a.add(0);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 4:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][1] == null) {
								a.add(z�hler);
								a.add(1);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 5:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][2] == null) {
								a.add(z�hler);
								a.add(2);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 6:
						line = input[0][0] + input[1][1] + input[2][2];
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][z�hler] == null) {
								a.add(z�hler);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 7:
						int j = 2;
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[j][z�hler] == null) {
								a.add(j);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
							j = j - 1;
						}
						break;
					}
				}
				// Hier schaut er ob er ein Feld blockieren kann damit der Spieler nicht
				// gewinnt.
				if (line.equals(spielerZeichen + null + spielerZeichen)
						| line.equals(null + spielerZeichen + spielerZeichen)
						| line.equals(spielerZeichen + spielerZeichen + null)) {
					int z�hler = 0;
					// Hier schaut er welches Feld er blockieren muss.
					switch (i) {
					case 0:
						if (input[0][0] == null) {
							a.add(0);
							a.add(0);
							return a;
						}
						if (input[0][1] == null) {
							a.add(0);
							a.add(1);
							return a;
						}
						if (input[0][2] == null) {
							a.add(0);
							a.add(2);
							return a;
						}
						break;
					case 1:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[1][z�hler] == null) {
								a.add(1);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 2:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[2][z�hler] == null) {
								a.add(1);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 3:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][0] == null) {
								a.add(z�hler);
								a.add(0);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 4:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][1] == null) {
								a.add(z�hler);
								a.add(1);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 5:
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][2] == null) {
								a.add(z�hler);
								a.add(2);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 6:
						line = input[0][0] + input[1][1] + input[2][2];
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[z�hler][z�hler] == null) {
								a.add(z�hler);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
						}
						break;
					case 7:
						int j = 2;
						while (true) {
							if (z�hler > 2) {
								break;
							}
							if (input[j][z�hler] == null) {
								a.add(j);
								a.add(z�hler);
								return a;
							}
							z�hler = z�hler + 1;
							j = j - 1;
						}
						break;
					}
				}

			}
			// Hier wird als letzte Option ein Zuf�lliges Feld belegt.
			for (int z = 0; z < 2; z++) {
				int zahl1 = (int) (Math.random() * 3);
				a.add(zahl1);
			}
			return a;
		}
		if (schwierigkeit == 3) {
			// impossible			
		}
		return null;

	}

	public Computer(String spielerZeichen) {
		this.spielerZeichen = spielerZeichen;
	}

	public String getZeichen() {
		return zeichen;
	}
}
