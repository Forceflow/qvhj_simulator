package qvhj_simulator;

public class Main {

	public static void main(String[] args) {
		Player a = new Player("Jeroen");
		Player b = new Player("Tom");
		Player c = new Player("Elke");
		Player d = new Player("Katrijn");
		
		Quiz q = new Quiz(a, b, c, d);
		q.play();
	}
}
