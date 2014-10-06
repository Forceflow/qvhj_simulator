package qvhj_simulator;

import java.util.Random;

public class Team {
	public Player p0;
	public Player p1;
	
	private Random random_generator = new Random();
	
	public Team(Player p0, Player p1){
		this.p0 = p0;
		this.p1 = p1;
	}
	
	// Play a question
	public boolean playQuestion(int points, int smartness_boost){
		// try to answer question - fail if smartness not large enough
		if(random_generator.nextInt(100) < (this.getTeamSmartness() + smartness_boost)){
			this.addPoints(points);
			return true;
		}
		return false;
	}
	
	public boolean playQuestion(int points){
		return playQuestion(points, 0);
	}
	
	// Reward team players with points
	public void addPoints(int points){
		p0.addPoints(points);
		p1.addPoints(points);
	}
	
	// Get team smartness
	private int getTeamSmartness(){
//		double tsmartness = ((double)(this.p0.smartness + this.p1.smartness)) / 2.0;
//		return Integer.valueOf((int) Math.round(tsmartness));
		return Math.max(p0.smartness, p1.smartness);
	}

}