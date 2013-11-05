package fr.labri.scanlib;

public class DataLib {
	
	String library;
	int nbFiles;
	int totalFiles;
	public int getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}
	double ratio;
	
	public DataLib(String library, int nbFiles, double ratio, int totalFiles) {
		super();
		this.library = library;
		this.nbFiles = nbFiles;
		this.ratio = ratio;
		this.totalFiles = totalFiles;
	}
	
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	public int getNbFiles() {
		return nbFiles;
	}
	public void setNbFiles(int nbFiles) {
		this.nbFiles = nbFiles;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	
}
