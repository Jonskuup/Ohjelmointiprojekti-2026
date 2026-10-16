package entity;


public class Simulointiajo {

    private long seed;
    private double saapumisvali;
    private double premiumOsuus;
    private double simulointiaika;

    private int valmistuneetAsiakkaat;
    private int maksimijonopituus;
    private double keskimaarainenLapimenoaika;

    public Simulointiajo(long seed, double saapumisvali, double premiumOsuus, double simulointiaika,
                         int valmistuneetAsiakkaat, int maksimijonopituus, double keskimaarainenLapimenoaika) {
        this.seed = seed;
        this.saapumisvali = saapumisvali;
        this.premiumOsuus = premiumOsuus;
        this.simulointiaika = simulointiaika;
        this.valmistuneetAsiakkaat = valmistuneetAsiakkaat;
        this.maksimijonopituus = maksimijonopituus;
        this.keskimaarainenLapimenoaika = keskimaarainenLapimenoaika;
    }

    public long getSeed() {
        return seed;
    }

    public double getSaapumisvali() {
        return saapumisvali;
    }

    public double getPremiumOsuus() {
        return premiumOsuus;
    }

    public double getSimulointiaika() {
        return simulointiaika;
    }

    public int getValmistuneetAsiakkaat() {
        return valmistuneetAsiakkaat;
    }

    public int getMaksimijonopituus() {
        return maksimijonopituus;
    }

    public double getKeskimaarainenLapimenoaika() {
        return keskimaarainenLapimenoaika;
    }
}