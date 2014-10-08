package qvhj_simulator;

import java.util.ArrayList;

public class Main {

	public static final int HOW_MANY_SIMS = 1000;
	public static void main(String[] args) {
		
		// statistics
		int avg_0;
		int avg_1;
		int avg_2;
		int avg_3;
		
		for(int i = 0; i<HOW_MANY_SIMS; i++){
			Player a = new Player("Jeroen");
			Player b = new Player("Tom");
			Player c = new Player("Elke");
			Player d = new Player("Katrijn");
			Quiz_Basic q = new Quiz_Basic(a, b, c, d);
			q.play();
			q.printCSV();
			
			// getstats
			//ArrayList<Player> sorted_players = q.getSortedPlayers()
		}
	}
}
