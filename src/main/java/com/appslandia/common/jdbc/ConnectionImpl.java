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

package com.appslandia.common.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.appslandia.common.threading.ThreadLocalStorage;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConnectionImpl implements Connection {

	protected final Connection conn;
	protected final String dsName;
	protected ConnectionImpl outer;

	public ConnectionImpl(DataSource dataSource, String dsName) throws java.sql.SQLException {
		ConnectionImpl outer = CONNECTION_HOLDER.get();
		if (outer != null) {
			this.outer = outer;
		}
		this.conn = dataSource.getConnection();
		CONNECTION_HOLDER.set(this);

		this.dsName = AssertUtils.assertNotNull(dsName, "dsName must be not null.");
	}

	public ConnectionImpl(DataSource dataSource) throws java.sql.SQLException {
		this(dataSource, "");
	}

	public String getDsName() {
		return this.dsName;
	}

	// Utility methods

	public int executeUpdate(String sql) throws java.sql.SQLException {
		try (Statement stat = this.conn.createStatement()) {
			return stat.executeUpdate(sql);
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper) throws java.sql.SQLException {
		return executeMap(sql, keyMapper, valueMapper, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
			}
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper)
			throws java.sql.SQLException {
		return executeMap(sql, params, keyMapper, valueMapper, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map)
			throws java.sql.SQLException {
		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameters(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
			}
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn) throws java.sql.SQLException {
		return executeMap(sql, keyColumn, valueColumn, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
			}
		}
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, String keyColumn, String valueColumn) throws java.sql.SQLException {
		return executeMap(sql, params, keyColumn, valueColumn, new LinkedHashMap<>());
	}

	public <K, V> Map<K, V> executeMap(String sql, Map<String, Object> params, String keyColumn, String valueColumn, Map<K, V> map)
			throws java.sql.SQLException {
		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameters(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
			}
		}
	}

	public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper) throws java.sql.SQLException {
		return executeList(sql, mapper, new ArrayList<>());
	}

	public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeList(rs, mapper, list);
			}
		}
	}

	public <T> List<T> executeList(String sql, Map<String, Object> params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
		return executeList(sql, params, mapper, new ArrayList<>());
	}

	public <T> List<T> executeList(String sql, Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
		final Sql pSql = new Sql(sql);

		try (StatementImpl stat = new StatementImpl(this.conn, pSql)) {
			setParameters(stat, params);

			try (ResultSetImpl rs = stat.executeQuery()) {
				return JdbcUtils.executeList(rs, mapper, list);
			}
		}
	}

	public <T> T executeSingle(String sql, ResultSetMapper<T> mapper) throws java.sql.SQLException {
		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				return JdbcUtils.executeSingle(rs, mapper);
			}
		}
	}

	public <T> T executeScalar(String sql) throws java.sql.SQLException {
		return executeSingle(sql, rs -> ObjectUtils.cast(rs.getObject(1)));
	}

	public void executeQuery(String sql, ResultSetHandler handler) throws Exception {
		try (Statement stat = this.conn.createStatement()) {
			try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

				while (rs.next()) {
					handler.handle(rs);
				}
			}
		}
	}

	// java.sql.Connection

	@Override
	public java.sql.CallableStatement prepareCall(java.lang.String sql) throws java.sql.SQLException {
		return this.conn.prepareCall(sql);
	}

	@Override
	public java.sql.CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
		return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public java.sql.CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws java.sql.SQLException {
		return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public StatementImpl prepareStatement(java.lang.String sql) throws java.sql.SQLException {
		return new StatementImpl(this.conn.prepareStatement(sql));
	}

	@Override
	public StatementImpl prepareStatement(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
		return new StatementImpl(this.conn.prepareStatement(sql, autoGeneratedKeys));
	}

	@Override
	public StatementImpl prepareStatement(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
		return new StatementImpl(this.conn.prepareStatement(sql, columnIndexes));
	}

	@Override
	public StatementImpl prepareStatement(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
		return new StatementImpl(this.conn.prepareStatement(sql, columnNames));
	}

	@Override
	public StatementImpl prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
		return new StatementImpl(this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency));
	}

	@Override
	public StatementImpl prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws java.sql.SQLException {
		return new StatementImpl(this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
	}

	@Override
	public java.sql.Array createArrayOf(java.lang.String typeName, java.lang.Object[] elements) throws java.sql.SQLException {
		return this.conn.createArrayOf(typeName, elements);
	}

	@Override
	public java.sql.SQLXML createSQLXML() throws java.sql.SQLException {
		return this.conn.createSQLXML();
	}

	@Override
	public java.sql.Clob createClob() throws java.sql.SQLException {
		return this.conn.createClob();
	}

	@Override
	public java.sql.NClob createNClob() throws java.sql.SQLException {
		return this.conn.createNClob();
	}

	@Override
	public java.sql.Blob createBlob() throws java.sql.SQLException {
		return this.conn.createBlob();
	}

	@Override
	public java.sql.Statement createStatement() throws java.sql.SQLException {
		return this.conn.createStatement();
	}

	@Override
	public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
		return this.conn.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
		return this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public java.sql.Struct createStruct(java.lang.String typeName, java.lang.Object[] attributes) throws java.sql.SQLException {
		return this.conn.createStruct(typeName, attributes);
	}

	@Override
	public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
		return this.conn.setSavepoint();
	}

	@Override
	public java.sql.Savepoint setSavepoint(java.lang.String name) throws java.sql.SQLException {
		return this.conn.setSavepoint(name);
	}

	@Override
	public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) throws java.sql.SQLException {
		this.conn.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws java.sql.SQLException {
		this.conn.setAutoCommit(autoCommit);
	}

	@Override
	public void setCatalog(java.lang.String catalog) throws java.sql.SQLException {
		this.conn.setCatalog(catalog);
	}

	@Override
	public void setClientInfo(java.util.Properties properties) throws java.sql.SQLClientInfoException {
		this.conn.setClientInfo(properties);
	}

	@Override
	public void setClientInfo(java.lang.String name, java.lang.String value) throws java.sql.SQLClientInfoException {
		this.conn.setClientInfo(name, value);
	}

	@Override
	public void setHoldability(int holdability) throws java.sql.SQLException {
		this.conn.setHoldability(holdability);
	}

	@Override
	public void setReadOnly(boolean readOnly) throws java.sql.SQLException {
		this.conn.setReadOnly(readOnly);
	}

	@Override
	public void setSchema(java.lang.String schema) throws java.sql.SQLException {
		this.conn.setSchema(schema);
	}

	@Override
	public void setTransactionIsolation(int level) throws java.sql.SQLException {
		this.conn.setTransactionIsolation(level);
	}

	@Override
	public void setTypeMap(java.util.Map<java.lang.String, java.lang.Class<?>> map) throws java.sql.SQLException {
		this.conn.setTypeMap(map);
	}

	@Override
	public int getNetworkTimeout() throws java.sql.SQLException {
		return this.conn.getNetworkTimeout();
	}

	@Override
	public boolean getAutoCommit() throws java.sql.SQLException {
		return this.conn.getAutoCommit();
	}

	@Override
	public java.lang.String getCatalog() throws java.sql.SQLException {
		return this.conn.getCatalog();
	}

	@Override
	public java.util.Properties getClientInfo() throws java.sql.SQLException {
		return this.conn.getClientInfo();
	}

	@Override
	public java.lang.String getClientInfo(java.lang.String name) throws java.sql.SQLException {
		return this.conn.getClientInfo(name);
	}

	@Override
	public int getHoldability() throws java.sql.SQLException {
		return this.conn.getHoldability();
	}

	@Override
	public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
		return this.conn.getMetaData();
	}

	@Override
	public java.lang.String getSchema() throws java.sql.SQLException {
		return this.conn.getSchema();
	}

	@Override
	public int getTransactionIsolation() throws java.sql.SQLException {
		return this.conn.getTransactionIsolation();
	}

	@Override
	public java.util.Map<java.lang.String, java.lang.Class<?>> getTypeMap() throws java.sql.SQLException {
		return this.conn.getTypeMap();
	}

	@Override
	public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
		return this.conn.getWarnings();
	}

	@Override
	public boolean isClosed() throws java.sql.SQLException {
		return this.conn.isClosed();
	}

	@Override
	public boolean isReadOnly() throws java.sql.SQLException {
		return this.conn.isReadOnly();
	}

	@Override
	public boolean isValid(int timeout) throws java.sql.SQLException {
		return this.conn.isValid(timeout);
	}

	@Override
	public void releaseSavepoint(java.sql.Savepoint savepoint) throws java.sql.SQLException {
		this.conn.releaseSavepoint(savepoint);
	}

	@Override
	public void abort(java.util.concurrent.Executor executor) throws java.sql.SQLException {
		this.conn.abort(executor);
	}

	@Override
	public void clearWarnings() throws java.sql.SQLException {
		this.conn.clearWarnings();
	}

	@Override
	public void commit() throws java.sql.SQLException {
		this.conn.commit();
	}

	@Override
	public java.lang.String nativeSQL(java.lang.String sql) throws java.sql.SQLException {
		return this.conn.nativeSQL(sql);
	}

	@Override
	public void rollback() throws java.sql.SQLException {
		this.conn.rollback();
	}

	@Override
	public void rollback(java.sql.Savepoint savepoint) throws java.sql.SQLException {
		this.conn.rollback(savepoint);
	}

	// java.sql.Wrapper

	@Override
	public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException {
		return this.conn.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException {
		return this.conn.unwrap(iface);
	}

	// AutoCloseable

	@Override
	public void close() throws java.sql.SQLException {
		if (!this.conn.isClosed()) {
			ConnectionImpl outer = this.outer;
			this.conn.close();

			this.outer = null;
			CONNECTION_HOLDER.set(outer);
		}
	}

	static void setParameters(StatementImpl stat, Map<String, Object> params) throws java.sql.SQLException {
		for (Map.Entry<String, Object> param : params.entrySet()) {
			stat.setObject(param.getKey(), param.getValue());
		}
	}

	private static final ThreadLocalStorage<ConnectionImpl> CONNECTION_HOLDER = new ThreadLocalStorage<>();

	public static ConnectionImpl getCurrent() throws IllegalStateException {
		ConnectionImpl conn = CONNECTION_HOLDER.get();
		if (conn == null) {
			throw new IllegalStateException("No current connection found in the current thread.");
		}
		return conn;
	}

	public static boolean hasCurrent() {
		return CONNECTION_HOLDER.hasValue();
	}
}
