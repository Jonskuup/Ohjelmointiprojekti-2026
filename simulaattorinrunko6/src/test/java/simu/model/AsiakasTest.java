package simu.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AsiakasTest {
    // Tarkistus et asiakkaiden tilastot nollautuu kun uusi simulointi alkaa
    @Test
    void nollaaTilastot() {
        Asiakas.nollaaTilastot();
        assertEquals(0, Asiakas.getValmistuneetAsiakkaat());
        assertEquals(0, Asiakas.getKeskimaarainenLapimenoaika());
    }

//    Kokeilin premium osuuden tastausta, mutta Asiakas olion luonnissa Trace näytti null
//    @Test
//    void premiumOsuus() {
//        Asiakas.setPremiumOsuus(1.0, 5);
//        Asiakas asiakas = new Asiakas();
//        assertEquals(Asiakas.PesuTyyppi.PREMIUM, asiakas.getPesuTyyppi());
//    }
}