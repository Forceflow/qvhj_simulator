package qvhj_simulator;
import java.util.Random;

public class Player implements Comparable<Player>{
	public int points = 0; // current player score
	public int smartness = 50; // smartness, out of 100
	public String name = "Player";
	
	private Random random_generator = new Random();
	
	public Player(String name){
		this.name = name;
	}
	
	public boolean playQuestion(int points){
		// try to answer question - fail if smartness not large enough
		if(random_generator.nextInt(100) < this.smartness){
			this.addPoints(points);
			return true;
		}
		return false;
		
	}
	
	public void addPoints(int points){
		this.points += points;
	}
	
	public int getPoints(){
		return this.points;
	}

	@Override
	public int compareTo(Player o) {
		if(o.points > this.points){
			return -1;
		} else if (o.points < this.points) {
			return 1;
		}
		return 0;
	}
}
