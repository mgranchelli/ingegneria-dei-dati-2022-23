package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize
@JsonSerialize
public class Cell {
	
	private String className;
	private String innerHTML;
    private String isHeader;
    private String type;
    @JsonProperty("Coordinates")
    private Coordinate Coordinates;
    private String cleanedText;
    @JsonProperty("Rows")
    private Row[] Rows;

    public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getInnerHTML() {
		return innerHTML;
	}
	public void setInnerHTML(String innerHTML) {
		this.innerHTML = innerHTML;
	}
	public String getIsHeader() {
		return isHeader;
	}
	public void setIsHeader(String isHeader) {
		this.isHeader = isHeader;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Coordinate getCoordinates() {
		return Coordinates;
	}
	public void setCoordinates(Coordinate coordinates) {
		Coordinates = coordinates;
	}
	public String getCleanedText() {
		return cleanedText;
	}
	public void setCleanedText(String cleanedText) {
		this.cleanedText = cleanedText;
	}
	public Row[] getRows() {
		return Rows;
	}
	public void setRows(Row[] rows) {
		Rows = rows;
	}
	
}
