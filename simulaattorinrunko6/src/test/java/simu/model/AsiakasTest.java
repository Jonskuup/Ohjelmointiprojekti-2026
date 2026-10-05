package simu.model;

import org.junit.jupiter.api.*;
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
}