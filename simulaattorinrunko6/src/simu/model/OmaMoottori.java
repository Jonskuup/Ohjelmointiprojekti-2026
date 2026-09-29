package simu.model;

import controller.IKontrolleriForM;
import simu.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OmaMoottori extends Moottori{
	
	private Saapumisprosessi saapumisprosessi;

	private Palvelupiste[] palvelupisteet;

	public OmaMoottori(IKontrolleriForM kontrolleri){

		super(kontrolleri);

		palvelupisteet = new Palvelupiste[5];

		palvelupisteet[0]=new Palvelupiste(new Normal(10,6), tapahtumalista, TapahtumanTyyppi.PALVELUPISTE_VALMIS);
		palvelupisteet[1]=new Palvelupiste(new Normal(10,10), tapahtumalista, TapahtumanTyyppi.BASIC_VALMIS);
		palvelupisteet[2]=new Palvelupiste(new Normal(5,3), tapahtumalista, TapahtumanTyyppi.PREMIUM_VALMIS);
		palvelupisteet[3]=new Palvelupiste(new Normal(10,6), tapahtumalista, TapahtumanTyyppi.VAHAUS_VALMIS);
		palvelupisteet[4]=new Palvelupiste(new Normal(10,6), tapahtumalista, TapahtumanTyyppi.KUIVAUS_VALMIS);

		saapumisprosessi = new Saapumisprosessi(new Negexp(15,5), tapahtumalista, TapahtumanTyyppi.ARR1);

	}

	@Override
	protected void alustukset() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void suoritaTapahtuma(Tapahtuma t){  // B-vaiheen tapahtumat

		Asiakas a;
		switch ((TapahtumanTyyppi)t.getTyyppi()){

			case ARR1: palvelupisteet[0].lisaaJonoon(new Asiakas());
				       saapumisprosessi.generoiSeuraava();
					   kontrolleri.visualisoiAsiakas("saapuminen");
				break;
			case PALVELUPISTE_VALMIS: a = (Asiakas)palvelupisteet[0].otaJonosta();
				kontrolleri.visualisoiAsiakas("poistu_saapuminen");
				if (a.onPremium()) {
					palvelupisteet[2].lisaaJonoon(a);
					kontrolleri.visualisoiAsiakas("premium");
				} else {
					palvelupisteet[1].lisaaJonoon(a);
					kontrolleri.visualisoiAsiakas("basic");
				}
				break;
			case BASIC_VALMIS: a = (Asiakas)palvelupisteet[1].otaJonosta();
				kontrolleri.visualisoiAsiakas("poistu_basic"); //poistu basicista
				palvelupisteet[4].lisaaJonoon(a);
				kontrolleri.visualisoiAsiakas("kuivaus"); //mene kuivaukseen
				break;
			case PREMIUM_VALMIS: a = (Asiakas)palvelupisteet[2].otaJonosta();
				kontrolleri.visualisoiAsiakas("poistu_premium");
				palvelupisteet[3].lisaaJonoon(a);
				kontrolleri.visualisoiAsiakas("vahaus");
				break;
			case VAHAUS_VALMIS: a = (Asiakas)palvelupisteet[3].otaJonosta();
				kontrolleri.visualisoiAsiakas("poistu_vahaus");
				palvelupisteet[4].lisaaJonoon(a);
				kontrolleri.visualisoiAsiakas("kuivaus");
				break;
			case KUIVAUS_VALMIS:
				       a = (Asiakas)palvelupisteet[4].otaJonosta();
					   kontrolleri.visualisoiAsiakas("poistu_kuivaus");
					   a.setPoistumisaika(Kello.getInstance().getAika());
			           a.raportti();
		}
	}

	@Override
	protected void yritaCTapahtumat(){
		for (Palvelupiste p: palvelupisteet){
			if (!p.onVarattu() && p.onJonossa()){
				p.aloitaPalvelu();
			}
		}
	}

	@Override
	protected void tulokset() {
		System.out.println("\nSimulointi päättyi kello " + Kello.getInstance().getAika());

		System.out.println("\nPalvelupisteiden tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[0].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[0].getAktiivinenAika()));

		System.out.println("\nBasic pesun tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[1].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[1].getAktiivinenAika()));

		System.out.println("\nPremium pesun tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[2].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[2].getAktiivinenAika()));

		System.out.println("\nVahaus pisteen tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[3].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[3].getAktiivinenAika()));

		System.out.println("\nKuivaus pisteen tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[4].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[4].getAktiivinenAika()));

		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());
	}

	
}
