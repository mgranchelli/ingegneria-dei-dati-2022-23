package statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Cell;
import models.Table;


public class Statistics {
	
	private int tablesNumber = 0;
	private int columns = 0;
	private int rows = 0;
	private int emptyCells = 0;
	private HashMap<Integer, Integer> rowsDistribution = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> columnsDistribution = new HashMap<Integer, Integer>();
	private HashMap<String, Integer> typeCellDistribution = new HashMap<String, Integer>();
	private HashMap<Integer, Set<String>> mapIntValues = new HashMap<>();
	private HashMap<Integer, Integer> distinctColumnsValuesDistribution = new HashMap<>();
	
	public void getStatistics() {
		String jsonFilePath = "./inputFile/tablesTest.json";
		long start = System.currentTimeMillis();
		parse(jsonFilePath);
		
		System.out.println("Tables number: " + tablesNumber);
		System.out.println("Avg rows: " + rows/tablesNumber);
		System.out.println("Avg columns: " + columns/tablesNumber);
		System.out.println("Avg null value per table: " + emptyCells/tablesNumber);
		System.out.println("Cell type distribution: " + typeCellDistribution);
		System.out.println("Row distribution: " + rowsDistribution);
		System.out.println("Columns distribution: " + columnsDistribution);
		System.out.println("Distinct columns list strings: " + mapIntValues);
		System.out.println("Distinct columns number values: " + distinctColumnsValuesDistribution);
        
        System.out.println("Total Time Taken : "+ (System.currentTimeMillis() - start)/1000 + " secs");
		
	}
	
	
	public void parse(String jsonFilePath){

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(jsonFilePath))) {
            MappingIterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);
            
            value.forEachRemaining((u) -> {
            	// Numero di tabelle
            	tablesNumber ++;
            	
            	int currentRows = u.getMaxDimensions().getRow();
            	int currentColumns = u.getMaxDimensions().getColumn();
            	
            	// Numero di righe e colonne
            	rows += currentRows;
            	columns += currentColumns;
            	
            	// Numero valori nulli
            	for(Cell c: u.getCells()) {
            		if (c.getType().equals("EMPTY")) {
            			emptyCells++;
            		}
            	}
            	
            	// Distribuzione righe
            	Integer nRowMap = this.rowsDistribution.get(currentRows);
            	if (nRowMap != null) {
            		this.rowsDistribution.put(currentRows, nRowMap + 1);
            	}
            	else {
            		// Inizializza numero corrente di rows nella mappa  
            		this.rowsDistribution.put(currentRows, 1);
            	}
            	
            	// Distribuzione colonne
            	Integer nColumnMap = this.columnsDistribution.get(currentColumns);
            	if (nColumnMap != null) {
            		this.columnsDistribution.put(currentColumns, nColumnMap + 1);
            	}
            	else {
            		// Inizializza numero corrente di columns nella mappa  
            		this.columnsDistribution.put(currentColumns, 1);
            	}
            	
            	// Distribuzione tipi di celle
            	for(Cell c: u.getCells()) {
            		Integer nTypeMap = this.typeCellDistribution.get(c.getType());
                	if (nTypeMap != null) {
                		this.typeCellDistribution.put(c.getType(), nTypeMap + 1);
                	}
                	else { 
                		this.typeCellDistribution.put(c.getType(), 1);
                	}
            	}
            	
            	// Distribuzione valori distinti (colonna, lista di stringhe)
            	for(Cell c: u.getCells()) {
            		Set<String> values = mapIntValues.get(c.getCoordinates().getColumn());
            		if (values == null) {
            			values = new HashSet<>();
            			mapIntValues.put(c.getCoordinates().getColumn(), values);
            		}
            		values.add(c.getCleanedText());
            	}
            	
            });
            
         // Distribuzione valori distinti (colonna, numero di valori distinti)
            for (Integer s: mapIntValues.keySet()) {
        		distinctColumnsValuesDistribution.put(s, mapIntValues.get(s).size());
        	}


		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
