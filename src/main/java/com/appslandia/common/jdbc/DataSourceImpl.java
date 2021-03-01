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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DataSourceImpl extends InitializeObject implements DataSource {

	private String url;
	private String userName;
	private String password;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.url, "url is required.");
	}

	@Override
	public Connection getConnection() throws SQLException {
		initialize();

		Connection conn = null;
		if (this.userName != null) {
			conn = DriverManager.getConnection(this.url, this.userName, this.password);
		} else {
			conn = DriverManager.getConnection(this.url);
		}
		return conn;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public DataSourceImpl load(InputStream is) {
		assertNotInitialized();
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
		loadProps(ObjectUtils.cast(props));
		return this;
	}

	public DataSourceImpl load(Reader r) {
		assertNotInitialized();
		Properties props = new Properties();
		try {
			props.load(r);
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
		loadProps(ObjectUtils.cast(props));
		return this;
	}

	public DataSourceImpl load(File file) {
		assertNotInitialized();
		try (Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			return load(r);

		} catch (InitializeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
	}

	public DataSourceImpl load(String file) {
		return load(new File(file));
	}

	public DataSourceImpl loadProps(Map<String, String> props) {
		setUrl(props.get("url"));
		setUserName(props.get("userName"));
		setPassword(props.get("password"));
		return this;
	}

	public String getUrl() {
		return this.url;
	}

	public DataSourceImpl setUrl(String url) {
		assertNotInitialized();
		this.url = StringUtils.trimToNull(url);
		return this;
	}

	public String getUserName() {
		return this.userName;
	}

	public DataSourceImpl setUserName(String userName) {
		assertNotInitialized();
		this.userName = StringUtils.trimToNull(userName);
		return this;
	}

	public String getPassword() {
		return this.password;
	}

	public DataSourceImpl setPassword(String password) {
		assertNotInitialized();
		this.password = StringUtils.trimToNull(password);
		return this;
	}
}
