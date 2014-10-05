package qvhj_simulator;

import java.util.Random;

public class Team {
	public Player p1;
	public Player p2;
	
	private Random random_generator = new Random();
	
	public Team(Player p1, Player p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	// Play a question
	public boolean playQuestion(int points){
		// try to answer question - fail if smartness not large enough
		if(random_generator.nextInt(100) < this.getAverageTeamSmartness()){
			this.addPoints(points);
			return true;
		}
		return false;
	}
	
	// Reward team players with points
	public void addPoints(int points){
		p1.addPoints(points);
		p2.addPoints(points);
	}
	
	// Calculate team smartness as rounded average of player smartness
	private int getAverageTeamSmartness(){
		double tsmartness = ((double)(this.p1.smartness + this.p2.smartness)) / 2.0;
		return Integer.valueOf((int) Math.round(tsmartness));
	}

}