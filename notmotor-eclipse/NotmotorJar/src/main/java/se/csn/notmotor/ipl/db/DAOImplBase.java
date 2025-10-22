package se.csn.notmotor.ipl.db;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DAOImplBase implements RowToObjectMapper {

	private static final String DB2_TIMESTAMP_FORMAT = "yyyy-MM-dd-HH.mm.ss.000000";
	protected QueryProcessor qp;

	public DAOImplBase(QueryProcessor qp) {
		this.qp = qp;
	}

	public QueryProcessor getQueryProcessor() {
		return qp;
	}

	/**
	 * Skapar ett kolumn-nyckelpar typen COLNAMN='VARDE'
	 * 
	 * @param columnname Kolumnnamn
	 * @param value      Värde
	 * @return null om värdet är null, annars
	 */
	String makeColumnValuePair(String columnname, Object value) {
		if (value == null) {
			return columnname + " IS NULL";
		} else {
			return columnname + "=" + quoteValue(value);
		}
	}

	String makeUpdateQuery(String tablename, Object[] columnValuePairs, Object[] whereColumnValuePair) {
		if ((columnValuePairs == null) || (columnValuePairs.length < 2)) {
			throw new IllegalArgumentException("Måste ange kolumner som ska uppdateras");
		}
		if ((tablename == null) || (tablename.length() == 0)) {
			throw new IllegalArgumentException("Måste ange tabellnamn");
		}

		String query = "UPDATE " + tablename + " SET ";
		String cols = "";
		for (int i = 0; i < columnValuePairs.length; i += 2) {
			if (columnValuePairs[i + 1] == null) {
				cols += ((String) columnValuePairs[i] + "=null,");
			} else {
				cols += (makeColumnValuePair((String) columnValuePairs[i], columnValuePairs[i + 1]) + ",");
			}
		}

		cols = cols.substring(0, cols.length() - 1);

		String where = "";
		if (whereColumnValuePair != null) {
			if (whereColumnValuePair.length != 2) {
				throw new IllegalArgumentException("whereColumnValuePair ska innehålla kolumnnamn och värde");
			}
			where = " WHERE " + makeColumnValuePair((String) whereColumnValuePair[0], whereColumnValuePair[1]);
		}

		return query + cols + where;
	}

	/**
	 * Formaterar ett värde för användning i SQL-frågor.
	 *
	 * @param value Det värde som ska formateras för SQL
	 * @return en SQL-formaterad sträng, t.ex. omgiven med '' om sträng. null
	 *         returneras som "NULL".
	 */
	public static String quoteValue(Object value) {
		if (value == null) {
			return "NULL";
		}
		if (value instanceof String) {
			return "'" + (String) value + "'";
		} else if (value instanceof Integer) {
			return "" + ((Integer) value).intValue();
		} else if (value instanceof Long) {
			return "" + ((Long) value).longValue();
		} else if (value instanceof Boolean) {
			if (((Boolean) value).booleanValue()) {
				return "TRUE";
			} else {
				return "FALSE";
			}
		} else if (value instanceof Date) {
			// Formatera för timestamp:
			SimpleDateFormat sdf = new SimpleDateFormat(DB2_TIMESTAMP_FORMAT);
			return "TIMESTAMP('" + sdf.format((Date) value) + "')";
		} else {
			return value.toString();
		}
	}

	int getInt(Integer i, int defaultValue) {
		if (i != null) {
			return i.intValue();
		} else {
			return defaultValue;
		}
	}

	public static String addRestriction(String where, String colname, String operator, Object value) {
		return addRestriction(where, colname, operator, value, true);
	}

	public static String addRestriction(String where, String colname, String operator, Object value,
			boolean ignoreNull) {
		if (value != null) {
			if (where == null) {
				where = "";
			}
			if (where.length() > 0) {
				where = where + " AND ";
			}
			return where + "(" + colname + " " + operator + " " + quoteValue(value) + ")";
		}
		if (ignoreNull) {
			// Lägg inte till något villkor:
			return where;
		} else {
			if (where.length() > 0) {
				where = where + " AND ";
			}
			return where + "(" + colname + " IS NULL)";
		}
	}

	/**
	 * 
	 * @param where                 Den villkorssträng till vilken det nya
	 *                              LIKE-villkort ska
	 *                              adderas (med AND om det redan finns ett villkor)
	 * @param colname               Kolumnnamn i villkoret
	 * @param value                 Villkorssträngen. Om den är null så skapas inget
	 *                              LIKE-villkor.
	 * @param surroundWithWildcards Om true så ska villkoret omges med wildcard (%)
	 *                              såvida det inte redan fanns wildcard i början
	 *                              eller slut.
	 * @return en SQL-formaterad sträng för LIKE-villkoret
	 */
	public static String addLike(String where, String colname, String value, boolean surroundWithWildcards) {
		if (value == null) {
			return where;
		}
		if (value.length() == 0) {
			return where;
		}

		if (surroundWithWildcards) {
			if (value.charAt(0) != '%') {
				value = "%" + value;
			}
			if (value.charAt(value.length() - 1) != '%') {
				value += "%";
			}
		}
		return addRestriction(where, colname, "LIKE", value);
	}

	public static String addRestriction(String where, String restriction) {
		if (restriction == null) {
			// Lägg inte till något villkor:
			return where;
		}
		if (where.length() > 0) {
			where = where + " AND ";
		}
		return where + "(" + restriction + ")";
	}

}
