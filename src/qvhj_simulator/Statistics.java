package qvhj_simulator;

import java.util.ArrayList;

public class Statistics {
	public static double average(ArrayList<Integer> array){
		int sum = 0;
		for (Integer i : array){
			sum += i;
		}
		double average = ((double) sum) / ((double) array.size());
		return average;
	}
	
	public static double variance(double average, ArrayList<Integer> array){
		double variance = 0;
		for (Integer i : array){
			variance += (i*i);
		}
		return (variance / (double) array.size()) - (average*average);
	}
	
	public static double standard_deviance(double average, ArrayList<Integer> array){
		return Math.sqrt(variance(average,array));
	}

}
