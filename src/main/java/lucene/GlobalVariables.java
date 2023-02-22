package lucene;

public class GlobalVariables {
	
	private String pathIndex;
	private String pathInputFile;
	
	public GlobalVariables() {
		this.pathIndex = "target/idx";
		this.pathInputFile = "./inputFile/tables.json";
	}
	
	public String getPathIndex() {
		return pathIndex;
	}

	public String getPathInputFile() {
		return pathInputFile;
	}
}
