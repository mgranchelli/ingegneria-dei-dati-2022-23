package csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import models.AziendaRecord;

public class CSVReader {
	
	public String[] headerCSV = null;

	public List<AziendaRecord> readAziende(String fileName) {
		List<AziendaRecord> aziende = new ArrayList<>();
		Path pathToFile = Paths.get(fileName);
		boolean firstLine = true;
		
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			while (line != null) {
				String[] attributes = line.split(",");
//				System.out.println(line);
				if (firstLine) {
					headerCSV = attributes;
					line = br.readLine();
				}
				else {
					AziendaRecord azienda = createAzienda(attributes);
//					System.out.println(azienda.toString());
					aziende.add(azienda);
					line = br.readLine();
				}
				firstLine = false;
			}
			
			
		} catch (IOException ioe) {
			System.out.println("Last line!\n");
//			ioe.printStackTrace();
		}

		return aziende;

	}
	
	private static AziendaRecord createAzienda(String[] metadata) { 
		
		String[] input = new String[10];
		
		for (int i = 0; i < metadata.length; i++) {
			if (metadata[i] != "") {
				input[i] = metadata[i];
			}
			else {
				input[i] = null;
			}
		}
		
		if (metadata.length < 10) {
			for (int i=metadata.length; i < 10; i++) {
				input[i] = null;
			}
		}
		
		String id = input[0];
		String name = input[1];
		String country = input[2];
		String sector = input[3];
		String founded = input[4];
		String marketcap = input[5];
		String revenue = input[6];
		String employees = input[7];
		String links = input[8];
		String ceo = input[9];
		
		return new AziendaRecord(id, name, country, sector, founded, marketcap, revenue, employees, links, ceo); 
	} 

}
