package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonDeserialize
@JsonSerialize
public class Table {
	
	@JsonIgnore
    private String _id;
	private String className;
	private String id;
	private Cell[] cells;
	private String beginIndex;
	private String endIndex;
	
	private String referenceContext;
	private String type;
	private String classe;
	private MaxDimension maxDimensions;
	
	private String[] headersCleaned;
	private String keyColumn;
	
	public String get_id() {
	    return _id;
	}

	public void set_id(String _id) {
	    this._id = _id;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cell[] getCells() {
		return cells;
	}

	public void setCells(Cell[] cells) {
		this.cells = cells;
	}

	public String getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(String beginIndex) {
		this.beginIndex = beginIndex;
	}

	public String getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}

	public String getReferenceContext() {
		return referenceContext;
	}

	public void setReferenceContext(String referenceContext) {
		this.referenceContext = referenceContext;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public MaxDimension getMaxDimensions() {
		return maxDimensions;
	}

	public void setMaxDimensions(MaxDimension maxDimensions) {
		this.maxDimensions = maxDimensions;
	}

	public String[] getHeadersCleaned() {
		return headersCleaned;
	}

	public void setHeadersCleaned(String[] headersCleaned) {
		this.headersCleaned = headersCleaned;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}
}
