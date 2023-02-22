import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.queryparser.classic.ParseException;

import csv.CSVReader;
import csv.CSVWriter;
import lucene.IndexLoader;
import lucene.Indexer;
import lucene.MergeList;
import models.AziendaRecord;
import statistics.Statistics;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {

		menu();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		IndexLoader indexLoader = null;

		// Reading data using readLine
		String str = br.readLine();

		while (!str.equals("0")) {

			if (str.equals("1")) {
				Statistics statistics = new Statistics();
				statistics.getStatistics();

				System.out.println("\nTask completed!\n");
				menu();

			} else if (str.equals("2")) {
				Statistics statistics = new Statistics();
				statistics.getStatisticsAndCharts();

				System.out.println("\nTask completed!\n");
				menu();

			} else if (str.equals("3")) {
				new Indexer();
				System.out.println("Optimizing index...");
				indexLoader = new IndexLoader();

				System.out.println("\nTask completed!\n");
				menu();

			} else if (str.equals("4")) {

				if (indexLoader == null) {

					try {
						System.out.println("Optimizing index...");
						indexLoader = new IndexLoader();
					} catch (IndexNotFoundException e) {
						System.out.println("\nFirst create index!");
					}
				}

				if (indexLoader != null) {

					System.out.println("Type input string...");
					String input = br.readLine();

					System.out.println("Type a K ...");
					String K = br.readLine();
					try {
						Integer.parseInt(K);

						MergeList ml = new MergeList(indexLoader.getIndexSearcher());
						ml.run(input, Integer.parseInt(K));

						System.out.println("\nTask completed!\n");
					} catch (NumberFormatException e) {
						System.out.println("Invalid K!\n");
					}
				}

				menu();

			} else if (str.equals("5")) {
				
				if (indexLoader == null) {
					try {
						System.out.println("Optimizing index...");
						indexLoader = new IndexLoader();
					} catch (IndexNotFoundException e) {
						System.out.println("\nFirst create index!");
					}
				}
				
				if (indexLoader != null) {
					
					long start = System.currentTimeMillis();
					
					CSVReader csv = new CSVReader();
					List<AziendaRecord> aziende = csv.readAziende("./inputFile/2. linked_dataset.csv");
					
					MergeList ml = new MergeList(indexLoader.getIndexSearcher());
					for (AziendaRecord azienda : aziende) {
						Integer idLinkDB = ml.searchAzienda(azienda.getName()); 
						azienda.setIdDocDB(idLinkDB);
//						System.out.println(azienda.toString());
					}
					
					CSVWriter w = new CSVWriter("./outputFile/3. integrated_dataset.csv", csv.headerCSV);
					w.writeCSV(aziende);
					
					long end = System.currentTimeMillis();
					System.out.println("Total Time Taken: " + (end - start) / 1000F + " seconds");
				}	


				System.out.println("\nTask completed!\n");
				menu();

			} else {
				System.out.println("\nInvalid choice!");
				menu();
			}
			str = br.readLine();
		}
		System.out.println("Closing...");
		System.exit(0);
	}

	private static void menu() {
		System.out.println("\nChoose from these choices");
		System.out.println("-------------------------");
		System.out.println("0 - Quit");
		System.out.println("1 - Statistics");
		System.out.println("2 - Statistics and charts");
		System.out.println("3 - Indexing");
		System.out.println("4 - MergeList - Top K overlap");
		System.out.println("5 - Integration companies dataset");
		System.out.println("-------------------------");
	}

}
