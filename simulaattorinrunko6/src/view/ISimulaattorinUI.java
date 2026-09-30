package view;

public interface ISimulaattorinUI {
	
	// Kontrolleri tarvitsee syötteitä, jotka se välittää Moottorille
	public double getAika();
	public long getViive();
	public double getSaapumisvali();
	public double getPalvelupisteAika();
	public double getBasicAika();
	public double getPremiumAika();
	public double getVahausAika();
	public double getKuivausAika();
	public double getPremiumOsuus();
	
	//Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa 
	public void setLoppuaika(double aika);
	
	// Kontrolleri tarvitsee  
	public IVisualisointi getVisualisointi();

}
