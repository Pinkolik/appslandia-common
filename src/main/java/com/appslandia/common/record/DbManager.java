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

package com.appslandia.common.record;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.appslandia.common.jdbc.NonUniqueSqlException;
import com.appslandia.common.jdbc.ResultSetHandler;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.ResultSetMapper;
import com.appslandia.common.jdbc.Sql;
import com.appslandia.common.jdbc.StatementImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DbManager implements AutoCloseable {

	private Connection conn;
	private boolean internalConn;
	private Map<String, Statements> tableStats = new LinkedHashMap<>();

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

	protected void setParameter(StatementImpl stat, String parameterName, Object val, int sqlType) throws SQLException {
		if (val == null) {
			if (sqlType > 0) {
				stat.setNull(parameterName, sqlType);
			} else {
				stat.setObject(parameterName, null);
			}
		} else {
			if (sqlType > 0) {
				stat.setObject(parameterName, val, sqlType);
			} else {
				stat.setObject(parameterName, val);
			}
		}
	}

	protected void setParameter(StatementImpl stat, Map<String, Object> params) throws SQLException {
		for (Map.Entry<String, Object> param : params.entrySet()) {
			stat.setObject(param.getKey(), param.getValue());
		}
	}

	protected Statements getStatements(String tableName) {
		Statements stats = this.tableStats.get(tableName);
		if (stats == null) {
			stats = new Statements();
			this.tableStats.put(tableName, stats);
		}
		return stats;
	}

	public long insert(Record record, Table table) throws SQLException {
		return this.insert(record, table, false);
	}

	public void insertBatch(Record record, Table table) throws SQLException {
		this.insert(record, table, true);
	}

	protected long insert(Record record, Table table, boolean addBatch) throws SQLException {
		this.assertNotClosed();

		Statements stats = getStatements(table.getName());
		if (stats.insertStat == null) {
			stats.insertStat = new StatementImpl(this.conn, table.getInsertSql(), (table.getAutoKey() != null));
		}

		for (Field field : table.getFields()) {
			if (!field.isAutoKey()) {

				Object val = record.get(field.getName());
				setParameter(stats.insertStat, field.getName(), val, field.getSqlType());
			}
		}

		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = stats.insertStat.executeUpdate();
			if (table.getAutoKey() != null) {
				try (ResultSet rs = stats.insertStat.getGeneratedKeys()) {

					if (rs.next()) {
						Object generatedKey = rs.getObject(1);
						record.set(table.getAutoKey().getName(), ((Number) generatedKey).longValue());
						return record.get(table.getAutoKey().getName());
					}
				}
			}
		} else {
			stats.insertStat.addBatch();
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

		Statements stats = getStatements(table.getName());
		if (stats.updateStat == null) {
			stats.updateStat = new StatementImpl(this.conn, table.getUpdateSql());
		}

		for (Field field : table.getFields()) {
			if (field.isKey() || field.isUpdatable()) {

				Object val = record.get(field.getName());
				setParameter(stats.updateStat, field.getName(), val, field.getSqlType());
			}
		}
		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = stats.updateStat.executeUpdate();
		} else {
			stats.updateStat.addBatch();
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

		Statements stats = getStatements(table.getName());
		if (stats.deleteStat == null) {
			stats.deleteStat = new StatementImpl(this.conn, table.getDeleteSql());
		}

		for (Field field : table.getFields()) {
			if (field.isKey()) {

				Object val = key.get(field.getName());
				setParameter(stats.deleteStat, field.getName(), val, field.getSqlType());
			}
		}
		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = stats.deleteStat.executeUpdate();
		} else {
			stats.deleteStat.addBatch();
		}
		return rowAffected;
	}

	public Record getRecord(Record key, Table table) throws SQLException {
		this.assertNotClosed();

		Statements stats = getStatements(table.getName());
		if (stats.getStat == null) {
			stats.getStat = new StatementImpl(this.conn, table.getGetSql());
		}

		for (Field field : table.getFields()) {
			if (field.isKey()) {

				Object val = key.get(field.getName());
				setParameter(stats.getStat, field.getName(), val, field.getSqlType());
			}
		}
		try (ResultSet rs = stats.getStat.executeQuery()) {
			return RecordUtils.executeSingle(rs);
		}
	}

	public boolean exists(Record key, Table table) throws SQLException {
		this.assertNotClosed();

		Statements stats = getStatements(table.getName());
		if (stats.existsStat == null) {
			stats.existsStat = new StatementImpl(this.conn, table.getExistsSql());
		}

		for (Field field : table.getFields()) {
			if (field.isKey()) {

				Object val = key.get(field.getName());
				setParameter(stats.existsStat, field.getName(), val, field.getSqlType());
			}
		}
		long count = stats.existsStat.executeCountLong();
		return count == 1;
	}

	public List<Record> getAll(Table table) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSet rs = stat.executeQuery(table.getGetAllSql())) {

				return RecordUtils.executeList(rs);
			}
		}
	}

	public List<Record> executeList(String sql) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSet rs = stat.executeQuery(sql)) {

				return RecordUtils.executeList(rs);
			}
		}
	}

	public Record executeSingle(String sql) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSet rs = stat.executeQuery(sql)) {

				return RecordUtils.executeSingle(rs);
			}
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			return stat.executeUpdate(sql);
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper) throws SQLException {
		return executeMap(sql, keyMapper, valueMapper, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				while (rs.next()) {

					K k = keyMapper.map(rs);
					V v = valueMapper.map(rs);
					map.put(k, v);
				}
			}
		}
		return map;
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper)
			throws SQLException {
		return executeMap(sql, params, keyMapper, valueMapper, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map)
			throws SQLException {
		this.assertNotClosed();

		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameter(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				while (rs.next()) {

					K k = keyMapper.map(rs);
					V v = valueMapper.map(rs);
					map.put(k, v);
				}
			}
		}
		return map;
	}

	public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn) throws SQLException {
		return executeMap(sql, keyColumn, valueColumn, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn, Map<K, V> map) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {

			try (ResultSet rs = stat.executeQuery(sql)) {
				while (rs.next()) {

					K k = ObjectUtils.cast(rs.getObject(keyColumn));
					V v = ObjectUtils.cast(rs.getObject(valueColumn));
					map.put(k, v);
				}
			}
		}
		return map;
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, String keyColumn, String valueColumn) throws SQLException {
		return executeMap(sql, params, keyColumn, valueColumn, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, String keyColumn, String valueColumn, Map<K, V> map) throws SQLException {
		this.assertNotClosed();

		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameter(stat, params);

			try (ResultSet rs = stat.executeQuery()) {
				while (rs.next()) {

					K k = ObjectUtils.cast(rs.getObject(keyColumn));
					V v = ObjectUtils.cast(rs.getObject(valueColumn));
					map.put(k, v);
				}
			}
		}
		return map;
	}

	public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper) throws SQLException {
		return executeList(sql, mapper, new ArrayList<>());
	}

	public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper, List<T> list) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				while (rs.next()) {

					T t = mapper.map(rs);
					list.add(t);
				}
			}
		}
		return list;
	}

	public <T> List<T> executeList(String sql, Map<String, Object> params, ResultSetMapper<T> mapper) throws SQLException {
		return executeList(sql, params, mapper, new ArrayList<>());
	}

	public <T> List<T> executeList(String sql, Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list) throws SQLException {
		this.assertNotClosed();

		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameter(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				while (rs.next()) {

					T t = mapper.map(rs);
					list.add(t);
				}
			}
		}
		return list;
	}

	public <T> T executeSingle(String sql, ResultSetMapper<T> mapper) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				T t = null;
				boolean rsRead = false;

				while (rs.next()) {
					if (rsRead) {
						throw new NonUniqueSqlException();
					}
					rsRead = true;
					t = mapper.map(rs);
				}
				return t;
			}
		}
	}

	public <T> T executeScalar(String sql) throws SQLException {
		return executeSingle(sql, rs -> ObjectUtils.cast(rs.getObject(1)));
	}

	public void executeQuery(String sql, ResultSetHandler handler) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				while (rs.next()) {
					handler.handle(rs);
				}
			}
		}
	}

	public void executeBatch() throws SQLException {
		this.assertNotClosed();
		AssertUtils.assertTrue(!this.conn.getAutoCommit());

		for (Statements stats : this.tableStats.values()) {
			if (stats.deleteStat != null) {
				stats.deleteStat.executeBatch();
			}
			if (stats.updateStat != null) {
				stats.updateStat.executeBatch();
			}
			if (stats.insertStat != null) {
				stats.insertStat.executeBatch();
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
		List<Statements> tblStats = new ArrayList<>(this.tableStats.values());
		Collections.reverse(tblStats);

		for (Statements stats : tblStats) {
			if (stats.insertStat != null) {
				stats.insertStat.close();
			}
			if (stats.updateStat != null) {
				stats.updateStat.close();
			}
			if (stats.deleteStat != null) {
				stats.deleteStat.close();
			}
			if (stats.getStat != null) {
				stats.getStat.close();
			}
			if (stats.existsStat != null) {
				stats.existsStat.close();
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

	static class Statements {
		StatementImpl insertStat;
		StatementImpl updateStat;
		StatementImpl deleteStat;
		StatementImpl getStat;
		StatementImpl existsStat;
	}
}
