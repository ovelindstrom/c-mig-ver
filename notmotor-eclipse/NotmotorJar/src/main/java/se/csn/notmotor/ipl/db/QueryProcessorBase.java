package se.csn.notmotor.ipl.db;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.csn.ark.common.dal.db.DatabaseException;

public abstract class QueryProcessorBase implements QueryProcessor {

	protected List<QueryListener> listeners;
	protected boolean handleConnection = true;

	public QueryProcessorBase() {
		listeners = new ArrayList<QueryListener>();
	}

	public int executeSafely(String SQL) {
		try {
			return executeThrowException(SQL);
		} catch (DatabaseException re) {
			return -1;
		}
	}

	public int executeThrowException(String SQL) {
		Statement st = null;
		Connection conn = null;
		int result = -1;
		try {
			conn = getConnection();
			st = conn.createStatement();
			reportSql("executeThrowException: " + SQL);
			result = st.executeUpdate(SQL); // NOSONAR
		} catch (SQLException sqle) {
			reportError("executeThrowException: " + sqle);
			throw new DatabaseException("executeThrowException() failed: " + sqle);
		} finally {
			handleResources(null, st, conn);
		}
		return result;
	}

	public byte[] getBlob(ResultSet rs, String colName) throws SQLException {
		Blob blob = rs.getBlob(colName);
		return blob.getBytes(1, (int) blob.length());
	}

	public String getClob(ResultSet rs, String colName) throws SQLException {
		Clob clob = rs.getClob(colName);
		return clob.getSubString(1, (int) clob.length());
	}

	public int getInt(String SQL, int returnIfNoResult) {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			reportSql("getInt: " + SQL);
			rs = st.executeQuery(SQL); // NOSONAR
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return returnIfNoResult;
			}
		} catch (SQLException sqle) {
			reportError("getInt: " + sqle);
			throw new DatabaseException("getInt() failed: " + sqle);
		} finally {
			handleResources(rs, st, conn);
		}
	}

	public long getLong(String SQL, long returnIfNoResult) {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			reportSql("getLong: " + SQL);
			rs = st.executeQuery(SQL); // NOSONAR
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				return returnIfNoResult;
			}
		} catch (SQLException sqle) {
			reportError("getLong: " + sqle);
			throw new DatabaseException("Execute failed: " + sqle);
		} finally {
			handleResources(rs, st, conn);
		}
	}

	public boolean getBoolean(String SQL, boolean returnIfNoResult) {
		int res = getInt(SQL, Integer.MIN_VALUE);
		if (res == Integer.MIN_VALUE) {
			return returnIfNoResult;
		} else {
			return (res == 1);
		}
	}

	public String getString(String SQL, String returnIfNoResult) {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			reportSql("getString: " + SQL);
			rs = st.executeQuery(SQL); // NOSONAR
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return returnIfNoResult;
			}
		} catch (SQLException sqle) {
			reportError("getString: " + sqle);
			throw new DatabaseException("Execute failed: " + sqle);
		} finally {
			handleResources(rs, st, conn);
		}
	}

	public String[] getStringArray(String SQL, String[] returnIfNoResult) {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			reportSql("getStringArray: " + SQL);
			rs = st.executeQuery(SQL); // NOSONAR
			if (rs.next()) {
				ResultSetMetaData meta = rs.getMetaData();
				String[] res = new String[meta.getColumnCount()];
				for (int i = 1; i <= res.length; i++) {
					res[i] = rs.getString(i);
				}
				return res;
			} else {
				return returnIfNoResult;
			}
		} catch (SQLException sqle) {
			reportError("getStringArray: " + sqle);
			throw new DatabaseException("Execute failed: " + sqle);
		} finally {
			handleResources(rs, st, conn);
		}
	}

	/**
	 * Skapar java-objekt från en databasfråga.
	 * 
	 * @param SQL    sql-fråga
	 * @param mapper Ett objekt som kan översätta en rad i svaret till
	 *               ett Java-objekt
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List processQuery(String SQL, RowToObjectMapper mapper) {
		Statement st = null;
		ResultSet rs = null;
		List list = new ArrayList();
		Connection c = null;
		try {
			c = getConnection();
			st = c.createStatement();
			reportSql("processQuery: " + SQL);
			rs = st.executeQuery(SQL); // NOSONAR
			while (rs.next()) {
				list.add(mapper.newRow(rs));
			}
		} catch (SQLException sqle) {
			reportError("processQuery: " + sqle);
			throw new DatabaseException("Execute failed: " + sqle);
		} finally {
			handleResources(rs, st, c);
		}
		return list;
	}

	public Object getObject(String SQL, RowToObjectMapper mapper) {
		List list = processQuery(SQL, mapper);
		if (list.size() == 0) {
			return null;
		} else if (list.size() > 1) {
			throw new IllegalArgumentException("Frågan " + SQL + " returnerade mer än en rad");
		} else {
			return list.get(0);
		}
	}

	public long getSequence(String sequenceName) {
		return getLong("VALUES NEXTVAL FOR " + sequenceName, -1);
	}

	public long getCounter(String table, String counterName, String nameCol, String valueCol) {

		String sequenceName = counterName;
		if (sequenceName.endsWith("ID")) {
			sequenceName = sequenceName.substring(0, sequenceName.length() - 2);
		}
		if (!sequenceName.endsWith("_SEQ")) {
			sequenceName = sequenceName + "_SEQ";
		}
		return getSequence(sequenceName);

		/*
		 * // TODO: Snygga till om detta funkar.
		 * Statement st = null;
		 * ResultSet rs = null;
		 * Connection conn = null;
		 * boolean autocommit = false;
		 * String sql = null;
		 * try {
		 * 
		 * conn = getConnection();
		 * autocommit = conn.getAutoCommit();
		 * conn.setAutoCommit(false);
		 * st = conn.createStatement();
		 * long counter = 1;
		 * sql = "SELECT " + valueCol + " FROM " + table + " WHERE " + nameCol + "='" +
		 * counterName +"' FOR UPDATE";
		 * reportSql("getCounter: " + sql);
		 * rs = st.executeQuery(sql);
		 * if(rs.next()) {
		 * counter = rs.getLong(1);
		 * sql = "UPDATE " + table + " SET " + valueCol + "=" + (counter+1) + " WHERE "
		 * + nameCol + "='" + counterName + "'";
		 * reportSql("getCounter: " + sql);
		 * st.executeUpdate(sql);
		 * } else {
		 * sql = "INSERT INTO " + table + " (" + nameCol + "," + valueCol +
		 * ") VALUES ('" + counterName + "', 2)";
		 * reportSql("getCounter: " + sql);
		 * st.executeUpdate(sql);
		 * }
		 * conn.commit();
		 * conn.setAutoCommit(autocommit);
		 * return counter;
		 * } catch(SQLException sqle) {
		 * reportError("getCounter: " + sqle);
		 * throw new DatabaseException("Execute failed: " + sqle);
		 * } finally {
		 * handleResources(rs, st, conn);
		 * }
		 */
	}

	public long getCounter(String table, String counterName) {
		return getCounter(table, counterName, "NAMN", "VARDE");
	}

	public abstract Connection getConnection();

	/**
	 * Implementera denna metod för att fullborda resurshanteringen.
	 * Den anropas efter varje sql-anrop.
	 * 
	 * @param conn Använd connection. Kan vara null.
	 */
	protected abstract void handleConnection(Connection conn) throws SQLException;

	protected void handleResources(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null) {
				// log.debug("Closing resultset");
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (handleConnection) {
				handleConnection(conn);
			}
		} catch (SQLException sqle) {
			reportError("handleResources: " + sqle);
			throw new IllegalStateException("Kunde inte stänga resurser: " + sqle);
		}
	}

	public void addQueryListener(QueryListener listener) {
		listeners.add(listener);
	}

	public void removeQueryListener(QueryListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	public List getQueryListeners() {
		return listeners;
	}

	protected void reportSql(String sql) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			QueryListener ql = (QueryListener) it.next();
			ql.sqlQuery(sql);
		}
	}

	protected void reportError(String error) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			QueryListener ql = (QueryListener) it.next();
			ql.sqlError(error);
		}
	}

}
