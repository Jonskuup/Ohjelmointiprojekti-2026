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
	private static long sum = 0;
	private static final Bernoulli pesuvalintaGeneraattori = new Bernoulli(0.4);
	
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
	
	public void raportti(){
		Trace.out(Trace.Level.INFO, "\nAsiakas "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " pesuohjelma: " + pesuTyyppi);
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui: " +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui: " +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi: " +(poistumisaika-saapumisaika));
		sum += (poistumisaika-saapumisaika);
		double keskiarvo = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ keskiarvo);
	}

}
