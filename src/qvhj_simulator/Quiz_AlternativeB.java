package qvhj_simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Quiz_AlternativeB {
	
	// CONFIGURE ROUNDS AND MODEL ASSUMPTIONS HERE
	// Round 1
	public static final int R1_POINTS_START = 50; // start points for every player
	public static final int R1_POINTS = 20; // reward for getting question right
	public static final int R1_POINTS_FAULT = 10; // penalty for getting question wrong
	// Round 2
	public static final int R2_N_PER_TEAM = 2; // how many picture questions per team
	public static final int R2_POINTS_FIRST = 20; // reward for getting first question right
	public static final int R2_POINTS_SECOND = 40; // reward for getting second question right
	// Round 3
	public static final int R3_N_PER_TEAM = 2; // how many headlines per team
	public static final int R3_N_WORDS = 5; // how many words per headline to guess
	public static final int R3_POINTS = 10; // how many points per headline
	// ROUND 4
	public static final int R4_ASSUME_BAGGED = 3; // how many items do we assume players definitely will get
	public static final int R4_POINTS_STEP = 20; // steps for points in round 4
	
	public Player p0;
	public Player p1;
	public Player p2;
	public Player p3;

	private Team t0;
	private Team t1;
	
	public boolean[] exaequo_last = new boolean[4];
	public boolean[] exaequo_first = new boolean[4];
	
	private Random random_generator = new Random();
	public Quiz_AlternativeB(Player p0, Player p1, Player p2, Player p3){
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
		exaequo_first[0] = checkForExAequoFirst();
		exaequo_last[0] = checkForExAequoLast();
		generateTeams();
		
		
		playRoundTwo();
		exaequo_first[1] = checkForExAequoFirst();
		exaequo_last[1] = checkForExAequoLast();
		generateTeams();
		
		
		playRoundThree();
		exaequo_first[2] = checkForExAequoFirst();
		exaequo_last[2] = checkForExAequoLast();
		generateTeams();
		
		playRoundFour();
		exaequo_first[3] = checkForExAequoFirst();
		exaequo_last[3] = checkForExAequoLast();
	}

	/**
	 * ROUND 1
	 * No teams are formed. Every player gets introduced, and the other players get a question about each player.
	 */
	private void playRoundOne(){
		// SET ALL PLAYERS POINTS TO R1_POINTS_START
		this.p0.points = R1_POINTS_START;
		this.p1.points = R1_POINTS_START;
		this.p2.points = R1_POINTS_START;
		this.p3.points = R1_POINTS_START;
		// questions about p0
		this.p1.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p2.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p3.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		// questions about p1
		this.p2.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p3.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p0.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		// questions about p2
		this.p3.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p0.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p1.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		// questions about p3
		this.p0.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p1.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p2.playQuestion(R1_POINTS,R1_POINTS_FAULT);
	}

	/**
	 * ROUND 2
	 * 4 sets of 2 questions are asked, 1 or 2 points per question respectively. When question fails, it goes to other team.
	 */
	private void playRoundTwo(){
		for(int i = 0; i<R2_N_PER_TEAM; i++){
		playRoundTwoQuestionSet(t1, t0);
		playRoundTwoQuestionSet(t0, t1);
		}
	}

	/** ROUND 3
	 * Each team can win 10 points by guessing paper headlines - if they don't get anything, other team gets a try
	 */
	private void playRoundThree(){
		
		for(int i = 0; i < R3_N_PER_TEAM; i++){
			// T1 guessround
			int guess = random_generator.nextInt(R3_N_WORDS);
			t1.addPoints(guess*R3_POINTS);
			// T0 guessround
			guess = random_generator.nextInt(R3_N_WORDS);
			t0.addPoints(guess*R3_POINTS);		
		}
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
				if(! t1.playQuestion(i*R4_POINTS_STEP, i*5)){ // ask t1 a question
					t0.playQuestion(i*R4_POINTS_STEP, i*5); // if fail, ask t0 a question
				}
			}
			// Team 0
			if(t0_bag >= i) {// if t1 has enough questions in the bag
				if(! t0.playQuestion(i*R4_POINTS_STEP, i*5)){ // ask t1 a question
					t1.playQuestion(i*R4_POINTS_STEP, i*5); // if fail, ask t0 a question
				}
			}
		}
	}

	/**
	 * Play a question set of round 2 (one question for 1 point, one question for 2 points) - if team A answers wrong, question goes to other team
	 */
	private void playRoundTwoQuestionSet(Team A, Team B){
		// Team A plays a question set
		if(! A.playQuestion(R2_POINTS_FIRST)){ // if they fail
			B.playQuestion(R2_POINTS_FIRST); // Team B gets a try
		}
		if(! A.playQuestion(R2_POINTS_SECOND)){
			B.playQuestion(R2_POINTS_SECOND);
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
	 * Generate teams - t1 will always contain the team with the player with the lowest score
	 */
	private void generateTeamsNotFirstRound(){
		// get list of players sorted by score
		ArrayList<Player> sorted_players = this.getSortedPlayers();
		Player lastplayer = sorted_players.get(3); // get player in last place

		Player forbidden = t0.p1; // player which last player cannot teamup with
		Team otherteam = t1; // the other team
		
		// In which team is last player?
		// He's in T0
		if(t0.p0 == lastplayer){
			forbidden = t0.p1;
			otherteam = t1;
		} else if (t0.p1 == lastplayer){
			forbidden = t0.p0;
			otherteam = t1;
		// He's in T1
		} else if (t1.p0 == lastplayer){
			forbidden = t1.p1;
			otherteam = t0;
		} else if (t1.p1 == lastplayer){
			forbidden = t1.p0;
			otherteam = t0;
		}
		
		int partner = random_generator.nextInt(2);
		if(partner == 0){
			t1 = new Team(lastplayer, otherteam.p0);
			t0 = new Team(forbidden, otherteam.p1);
		}
		if(partner == 1){
			t1 = new Team(lastplayer, otherteam.p1);
			t0 = new Team(forbidden, otherteam.p0);
		}
	}
	
	private boolean checkForExAequoFirst(){
		ArrayList<Player> players = getSortedPlayers();
		if(players.get(0).points == players.get(1).points){
			return true;
		}
		return false;
	}
	
	private boolean checkForExAequoLast(){
		ArrayList<Player> players = getSortedPlayers();
		if(players.get(3).points == players.get(2).points){
			return true;
		}
		return false;
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

