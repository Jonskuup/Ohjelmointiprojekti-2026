package controller;

import javafx.application.Platform;
import simu.framework.IMoottori;
import simu.model.OmaMoottori;
import view.ISimulaattorinUI;

public class Kontrolleri implements IKontrolleriForM, IKontrolleriForV {   // UUSI

	private IMoottori moottori;
	private ISimulaattorinUI ui;

	public Kontrolleri(ISimulaattorinUI ui) {
		this.ui = ui;
	}

	// Moottorin ohjausta:

	@Override
	public void kaynnistaSimulointi() {
		moottori = new OmaMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten
		moottori.setSimulointiaika(ui.getAika());
		moottori.setViive(ui.getViive());
		ui.getVisualisointi().tyhjennaNaytto();
		((Thread)moottori).start();
		//((Thread)moottori).run(); // Ei missään tapauksessa näin. Miksi?
	}

	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		if (moottori != null) {
			moottori.setViive((long)(moottori.getViive()*1.10));
		}
	}

	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		if (moottori != null) {
			moottori.setViive((long)(moottori.getViive()*0.9));
		}
	}

	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen:

	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(()->ui.setLoppuaika(aika));
	}

	@Override
	public void visualisoiAsiakas() {
		Platform.runLater(new Runnable(){
			public void run(){
				ui.getVisualisointi().uusiAsiakas();
			}
		});
	}

	// --- UUSI METODI RAPORTTI-IKKUNAA VARTEN ---
	@Override
	public void naytaLoppuraportti(String raportti) {
		Platform.runLater(() -> {
			javafx.stage.Stage ikkuna = new javafx.stage.Stage();
			ikkuna.setTitle("Simulaation loppuraportti");

			javafx.scene.control.TextArea alue = new javafx.scene.control.TextArea(raportti);
			alue.setEditable(false); // Estää tekstin muokkaamisen
			alue.setPrefSize(420, 380);
			alue.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14;"); // Siisti tasalevyinen fontti

			javafx.scene.layout.VBox paneeli = new javafx.scene.layout.VBox(alue);
			ikkuna.setScene(new javafx.scene.Scene(paneeli));
			ikkuna.show();
		});
	}
}



