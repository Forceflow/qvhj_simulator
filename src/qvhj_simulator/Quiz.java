package qvhj_simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Quiz {
	public Player p0;
	public Player p1;
	public Player p2;
	public Player p3;
	
	private Team t0;
	private Team t1;
	
	private Random random_generator = new Random();
	
	public Quiz(Player p0, Player p1, Player p2, Player p3){
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
		//printScores();
		generateTeams();
		playRoundTwo();
		//printScores();
		generateTeams();
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
	 * 4 sets of 2 questions are asked, 1 or 2 points per question respectively. When question fails, it goes to other team.
	 */
	private void playRoundTwo(){
		playRoundTwoQuestionSet(t1, t0);
		playRoundTwoQuestionSet(t0, t1);
		playRoundTwoQuestionSet(t1, t0);
		playRoundTwoQuestionSet(t0, t1);
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
	 * Generate teams - t1 will always be the team with the player with the lowest score
	 */
	private void generateTeams(){
		// get list of players sorted by score
		ArrayList<Player> sorted_players = this.getScores();
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
			this.t0 = new Team(sorted_players.get(0), sorted_players.get(3));
		} else if (partner == 2) {
			// last player teams up with player in third place
			this.t1 = new Team(lastplayer, sorted_players.get(2));
			this.t0 = new Team(sorted_players.get(0), sorted_players.get(1));
		}
	}
	
	public ArrayList<Player> getScores(){
		ArrayList<Player> scores = new ArrayList<Player>();
		scores.add(p0);
		scores.add(p1);
		scores.add(p2);
		scores.add(p3);
		Collections.sort(scores, Collections.reverseOrder());
		return scores;
	}
	
	public void printScores(){
		System.out.println(p0.getPoints() + " " + p1.getPoints() + " " + p2.getPoints() + " " + p3.getPoints());
	}

}
