package simu.model;

import simu.framework.*;
import java.util.LinkedList;
import eduni.distributions.ContinuousGenerator;

/**
 * Palvelupiste kuvaa yhtä autopesulan palvelupistettä.
 *
 * Jokasella pisteellä on oma jono mihin lisätään asiakkaita.
 * Palvelupiste palvelee yhtä asiakasta kerrallaan ja generaattorin avulla se saa palveluajan.
 *
 * Tässä on myös palveltujen asiakkaiden määrä, aktiivinen aika ja pisin jono.
 */
public class Palvelupiste {

	/**
	 * Palvelupisteen jono, missä asiakkaat odottaa palvelua.
	 */
	private final LinkedList<Asiakas> jono = new LinkedList<>(); // Tietorakennetoteutus

	/**
	 * Generaattori, jolla muodostetaan palveluun kuluva aika.
	 */
	private final ContinuousGenerator generator;

	/**
	 * Simulaation tapahtumalista, johon lisätään valmistuneita palveluita.
	 */
	private final Tapahtumalista tapahtumalista;

	/**
	 * Tässä nähdään onko palvelu valmistunut.
	 */
	private final TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;

	/**
	 * Kertoo sen onko palvelupiste tällä hetkellä varattu.
	 */
	private boolean varattu = false;

	/**
	 * Palvelupisteessä palveltujen asiakkaiden määrä.
	 */
	private int palvellutAsiakkaat = 0;

	/**
	 * Palvelupisteen palveluihin käyttämä aika yhteensä.
	 */
	private double aktiivinenAika = 0;

	/**
	 * Palvelupisteelle muodostuneen pisimmän jonon pituus.
	 */
	private int maksimiJonopituus = 0;

	/**
	 * Luo uuden palvelupisteen.
	 * Sille annetaan generaattori, josta muodostuu palveluaika. Sille luodaan myös tapahtumalista ja tapahtumatyyppi.
	 *
	 * @param generator muodostaa palveluun kuluvan ajan
	 * @param tapahtumalista simulaation tapahtumalista
	 * @param tyyppi palvelun valmistumisesta tuleva tapahtumatyyppi
	 */
	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;

	}

	/**
	 * Lisää asiakkaan palvelupisteen jonoon.
	 *
	 * @param a jonoon lisättävä asiakas
	 */
	public void lisaaJonoon(Asiakas a) {   // Jonon 1. asiakas aina palvelussa
		jono.add(a);
		int jonottavienMaara = jono.size();
		if (varattu) {
			jonottavienMaara--;
		}
		if (jonottavienMaara > maksimiJonopituus) {
			maksimiJonopituus = jonottavienMaara;
		}

	}

	/**
	 * Poistaa palvelussa olleen asiakkaan jonosta kun se on käynyt palvelun läpi.
	 * Kun asiakas poistetaan, palvelupiste vapautuu ja palveltujen asiakkaiden määrä kasvaa yhellä.
	 *
	 * @return palvelusta valmistunut asiakas
	 */
	public Asiakas otaJonosta() {  // Poistetaan palvelussa ollut
		varattu = false;
		palvellutAsiakkaat++;
		return jono.poll();
	}

	/**
	 * Aloittaa palvelun jonon ensimmäiselle asiakkaalle.
	 * Palvelupiste asetetaan varatuksi.
	 */
	public void aloitaPalvelu() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana

		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.peek().getId());

		varattu = true;
		double palveluaika = generator.sample();
		aktiivinenAika += palveluaika;
		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
	}

	/**
	 * Tarkistaa onko palvelupiste varattu.
	 *
	 * @return true jos palvelupiste on varattu muuten false
	 */
	public boolean onVarattu() {
		return varattu;
	}

	/**
	 * Tarkistaa onko palvelupisteen jonossa asiakkaita.
	 *
	 * @return true jos jonossa on asiakas muuten false
	 */
	public boolean onJonossa() {
		return jono.size() != 0;
	}

	/**
	 * Palauttaa palvelupisteessä palveltujen asiakkaiden määrän.
	 *
	 * @return palveltujen asiakkaiden määrä
	 */
	public int getPalvellutAsiakkaat() {
		return palvellutAsiakkaat;
	}

	/**
	 * Palauttaa palvelupisteen aktiivisen ajan.
	 *
	 * @return palvelupisteen aktiivinen aika
	 */
	public double getAktiivinenAika() {
		return aktiivinenAika;
	}

	/**
	 * Palauttaa palvelupisteen pisimmän jonon pituuden.
	 *
	 * @return pisimmän jonon pituus
	 */
	public int getMaksimiJonopituus() {
		return maksimiJonopituus;
	}

	/**
	 * Laskee palvelupisteen keskimääräisen palveluajan.
	 * Jos asiakkaita ei ole vielä palveltu loppuun palautuu 0.
	 *
 	 * @return palvelupisteen keskimääräinen palveluaika
	 */
	public double getKeskimääräinenPalveluaika(){
		if (palvellutAsiakkaat == 0) return 0;
		return aktiivinenAika / palvellutAsiakkaat;
	}

	/**
	 * Tulostaa palvelupisteen keräämät tilastot.
	 */
	public void raportti() {
		Trace.out(Trace.Level.INFO, "--- Palvelupiste raportti ---");
		Trace.out(Trace.Level.INFO, "Palvellut asiakkaat" + palvellutAsiakkaat);
		Trace.out(Trace.Level.INFO, "Aktiivinen aika yhteensä" + aktiivinenAika);
		Trace.out(Trace.Level.INFO, "Maksimi jononpituus" +  maksimiJonopituus);
		Trace.out(Trace.Level.INFO, "Keskimääräinen palveluaika" + getKeskimääräinenPalveluaika());

	}
}

