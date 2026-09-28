package simu.model;

import simu.framework.*;
import java.util.LinkedList;
import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class Palvelupiste {

	private final LinkedList<Asiakas> jono = new LinkedList<>(); // Tietorakennetoteutus
	private final ContinuousGenerator generator;
	private final Tapahtumalista tapahtumalista;
	private final TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;

	//JonoStartegia strategia; //optio: asiakkaiden järjestys

	private boolean varattu = false;
	private int palvellutAsiakkaat = 0;
	private double aktiivinenAika = 0;
	private int maksimiJonopituus = 0;

	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;

	}


	public void lisaaJonoon(Asiakas a) {   // Jonon 1. asiakas aina palvelussa
		jono.add(a);
		if (jono.size() > maksimiJonopituus) {
			maksimiJonopituus = jono.size();
		}

	}


	public Asiakas otaJonosta() {  // Poistetaan palvelussa ollut
		varattu = false;
		return jono.poll();
	}


	public void aloitaPalvelu() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana

		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.peek().getId());

		varattu = true;
		double palveluaika = generator.sample();
		aktiivinenAika += palveluaika;
		palvellutAsiakkaat++;
		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
	}


	public boolean onVarattu() {
		return varattu;
	}


	public boolean onJonossa() {
		return jono.size() != 0;
	}


	public int getPalvellutAsiakkaat() {
		return palvellutAsiakkaat;
	}

	public double getAktiivinenAika() {
		return aktiivinenAika;
	}

	public int getMaksimiJonopituus() {
		return maksimiJonopituus;
	}

	public double getKeskimääräinenPalveluaika(){
		if (palvellutAsiakkaat == 0) return 0;
		return aktiivinenAika / palvellutAsiakkaat;
	}

	public void raportti() {
		Trace.out(Trace.Level.INFO, "--- Palvelupiste raportti ---");
		Trace.out(Trace.Level.INFO, "Palvellut asiakkaat" + palvellutAsiakkaat);
		Trace.out(Trace.Level.INFO, "Aktiivinen aika yhteensä" + aktiivinenAika);
		Trace.out(Trace.Level.INFO, "Maksimi jononpituus" +  maksimiJonopituus);
		Trace.out(Trace.Level.INFO, "Keskimääräinen palveluaika" + getKeskimääräinenPalveluaika());

	}
}

