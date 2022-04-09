package game;

public class Schiedsrichter {
	private String spieler1Zeichen;
	private String spieler2Zeichen;
	
	
	public String werHatGewonnen(String[][] input) {
		
		//Überprüfung, ob ein Spieler Gewonnen hat.
        for (int a = 0; a < 8; a++) {
            String line = null;
            switch (a) {

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
                line = input[0][2] + input[1][2] + input[2][2];
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
            if (line.equals(spieler1Zeichen + spieler1Zeichen + spieler1Zeichen)) {
                return spieler1Zeichen;
            } else if (line.equals(spieler2Zeichen + spieler2Zeichen + spieler2Zeichen)) {
                return spieler2Zeichen;
            }
        }
		return "0";
    }


	public Schiedsrichter(String spieler1Zeichen, String spieler2Zeichen) {
		this.spieler1Zeichen = spieler1Zeichen;
		this.spieler2Zeichen = spieler2Zeichen;
	}
	
}


