package qvhj_simulator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Main class to run some quizzes and collect results
 * 
 * @author JEROEN BAERT
 *
 */
public class Main {

	public static final int HOW_MANY_SIMS = 100; // how many simulations do you want to run?
	
	public static void main(String[] args) {

		// statistics
		ArrayList<ArrayList<Player>> winning_players = new ArrayList<ArrayList<Player>>();
		ArrayList<Integer> scores_0 = new ArrayList<Integer>();
		ArrayList<Integer> scores_1 = new ArrayList<Integer>();
		ArrayList<Integer> scores_2 = new ArrayList<Integer>();
		ArrayList<Integer> scores_3 = new ArrayList<Integer>();
		int[] exaequos_first = new int[4];
		int[] exaequos_last = new int[4];

		for(int i = 0; i<HOW_MANY_SIMS; i++){
			
			// CREATE PLAYERS AND QUIZ
			Player a = new Player("Jeroen");
			Player b = new Player("Katrijn");
			Player c = new Player("Elke");
			Player d = new Player("Vincent");
			Quiz_Final q = new Quiz_Final(a, b, c, d);
			
			// PlAY QUIZ
			q.play();
			q.printCSV();

			// COLLECT QUIZ RESULTS
			// Get winning players
			ArrayList<Player> sorted_players = q.getSortedPlayers();
			winning_players.add(sorted_players);
			// Get final points
			scores_0.add(sorted_players.get(0).points);
			scores_1.add(sorted_players.get(1).points);
			scores_2.add(sorted_players.get(2).points);
			scores_3.add(sorted_players.get(3).points);
			// Check for ex aequos after each round
			if(q.exaequo_last[0]){exaequos_last[0]++;}
			if(q.exaequo_first[0]){exaequos_first[0]++;}
			if(q.exaequo_last[1]){exaequos_last[1]++;}
			if(q.exaequo_first[1]){exaequos_first[1]++;}
			if(q.exaequo_last[2]){exaequos_last[2]++;}
			if(q.exaequo_first[2]){exaequos_first[2]++;}
			if(q.exaequo_last[3]){exaequos_last[3]++;}
			if(q.exaequo_first[3]){exaequos_first[3]++;}
		}
		
		// CALCULATE TEST SET STATISTICS (over HOW_MANY_SIMS)
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
		double vco_0 = std_0 / avg_0;
		double vco_1 = std_1 / avg_1;
		double vco_2 = std_2 / avg_2;
		double vco_3 = std_3 / avg_3;
		double ex_0_last = (exaequos_last[0] / (double) HOW_MANY_SIMS) * 100;
		double ex_1_last = (exaequos_last[1] / (double) HOW_MANY_SIMS) * 100;
		double ex_2_last = (exaequos_last[2] / (double) HOW_MANY_SIMS) * 100;
		double ex_3_last = (exaequos_last[3] / (double) HOW_MANY_SIMS) * 100;
		double ex_0_first = (exaequos_first[0] / (double) HOW_MANY_SIMS) * 100;
		double ex_1_first = (exaequos_first[1] / (double) HOW_MANY_SIMS) * 100;
		double ex_2_first = (exaequos_first[2] / (double) HOW_MANY_SIMS) * 100;
		double ex_3_first = (exaequos_first[3] / (double) HOW_MANY_SIMS) * 100;
		
		// PRINT STATISTICS
		System.out.println("Ran " + HOW_MANY_SIMS + " simulations.");
		System.out.println("");
		System.out.println("Averages: " + avg_0 + " | " + avg_1 + " | " + avg_2 + " | " + avg_3);
		System.out.println("Std. Dev: " + std_0 + " | " + std_1 + " | " + std_2 + " | " + std_3);
		System.out.println("Var. Coe: " + vco_0 + " | " + vco_1 + " | " + vco_2 + " | " + vco_3);
		System.out.println("Minimum : " + min_0 + " | " + min_1 + " | " + min_2 + " | " + min_3);
		System.out.println("Maximum : " + max_0 + " | " + max_1 + " | " + max_2 + " | " + max_3);
		System.out.println("Number of ex aequos last place: " + ex_0_last + " | " + ex_1_last + " | " + ex_2_last + " | " + ex_3_last);
		System.out.println("Number of ex aequos first place: " + ex_0_first + " | " + ex_1_first + " | " + ex_2_first + " | " + ex_3_first);
	}
}
