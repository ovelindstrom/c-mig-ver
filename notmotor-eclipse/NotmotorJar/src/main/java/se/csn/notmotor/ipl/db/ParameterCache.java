package se.csn.notmotor.ipl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.csn.ark.common.util.logging.Log;
import se.csn.common.util.cache.TimeoutCache;
import se.csn.notmotor.ipl.model.Setting;

/**
 * Denna klass fungerar som en proxy for alla parametrar som 
 * anvands av notmotorn
 */
public class ParameterCache implements ParameterKalla {

    public static final int DEFAULT_REFRESHTID = -1;
    static final String[] FALSE_STRANGAR = {"N", "NEJ", "NO", "F", "FALSE", "0"};
    static final String[] TRUE_STRANGAR = {"J", "JA", "Y", "YES", "T", "TRUE", "1"};

    private TimeoutCache cache;
    private QueryProcessor qp;
    private Log log = Log.getInstance(ParameterCache.class);

    private String tabellnamn, namnCol, beskrivningCol;

    public ParameterCache(QueryProcessor qp) {
        this(qp, DEFAULT_REFRESHTID);
    }

    public ParameterCache(QueryProcessor qp, String namnkolumn, String vardekolumn) {
        if (qp == null) {
            throw new IllegalArgumentException("QueryProcessor måste anges");
        }
        if (namnkolumn == null) {
            throw new IllegalArgumentException("Namnkolumn måste anges");
        }
        if (vardekolumn == null) {
            throw new IllegalArgumentException("Vardekolumn måste anges");
        }

        namnCol = namnkolumn;
    }

    /**
     * 
     * @param ds
     * @param parameterlivstid Anges i millisekunder
     */
    public ParameterCache(QueryProcessor qp, long refreshtidIMillis) {
        this.qp = qp;
        cache = new TimeoutCache(refreshtidIMillis);
    }

    public String getStringParam(String namn) {
        return getStringParam(namn, null);
    }

    public String getStringParam(String namn, String defaultVarde) {
        String val = (String) cache.get(namn);
        if (val == null) {
            // Cachemiss: slå mot databas
            val = qp.getString("SELECT VARDE FROM PARAMETER WHERE NAMN='" + namn + "'", defaultVarde);
            if (val != null) {
                cache.put(namn, val);
            }
        }
        return val;
    }

    public void setStringParam(String namn, String varde) {
        cache.put(namn, varde);
        // Kolla om parametern finns:
        int rader = qp.getInt("SELECT COUNT(*) FROM PARAMETER WHERE NAMN='" + namn + "'", 0);
        if (rader == 0) {
            qp.executeThrowException("INSERT INTO PARAMETER (NAMN, VARDE) VALUES ('" + namn + "', " + DAOImplBase.quoteValue(varde) + ")");
        } else {
            qp.executeThrowException("UPDATE PARAMETER SET VARDE=" + DAOImplBase.quoteValue(varde) + " WHERE NAMN='" + namn + "'");
        }
    }

    public int getIntParam(String namn, int defaultVarde) {
        String val = getStringParam(namn);
        if (val == null) {
            return defaultVarde;
        } else {
            return val.trim().length() > 0 ? Integer.parseInt(val) : 0;
        }
    }

    public void setIntParam(String namn, int varde) {
        setStringParam(namn, Integer.toString(varde));
    }

    public long getLongParam(String namn, long defaultVarde) {
        String val = getStringParam(namn);
        if (val == null) {
            return defaultVarde;
        } else {
            return val.trim().length() > 0 ? Long.parseLong(val) : 0;
        }
    }

    public void setLongParam(String namn, long varde) {
        setStringParam(namn, Long.toString(varde));
    }


    public boolean getBooleanParam(String namn, boolean defaultVarde) {
        String val = getStringParam(namn);
        if (val == null) {
            return defaultVarde;
        }
        try {
            return strToBool(val);
        } catch (IllegalArgumentException iae) {
            log.error("Felaktigt boolean-värde", iae);
            return defaultVarde;
        }
    }

    public static boolean strToBool(String value) {
        for (int i = 0;i < TRUE_STRANGAR.length;i++) {
            if (TRUE_STRANGAR[i].equalsIgnoreCase(value)) {
                return true;
            }
        }
        for (int i = 0;i < FALSE_STRANGAR.length;i++) {
            if (FALSE_STRANGAR[i].equalsIgnoreCase(value)) {
                return false;
            }
        }
        throw new IllegalArgumentException("Kunde inte konvertera '" + value + "' till true eller false");
    }

    public void setBooleanParam(String namn, boolean varde) {
        setStringParam(namn, varde ? "J" : "N");
    }

    public void reload() {
        cache.clear();
    }

    public void setRefreshtid(long refreshtidIMillis) {
        cache.setLifetimeInMilliseconds(refreshtidIMillis);
    }

    static class Entry {
        private String namn, varde;

        public Entry(String namn, String varde) {
            this.namn = namn;
            this.varde = varde;
        }

        public String getNamn() {
            return namn;
        }

        public String getVarde() {
            return varde;
        }

    }

    public static class ParamMapper implements RowToObjectMapper {
        public Object newRow(ResultSet rs) throws SQLException {
            return new Entry(rs.getString("NAMN"), rs.getString("VARDE"));
        }
    }

    public Map<String, String> getStringParamMap() {
        List names = qp.processQuery("SELECT NAMN, VARDE FROM PARAMETER", new ParamMapper() {

        });
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator it = names.iterator();it.hasNext();) {
            Entry entry = (Entry) it.next();
            map.put(entry.namn, entry.varde);
        }
        return map;
    }

    public String getDescription(String namn) {
        if (namn == null) {
            return null;
        }
        if (beskrivningCol == null) {
            return "";
        }
        return qp.getString("SELECT " + beskrivningCol + " FROM " + tabellnamn + " WHERE " + namnCol + "='" + namn + "'", null);
    }


    public Setting[] getSettings() {
        // TODO: Implementera.
        return null;
    }
}
