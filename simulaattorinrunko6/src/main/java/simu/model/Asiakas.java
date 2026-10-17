package simu.model;

import eduni.distributions.Bernoulli;
import simu.framework.*;

/**
 * Tämä Asiakas luokka kuvaa simulaatiossa liikkuvaa autoa.
 * Jokaisella asiakkaalla on oma id, saapumisaika, poistumisaika ja pesutyyppi.
 * Pesutyyppi voi olla joko basic tai premium.
 *
 * Kun uusi asiakas luodaan siihen tallennetaan sen hetkisen simulaation aika ja valitaan pesutyyppi.
 * Pesutyypin valintaan vaikuttaa käyttäjän syöttämä premium pesun osuus.
 *
 * Luokka muistaa myös valmistuneiden asiakkaiden määrän ja niiden läpimenoajan.
 */
public class Asiakas {

	/**
	 * Asiakkaan pesutyyppi, joka on joko basic tai premium.
	 */
	public enum PesuTyyppi{
		/**
		 * Peruspesu
		 */
		BASIC,
		/**
		 * Premiumpesu
		 */
		PREMIUM
	}

	/**
	 * Asiakkaan saapumisaika simulaatioon.
	 */
	private double saapumisaika;

	/**
	 * Asiakkaan poistumisaika simulaatiosta.
	 */
	private double poistumisaika;

	/**
	 * Asiakkaan oma id.
	 */
	private int id;

	/**
	 * Asiakkaalle valittu pesu basic tai premium.
	 */
	private PesuTyyppi pesuTyyppi;

	/**
	 * Laskuri jolla jokanen asiakas saa oman id:n.
	 */
	private static int i = 1;

	/**
	 * Kaikkien valmistuneiden asiakkaiden läpimenoajat yhteensä.
	 */
	private static double lapimenoaikojenSUmma = 0;

	/**
	 * Simulaatiossa valmistuneiden asiakkaiden määrä.
	 */
	private static int valmistuneetAsiakkaat = 0;

	/**
	 * Generaattori, jonka avulla asiakkaalle valitaan basic tai premium pesu.
	 */
	private static Bernoulli pesuvalintaGeneraattori = new Bernoulli(0.4);

	/**
	 * Tässä luodaan uusi asiakas simulaatioon. Sitten se saa oman id:n ja sen saapumisaika tulee simulaation kellosta.
	 * Tämän jälkeen asiakas on joko basic tai premium pesu.
	 *
	 * Pesutyyppi valitaan sillä premium pesulle annetulla osuudella esim. jos premium osuus on 40 % se tarkottaa, että
	 * noin 40 % asiakkaista saa premium pesun ja loput basic pesun.
	 */
	public Asiakas(){
	    id = i++;
	    
		saapumisaika = Kello.getInstance().getAika();

		pesuTyyppi = (pesuvalintaGeneraattori.sample() == 1) ? PesuTyyppi.PREMIUM : PesuTyyppi.BASIC;

		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika + ", valitsi pesuohjelman: " + pesuTyyppi);
	}

	/**
	 * Palauttaa asiakkaan poistumisajan.
	 *
	 * @return asiakkaan poistumisaika.
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * Asettaa asiakkaan poistumisajan.
	 *
	 * @param poistumisaika asiakkaan poistumisaika.
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	/**
	 * Palauttaa asiakkaan saapumisajan.
	 *
	 * @return asiakkaan saapumisaika
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}


	/**
	 * Asettaa asiakkaan saapumisajan.
	 *
	 * @param saapumisaika asiakkaan saapumisaika
	 */
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}

	/**
	 * Palauttaa asiakkaan id:n.
	 *
	 * @return asiakkaan id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Palauttaa asiakkaalle valitun pesutyypin.
	 *
	 * @return asiakkaan pesutyyppi
	 */
	public PesuTyyppi getPesuTyyppi() {
		return pesuTyyppi;
	}

	/**
	 * Asettaa asiakkaalle pesutyypin.
	 *
	 * @param pesuTyyppi asiakkaalle asetettava pesutyyppi
	 */
	public void setPesuTyyppi(PesuTyyppi pesuTyyppi) {
		this.pesuTyyppi = pesuTyyppi;
	}

	/**
	 * Tässä tarkistetaan onko asiakkaalla premium pesu.
	 * Tätä voidaan siis käyttää jos halutaan tietää, mille reitille asiakas pitää seuraavaks ohjata.
	 *
	 * @return true jos asiakkaalla on premium pesu, muuten false.
	 */
	public boolean onPremium() {
		return pesuTyyppi == PesuTyyppi.PREMIUM;
	}

	/**
	 * 	Premium pesun todennäköisyyden asettaminen.
	 *	Premium osuus on luku väliltä 0-1. Esim 0.4 tarkottaa 40 % premium asiakkaita.
	 *
	 * @param premiumOsuus premium pesun osuus väliltä 0-1.
	 * @param seed satunnaslukujen muodostamiseen käytettävä seed.
 	 */
	public static void setPremiumOsuus(double premiumOsuus, long seed) {
		pesuvalintaGeneraattori = new Bernoulli(premiumOsuus, seed);
	}

	/**
	 * Tämä tekee asiakkaan loppuraportin kun asiakas poistuu simulaatiosta.
	 *
	 * Tässä metodissa tulostetaan asiakkaan pesutyyppi, saapumisaika, poistumisaika ja se kuinka kauan asiakas oli simulaatiossa.
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "\nAsiakas "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " pesuohjelma: " + pesuTyyppi);
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui: " +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui: " +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi: " +(poistumisaika-saapumisaika));
		double lapimenoaika = poistumisaika - saapumisaika;
		lapimenoaikojenSUmma += lapimenoaika;
		valmistuneetAsiakkaat++;
		double keskiarvo = lapimenoaikojenSUmma / valmistuneetAsiakkaat;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ keskiarvo);
	}

	/**
	 * Palauttaa simulaatiossa valmistuneiden asiakkaiden määrän.
	 *
	 * @return valmistuneiden asiakkaiden määrä
	 */
	public static int getValmistuneetAsiakkaat() {
		return valmistuneetAsiakkaat;
	}

	/**
	 * Palauttaa valmistuneiden asiakkaiden keskimääräisen läpimenoajan.
	 *
	 * @return asiakkaiden keskimääräinen läpimenoaika
 	 */
	public static double getKeskimaarainenLapimenoaika() {
		if (valmistuneetAsiakkaat == 0) {
			return 0;
		}
		return lapimenoaikojenSUmma / valmistuneetAsiakkaat;
	}

	/**
	 * 	Nollaa asiakkaiden yhteiset tilastot uutta simulointia varten.
 	 */
	public static void nollaaTilastot() {
		lapimenoaikojenSUmma = 0;
		valmistuneetAsiakkaat = 0;
		i = 1; // asiakkaiden ID alkaa ykkösestä
	}

}
