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
					   kontrolleri.visualisoiAsiakas();
				break;
			case PALVELUPISTE_VALMIS: a = (Asiakas)palvelupisteet[0].otaJonosta();
				if (a.onPremium()) {
					palvelupisteet[2].lisaaJonoon(a);
				} else {
					palvelupisteet[1].lisaaJonoon(a);
				}
				break;
			case BASIC_VALMIS: a = (Asiakas)palvelupisteet[1].otaJonosta();
				   	   palvelupisteet[4].lisaaJonoon(a);
				break;
			case PREMIUM_VALMIS: a = (Asiakas)palvelupisteet[2].otaJonosta();
				palvelupisteet[3].lisaaJonoon(a);
				break;
			case VAHAUS_VALMIS: a = (Asiakas)palvelupisteet[3].otaJonosta();
				palvelupisteet[4].lisaaJonoon(a);
				break;
			case KUIVAUS_VALMIS:
				       a = (Asiakas)palvelupisteet[4].otaJonosta();
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

		// Kootaan raportin teksti erillistä ikkunaa varten
		StringBuilder raportti = new StringBuilder();
		raportti.append("=== SIMULAATION LOPPURAPORTTI ===\n\n");
		raportti.append("Kokonaisaika: ").append(String.format("%.2f", Kello.getInstance().getAika())).append("\n\n");

		String[] nimet = {"Vastaanotto", "Basic pesu", "Premium pesu", "Vahaus", "Kuivaus"};
		for (int i = 0; i < palvelupisteet.length; i++) {
			raportti.append(nimet[i]).append(":\n");
			raportti.append(" - Palvellut autot: ").append(palvelupisteet[i].getPalvellutAsiakkaat()).append("\n");
			raportti.append(" - Aktiivinen aika: ").append(String.format("%.2f", palvelupisteet[i].getAktiivinenAika())).append("\n");
			raportti.append(" - Maksimijono: ").append(palvelupisteet[i].getMaksimiJonopituus()).append("\n");
			raportti.append(" - Ka. palveluaika: ").append(String.format("%.2f", palvelupisteet[i].getKeskimääräinenPalveluaika())).append("\n\n");
		}

		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());
		kontrolleri.naytaLoppuraportti(raportti.toString());
	}

	
}
