package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Visualisointi extends Canvas implements IVisualisointi{

	private final GraphicsContext gc;
	private Image auto;
	private Image autoOdottaa;
	private Image autoPesu;
	private Image autoVahaus;
	private Image autoKuivaus;

	// Määritellään muuttujat, jotka pitävät kirjaa eri vaiheiden autojen määrästä
	private int saapuminenMaara = 0;
	private int basicMaara = 0;
	private int premiumMaara = 0;
	private int vahausMaara = 0;
	private int kuivausMaara = 0;

	public Visualisointi(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();

		auto = new Image(getClass().getResourceAsStream("auto.png")); //Lisätään kuva resursseista
		autoOdottaa = new Image(getClass().getResourceAsStream("carwait.png"));
		autoPesu = new Image(getClass().getResourceAsStream("carwash.png"));
		autoVahaus = new Image(getClass().getResourceAsStream("carpolish.png"));
		autoKuivaus = new Image(getClass().getResourceAsStream("carfinish.png"));

		tyhjennaNaytto();
	}

	public void tyhjennaNaytto() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());

		//saapuminen
		gc.setFill(Color.WHITE);
		gc.fillRect(40, 90, 140, 100);
		gc.drawImage(autoOdottaa, 80, 55,50,45);
		gc.setFill(Color.BLACK);
		gc.fillText("SAAPUMINEN", 70, 120);

		//basic
		gc.setFill(Color.WHITE);
		gc.fillRect(210, 50, 100, 70);
		gc.drawImage(autoPesu, 292, 20, 45, 40);
		gc.setFill(Color.BLACK);
		gc.fillText("BASIC", 300, 80);

		//premium
		gc.setFill(Color.WHITE);
		gc.fillRect(210, 180, 100, 70);
		gc.drawImage(autoPesu, 240, 150, 45, 40);
		gc.setFill(Color.BLACK);
		gc.fillText("PREMIUM", 235, 210);

		//vahaus
		gc.setFill(Color.WHITE);
		gc.fillRect(330, 180, 100, 70);
		gc.drawImage(autoVahaus,355, 150,45, 40);
		gc.setFill(Color.BLACK);
		gc.fillText("VAHAUS", 355, 210);

		//kuivaus
		gc.setFill(Color.WHITE);
		gc.fillRect(450, 100, 100, 70);
		gc.drawImage(autoKuivaus, 475, 70, 45, 40);
		gc.setFill(Color.BLACK);
		gc.fillText("KUIVAUS", 475, 130);

	}
	
	public void uusiAsiakas(String vaihe) {

		if (vaihe.equals("saapuminen")) {
			saapuminenMaara++;
		} else if (vaihe.equals("basic")) {
			basicMaara++;
		} else if (vaihe.equals("premium")) {
			premiumMaara++;
		} else if (vaihe.equals("vahaus")) {
			vahausMaara++;
		} else if (vaihe.equals("kuivaus")) {
			kuivausMaara++;
		}

		piirraAutot();
	}

	//poistaa asiakkaan visualisoinnista, kun asiakas on poistunut vaiheesta
	@Override
	public void poistaAsiakas(String vaihe) {
		if (vaihe.equals("poistu_saapuminen")) {
			if (saapuminenMaara > 0) {
				saapuminenMaara--;
			}
		} else if (vaihe.equals("poistu_basic")) {
			if (basicMaara > 0) {
				basicMaara--;
			}
		} else if (vaihe.equals("poistu_premium")) {
			if (premiumMaara > 0) {
				premiumMaara--;
			}
		} else if (vaihe.equals("poistu_vahaus")) {
			if (vahausMaara > 0) {
				vahausMaara--;
			}
		} else if (vaihe.equals("poistu_kuivaus")) {
			if (kuivausMaara > 0) {
				kuivausMaara--;
			}
		}

		piirraAutot();
	}

	private void piirraAutot() {

		tyhjennaNaytto();

		//piirtää kaikki autot uudelleen, kun asiakas poistuu vaiheesta
		for (int i = 0; i < saapuminenMaara; i++) {
			gc.drawImage(auto, 145 - i * 25, 145, 20, 14);
		}

		for (int i = 0; i < basicMaara; i++) {
			gc.drawImage(auto, 345 - i * 25, 105, 20, 14);
		}

		for (int i = 0; i < premiumMaara; i++) {
			gc.drawImage(auto, 280 - i * 25, 235, 20, 14);
		}

		for (int i = 0; i < vahausMaara; i++) {
			gc.drawImage(auto, 400 - i * 25, 235, 20, 14);
		}

		for (int i = 0; i < kuivausMaara; i++) {
			gc.drawImage(auto, 520 - i * 25, 155, 20, 14);
		}
	}

	public void nollaaNaytto() {
		saapuminenMaara = 0;
		basicMaara = 0;
		premiumMaara = 0;
		vahausMaara = 0;
		kuivausMaara = 0;

		tyhjennaNaytto();
	}
}
