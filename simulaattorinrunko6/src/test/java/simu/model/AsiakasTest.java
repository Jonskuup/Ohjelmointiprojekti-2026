package simu.model;

import org.junit.jupiter.api.*;
import simu.framework.Kello;
import simu.framework.Trace;

import static org.junit.jupiter.api.Assertions.*;

class AsiakasTest {
    // Tarkistus et asiakkaiden tilastot nollautuu kun uusi simulointi alkaa
    @Test
    void nollaaTilastot() {
        Asiakas.nollaaTilastot();
        assertEquals(0, Asiakas.getValmistuneetAsiakkaat());
        assertEquals(0, Asiakas.getKeskimaarainenLapimenoaika());
    }

    // Testaus kun premium osuus on 100% että asiakas valitsee oikeasti premium pesun
    @Test
    void premiumOsuus() {
        Trace.setTraceLevel(Trace.Level.INFO);
        Asiakas.setPremiumOsuus(1.0, 5);
        Asiakas asiakas = new Asiakas();
        assertEquals(Asiakas.PesuTyyppi.PREMIUM, asiakas.getPesuTyyppi());
    }

    // Testaus kun premium osuus on 0% että asiakas saa basic pesun
    @Test
    void basicOsuus() {
        Trace.setTraceLevel(Trace.Level.INFO);
        Asiakas.setPremiumOsuus(0.0, 5);
        Asiakas asiakas = new Asiakas();
        assertEquals(Asiakas.PesuTyyppi.BASIC, asiakas.getPesuTyyppi());
    }

    // Tarkistus et uusi asiakas saa saapumisajaksi kellon nykyisen ajan
    @Test
    void asiakkaanSaapumisaika() {
        Trace.setTraceLevel(Trace.Level.INFO);
        Kello.getInstance().setAika(25);
        Asiakas asiakas = new Asiakas();
        assertEquals(25, asiakas.getSaapumisaika());
    }

    // Tarkistus et premium asiakas tunnistetaan että se on premium
    @Test
    void onPremium() {
        Trace.setTraceLevel(Trace.Level.INFO);
        Asiakas.setPremiumOsuus(1.0, 5);
        Asiakas asiakas = new Asiakas();
        assertTrue(asiakas.onPremium());
    }

    // Tarkistus et asiakkaan poistumisaika tallentuu oikein
    @Test
    void poistumisaika() {
        Trace.setTraceLevel(Trace.Level.INFO);
        Asiakas asiakas = new Asiakas();
        asiakas.setPoistumisaika(50);
        assertEquals(50, asiakas.getPoistumisaika());
    }
}