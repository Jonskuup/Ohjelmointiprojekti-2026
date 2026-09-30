package simu.model;

import eduni.distributions.Bernoulli;
import simu.framework.*;

// TODO:
// Asiakas koodataan simulointimallin edellyttämällä tavalla (data!)
public class Asiakas {

	public enum PesuTyyppi{
		BASIC,
		PREMIUM
	}

	private double saapumisaika;
	private double poistumisaika;
	private int id;
	private PesuTyyppi pesuTyyppi;
	private static int i = 1;
	private static double lapimenoaikojenSUmma = 0;
	private static int valmistuneetAsiakkaat = 0;
	private static Bernoulli pesuvalintaGeneraattori = new Bernoulli(0.4);
	
	public Asiakas(){
	    id = i++;
	    
		saapumisaika = Kello.getInstance().getAika();

		pesuTyyppi = (pesuvalintaGeneraattori.sample() == 1) ? PesuTyyppi.PREMIUM : PesuTyyppi.BASIC;

		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika + ", valitsi pesuohjelman: " + pesuTyyppi);
	}

	public double getPoistumisaika() {
		return poistumisaika;
	}

	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	public double getSaapumisaika() {
		return saapumisaika;
	}

	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	


	public int getId() {
		return id;
	}

	public PesuTyyppi getPesuTyyppi() {
		return pesuTyyppi;
	}

	public void setPesuTyyppi(PesuTyyppi pesuTyyppi) {
		this.pesuTyyppi = pesuTyyppi;
	}
	public boolean onPremium() {
		return pesuTyyppi == PesuTyyppi.PREMIUM;
	}

	// Asiakas voi vaihtaa premium todennäköisyyttä
	public static void setPremiumOsuus(double premiumOsuus, long seed) {
		pesuvalintaGeneraattori = new Bernoulli(premiumOsuus, seed);
	}
	
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

	// palauttaa simulaatiossa valmistuneiden asiakkaiden määrän
	public static int getValmistuneetAsiakkaat() {
		return valmistuneetAsiakkaat;
	}

	// palauttaa valmistuneiden asiakkaiden keskimääräisen läpimenoajan
	public static double getKeskimaarainenLapimenoaika() {
		if (valmistuneetAsiakkaat == 0) {
			return 0;
		}
		return lapimenoaikojenSUmma / valmistuneetAsiakkaat;
	}

	// nollaa asiakkaiden yhteiset tilastot uutta simulointia varten
	public static void nollaaTilastot() {
		lapimenoaikojenSUmma = 0;
		valmistuneetAsiakkaat = 0;
		i = 1; // asiakkaiden ID alkaa ykkösestä
	}

}
