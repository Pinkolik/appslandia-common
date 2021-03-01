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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringFormat;

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

	public static String[] getColumnLabels(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		List<String> labels = new ArrayList<>(md.getColumnCount());

		for (int idx = 1; idx <= md.getColumnCount(); idx++) {
			labels.add(md.getColumnLabel(idx));
		}
		return labels.toArray(new String[labels.size()]);
	}

	public static long fixIdSeq(String tableName, String idColumn, boolean idInt, String idPkSeq, String idNotNull, String idPk, String idSeq, Connection conn)
			throws SQLException {
		AssertUtils.assertNotNull(tableName);
		AssertUtils.assertNotNull(idColumn);

		if (idPkSeq == null) {
			AssertUtils.assertNotNull(idNotNull);
			AssertUtils.assertNotNull(idPk);
			AssertUtils.assertNotNull(idSeq);
		}

		long seq = 0;
		try (Statement stat = conn.createStatement()) {

			// Add tempIdCol
			String tempIdCol = idColumn + "_TEMP";
			stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} ADD {} {}", tableName, tempIdCol, (idInt ? "INT" : "BIGINT")));

			// Update tempIdCol
			try (PreparedStatement updStat = conn.prepareStatement(StringFormat.fmt("UPDATE {} SET {}=? WHERE {}=?", tableName, tempIdCol, idColumn))) {
				try (ResultSet rs = stat.executeQuery(StringFormat.fmt("SELECT {} FROM {} ORDER BY {}", idColumn, tableName, idColumn))) {

					while (rs.next()) {
						seq++;

						if (idInt) {
							updStat.setInt(1, (int) seq);
							updStat.setInt(2, rs.getInt(1));
						} else {
							updStat.setLong(1, seq);
							updStat.setLong(2, rs.getLong(1));
						}
						updStat.addBatch();

						if (seq % 100 == 0)
							updStat.executeBatch();
					}
				}

				updStat.executeBatch();
			}

			// Re-create ID
			stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} DROP COLUMN {}", tableName, idColumn));
			stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} ADD {} {}", tableName, idColumn, (idInt ? "INT" : "BIGINT")));
			stat.executeUpdate(StringFormat.fmt("UPDATE {} SET {}={}", tableName, idColumn, tempIdCol));

			if (idPkSeq != null) {
				stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} {}", tableName, idPkSeq));
			} else {
				stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} {}", tableName, idNotNull));
				stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} {}", tableName, idPk));
				stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} {}", tableName, idSeq));
			}

			// Drop tempIdCol
			stat.executeUpdate(StringFormat.fmt("ALTER TABLE {} DROP COLUMN {}", tableName, tempIdCol));

			return seq;
		}
	}
}
