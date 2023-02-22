package csv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import models.AziendaRecord;

public class CSVWriter {

	private String pathOutCSV;
	private String header;

	public CSVWriter(String pathOutCSV, String[] header) {
		this.pathOutCSV = pathOutCSV;
		this.header = String.join(",", header);
	}

	public void writeCSV(List<AziendaRecord> aziende) {
		try {
			FileWriter writer = new FileWriter(this.pathOutCSV);
			writer.append(header + ",idDocDB\n");

			for (AziendaRecord azienda : aziende) {
				writer.append(azienda.getId() + ",");
				writer.append(azienda.getName() + ",");
				writer.append(azienda.getCountry() + ",");
				writer.append(azienda.getSector() + ",");
				writer.append(azienda.getFounded() + ",");
				writer.append(azienda.getMarketcap() + ",");
				writer.append(azienda.getRevenue() + ",");
				writer.append(azienda.getEmployees() + ",");
				writer.append(azienda.getLinks() + ",");
				writer.append(azienda.getCeo() + ",");
				writer.append(azienda.getIdDocDB());
				writer.append("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
