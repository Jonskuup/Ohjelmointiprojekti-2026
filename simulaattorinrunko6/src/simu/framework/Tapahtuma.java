package simu.framework;

public class Tapahtuma implements Comparable<Tapahtuma> {

	private ITapahtumanTyyppi tyyppi;
	private double aika;
	
	public Tapahtuma(ITapahtumanTyyppi tyyppi, double aika){
		this.tyyppi = tyyppi;
		this.aika = aika;
	}
	
	public void setTyyppi(ITapahtumanTyyppi tyyppi) {
		this.tyyppi = tyyppi;
	}
	public ITapahtumanTyyppi getTyyppi() {
		return tyyppi;
	}
	public void setAika(double aika) {
		this.aika = aika;
	}
	public double getAika() {
		return aika;
	}

	@Override
	public int compareTo(Tapahtuma arg) {
		if (this.aika < arg.aika) return -1;
		else if (this.aika > arg.aika) return 1;
		return 0;
	}

}
