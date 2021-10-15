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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.ResultSetHandler;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.ResultSetMapper;
import com.appslandia.common.jdbc.Sql;
import com.appslandia.common.jdbc.StatementImpl;
import com.appslandia.common.threading.ThreadLocalStorage;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DbManager implements AutoCloseable {

	private Connection conn;
	private Map<String, StatementImpl> tableStats = new LinkedHashMap<>();

	public DbManager() throws SQLException {
		this(DbManager.getDataSource());
	}

	public DbManager(DataSource dataSource) throws SQLException {
		this.conn = dataSource.getConnection();
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

	protected void setParameters(StatementImpl stat, Map<String, Object> params) throws SQLException {
		for (Map.Entry<String, Object> param : params.entrySet()) {

			stat.setObject(param.getKey(), param.getValue());
		}
	}

	public Object insert(Record record, Table table) throws SQLException {
		return this.insert(record, table, false);
	}

	public void insertBatch(Record record, Table table) throws SQLException {
		this.insert(record, table, true);
	}

	protected Object insert(Record record, Table table, boolean addBatch) throws SQLException {
		this.assertNotClosed();

		StatementImpl stat = this.tableStats.get(table.getInsertSql().getName());
		if (stat == null) {
			stat = new StatementImpl(this.conn, table.getInsertSql(), (table.getAutoKey() != null));

			this.tableStats.put(table.getInsertSql().getName(), stat);
		}

		for (Field field : table.getFields()) {
			if (!field.isAutoKey()) {

				Object val = record.get(field.getName());
				setParameter(stat, field.getName(), val, field.getSqlType());
			}
		}

		int rowAffected = -1;
		if (!addBatch) {
			rowAffected = stat.executeUpdate();

			if (table.getAutoKey() != null) {
				try (ResultSet rs = stat.getGeneratedKeys()) {

					if (rs.next()) {
						Object generatedKey = rs.getObject(1);
						record.set(table.getAutoKey().getName(), generatedKey);

						return generatedKey;
					}
				}
			}
		} else {
			assertNotAutoCommit();

			stat.addBatch();
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

		StatementImpl stat = this.tableStats.get(table.getUpdateSql().getName());
		if (stat == null) {
			stat = new StatementImpl(this.conn, table.getUpdateSql());

			this.tableStats.put(table.getUpdateSql().getName(), stat);
		}

		for (Field field : table.getFields()) {
			if (field.isKey() || field.isUpdatable()) {

				Object val = record.get(field.getName());
				setParameter(stat, field.getName(), val, field.getSqlType());
			}
		}
		int rowAffected = -1;

		if (!addBatch) {
			rowAffected = stat.executeUpdate();
		} else {
			assertNotAutoCommit();

			stat.addBatch();
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

		StatementImpl stat = this.tableStats.get(table.getDeleteSql().getName());
		if (stat == null) {
			stat = new StatementImpl(this.conn, table.getDeleteSql());

			this.tableStats.put(table.getDeleteSql().getName(), stat);
		}

		for (Field field : table.getFields()) {
			if (field.isKey()) {

				Object val = key.get(field.getName());
				setParameter(stat, field.getName(), val, field.getSqlType());
			}
		}
		int rowAffected = -1;

		if (!addBatch) {
			rowAffected = stat.executeUpdate();
		} else {
			assertNotAutoCommit();

			stat.addBatch();
		}
		return rowAffected;
	}

	public Record getRecord(Record key, Table table) throws SQLException {
		this.assertNotClosed();

		StatementImpl stat = this.tableStats.get(table.getGetSql().getName());
		if (stat == null) {
			stat = new StatementImpl(this.conn, table.getGetSql());

			this.tableStats.put(table.getGetSql().getName(), stat);
		}

		for (Field field : table.getFields()) {
			if (field.isKey()) {

				Object val = key.get(field.getName());
				setParameter(stat, field.getName(), val, field.getSqlType());
			}
		}
		try (ResultSetImpl rs = stat.executeQuery()) {

			final String[] columnLabels = JdbcUtils.getColumnLabels(rs);
			return JdbcUtils.executeSingle(rs, r -> RecordUtils.toRecord(r, columnLabels));
		}
	}

	public boolean exists(Record key, Table table) throws SQLException {
		this.assertNotClosed();

		StatementImpl stat = this.tableStats.get(table.getExistsSql().getName());
		if (stat == null) {
			stat = new StatementImpl(this.conn, table.getExistsSql());

			this.tableStats.put(table.getExistsSql().getName(), stat);
		}

		for (Field field : table.getFields()) {
			if (field.isKey()) {

				Object val = key.get(field.getName());
				setParameter(stat, field.getName(), val, field.getSqlType());
			}
		}

		Number count = stat.executeScalar();
		if (count == null) {
			return false;
		}
		if (count.longValue() > 1) {
			throw new IllegalStateException("Duplicate key.");
		}
		return true;
	}

	public List<Record> executeList(String sql) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				final String[] columnLabels = JdbcUtils.getColumnLabels(rs);
				return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r, columnLabels), new ArrayList<>());
			}
		}
	}

	public Record executeSingle(String sql) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				final String[] columnLabels = JdbcUtils.getColumnLabels(rs);
				return JdbcUtils.executeSingle(rs, r -> RecordUtils.toRecord(r, columnLabels));
			}
		}
	}

	// Utility methods

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

				return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
			}
		}
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
			setParameters(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
			}
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn) throws SQLException {
		return executeMap(sql, keyColumn, valueColumn, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn, Map<K, V> map) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
			}
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, String keyColumn, String valueColumn) throws SQLException {
		return executeMap(sql, params, keyColumn, valueColumn, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, String keyColumn, String valueColumn, Map<K, V> map) throws SQLException {
		this.assertNotClosed();

		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameters(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
			}
		}
	}

	public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper) throws SQLException {
		return executeList(sql, mapper, new ArrayList<>());
	}

	public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper, List<T> list) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeList(rs, mapper, list);
			}
		}
	}

	public <T> List<T> executeList(String sql, Map<String, Object> params, ResultSetMapper<T> mapper) throws SQLException {
		return executeList(sql, params, mapper, new ArrayList<>());
	}

	public <T> List<T> executeList(String sql, Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list) throws SQLException {
		this.assertNotClosed();

		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameters(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				return JdbcUtils.executeList(rs, mapper, list);
			}
		}
	}

	public <T> T executeSingle(String sql, ResultSetMapper<T> mapper) throws SQLException {
		this.assertNotClosed();

		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeSingle(rs, mapper);
			}
		}
	}

	public <T> T executeScalar(String sql) throws SQLException {
		return executeSingle(sql, rs -> ObjectUtils.cast(rs.getObject(1)));
	}

	public void executeQuery(String sql, ResultSetHandler handler) throws Exception {
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
		assertNotAutoCommit();

		for (StatementImpl stat : this.tableStats.values()) {
			stat.executeBatch();
		}
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.assertNotClosed();
		this.conn.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		this.assertNotClosed();
		return this.conn.getAutoCommit();
	}

	public void commit() throws SQLException {
		this.assertNotClosed();
		assertNotAutoCommit();

		this.conn.commit();
	}

	public void rollback() throws SQLException {
		this.assertNotClosed();
		assertNotAutoCommit();

		this.conn.rollback();
	}

	protected void assertNotAutoCommit() throws SQLException {
		if (this.conn.getAutoCommit()) {
			throw new SQLException("auto commit must be disabled.");
		}
	}

	protected void assertNotClosed() throws SQLException {
		if (this.closed) {
			throw new SQLException(this + " closed.");
		}
	}

	private boolean closed = false;

	private void closeStatements() throws SQLException {
		List<StatementImpl> stats = new ArrayList<>(this.tableStats.values());

		for (int i = stats.size() - 1; i >= 0; i--) {
			stats.get(i).close();
		}
	}

	@Override
	public void close() throws SQLException {
		if (!this.closed) {
			closeStatements();

			if (!this.conn.getAutoCommit()) {
				this.conn.setAutoCommit(true);
			}

			this.conn.close();
			this.closed = true;
		}
	}

	private static final ThreadLocalStorage<DataSource> DS_HOLDER = new ThreadLocalStorage<>();

	public static DataSource getDataSource() throws IllegalStateException {
		DataSource ds = DS_HOLDER.get();
		if (ds == null) {
			throw new IllegalStateException("No current DataSource found in the current thread.");
		}
		return ds;
	}

	public static void setDataSource(DataSource ds) {
		DS_HOLDER.set(ds);
	}
}
