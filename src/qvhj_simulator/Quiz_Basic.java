package qvhj_simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Quiz_Basic {
	public Player p0;
	public Player p1;
	public Player p2;
	public Player p3;
	
	private Team t0;
	private Team t1;
	
	private Random random_generator = new Random();
	//private final static Logger LOGGER = Logger.getLogger(Quiz.class.getName());
	
	public Quiz_Basic(Player p0, Player p1, Player p2, Player p3){
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	/**
	 * Play a full quiz
	 */
	public void play(){
		playRoundOne();
		generateTeams();
		playRoundTwo();
		generateTeams();
		playRoundThree();
		generateTeams();
		playRoundFour();
	}
	
	/**
	 * ROUND 1
	 * No teams are formed. Every player gets introduced, and the other players get a question about each player.
	 */
	private void playRoundOne(){
		// questions about p0
		this.p1.playQuestion(1);
		this.p2.playQuestion(1);
		this.p3.playQuestion(1);
		// questions about p1
		this.p2.playQuestion(1);
		this.p3.playQuestion(1);
		this.p0.playQuestion(1);
		// questions about p2
		this.p3.playQuestion(1);
		this.p0.playQuestion(1);
		this.p1.playQuestion(1);
		// questions about p3
		this.p0.playQuestion(1);
		this.p1.playQuestion(1);
		this.p2.playQuestion(1);
	}
	
	/**
	 * ROUND 2
	 * 6 sets of 2 questions are asked, 1 or 2 points per question respectively. When question fails, it goes to other team.
	 */
	private void playRoundTwo(){
		playRoundTwoQuestionSet(t1, t0);
		playRoundTwoQuestionSet(t0, t1);
		playRoundTwoQuestionSet(t1, t0);
		playRoundTwoQuestionSet(t0, t1);
		playRoundTwoQuestionSet(t1, t0);
		playRoundTwoQuestionSet(t0, t1);
	}
	
	/** ROUND 3
	 * Each team can win 10 points by guessing paper headlines - if they don't get anything, other team gets a try
	 * 
	 * In this model, each team gets 5 guesses correct every time, the rest is random
	 */
	private void playRoundThree(){
		// T1 guessround
		int guess = random_generator.nextInt(5);
		int guess_rest = random_generator.nextInt(5-guess);
		t1.addPoints(5+guess);
		t0.addPoints(guess_rest);
		
		// T0 guessround
		guess = random_generator.nextInt(5);
		guess_rest = random_generator.nextInt(5-guess);
		t0.addPoints(5+guess);
		t1.addPoints(guess_rest);
	}
	
	/**
	 * ROUND 4
	 * Teams can "bag" a topics, from 0 to 5 items.
	 * Every question rounds, points get incremented : first round for 1 point, second for 2 points, etc ...
	 * In case of question fail, the question goes to the other team.
	 * 
	 * In this model, we estimate that every team bags 3 - 5 topics
	 * In this model, every round gives a +5% smartness boost to the teams, since the last question is about topics they know more of
	 */
	private void playRoundFour(){
		// How many topics bagged?
		int t0_bag = 3 + random_generator.nextInt(3);
		int t1_bag = 3 + random_generator.nextInt(3);
		
		// play five question rounds for incrementing point value
		for(int i = 1; i<=5; i++){		
			// Team 1
			if(t1_bag >= i) {// if t1 has enough questions in the bag
				if(! t1.playQuestion(i, i*5)){ // ask t1 a question
					t0.playQuestion(i, i*5); // if fail, ask t0 a question
				}
			}
			// Team 0
			if(t0_bag >= i) {// if t1 has enough questions in the bag
				if(! t0.playQuestion(i, i*5)){ // ask t1 a question
					t1.playQuestion(i, i*5); // if fail, ask t0 a question
				}
			}
		}
	}
	
	/**
	 * Play a question set of round 2 (one question for 1 point, one question for 2 points) - if team A answers wrong, question goes to other team
	 */
	private void playRoundTwoQuestionSet(Team A, Team B){
		// Team A plays a question set
		if(! A.playQuestion(1)){ // if they fail
			B.playQuestion(1); // Team B gets a try
		}
		if(! A.playQuestion(2)){
			B.playQuestion(2);
		}
	}
	
	/**
	 * Generate teams - t1 will always contain the team with the player with the lowest score
	 */
	private void generateTeams(){
		// get list of players sorted by score
		ArrayList<Player> sorted_players = this.getSortedPlayers();
		Player lastplayer = sorted_players.get(3); // get player in last place
		
		// last player randomly picks partner
		int partner = random_generator.nextInt(3);
		
		if(partner == 0){ 
			// last player teams up with player in first place
			this.t1 = new Team(lastplayer, sorted_players.get(0));
			this.t0 = new Team(sorted_players.get(1), sorted_players.get(2));
		} else if (partner == 1) {
			// last player teams up with player in second place
			this.t1 = new Team(lastplayer, sorted_players.get(1));
			this.t0 = new Team(sorted_players.get(0), sorted_players.get(2));
		} else if (partner == 2) {
			// last player teams up with player in third place
			this.t1 = new Team(lastplayer, sorted_players.get(2));
			this.t0 = new Team(sorted_players.get(0), sorted_players.get(1));
		}
	}
	
	/**
	 * Get a list of players, sorted by points in decreasing order
	 */
	public ArrayList<Player> getSortedPlayers(){
		ArrayList<Player> sorted_players = new ArrayList<Player>();
		sorted_players.add(p0);
		sorted_players.add(p1);
		sorted_players.add(p2);
		sorted_players.add(p3);
		Collections.sort(sorted_players, Collections.reverseOrder());
		return sorted_players;
	}
	
	public void printScores(){
		System.out.println(p0.getPoints() + " " + p1.getPoints() + " " + p2.getPoints() + " " + p3.getPoints());
	}
	
	public void printCSV(){
		ArrayList<Player> players = getSortedPlayers();
		String csv = "";
		for(int i = 0; i < 4; i++){
			csv+= players.get(i).getPoints();
			if(i != 3){
				csv+=",";
			}
		}
		System.out.println(csv);
	}

}
