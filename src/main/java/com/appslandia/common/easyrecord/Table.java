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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.jdbc.Sql;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Table extends InitializeObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Field autoKey;
	final List<Field> fields = new ArrayList<>();

	private Sql insertSql;
	private Sql updateSql;
	private Sql deleteSql;

	private Sql getSql;
	private Sql existsSql;
	private String getAllSql;

	public Table() {
	}

	public Table(String name) {
		setName(name);
	}

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.name, "name is required.");
		AssertUtils.assertTrue(!this.fields.isEmpty(), "fields are required.");

		int keyCount = 0;
		for (Field field : this.fields) {
			AssertUtils.assertNotNull(field.getName(), "field name is required.");

			if (field.getKeyType() != KeyType.NON_KEY) {
				keyCount++;

				// Key field
				field.addConstraint("required", null);
				field.setUpdatable(false);

				if (field.getKeyType() == KeyType.AUTO_KEY) {
					AssertUtils.assertNull(this.autoKey, "autoKey duplicated.");

					this.autoKey = field;
				}
			}
		}

		// No keys
		if (keyCount == 0) {
			throw new IllegalArgumentException("No keys found.");
		}

		this.insertSql = new Sql().sql(this.buildInsertSQL());
		this.updateSql = new Sql().sql(this.buildUpdateSQL());
		this.deleteSql = new Sql().sql(this.buildDeleteSQL());

		this.getSql = new Sql().sql(this.buildGetSQL());
		this.existsSql = new Sql().sql(this.buildExistsSQL());
		this.getAllSql = this.buildGetAllSQL();
	}

	protected String buildInsertSQL() {
		TextBuilder sb = new TextBuilder().append("INSERT INTO ").append(this.name);
		sb.append(" ( ");
		boolean isFirst = true;
		for (Field field : this.fields) {
			if (!field.isAutoKey()) {
				if (isFirst) {
					sb.append(field.getName());
					isFirst = false;
				} else {
					sb.append(", ").append(field.getName());
				}
			}
		}
		sb.append(" )");
		sb.append(" VALUES ( ");
		isFirst = true;
		for (Field field : this.fields) {
			if (!field.isAutoKey()) {
				if (isFirst) {
					sb.append(field.getParamName());
					isFirst = false;
				} else {
					sb.append(",").append(field.getParamName());
				}
			}
		}
		sb.append(" )");
		return sb.toString();
	}

	protected String buildUpdateSQL() {
		TextBuilder sb = new TextBuilder().append("UPDATE ").append(this.name);
		sb.append(" SET ");
		boolean isFirst = true;
		for (Field field : this.fields) {
			if (field.isUpdatable()) {
				if (isFirst) {
					sb.append(field.getName()).append("=").append(field.getParamName());
					isFirst = false;
				} else {
					sb.append(", ").append(field.getName()).append("=").append(field.getParamName());
				}
			}
		}
		sb.append(" WHERE ");

		this.appendWhereKeyConditions(sb);
		return sb.toString();
	}

	protected String buildDeleteSQL() {
		TextBuilder sb = new TextBuilder().append("DELETE FROM ").append(this.name);
		sb.append(" WHERE ");

		this.appendWhereKeyConditions(sb);
		return sb.toString();
	}

	protected String buildExistsSQL() {
		TextBuilder sb = new TextBuilder().append("SELECT COUNT(1) FROM ").append(this.name);
		sb.append(" WHERE ");

		this.appendWhereKeyConditions(sb);
		return sb.toString();
	}

	protected String buildGetSQL() {
		TextBuilder sb = new TextBuilder().append("SELECT * FROM ").append(this.name);
		sb.append(" WHERE ");

		this.appendWhereKeyConditions(sb);
		return sb.toString();
	}

	protected void appendWhereKeyConditions(TextBuilder sqlBuilder) {
		boolean isFirst = true;
		for (Field field : this.fields) {
			if (field.isKey()) {
				if (isFirst) {
					sqlBuilder.append(field.getName()).append("=").append(field.getParamName());
					isFirst = false;
				} else {
					sqlBuilder.append(" AND ").append(field.getName()).append("=").append(field.getParamName());
				}
			}
		}
	}

	protected String buildGetAllSQL() {
		TextBuilder sb = new TextBuilder().append("SELECT * FROM ").append(this.name);
		return sb.toString();
	}

	public String getName() {
		initialize();
		return this.name;
	}

	public Table setName(String name) {
		assertNotInitialized();
		this.name = name;
		return this;
	}

	public List<Field> getFields() {
		initialize();
		return this.fields;
	}

	public Table addFields(String... fieldNames) {
		assertNotInitialized();
		for (String fieldName : fieldNames) {
			this.fields.add(new Field(fieldName));
		}
		return this;
	}

	public Table addField(Field field) {
		assertNotInitialized();
		this.fields.add(field);
		return this;
	}

	public Table addField(String fieldName, int sqlType) {
		assertNotInitialized();
		this.fields.add(new Field(fieldName).setSqlType(sqlType));
		return this;
	}

	public Table addAutoKey(String fieldName) {
		assertNotInitialized();
		this.fields.add(new Field(fieldName).setKeyType(KeyType.AUTO_KEY));
		return this;
	}

	public Table addKeys(String... fieldNames) {
		assertNotInitialized();
		for (String fieldName : fieldNames) {
			this.fields.add(new Field(fieldName).setKeyType(KeyType.KEY));
		}
		return this;
	}

	public Field getAutoKey() {
		initialize();
		return this.autoKey;
	}

	public Sql getInsertSql() {
		initialize();
		return this.insertSql;
	}

	public Sql getUpdateSql() {
		initialize();
		return this.updateSql;
	}

	public Sql getDeleteSql() {
		initialize();
		return this.deleteSql;
	}

	public Sql getGetSql() {
		initialize();
		return this.getSql;
	}

	public Sql getExistsSql() {
		initialize();
		return this.existsSql;
	}

	public String getGetAllSql() {
		initialize();
		return this.getAllSql;
	}
}
