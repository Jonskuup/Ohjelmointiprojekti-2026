package entity;

public class AjonPalvelupiste {

    private String nimi;
    private double palveluajanKeskiarvo;
    private double palveluajanHajonta;
    private int palvellutAsiakkaat;
    private double aktiivinenAika;
    private int maksimijonopituus;

    public AjonPalvelupiste(String nimi, double palveluajanKeskiarvo, double palveluajanHajonta,
                            int palvellutAsiakkaat, double aktiivinenAika, int maksimijonopituus) {
        this.nimi = nimi;
        this.palveluajanKeskiarvo = palveluajanKeskiarvo;
        this.palveluajanHajonta = palveluajanHajonta;
        this.palvellutAsiakkaat = palvellutAsiakkaat;
        this.aktiivinenAika = aktiivinenAika;
        this.maksimijonopituus = maksimijonopituus;
    }

    public String getNimi() {
        return nimi;
    }

    public double getPalveluajanKeskiarvo() {
        return palveluajanKeskiarvo;
    }

    public double getPalveluajanHajonta() {
        return palveluajanHajonta;
    }

    public int getPalvellutAsiakkaat() {
        return palvellutAsiakkaat;
    }

    public double getAktiivinenAika() {
        return aktiivinenAika;
    }

    public int getMaksimijonopituus() {
        return maksimijonopituus;
    }
}