package qvhj_simulator;

public class Main {

	public static void main(String[] args) {
		
		for(int i = 0; i<1000; i++){
			Player a = new Player("Jeroen");
			Player b = new Player("Tom");
			b.smartness = 20;
			Player c = new Player("Elke");
			Player d = new Player("Katrijn");
			Quiz q = new Quiz(a, b, c, d);
			q.play();
			q.printCSV();
		}
	}
}
