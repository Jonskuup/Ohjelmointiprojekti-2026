package view;


public interface IVisualisointi {

	public void tyhjennaNaytto();
	
	public void uusiAsiakas(String vaihe);

	//poistaa asiakkaan visualisoinnista, kun asiakas on poistunut vaiheesta
	public void poistaAsiakas(String vaihe);
		
}

