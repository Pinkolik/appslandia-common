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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.NonUniqueSqlException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public final class RecordUtils {

	public static Record executeSingle(PreparedStatement stat) throws SQLException {
		try (ResultSet rs = stat.executeQuery()) {
			return executeSingle(rs);
		}
	}

	public static Record executeSingle(ResultSet rs) throws SQLException {
		final String[] columnLabels = JdbcUtils.getColumnLabels(rs);

		Record t = null;
		boolean rsRead = false;

		while (rs.next()) {
			if (rsRead) {
				throw new NonUniqueSqlException();
			}
			rsRead = true;
			t = toRecord(rs, columnLabels);
		}
		return t;
	}

	public static List<Record> executeList(PreparedStatement stat) throws SQLException {
		try (ResultSet rs = stat.executeQuery()) {
			return executeList(rs);
		}
	}

	public static List<Record> executeList(ResultSet rs) throws SQLException {
		final String[] columnLabels = JdbcUtils.getColumnLabels(rs);

		List<Record> list = new ArrayList<>();
		while (rs.next()) {
			list.add(toRecord(rs, columnLabels));
		}
		return list;
	}

	private static Record toRecord(ResultSet rs, String[] columnLabels) throws SQLException {
		Record record = new Record();
		for (int col = 1; col <= columnLabels.length; col++) {
			record.set(columnLabels[col - 1], rs.getObject(col));
		}
		return record;
	}
}
