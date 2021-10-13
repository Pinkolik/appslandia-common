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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JdbcUtils {

	public static void rollback(Connection conn) throws SQLException {
		if (conn != null) {
			conn.rollback();
		}
	}

	public static void setAutoCommit(Connection conn) throws SQLException {
		if (conn != null) {
			conn.setAutoCommit(true);
		}
	}

	public static void closeQuietly(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ignore) {
			}
		}
	}

	public static <T> T executeSingle(ResultSetImpl rs, ResultSetMapper<T> mapper) throws SQLException {
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

	public static <T> List<T> executeList(ResultSetImpl rs, ResultSetMapper<T> mapper, List<T> list) throws SQLException {
		while (rs.next()) {
			T t = mapper.map(rs);
			list.add(t);
		}
		return list;
	}

	public static <K, V> Map<K, V> executeMap(ResultSetImpl rs, String keyColumn, String valueColumn, Map<K, V> map) throws SQLException {
		while (rs.next()) {

			K k = ObjectUtils.cast(rs.getObject(keyColumn));
			V v = ObjectUtils.cast(rs.getObject(valueColumn));

			map.put(k, v);
		}
		return map;
	}

	public static <K, V> Map<K, V> executeMap(ResultSetImpl rs, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map)
			throws SQLException {
		while (rs.next()) {

			K k = keyMapper.map(rs);
			V v = valueMapper.map(rs);

			map.put(k, v);
		}
		return map;
	}

	public static String[] getColumnLabels(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		List<String> labels = new ArrayList<>(md.getColumnCount());

		for (int idx = 1; idx <= md.getColumnCount(); idx++) {
			labels.add(md.getColumnLabel(idx));
		}
		return labels.toArray(new String[labels.size()]);
	}
}
