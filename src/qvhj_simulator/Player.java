package qvhj_simulator;
import java.util.Random;

/**
 * This class represents a Player
 * @author Jeroen Baert
 *
 */
public class Player implements Comparable<Player>{
	public int points = 0; // current player score
	public int smartness = 50; // smartness, out of 100
	public String name = "Player"; // player name
	
	private Random random_generator = new Random(); // our very own rng
	
	public Player(String name){
		this.name = name;
	}
	
	/**
	 * Play a question
	 * @param points Points rewarded to player if answered correctly
	 * @param damage Points deducted when player answers incorrectly
	 * @return boolean: true of the question was answered correctly, false otherwise
	 */
	public boolean playQuestion(int points, int damage){
		// try to answer question - fail if smartness not large enough
		if(random_generator.nextInt(100) < this.smartness){
			this.addPoints(points);
			return true;
		}
		this.addPoints(-damage);
		return false;
		
	}
	
	/**
	 * Give a player points
	 * @param points
	 */
	public void addPoints(int points){
		this.points += points;
	}
	
	/**
	 * Get current player points
	 * @return
	 */
	public int getPoints(){
		return this.points;
	}

	/**
	 * Overriding compare function: A > B if Player A has more or equal points than B
	 */
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
