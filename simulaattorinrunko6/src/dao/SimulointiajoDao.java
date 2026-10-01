package dao;

import entity.Simulointiajo;
import entity.AjonPalvelupiste;
import datasource.MariaDbConnection;

import java.sql.*;
import java.util.List;

public class SimulointiajoDao {

    public void persist(Simulointiajo ajo, List<AjonPalvelupiste> pisteet) {
        Connection conn = MariaDbConnection.getConnection();

        String ajoSql = "INSERT INTO SIMULOINTIAJO " +
                "(seed, saapumisvali, premium_osuus, simulointiaika, " +
                " valmistuneet_asiakkaat, maksimijonopituus, keskimaarainen_lapimenoaika) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String pisteSql = "INSERT INTO AJON_PALVELUPISTE " +
                "(ajo_id, nimi, palveluaika_ka, palveluaika_hajonta, palvellut_asiakkaat, aktiivinen_aika, maksimijonopituus) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement ajoPs = conn.prepareStatement(ajoSql, Statement.RETURN_GENERATED_KEYS);
            ajoPs.setLong(1, ajo.getSeed());
            ajoPs.setDouble(2, ajo.getSaapumisvali());
            ajoPs.setDouble(3, ajo.getPremiumOsuus());
            ajoPs.setDouble(4, ajo.getSimulointiaika());
            ajoPs.setInt(5, ajo.getValmistuneetAsiakkaat());
            ajoPs.setInt(6, ajo.getMaksimijonopituus());
            ajoPs.setDouble(7, ajo.getKeskimaarainenLapimenoaika());
            ajoPs.executeUpdate();

            int ajoId;
            try (ResultSet avaimet = ajoPs.getGeneratedKeys()) {
                avaimet.next();
                ajoId = avaimet.getInt(1);
            }

            PreparedStatement pistePs = conn.prepareStatement(pisteSql);
            for (AjonPalvelupiste p : pisteet) {
                pistePs.setInt(1, ajoId);
                pistePs.setString(2, p.getNimi());
                pistePs.setDouble(3, p.getPalveluajanKeskiarvo());
                pistePs.setDouble(4, p.getPalveluajanHajonta());
                pistePs.setInt(5, p.getPalvellutAsiakkaat());
                pistePs.setDouble(6, p.getAktiivinenAika());
                pistePs.setInt(7, p.getMaksimijonopituus());
                pistePs.addBatch();
            }
            pistePs.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}