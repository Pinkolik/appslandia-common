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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.ResultSetMapper;
import com.appslandia.common.jdbc.StatementImpl;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public final class RecordUtils {

	public static Record executeSingle(StatementImpl stat) throws SQLException {

		ResultSetMapper<Record> mapper = new ResultSetMapper<Record>() {

			@Override
			public Record map(ResultSetImpl rs) throws SQLException {
				Record record = new Record();
				ResultSetMetaData rsmd = rs.getMetaData();

				for (int col = 1; col <= rsmd.getColumnCount(); col++) {
					String fieldLabel = rsmd.getColumnLabel(col);
					record.set(fieldLabel, rs.getObject(col));
				}

				return record;
			}
		};
		return stat.executeSingle(mapper);
	}

	public static List<Record> executeList(StatementImpl stat) throws SQLException {
		try (ResultSetImpl rs = stat.executeResult()) {
			return executeList(rs);
		}
	}

	public static List<Record> executeList(ResultSetImpl rs) throws SQLException {
		List<Record> list = new ArrayList<>();
		final String[] columnLabels = JdbcUtils.getColumnLabels(rs);

		ResultSetMapper<Record> mapper = new ResultSetMapper<Record>() {

			@Override
			public Record map(ResultSetImpl rs) throws SQLException {
				Record record = new Record();
				for (int col = 1; col <= columnLabels.length; col++) {
					record.set(columnLabels[col - 1], rs.getObject(col));
				}
				return record;
			}
		};

		while (rs.next()) {
			list.add(mapper.map(rs));
		}
		return list;
	}
}
