package controller;

public class ImportHandler {
	private String importFolderPath;
	
	public ImportHandler() {
		
	}
	
	public ImportHandler(String importPath) {
		importFolderPath = importPath;
	}
	
	public synchronized boolean importIntoDB() {
		
		return true;
	}
}
