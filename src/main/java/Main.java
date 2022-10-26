import java.io.BufferedReader;
import java.io.InputStreamReader;

import statistics.Statistics;

public class Main {

	public static void main(String[] args) throws Exception {
		
		menu();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Reading data using readLine
		String str = br.readLine();

		while (!str.equals("0")) {

			if (str.equals("1")) {
				Statistics statistics = new Statistics();
				statistics.getStatistics();
				menu();

			} else if (str.equals("2")) {
				
				menu();

			} else if (str.equals("3")) {
				
				menu();
			}

			else {
				System.out.println("\nInvalid choice!");
				menu();
			}
			str = br.readLine();
		}
		System.out.println("Closing...");
	}
	
	private static void menu() {
		System.out.println("\nChoose from these choices");
		System.out.println("-------------------------");
		System.out.println("0 - Quit");
		System.out.println("1 - Statistics");
		System.out.println("2 - ...");
		System.out.println("3 - ...");
		System.out.println("-------------------------");
	}

}
