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

import lucene.GlobalVariables;
import models.Cell;
import models.Table;

public class Statistics {

	private GlobalVariables gv;
	private int tablesNumber;
	private int columns;
	private int rows;
	private int emptyCells;
	private int totalCells;
	private HashMap<Integer, Integer> rowsDistribution;
	private HashMap<Integer, Integer> columnsDistribution;
	private HashMap<String, Integer> typeCellDistribution;
	private HashMap<Integer, Set<String>> mapIntValues;
	private HashMap<Integer, Integer> distinctColumnsValuesDistribution;
	
	public Statistics() {
		this.gv = new GlobalVariables();
		this.tablesNumber = 0;
		this.columns = 0;
		this.rows = 0;
		this.emptyCells = 0;
		this.totalCells = 0;
		this.rowsDistribution = new HashMap<Integer, Integer>();
		this.columnsDistribution = new HashMap<Integer, Integer>();
		this.typeCellDistribution = new HashMap<String, Integer>();
		this.mapIntValues = new HashMap<>();
		this.distinctColumnsValuesDistribution = new HashMap<>();
		
	}
	

	public void getStatistics() {
		
		long start = System.currentTimeMillis();
		System.out.println("\nGenerating statistics...");
		
		parse(this.gv.getPathInputFile());
		
		SortMapByValue sortMap = new SortMapByValue();
		
		System.out.println("Tables number: " + this.tablesNumber);
		System.out.println("Avg rows: " + this.rows / this.tablesNumber);
		System.out.println("Avg columns: " + this.columns / this.tablesNumber);
		System.out.println("Avg null value per table: " + this.emptyCells / this.tablesNumber);
		System.out.println("Total cells: " + this.totalCells);
		System.out.println("Cell type distribution: " + this.typeCellDistribution);
		System.out.println("Row distribution (ROWS, N.TAB): " + sortMap.getSortedFirstNElement(this.rowsDistribution, 20));
		System.out.println("Columns distribution (COLUMNS, N.TAB): " + sortMap.getSortedFirstNElement(this.columnsDistribution, 20));
		System.out.println("Distinct columns number values (TAB, DISTINCT): " + sortMap.getSortedFirstNElement(this.distinctColumnsValuesDistribution, 20));

		System.out.println("Total Time Taken: " + (System.currentTimeMillis() - start) / 1000 + " secs");

	}
	
	public void getStatisticsAndCharts() {
		this.getStatistics();
		System.out.println("\nGenerating charts...");
		Charts avgRowsColsChart = new Charts("Avg rows and columns", "Avg rows and columns", rows / tablesNumber, columns / tablesNumber);
		avgRowsColsChart.pack();
		avgRowsColsChart.setVisible(true);
		
		Charts columnsDistributionChart = new Charts("Columns distribution", "Columns distribution", columnsDistribution, "columns");
		columnsDistributionChart.pack();
		columnsDistributionChart.setVisible(true);
		
		Charts rowsDistributionChart = new Charts("Rows distribution", "Rows distribution", rowsDistribution, "rows");
		rowsDistributionChart.pack();
		rowsDistributionChart.setVisible(true);
		
		Charts distinctValuesDistributionChart = new Charts("Distinct values distribution per columns", 
				"Distinct values distribution per columns", distinctColumnsValuesDistribution, "columns");
		distinctValuesDistributionChart.pack();
		distinctValuesDistributionChart.setVisible(true);
		
		Charts cellsTypeDistributionChart = new Charts("Cells type distribution", "Cells type distribution", typeCellDistribution);
		cellsTypeDistributionChart.pack();
		cellsTypeDistributionChart.setVisible(true);
	}

	private void parse(String jsonFilePath) {

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();

		try (BufferedReader br = new BufferedReader(new FileReader(jsonFilePath))) {
			MappingIterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);

			value.forEachRemaining((u) -> {
				// Numero di tabelle
				this.tablesNumber++;

				int currentRows = u.getMaxDimensions().getRow();
				int currentColumns = u.getMaxDimensions().getColumn();

				// Numero di righe e colonne
				this.rows += currentRows;
				this.columns += currentColumns;

				// Numero valori nulli
				for (Cell c : u.getCells()) {
					if (c.getType().equals("EMPTY")) {
						this.emptyCells++;
					}
				}

				// Distribuzione righe
				Integer nRowMap = this.rowsDistribution.get(currentRows);
				if (nRowMap != null) {
					this.rowsDistribution.put(currentRows, nRowMap + 1);
				} else {
					// Inizializza numero corrente di rows nella mappa
					this.rowsDistribution.put(currentRows, 1);
				}

				// Distribuzione colonne
				Integer nColumnMap = this.columnsDistribution.get(currentColumns);
				if (nColumnMap != null) {
					this.columnsDistribution.put(currentColumns, nColumnMap + 1);
				} else {
					// Inizializza numero corrente di columns nella mappa
					this.columnsDistribution.put(currentColumns, 1);
				}

				// Distribuzione tipi di celle
				for (Cell c : u.getCells()) {
					Integer nTypeMap = this.typeCellDistribution.get(c.getType());
					if (nTypeMap != null) {
						this.typeCellDistribution.put(c.getType(), nTypeMap + 1);
					} else {
						this.typeCellDistribution.put(c.getType(), 1);
					}
				}

				// Distribuzione valori distinti (colonna, lista di stringhe)
				for (Cell c : u.getCells()) {
					Set<String> values = this.mapIntValues.get(c.getCoordinates().getColumn());
					if (values == null) {
						values = new HashSet<>();
						this.mapIntValues.put(c.getCoordinates().getColumn(), values);
					}
					values.add(c.getCleanedText());
				}
				
				if (this.tablesNumber % 50000 == 0) {
					System.out.println("...");
				}

			});

			// Distribuzione valori distinti (colonna, numero di valori distinti)
			for (Integer s : this.mapIntValues.keySet()) {
				this.distinctColumnsValuesDistribution.put(s, this.mapIntValues.get(s).size());
			}
			
			// Celle totali
			for (String s : this.typeCellDistribution.keySet()) {
				this.totalCells += this.typeCellDistribution.get(s);
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
