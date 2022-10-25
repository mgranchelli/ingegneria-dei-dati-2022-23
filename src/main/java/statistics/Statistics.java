package statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Table;


public class Statistics {
	
	private int tablesNumber = 0;
	private int columns = 0;
	private int rows = 0;
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Statistics testJson = new Statistics();
        String jsonFilePath = "./inputFile/tablesTest_1.json";
        testJson.parse(jsonFilePath);
        
        System.out.println("Total Time Taken : "+ (System.currentTimeMillis() - start)/1000 + " secs");
		
	}
	
	
	
	public void parse(String jsonFilePath){

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		
		try(BufferedReader br = new BufferedReader(new FileReader(jsonFilePath))) {
            MappingIterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);
            System.out.println(value.toString());
            value.forEachRemaining((u) -> {
            	tablesNumber ++;
            	rows += u.getMaxDimensions().getRow();
            	columns += u.getMaxDimensions().getColumn();
            });


		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Tables number: " + tablesNumber);
		System.out.println("Avg rows: " + rows/tablesNumber);
		System.out.println("Avg columns: " + columns/tablesNumber);
    }
}
