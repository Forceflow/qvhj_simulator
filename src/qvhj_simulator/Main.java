package qvhj_simulator;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

	public static final int HOW_MANY_SIMS = 5000;
	public static void main(String[] args) {

		// statistics
		ArrayList<Integer> scores_0 = new ArrayList<Integer>();
		ArrayList<Integer> scores_1 = new ArrayList<Integer>();
		ArrayList<Integer> scores_2 = new ArrayList<Integer>();
		ArrayList<Integer> scores_3 = new ArrayList<Integer>();

		for(int i = 0; i<HOW_MANY_SIMS; i++){
			
			// create players
			Player a = new Player("Jeroen");
			Player b = new Player("Tom");
			Player c = new Player("Elke");
			Player d = new Player("Katrijn");
			
			// Play quiz
			Quiz_Basic q = new Quiz_Basic(a, b, c, d);
			q.play();
			//q.printCSV();

			// collect quiz results
			ArrayList<Player> sorted_players = q.getSortedPlayers();
			scores_0.add(sorted_players.get(0).points);
			scores_1.add(sorted_players.get(1).points);
			scores_2.add(sorted_players.get(2).points);
			scores_3.add(sorted_players.get(3).points);
		}
		
		double avg_0 = Statistics.average(scores_0);
		double avg_1 = Statistics.average(scores_1);
		double avg_2 = Statistics.average(scores_2);
		double avg_3 = Statistics.average(scores_3);
		double std_0 = Statistics.standard_deviance(avg_0, scores_0);
		double std_1 = Statistics.standard_deviance(avg_1, scores_1);
		double std_2 = Statistics.standard_deviance(avg_2, scores_2);
		double std_3 = Statistics.standard_deviance(avg_3, scores_3);
		int min_0 = Collections.min(scores_0);
		int min_1 = Collections.min(scores_1);
		int min_2 = Collections.min(scores_2);
		int min_3 = Collections.min(scores_3);
		int max_0 = Collections.max(scores_0);
		int max_1 = Collections.max(scores_1);
		int max_2 = Collections.max(scores_2);
		int max_3 = Collections.max(scores_3);
		
		System.out.println("Averages: " + avg_0 + " | " + avg_1 + " | " + avg_2 + " | " + avg_3);
		System.out.println("Std. Dev: " + std_0 + " | " + std_1 + " | " + std_2 + " | " + std_3);
		System.out.println("Minimum : " + min_0 + " | " + min_1 + " | " + min_2 + " | " + min_3);
		System.out.println("Maximum : " + max_0 + " | " + max_1 + " | " + max_2 + " | " + max_3);
	}
}
