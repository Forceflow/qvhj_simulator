package qvhj_simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * QVHJ Latest Model
 * @author Jeroen Baert
 */
public class Quiz_AlternativeB {
	
	// CONFIGURE ROUNDS AND MODEL ASSUMPTIONS HERE
	
	// Round 1
	public static final int R1_POINTS = 20; // reward for getting question right in round 1
	public static final int R1_POINTS_FAULT = 0; // punishment for getting question wrong in round 2
	// Round 2
	public static final int R2_N_PER_TEAM = 2; // how many questions per team in round 2
	public static final int R2_POINTS_FIRST = 20; // reward for getting first question right in round 2
	public static final int R2_POINTS_SECOND = 40; // reward for getting second question right in round 2
	// Round 3
	public static final int R3_N_PER_TEAM = 8; // how many headlines per team in round 3
	public static final int R3_N_WORDS = 1; // how many words per headline to guess in round 3
	public static final int R3_POINTS = 10; // how many points per headline in round 3
	// ROUND 4
	public static final int R4_BAGGABLE = 8; // how many questions you can "bag" in round 4
	public static final int R4_ASSUME_BAGGED = 3; // how many questions do we assume teams will definitely get in round 4
	public static final int R4_POINTS_STEP = 20; // every question round, points increase by this amount in round 4
	
	// Quiz players
	public Player p0;
	public Player p1;
	public Player p2;
	public Player p3;

	// Teams (these change during the quiz)
	private Team t0;
	private Team t1;
	
	// Recording the amount of ex aequos
	public boolean[] exaequo_last = new boolean[4];
	public boolean[] exaequo_first = new boolean[4];
	
	// Our very own random number generator
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
		pubVote();
		
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
	 * ROUND 0
	 * The pubvote round distributes 1,2,3 or 4 points randomly over the players.
	 * This is actually disguised score jittering at the start which prevents ex aequos.
	 * 
	 * As long as all subsequent points are multiples of 5, it is mathematically guaranteed
	 * that no one can reach the same score.
	 */
	private void pubVote(){
		ArrayList<Integer> eurootjes = new ArrayList<Integer>();
		eurootjes.add(1);
		eurootjes.add(2);
		eurootjes.add(3);
		eurootjes.add(4);
		Collections.shuffle(eurootjes);
		p0.addPoints(eurootjes.get(0));
		p1.addPoints(eurootjes.get(1));
		p2.addPoints(eurootjes.get(2));
		p3.addPoints(eurootjes.get(3));
	}

	/**
	 * ROUND 1
	 * No teams are formed. Every player gets introduced, and the other players 
	 * get a multiple choice question about each player.
	 * Everybody answers simultanuously using a button push.
	 */
	private void playRoundOne(){
		// question about p0
		this.p1.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p2.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p3.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		
		// question about p1
		this.p2.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p3.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p0.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		
		// question about p2
		this.p3.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p0.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p1.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		
		// question about p3
		this.p0.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p1.playQuestion(R1_POINTS,R1_POINTS_FAULT);
		this.p2.playQuestion(R1_POINTS,R1_POINTS_FAULT);
	}

	/**
	 * ROUND 2
	 * R2_N_PER_TEAM * 2 sets of 2 questions are asked, the first for R2_POINTS_FIRST points,
	 * the second one for R2_POINTS_SECOND points.
	 * 
	 * When question fails, it goes to other team.
	 */
	private void playRoundTwo(){
		for(int i = 0; i<R2_N_PER_TEAM; i++){
			// t1 plays a question set
			if(! t1.playQuestion(R2_POINTS_FIRST)){ // if they fail
				t0.playQuestion(R2_POINTS_FIRST); // other team gets a try
			}
			if(! t1.playQuestion(R2_POINTS_SECOND)){
				t0.playQuestion(R2_POINTS_SECOND);
			}
			// t0 plays a question set
			if(! t0.playQuestion(R2_POINTS_FIRST)){ // if they fail
				t1.playQuestion(R2_POINTS_FIRST); // other team gets a try
			}
			if(! t0.playQuestion(R2_POINTS_SECOND)){
				t1.playQuestion(R2_POINTS_SECOND);
			}
		}
	}

	/** 
	 * ROUND 3
	 * Each team can win R3_POINTS points by guessing a word in a paper headline.
	 * Every team gets R3_N_PER_TEAM headlines to guess a word in. In case of failure, it doesn't
	 * go to the other team. Quick-fire round.
	 */
	private void playRoundThree(){
		
		for(int i = 0; i < R3_N_PER_TEAM; i++){
			// T1 guessround
			int guess = random_generator.nextInt(R3_N_WORDS+1);
			t1.addPoints(guess*R3_POINTS);
			// T0 guessround
			guess = random_generator.nextInt(R3_N_WORDS+1);
			t0.addPoints(guess*R3_POINTS);		
		}
	}

	/**
	 * ROUND 4
	 * 
	 * Teams can "bag" a topics, from 0 to R4_BAGGABLE
	 * Every question rounds, points get incremented.
	 * In case of question fail, the question goes to the other team.
	 * 
	 * In this model, we estimate that every team bags R4_ASSUME_BAGGED
	 * In this model, every round gives a +5% smartness boost to the teams, 
	 * since the last question is about topics they claim to be more knowledgable of.
	 */
	private void playRoundFour(){
		// How many topics bagged?
		int t0_bag = R4_ASSUME_BAGGED;
		int t1_bag = R4_ASSUME_BAGGED;
		
		int to_verdeel = R4_BAGGABLE - (2*R4_ASSUME_BAGGED);
		// how many of remaining points did t0 bag?
		int t0_extra_bag = random_generator.nextInt(to_verdeel+1);
		int t1_extra_bag = random_generator.nextInt((to_verdeel - t0_extra_bag) + 1);
		
		t0_bag += t0_extra_bag;
		t1_bag += t1_extra_bag;
		
		// play question rounds for incrementing point value
		int i_step = 1;
		while((t0_bag > 0) || (t1_bag > 0)){		
			// Team 1
			if(t1_bag > 0) {// if t1 has enough questions in the bag
				if(! t1.playQuestion(i_step*R4_POINTS_STEP, i_step*5)){ // ask t1 a question
					t0.playQuestion(i_step*R4_POINTS_STEP, i_step*5); // if fail, ask t0 a question
				}
				t1_bag--;
			}
			// Team 0
			if(t0_bag > 0) {// if t1 has enough questions in the bag
				if(! t0.playQuestion(i_step*R4_POINTS_STEP, i_step*5)){ // ask t1 a question
					t1.playQuestion(i_step*R4_POINTS_STEP, i_step*5); // if fail, ask t0 a question
				}
				t0_bag--;
			}
			i_step++;
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
	
	/**
	 * Check for an ex aequo for first place
	 * @return true if an ex aquo was detected
	 */
	private boolean checkForExAequoFirst(){
		ArrayList<Player> players = getSortedPlayers();
		if(players.get(0).points == players.get(1).points){
			return true;
		}
		return false;
	}
	
	/**
	 * Check for an ex aequo in last place
	 * @return true if an ex aequo was detected
	 */
	private boolean checkForExAequoLast(){
		ArrayList<Player> players = getSortedPlayers();
		if(players.get(3).points == players.get(2).points){
			return true;
		}
		return false;
	}

	/**
	 * Get a list of players, sorted by points in decreasing order
	 * @return An ArrayList of sorted Player objects
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

	/**
	 * Print current player points in cleartext, from player p0 to p3 
	 */
	public void printScores(){
		System.out.println(p0.getPoints() + " " + p1.getPoints() + " " + p2.getPoints() + " " + p3.getPoints());
	}

	/**
	 * Print current player points in CSV, from highest to lowest score
	 */
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

