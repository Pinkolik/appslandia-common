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
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.jdbc.Sql;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private int sqlType = 0;

	private KeyType keyType = KeyType.NON_KEY;
	private boolean updatable = true;

	private Map<String, Object> constraints = new LinkedHashMap<>();

	public Field() {
	}

	public Field(String name) {
		setName(name);
	}

	public boolean isAutoKey() {
		return this.keyType == KeyType.AUTO_KEY;
	}

	public boolean isKey() {
		return this.keyType != KeyType.NON_KEY;
	}

	public String getParamName() {
		return Sql.getParamPrefix() + this.name;
	}

	public String getName() {
		return this.name;
	}

	public Field setName(String name) {
		this.name = name;
		return this;
	}

	public int getSqlType() {
		return sqlType;
	}

	public Field setSqlType(int sqlType) {
		this.sqlType = sqlType;
		return this;
	}

	public KeyType getKeyType() {
		return this.keyType;
	}

	public Field setKeyType(KeyType keyType) {
		this.keyType = keyType;
		return this;
	}

	public boolean isUpdatable() {
		return this.updatable;
	}

	public Field setUpdatable(boolean updatable) {
		this.updatable = updatable;
		return this;
	}

	public Map<String, Object> getConstraints() {
		return this.constraints;
	}

	public Object getConstraint(String name) {
		return this.constraints.get(name);
	}

	public Field addConstraint(String name, Object constraintArgs) {
		this.constraints.put(name, constraintArgs);
		return this;
	}
}
