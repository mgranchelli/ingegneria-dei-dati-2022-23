package lucenex;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws Exception {

		System.out.println("Type close to exit or insert query: ");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Reading data using readLine
		String str = br.readLine();

		while (!str.equals("close")) {

			String[] result = str.split(" ", 2);
			String key = result[0];
			
			if ((result.length > 1) & (key.equals("nome") | key.equals("contenuto"))) {
				String textQuery = result[1];
				if (!textQuery.strip().equals("")) {
					QueryIndex qIndex = new QueryIndex(); 
					qIndex.generateQuery(key, textQuery);
					System.out.println("\nType close to exit or insert query: ");
				}
				else {
					System.out.println("Invalid term sequence!");
					System.out.println("Type close to exit or insert query: ");
				}
			}
			else {
				System.out.println("Query must start with keyword \"nome\" or \"contenuto!\" followed by a sequence of terms!\n");
				System.out.println("Type close to exit or insert query: ");
			}
			str = br.readLine();
		}
		System.out.println("Closing...");
	}

}
