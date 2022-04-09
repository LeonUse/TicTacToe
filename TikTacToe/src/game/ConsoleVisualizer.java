package game;

public class ConsoleVisualizer {

	
	public static void zeichneSpielfeld(String[][] input) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				System.out.print(input[j][i] + "  ");
			}
			System.out.println();
		}
		
	}
}
