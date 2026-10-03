package simu.model;

import controller.IKontrolleriForM;
import simu.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import dao.SimulointiajoDao;
import entity.Simulointiajo;
import entity.AjonPalvelupiste;
import java.util.ArrayList;
import java.util.List;

public class OmaMoottori extends Moottori{
	
	private Saapumisprosessi saapumisprosessi;

	private Palvelupiste[] palvelupisteet;

	private double saapumisvali;
	private double premiumOsuus;
	private double [] palveluaikojenKa;
	private double[] palveluajatHajonta = {6, 10, 3, 6, 6};

	private final long seed = 5;

	public OmaMoottori(IKontrolleriForM kontrolleri, double saapumisvali, double palvelupisteAika, double basicAika, double premiumAika, double vahausAika, double kuivausAika, double premiumOsuus) {

		super(kontrolleri);

		Asiakas.nollaaTilastot();

		palvelupisteet = new Palvelupiste[5];

		palvelupisteet[0]=new Palvelupiste(new Normal(palvelupisteAika,6, seed), tapahtumalista, TapahtumanTyyppi.PALVELUPISTE_VALMIS);
		palvelupisteet[1]=new Palvelupiste(new Normal(basicAika,10, seed + 1), tapahtumalista, TapahtumanTyyppi.BASIC_VALMIS);
		palvelupisteet[2]=new Palvelupiste(new Normal(premiumAika,3, seed + 2), tapahtumalista, TapahtumanTyyppi.PREMIUM_VALMIS);
		palvelupisteet[3]=new Palvelupiste(new Normal(vahausAika,6, seed + 3), tapahtumalista, TapahtumanTyyppi.VAHAUS_VALMIS);
		palvelupisteet[4]=new Palvelupiste(new Normal(kuivausAika,6, seed + 4), tapahtumalista, TapahtumanTyyppi.KUIVAUS_VALMIS);

		saapumisprosessi = new Saapumisprosessi(new Negexp(saapumisvali,seed + 5), tapahtumalista, TapahtumanTyyppi.ARR1);

		Asiakas.setPremiumOsuus(premiumOsuus, seed +6);

		this.saapumisvali = saapumisvali;
		this.premiumOsuus = premiumOsuus;
		this.palveluaikojenKa = new double[]{palvelupisteAika, basicAika, premiumAika, vahausAika, kuivausAika};
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

		System.out.println("\nPalvelupisteiden tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[0].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[0].getAktiivinenAika()));
		System.out.println("Maksimi jononpituus: " + palvelupisteet[0].getMaksimiJonopituus());

		System.out.println("\nBasic pesun tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[1].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[1].getAktiivinenAika()));
		System.out.println("Maksimi jononpituus: " + palvelupisteet[1].getMaksimiJonopituus());

		System.out.println("\nPremium pesun tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[2].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[2].getAktiivinenAika()));
		System.out.println("Maksimi jononpituus: " + palvelupisteet[2].getMaksimiJonopituus());

		System.out.println("\nVahaus pisteen tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[3].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[3].getAktiivinenAika()));
		System.out.println("Maksimi jononpituus: " + palvelupisteet[3].getMaksimiJonopituus());

		System.out.println("\nKuivaus pisteen tulokset: ");
		System.out.println("Palveltujen autojen määrä: " + (palvelupisteet[4].getPalvellutAsiakkaat()));
		System.out.println("Palveluun käytetty aika yhteensä: " + (palvelupisteet[4].getAktiivinenAika()));
		System.out.println("Maksimi jononpituus: " + palvelupisteet[4].getMaksimiJonopituus());

		System.out.println("\nKoko simulaation tulokset: ");
		System.out.println("Valmistuneiden autojen määrä: " + Asiakas.getValmistuneetAsiakkaat());
		System.out.println("Keskimääräinen läpimenoaika: " + Asiakas.getKeskimaarainenLapimenoaika());

		String[] nimet = {"Vastaanotto", "Basic pesu", "Premium pesu", "Vahaus", "Kuivaus"};

		// Kerätään dataa analyysiä varten
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

		// Kirjoitetaan helposti luettava raportti
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

		List<AjonPalvelupiste> pisteet = new ArrayList<>();
		int maksimiJonoKaikista = 0;
		for (int k = 0; k < palvelupisteet.length; k++) {
			Palvelupiste p = palvelupisteet[k];
			pisteet.add(new AjonPalvelupiste(
					nimet[k], palveluaikojenKa[k], palveluajatHajonta[k], p.getPalvellutAsiakkaat(), p.getAktiivinenAika(), p.getMaksimiJonopituus()));
			maksimiJonoKaikista = Math.max(maksimiJonoKaikista, p.getMaksimiJonopituus());
		}
		Simulointiajo ajo = new Simulointiajo(
				seed, saapumisvali, premiumOsuus, Kello.getInstance().getAika(),
				Asiakas.getValmistuneetAsiakkaat(), maksimiJonoKaikista,
				Asiakas.getKeskimaarainenLapimenoaika());

			new SimulointiajoDao().persist(ajo, pisteet);

		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());
		kontrolleri.naytaLoppuraportti(raportti.toString());
	}

	
}
