package se.csn.notmotor.ipl.db;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.csn.ark.common.dal.db.DatabaseException;
import se.csn.ark.common.util.logging.Log;
import se.csn.notmotor.ipl.model.Avsandare;
import se.csn.notmotor.ipl.model.Bilaga;
import se.csn.notmotor.ipl.model.Kanal;
import se.csn.notmotor.ipl.model.Meddelande;
import se.csn.notmotor.ipl.model.MeddelandeHandelse;
import se.csn.notmotor.ipl.model.Mottagare;

/**
 * CRUD for DTOMeddelande.
 */
public class DAOMeddelandeImpl extends DAOImplBase implements DAOMeddelande {

    private DAOAvsandare avsandareHandler;
    private DAOMottagare mottagareHandler;
    private DAOBilaga bilagaHandler;
    private DAOHandelse handelseHandler;
    private Log log = Log.getInstance(DAOMeddelandeImpl.class);

    static class IdMapper implements RowToObjectMapper {

        public String newRow(ResultSet rs) throws SQLException {
            return rs.getString(1);
        }
    }

    public long createMeddelande(Meddelande m) {
        // Måste finnas: minst en mottagare, avsändare.
        if (m == null) {
            throw new IllegalArgumentException("Meddelande måste vara satt");
        }
        if (m.getAvsandare() == null) {
            throw new IllegalArgumentException("Avsändare måste vara satt");
        }
        if ((m.getMottagare() == null) || (m.getMottagare().length == 0)) {
            throw new IllegalArgumentException("Måste finnas minst en mottagare");
        }

        // Skapa avsändare (eller återanvänd om det finns en):
        int avsandarid = avsandareHandler.getId(m.getAvsandare());
        if (avsandarid == -1) {
            avsandarid = avsandareHandler.createAvsandare(m.getAvsandare());
        }

        // Skapa meddelanderad:
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = qp.getConnection();
            ((ControlledCommitQueryProcessor) qp).setCommitConnection(conn, false);

            // Vi håller ihop transaktionen så att alla inserts/updates görs samtidigt:

            String query = "INSERT INTO MEDDELANDE (ID,AVSANDARE,KANAL,RUBRIK,TEXT,"
                + "RUBRIKENCODING,TEXTENCODING,CALLBACKURL,CALLBACKMASK,CSNNUMMER,SKAPADTIDPUNKT,STATUS,SKICKATIDIGAST,MIMETYP) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (Iterator it = qp.getQueryListeners().iterator();it.hasNext();) {
                QueryListener ql = (QueryListener) it.next();
                ql.sqlQuery(query);
            }

            long id = qp.getCounter("SEKVENS", "MEDDELANDEID");
            ps = conn.prepareStatement(query);
            ps.setLong(1, id);
            ps.setInt(2, avsandarid);
            ps.setString(3, m.getKanal());
            ps.setString(4, m.getRubrik());
            if (m.getMeddelandetext() != null) {
                ps.setCharacterStream(5, new StringReader(m.getMeddelandetext()), m.getMeddelandetext().length());
            } else {
                ps.setCharacterStream(5, new StringReader(""), 0);
            }
            ps.setString(6, m.getRubrikEncoding());
            ps.setString(7, m.getMeddelandeEncoding());
            ps.setString(8, m.getCallbackURL());
            int mask = 0;
            if (m.getCallbackMask() != null) {
                mask = m.getCallbackMask().intValue();
            }
            ps.setInt(9, mask);
            int csnnummer = 0;
            if (m.getCsnnummer() != null) {
                csnnummer = m.getCsnnummer().intValue();
            }
            ps.setInt(10, csnnummer);
            Date datum = m.getSkapad();
            if (datum == null) {
                datum = new Date();
            }
            ps.setTimestamp(11, new Timestamp(datum.getTime()));
            ps.setInt(12, MeddelandeHandelse.MOTTAGET);
            Timestamp skickaTidigast = null;
            if (m.getSkickaTidigast() != null) {
                skickaTidigast = new Timestamp(m.getSkickaTidigast().getTime());
            }
            ps.setTimestamp(13, skickaTidigast);
            ps.setString(14, m.getMimetyp());
            int result = ps.executeUpdate();

            if (result != 1) {
                throw new SQLException("INSERT i MEDDELANDE returnerade " + result);
            }

            // Skapa mottagare:
            for (int i = 0;i < m.getMottagare().length;i++) {
                mottagareHandler.createMottagare(m.getMottagare()[i], id);
            }

            // Skapa bilagor:
            if (m.getBilagor() != null) {
                for (int i = 0;i < m.getBilagor().length;i++) {
                    bilagaHandler.createBilaga(m.getBilagor()[i], id);
                }
            }

            // Skapa händelserader:
            if (m.getHandelser() != null) {
                for (int i = 0;i < m.getHandelser().length;i++) {
                    handelseHandler.createHandelse(m.getHandelser()[i], id);
                }
            }
            conn.commit();
            m.setId(Long.valueOf(id));
            return id;
        } catch (SQLException e) {
            log.error("Kunde inte skapa meddelande i databasen: ", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    log.error("Försökte köra rollback men misslyckades", e2);
                }
            }
            throw new IllegalStateException("Kunde inte skapa meddelande i databasen", e);
        } catch (DatabaseException de) {
            log.error("Kunde inte skapa meddelande i databasen: ", de);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    log.error("Försökte köra rollback men misslyckades", e2);
                }
            }
            throw new IllegalStateException("Kunde inte skapa meddelande i databasen" + de);
        } finally {
            try {
                ((ControlledCommitQueryProcessor) qp).setCommitConnection(conn, true);
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new IllegalStateException("Kunde inte stänga resurser");
            }
        }
    }

    /**
     * Letar ratt pa meddelanderader som har status MOTTAGET och satter om
     * deras status till -instans, detta for att markera att de ska hanteras
     * av en viss instans.
     * <p>
     * Parametern 'kanalerMedBegransning' listar de inkommande kanaler som ar
     * nedprioriterade och dar mangden meddelanden som far bearbetas ar begransad.
     * Varje element i listan ar av typen Kanal och innehaller uppgifter om kanalens
     * oppettider och hur manga meddelanden fran kanalen som far bearbetas.
     * </p>
     * <p>
     * En kanal med begransningar kommer alltid att bearbetas efter kanaler utan
     * begransningar, dvs ha en lagre prioritet. Om det finns flera begransade kanaler kommer de
     * att bearbetas i den turordning som anges i listan 'kanalerMedBegransning'.
     * </p>
     * <p>
     * Metoden markerar SOM MEST det angivna antalet meddelanden. Det kan bli farre
     * aven om det finns fler meddelanden an de markerade i status MOTTAGET; det
     * beror pa hur det gatt med tidigare sandningar mm. Om det finns meddelanden
     * med status MOTTAGET (och en kanal som inte begransar utskick) sa kommer
     * minst ett av dem att markeras.
     * <p>
     * OBS! Metoden kommer att gora commit() for att sakerstalla att det inte
     * blir deadlock. Deadlock kan annars potentiellt intraffa i kombinationen
     * MEDDELANDE - HANDELSE.
     * </p>
     * 
     * @param instans                 Den instans som markeras som "ägare" till
     *                                meddelandena
     * @param antalMeddelanden        Det maximala antal meddelanden som markeras
     * @param kanalerMedBegransningar Lista med nedprioriterade inkanaler med
     *                                särskilda begränsningar
     */
    @SuppressWarnings("unchecked")
    public void markeraMeddelandenForInstans(int instans, int antalMeddelanden, List<Kanal> kanalerMedBegransningar) {
        if (instans < 1) {
            throw new IllegalArgumentException("Instans måste vara större än 1");
        }
        if (antalMeddelanden < 1) {
            throw new IllegalArgumentException("Måste hämta minst 1 meddelande");
        }
        if (kanalerMedBegransningar == null) {
            throw new IllegalArgumentException("Listan med kanaler får inte vara null");
        }

        // 1. SELECT:
        List<String> meddelandeNr = new ArrayList<String>();

        // Hämta meddelanden som ligger på en kanal utan nedprioritering
        String sql = "SELECT ID FROM MEDDELANDE WHERE STATUS = " + MeddelandeHandelse.MOTTAGET;
        if (kanalerMedBegransningar.size() > 0) {
            StringBuffer inClause = new StringBuffer();
            for (Kanal kanal : kanalerMedBegransningar) {
                if (inClause.length() > 0) {
                    inClause.append(',');
                }
                inClause.append("'" + kanal.getNamn() + "'");
            }
            sql += " AND (KANAL IS NULL OR KANAL NOT IN (" + inClause.toString() + "))";
        }
        sql += " AND ((SKICKATIDIGAST IS NULL) OR (SKICKATIDIGAST <= CURRENT TIMESTAMP))"
            + " FETCH FIRST " + antalMeddelanden + " ROWS ONLY";
        meddelandeNr.addAll(qp.processQuery(sql, new IdMapper()));

        // Hämta meddelanden som ligger på nedprioriterade kanaler
        for (Kanal kanal : kanalerMedBegransningar) {
            // Kontrollera att kanalen är öppen
            if (kanal.isOppen()) {
                // Kontrollera om kanalen är sovande
                if (kanal.isSovande()) {
                    if (log.isDebugEnabled()) {
                        log.info("Kanal " + kanal.getNamn() + " sover. "
                            + kanal.getSovtidKvarMillisekunder() + " ms kvar till nästa batch.");
                    }
                    break;
                }
                kanal.setSoverTimestamp(null);

                // Beräkna hur många meddelanden som får markeras i aktuell kanal
                int antalSenasteTimmen = getMarkeradeSenasteTimmen(kanal.getNamn());
                int maxAntalPerTimme = kanal.getMaxAntalPerTimme() >= 0 ? kanal.getMaxAntalPerTimme()
                    : Integer.MAX_VALUE;
                int batchstorlek = kanal.getBatchStorlek() >= 0 ? kanal.getBatchStorlek() : Integer.MAX_VALUE;
                int kvarAttSkickaDennaTimme = maxAntalPerTimme - antalSenasteTimmen;
                int kvarAttSkickaIBatch = kanal.getBatchKvar() >= 0 ? kanal.getBatchKvar() : Integer.MAX_VALUE;
                int maxAntal = antalMeddelanden - meddelandeNr.size();
                int antal = Math.min(Math.min(kvarAttSkickaIBatch, batchstorlek),
                    Math.min(kvarAttSkickaDennaTimme, maxAntal));
                /*
                 * if (log.isDebugEnabled()) {
                 * log.debug(kanal.getNamn() + ":"
                 * + "\nmarkerade senaste timmen = " + antalSenasteTimmen
                 * + "\nmax per timme = " + maxAntalPerTimme
                 * + "\nbatchstorlek = " + batchstorlek
                 * + "\nkvar att skicka denna timme = " + kvarAttSkickaDennaTimme
                 * + "\nkvar att skicka i aktuell batch = " + kvarAttSkickaIBatch
                 * + "\nmax att markera (alla kanaler) = " + antalMeddelanden
                 * + "\nredan markerat (alla kanaler) = " + meddelandeNr.size()
                 * + "\nantal att markera = " + antal);
                 * }
                 */
                if (antal > 0) {
                    sql = "SELECT ID FROM MEDDELANDE WHERE STATUS = " + MeddelandeHandelse.MOTTAGET
                        + " AND KANAL = '" + kanal.getNamn() + "'"
                        + " AND ((SKICKATIDIGAST IS NULL) OR (SKICKATIDIGAST <= CURRENT TIMESTAMP))"
                        + " FETCH FIRST " + antal + " ROWS ONLY";
                    List<String> resultat = qp.processQuery(sql, new IdMapper());
                    meddelandeNr.addAll(resultat);
                    kanal.setAntalMarkerade(resultat.size());
                }
            }
        }

        // Avbryt om inget meddelande hittats
        if (meddelandeNr.isEmpty()) {
            return;
        }

        // 2. UPDATE WHERE IN:
        String inClause = StringUtils.join(meddelandeNr.toArray(), ',');
        log.debug("Meddelande IDn : " + inClause);
        sql = "UPDATE MEDDELANDE SET STATUS=" + (-instans) + " WHERE ID IN (" + inClause + ")";

        int updated = qp.executeThrowException(sql);

        try {
            qp.getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Kunde inte commita" + e);
        }

        if (log.isDebugEnabled()) {
            String msg = "Markerade " + updated + " rader för instans " + instans;
            if (kanalerMedBegransningar.size() > 0) {
                msg += " varav:";
                for (Kanal kanal : kanalerMedBegransningar) {
                    msg += "\n" + kanal.getAntalMarkerade() + " på kanal '" + kanal.getNamn() + "'";
                }
            }
            log.debug(msg);
        }
    }

    /**
     * Returnerar antal meddelanden som markerats for utskick senaste timmen
     * fran en given inkanal. Antalet ar summan av de meddelanden som skickats
     * och darmed fatt en skickat-tidsstampel plus de meddelanden som har
     * markerats av nagon process och haller pa att skickas, men annu ej fatt nagon
     * skickat-tidsstampel.
     * 
     * @param kanal namn på kanal att hämta uppgifter om
     * @return antal meddelanden som markerats/sänts senaste timmen
     */
    private int getMarkeradeSenasteTimmen(String kanal) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        String enTimmeSedan = DAOImplBase.quoteValue(cal.getTime());
        String sql = "SELECT COUNT(*) FROM MEDDELANDE WHERE KANAL = '" + kanal + "'"
            + " AND (SANTTIDPUNKT >= " + enTimmeSedan + " OR STATUS < 0)";
        return qp.getInt(sql, 0);
    }

    /**
     * Soker ut de meddelanden som har status satt till -instans,
     * alltsa de meddelanden som markerats med metoden markeraMeddelandenForInstans.
     */
    public List getMarkeradeMeddelanden(int instans) {
        List list = qp.processQuery("SELECT ID, AVSANDARE, KANAL, RUBRIK, TEXT, "
            + "RUBRIKENCODING, TEXTENCODING, CALLBACKURL, CALLBACKMASK, CSNNUMMER, SKAPADTIDPUNKT, "
            + "SANTTIDPUNKT, SKICKATIDIGAST, MIMETYP "
            + "FROM MEDDELANDE WHERE STATUS=" + (-instans), this);
        return list;
    }

    public DAOMeddelandeImpl(QueryProcessor qp, DAOAvsandare ah,
                             DAOMottagare mh, DAOBilaga bh, DAOHandelse hh) {
        super(qp);
        avsandareHandler = ah;
        mottagareHandler = mh;
        bilagaHandler = bh;
        handelseHandler = hh;
    }

    public Object newRow(ResultSet rs) throws SQLException {
        Meddelande m = new Meddelande();
        long id = rs.getLong("ID");
        m.setId(Long.valueOf(id));

        m.setRubrik(rs.getString("RUBRIK"));
        m.setMeddelandetext(qp.getClob(rs, "TEXT"));
        m.setRubrikEncoding(rs.getString("RUBRIKENCODING"));
        m.setMeddelandeEncoding(rs.getString("TEXTENCODING"));

        m.setCallbackURL(rs.getString("CALLBACKURL"));
        m.setCallbackMask(Integer.valueOf(rs.getInt("CALLBACKMASK")));

        m.setCsnnummer(Integer.valueOf(rs.getInt("CSNNUMMER")));
        m.setSkapad(rs.getTimestamp("SKAPADTIDPUNKT"));
        m.setSkickat(rs.getTimestamp("SANTTIDPUNKT"));
        m.setSkickaTidigast(rs.getTimestamp("SKICKATIDIGAST"));
        m.setMimetyp(rs.getString("MIMETYP"));
        m.setKanal(rs.getString("KANAL"));

        // Hämta avsändare:
        m.setAvsandare(avsandareHandler.getAvsandare(rs.getInt("AVSANDARE")));

        // Hämta mottagare:
        List<Mottagare> mottagare = mottagareHandler.getMottagareForMeddelande(id);
        m.setMottagare((Mottagare[]) mottagare.toArray(new Mottagare[0]));

        // Hämta bilagor:
        List<Bilaga> bilagor = bilagaHandler.getBilagorForMeddelande(id);
        m.setBilagor((Bilaga[]) bilagor.toArray(new Bilaga[0]));

        // Hämta händelser:
        List<MeddelandeHandelse> handelser = handelseHandler.getHandelserForMeddelande(id);
        m.setHandelser((MeddelandeHandelse[]) handelser.toArray(new MeddelandeHandelse[0]));

        return m;
    }

    public Meddelande getMeddelande(long meddelandeId) {
        List list = qp.processQuery("SELECT * FROM MEDDELANDE WHERE ID=" + meddelandeId, this);
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new IllegalStateException("Hittade mer än ett meddelande med id " + meddelandeId);
        }
        return (Meddelande) list.get(0);
    }

    public void deleteMeddelande(long meddelandeId) {
        qp.executeThrowException("DELETE FROM MEDDELANDE WHERE ID = " + meddelandeId);
    }

    public void updateMeddelande(Meddelande meddelande) {
        // TODO: Implementera
    }

    public Meddelande[] sokMeddelanden(
            Date from,
            Date tom,
            Avsandare[] avsandare,
            Mottagare[] mottagare,
            String textinnehall,
            Integer minstorlek,
            Integer maxstorlek,
            Integer handelseMask,
            Integer felmask,
            Bilaga[] bilagor) {
        // Bygg SQL-satsen.
        // String sql = "SELECT * FROM MEDDELANDE WHERE ";
        String where = "";
        where = addRestriction(where, "SKAPADTIDPUNKT", ">=", from);
        where = addRestriction(where, "SKAPADTIDPUNKT", "<=", tom);

        // Bygg in-lista för avsändare:
        if ((avsandare != null) && (avsandare.length > 0)) {
            String inAvsandare = "AVSANDARE IN ";
            for (int i = 0;i < avsandare.length;i++) {
                if (avsandare[i] == null) {
                    continue;
                }
                Long id = avsandare[i].getId();
                if (id == null) {
                    throw new IllegalArgumentException("Felaktig avsändare, sökning görs på ID: " + avsandare[i]);
                }
                inAvsandare = inAvsandare + id.intValue() + ",";
            }
            if (inAvsandare.length() > "AVSANDARE IN ".length()) {
                inAvsandare = inAvsandare.substring(0, inAvsandare.length() - 1);
                where = addRestriction(where, inAvsandare);
            }
        }

        // // Bygg fråga för mottagare.
        // if ((mottagare != null) && (mottagare.length > 0)) {
        //
        // }

        // textinnehåll:
        if (textinnehall != null) {
            where = addRestriction(where, "TEXT LIKE '%" + textinnehall + "%'");
        }

        where = addRestriction(where, "SIZE(TEXT)", ">=", minstorlek);
        where = addRestriction(where, "SIZE(TEXT)", "<=", maxstorlek);

        // Bygg fråga för händelser och status:

        return null;
    }
}
