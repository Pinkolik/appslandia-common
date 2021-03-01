// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.easyrecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.StatementImpl;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DbManager implements AutoCloseable {

	private Connection conn;
	private boolean internalConn;
	private Map<String, StatCache> statCache = new LinkedHashMap<>();

	public DbManager() throws SQLException {
		this(DbManager.getDataSource());
	}

	public DbManager(Connection conn) {
		this.conn = conn;
		this.internalConn = false;
	}

	public DbManager(DataSource dataSource) throws SQLException {
		this.conn = dataSource.getConnection();
		this.internalConn = true;
	}

	public Connection getConnection() {
		return this.conn;
	}

	protected Object toSqlObject(Object val, int sqlType) {
		if (val == null) {
			return null;
		}
		if (Date.class.isAssignableFrom(val.getClass())) {
			if ((val.getClass() == java.sql.Date.class) || (val.getClass() == java.sql.Time.class) || (val.getClass() == java.sql.Timestamp.class)) {
				return val;
			}
			if (sqlType == Types.TIME) {
				return new java.sql.Time(((Date) val).getTime());
			}
			if (sqlType == Types.TIMESTAMP) {
				return new java.sql.Timestamp(((Date) val).getTime());
			}
			return new java.sql.Date(((Date) val).getTime());
		}
		return val;
	}

	public long insert(Record record, Table table) throws SQLException {
		return this.insert(record, table, false);
	}

	public void insertBatch(Record record, Table table) throws SQLException {
		this.insert(record, table, true);
	}

	protected void setParameter(StatementImpl stat, String parameterName, Object val, int sqlType) throws SQLException {
		if (val != null) {
			stat.setObject(parameterName, val);
		} else {
			if (sqlType > 0) {
				stat.setNull(parameterName, sqlType);
			} else {
				stat.setObject(parameterName, null);
			}
		}
	}

	protected StatCache getStatCache(String tableName) {
		StatCache cache = this.statCache.get(tableName);
		if (cache == null) {
			cache = new StatCache();
			this.statCache.put(tableName, cache);
		}
		return cache;
	}

	protected long insert(Record record, Table table, boolean addBatch) throws SQLException {
		this.assertNotClosed();
		StatCache cache = getStatCache(table.getName());
		if (cache.insertStat == null) {
			cache.insertStat = new StatementImpl(this.conn, table.getInsertSql(), (table.getAutoKey() != null));
		}

		for (Field field : table.getFields()) {
			if (!field.isAutoKey()) {
				Object val = record.get(field.getName());
				val = this.toSqlObject(val, field.getSqlType());
				setParameter(cache.insertStat, field.getName(), val, field.getSqlType());
			}
		}

		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = cache.insertStat.executeUpdate();
			if (table.getAutoKey() != null) {
				try (ResultSet rs = cache.insertStat.getGeneratedKeys()) {
					if (rs.next()) {
						Object generatedKey = rs.getObject(1);
						record.set(table.getAutoKey().getName(), ((Number) generatedKey).longValue());
						return record.get(table.getAutoKey().getName());
					}
				}
			}
		} else {
			cache.insertStat.addBatch();
		}
		return rowAffected;
	}

	public int update(Record record, Table table) throws SQLException {
		return this.update(record, table, false);
	}

	public void updateBatch(Record record, Table table) throws SQLException {
		this.update(record, table, true);
	}

	protected int update(Record record, Table table, boolean addBatch) throws SQLException {
		this.assertNotClosed();
		StatCache cache = getStatCache(table.getName());
		if (cache.updateStat == null) {
			cache.updateStat = new StatementImpl(this.conn, table.getUpdateSql());
		}
		for (Field field : table.getFields()) {
			if (field.isKey() || field.isUpdatable()) {
				Object val = record.get(field.getName());
				val = this.toSqlObject(val, field.getSqlType());
				setParameter(cache.updateStat, field.getName(), val, field.getSqlType());
			}
		}
		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = cache.updateStat.executeUpdate();
		} else {
			cache.updateStat.addBatch();
		}
		return rowAffected;
	}

	public int delete(Record key, Table table) throws SQLException {
		return this.delete(key, table, false);
	}

	public void deleteBatch(Record key, Table table) throws SQLException {
		this.delete(key, table, true);
	}

	protected int delete(Record key, Table table, boolean addBatch) throws SQLException {
		this.assertNotClosed();
		StatCache cache = getStatCache(table.getName());
		if (cache.deleteStat == null) {
			cache.deleteStat = new StatementImpl(this.conn, table.getDeleteSql());
		}
		for (Field field : table.getFields()) {
			if (field.isKey()) {
				Object val = key.get(field.getName());
				val = this.toSqlObject(val, field.getSqlType());
				setParameter(cache.deleteStat, field.getName(), val, field.getSqlType());
			}
		}
		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = cache.deleteStat.executeUpdate();
		} else {
			cache.deleteStat.addBatch();
		}
		return rowAffected;
	}

	public Record getRecord(Record key, Table table) throws SQLException {
		this.assertNotClosed();
		StatCache cache = getStatCache(table.getName());
		if (cache.getStat == null) {
			cache.getStat = new StatementImpl(this.conn, table.getGetSql());
		}
		for (Field field : table.getFields()) {
			if (field.isKey()) {
				Object val = key.get(field.getName());
				val = this.toSqlObject(val, field.getSqlType());
				setParameter(cache.getStat, field.getName(), val, field.getSqlType());
			}
		}
		return RecordUtils.executeSingle(cache.getStat);
	}

	public boolean exists(Record key, Table table) throws SQLException {
		this.assertNotClosed();
		StatCache cache = getStatCache(table.getName());
		if (cache.existsStat == null) {
			cache.existsStat = new StatementImpl(this.conn, table.getExistsSql());
		}
		for (Field field : table.getFields()) {
			if (field.isKey()) {
				Object val = key.get(field.getName());
				val = this.toSqlObject(val, field.getSqlType());
				setParameter(cache.existsStat, field.getName(), val, field.getSqlType());
			}
		}
		long count = cache.existsStat.executeCountLong();
		return count == 1;
	}

	public List<Record> getAll(Table table) throws SQLException {
		this.assertNotClosed();
		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(table.getGetAllSql()))) {
				return RecordUtils.executeList(rs);
			}
		}
	}

	public void executeBatch() throws SQLException {
		this.assertNotClosed();
		AssertUtils.assertTrue(!this.conn.getAutoCommit());

		for (StatCache cache : this.statCache.values()) {
			if (cache.insertStat != null) {
				cache.insertStat.executeBatch();
			}
			if (cache.updateStat != null) {
				cache.updateStat.executeBatch();
			}
			if (cache.deleteStat != null) {
				cache.deleteStat.executeBatch();
			}
		}
	}

	public void beginTransaction() throws SQLException {
		this.assertNotClosed();
		this.conn.setAutoCommit(false);
	}

	public void commit() throws SQLException {
		this.assertNotClosed();
		this.conn.commit();
	}

	public void rollback() throws SQLException {
		this.assertNotClosed();
		this.conn.rollback();
	}

	protected void assertNotClosed() throws SQLException {
		if (this.closed) {
			throw new SQLException(this + " closed.");
		}
	}

	private boolean closed = false;

	private void closeStatements() throws SQLException {
		List<StatCache> caches = new ArrayList<>(this.statCache.values());
		Collections.reverse(caches);

		for (StatCache cache : caches) {
			if (cache.insertStat != null) {
				cache.insertStat.close();
			}
			if (cache.updateStat != null) {
				cache.updateStat.close();
			}
			if (cache.deleteStat != null) {
				cache.deleteStat.close();
			}
			if (cache.getStat != null) {
				cache.getStat.close();
			}
			if (cache.existsStat != null) {
				cache.existsStat.close();
			}
		}
	}

	@Override
	public void close() throws SQLException {
		if (!this.closed) {
			closeStatements();

			if (this.internalConn) {
				this.conn.close();
			}
			this.closed = true;
		}
	}

	private static DataSource __dataSource;

	public static DataSource getDataSource() {
		return AssertUtils.assertNotNull(__dataSource);
	}

	public static void setDataSource(DataSource dataSource) {
		AssertUtils.assertNull(dataSource);
		__dataSource = dataSource;
	}

	static class StatCache {
		StatementImpl insertStat;
		StatementImpl updateStat;
		StatementImpl deleteStat;
		StatementImpl getStat;
		StatementImpl existsStat;
	}
}
