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

		String[] nimet = {"Vastaanotto", "Basic pesu", "Premium pesu", "Vahaus", "Kuivaus"};

		// 1. Kerätään tarvittava data analyysiä varten
		int saapuneet = palvelupisteet[0].getPalvellutAsiakkaat(); // Vastaanotetut autot
		int valmiit = palvelupisteet[4].getPalvellutAsiakkaat();   // Kuivauksesta valmistuneet autot
		int kesken = saapuneet - valmiit; // Kuinka moni jäi vielä pesulaan sisälle

		int maxJono = 0;
		String pullonkaula = "";
		for (int i = 0; i < palvelupisteet.length; i++) {
			if (palvelupisteet[i].getMaksimiJonopituus() > maxJono) {
				maxJono = palvelupisteet[i].getMaksimiJonopituus();
				pullonkaula = nimet[i];
			}
		}

		// 2. Kirjoitetaan helposti luettava raportti
		StringBuilder raportti = new StringBuilder();

		raportti.append("YHTEENVETO:\n");
		raportti.append("- Simulaation kesto: ").append(String.format("%.2f", Kello.getInstance().getAika())).append(" minuuttia.\n");
		raportti.append("- Järjestelmään saapui: ").append(saapuneet).append(" asiakasta.\n");
		raportti.append("- Kokonaan palvellut: ").append(valmiit).append(" asiakasta.\n\n");

		raportti.append("SUORITUSKYKYANALYYSI (Miten asiat vaikuttivat):\n");

		// Analysoidaan kapasiteettia
		if (kesken > 0) {
			raportti.append("- Simulaation päättyessä järjestelmään jäi keskeneräisiä asiakkaita ").append(kesken).append(" kpl. ");
			raportti.append("Tämä tarkoittaa, että asiakkaita saapui ruuhkaisemmin kuin pesulan kapasiteetti ehti heitä käsitellä. ");
			raportti.append("Tuloksena osa autoista ei ehtinyt valmistua ajallaan.\n\n");
		} else {
			raportti.append("- Kaikki järjestelmään saapuneet asiakkaat ehdittiin palvella. Pesulan kapasiteetti oli täysin riittävä saapumismäärään nähden.\n\n");
		}

		// Analysoidaan pullonkauloja
		if (maxJono > 0) {
			raportti.append("- Järjestelmän merkittävin pullonkaula oli '").append(pullonkaula).append("', ");
			raportti.append("jonne kertyi pahimmillaan ").append(maxJono).append(" asiakkaan jono. ");
			raportti.append("Tämä piste rajoitti muiden pisteiden toimintaa ja pidensi suoraan asiakkaiden kokonaisläpimenoaikaa.\n\n");
		} else {
			raportti.append("- Jonoja ei päässyt syntymään yhdellekään pisteelle, eli toiminta oli erittäin sujuvaa ja asiakkaat liikkuivat pisteeltä toiselle odottamatta.\n\n");
		}

		raportti.append("PALVELUPISTEIDEN TARKAT TILASTOT:\n");
		for (int i = 0; i < palvelupisteet.length; i++) {
			raportti.append(nimet[i]).append(":\n");
			raportti.append("  Palvellut: ").append(palvelupisteet[i].getPalvellutAsiakkaat());
			raportti.append(" | Maksimijono: ").append(palvelupisteet[i].getMaksimiJonopituus());
			raportti.append(" | Ka. palveluaika: ").append(String.format("%.2f", palvelupisteet[i].getKeskimääräinenPalveluaika())).append("\n");
		}

		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());
		kontrolleri.naytaLoppuraportti(raportti.toString());
	}

	
}
