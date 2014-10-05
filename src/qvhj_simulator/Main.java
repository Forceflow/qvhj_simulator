package qvhj_simulator;

public class Main {

	public static void main(String[] args) {
		Player a = new Player("Jeroen");
		a.smartness = 85;
		Player b = new Player("Elke");
		b.smartness = 32;
		Team t = new Team(a,b);
		t.playQuestion(2);
		t.playQuestion(5);

	}

}
