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

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ResultSetImpl implements ResultSet {

	protected final ResultSet rs;

	public ResultSetImpl(ResultSet rs) {
		this.rs = rs;
	}

	// Java 8+ Date/Time

	public LocalDate getLocalDate(String columnLabel) throws java.sql.SQLException {
		LocalDate value = this.rs.getObject(columnLabel, LocalDate.class);
		return !this.rs.wasNull() ? value : null;
	}

	public LocalTime getLocalTime(String columnLabel) throws java.sql.SQLException {
		LocalTime value = this.rs.getObject(columnLabel, LocalTime.class);
		return !this.rs.wasNull() ? value : null;
	}

	public LocalDateTime getLocalDateTime(String columnLabel) throws java.sql.SQLException {
		LocalDateTime value = this.rs.getObject(columnLabel, LocalDateTime.class);
		return !this.rs.wasNull() ? value : null;
	}

	public OffsetTime getOffsetTime(String columnLabel) throws java.sql.SQLException {
		OffsetTime value = this.rs.getObject(columnLabel, OffsetTime.class);
		return !this.rs.wasNull() ? value : null;
	}

	public OffsetDateTime getOffsetDateTime(String columnLabel) throws java.sql.SQLException {
		OffsetDateTime value = this.rs.getObject(columnLabel, OffsetDateTime.class);
		return !this.rs.wasNull() ? value : null;
	}

	// Get Primitive Wrappers

	public Boolean getBoolean2(String columnLabel) throws java.sql.SQLException {
		boolean value = this.rs.getBoolean(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Byte getByte2(String columnLabel) throws java.sql.SQLException {
		byte value = this.rs.getByte(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Short getShort2(String columnLabel) throws java.sql.SQLException {
		short value = this.rs.getShort(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Integer getInt2(String columnLabel) throws java.sql.SQLException {
		int value = this.rs.getInt(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Long getLong2(String columnLabel) throws java.sql.SQLException {
		long value = this.rs.getLong(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Float getFloat2(String columnLabel) throws java.sql.SQLException {
		float value = this.rs.getFloat(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Double getDouble2(String columnLabel) throws java.sql.SQLException {
		double value = this.rs.getDouble(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Boolean getBoolean2(int columnIndex) throws java.sql.SQLException {
		boolean value = this.rs.getBoolean(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	public Byte getByte2(int columnIndex) throws java.sql.SQLException {
		byte value = this.rs.getByte(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	public Short getShort2(int columnIndex) throws java.sql.SQLException {
		short value = this.rs.getShort(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	public Integer getInt2(int columnIndex) throws java.sql.SQLException {
		int value = this.rs.getInt(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	public Long getLong2(int columnIndex) throws java.sql.SQLException {
		long value = this.rs.getLong(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	public Float getFloat2(int columnIndex) throws java.sql.SQLException {
		float value = this.rs.getFloat(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	public Double getDouble2(int columnIndex) throws java.sql.SQLException {
		double value = this.rs.getDouble(columnIndex);
		return !this.rs.wasNull() ? value : null;
	}

	// java.sql.ResultSet

	@Override
	public boolean getBoolean(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBoolean(columnIndex);
	}

	@Override
	public boolean getBoolean(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getBoolean(columnLabel);
	}

	@Override
	public java.lang.String getString(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getString(columnLabel);
	}

	@Override
	public java.lang.String getString(int columnIndex) throws java.sql.SQLException {
		return this.rs.getString(columnIndex);
	}

	@Override
	public java.lang.String getNString(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getNString(columnLabel);
	}

	@Override
	public java.lang.String getNString(int columnIndex) throws java.sql.SQLException {
		return this.rs.getNString(columnIndex);
	}

	@Override
	public byte getByte(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getByte(columnLabel);
	}

	@Override
	public byte getByte(int columnIndex) throws java.sql.SQLException {
		return this.rs.getByte(columnIndex);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBytes(columnIndex);
	}

	@Override
	public byte[] getBytes(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getBytes(columnLabel);
	}

	@Override
	public short getShort(int columnIndex) throws java.sql.SQLException {
		return this.rs.getShort(columnIndex);
	}

	@Override
	public short getShort(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getShort(columnLabel);
	}

	@Override
	public int getInt(int columnIndex) throws java.sql.SQLException {
		return this.rs.getInt(columnIndex);
	}

	@Override
	public int getInt(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getInt(columnLabel);
	}

	@Override
	public long getLong(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getLong(columnLabel);
	}

	@Override
	public long getLong(int columnIndex) throws java.sql.SQLException {
		return this.rs.getLong(columnIndex);
	}

	@Override
	public float getFloat(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getFloat(columnLabel);
	}

	@Override
	public float getFloat(int columnIndex) throws java.sql.SQLException {
		return this.rs.getFloat(columnIndex);
	}

	@Override
	public double getDouble(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getDouble(columnLabel);
	}

	@Override
	public double getDouble(int columnIndex) throws java.sql.SQLException {
		return this.rs.getDouble(columnIndex);
	}

	@Override
	@Deprecated
	public java.math.BigDecimal getBigDecimal(java.lang.String columnLabel, int scale) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnLabel, scale);
	}

	@Override
	public java.math.BigDecimal getBigDecimal(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnLabel);
	}

	@Override
	@Deprecated
	public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnIndex, scale);
	}

	@Override
	public java.math.BigDecimal getBigDecimal(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnIndex);
	}

	@Override
	public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getDate(columnIndex, cal);
	}

	@Override
	public java.sql.Date getDate(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getDate(columnLabel);
	}

	@Override
	public java.sql.Date getDate(java.lang.String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getDate(columnLabel, cal);
	}

	@Override
	public java.sql.Date getDate(int columnIndex) throws java.sql.SQLException {
		return this.rs.getDate(columnIndex);
	}

	@Override
	public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTime(columnIndex, cal);
	}

	@Override
	public java.sql.Time getTime(java.lang.String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTime(columnLabel, cal);
	}

	@Override
	public java.sql.Time getTime(int columnIndex) throws java.sql.SQLException {
		return this.rs.getTime(columnIndex);
	}

	@Override
	public java.sql.Time getTime(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getTime(columnLabel);
	}

	@Override
	public java.sql.Timestamp getTimestamp(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnLabel);
	}

	@Override
	public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnIndex, cal);
	}

	@Override
	public java.sql.Timestamp getTimestamp(java.lang.String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnLabel, cal);
	}

	@Override
	public java.sql.Timestamp getTimestamp(int columnIndex) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnIndex);
	}

	@Override
	public java.lang.Object getObject(int columnIndex, java.util.Map<java.lang.String, java.lang.Class<?>> map) throws java.sql.SQLException {
		return this.rs.getObject(columnIndex, map);
	}

	@Override
	public java.lang.Object getObject(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel);
	}

	@Override
	public java.lang.Object getObject(int columnIndex) throws java.sql.SQLException {
		return this.rs.getObject(columnIndex);
	}

	@Override
	public java.lang.Object getObject(java.lang.String columnLabel, java.util.Map<java.lang.String, java.lang.Class<?>> map) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, map);
	}

	@Override
	public <T> T getObject(int columnIndex, java.lang.Class<T> type) throws java.sql.SQLException {
		return this.rs.getObject(columnIndex, type);
	}

	@Override
	public <T> T getObject(java.lang.String columnLabel, java.lang.Class<T> type) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, type);
	}

	@Override
	public java.net.URL getURL(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getURL(columnLabel);
	}

	@Override
	public java.net.URL getURL(int columnIndex) throws java.sql.SQLException {
		return this.rs.getURL(columnIndex);
	}

	@Override
	public java.sql.Array getArray(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getArray(columnLabel);
	}

	@Override
	public java.sql.Array getArray(int columnIndex) throws java.sql.SQLException {
		return this.rs.getArray(columnIndex);
	}

	@Override
	public java.sql.SQLXML getSQLXML(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getSQLXML(columnLabel);
	}

	@Override
	public java.sql.SQLXML getSQLXML(int columnIndex) throws java.sql.SQLException {
		return this.rs.getSQLXML(columnIndex);
	}

	@Override
	public java.sql.Ref getRef(int columnIndex) throws java.sql.SQLException {
		return this.rs.getRef(columnIndex);
	}

	@Override
	public java.sql.Ref getRef(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getRef(columnLabel);
	}

	@Override
	public java.sql.RowId getRowId(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getRowId(columnLabel);
	}

	@Override
	public java.sql.RowId getRowId(int columnIndex) throws java.sql.SQLException {
		return this.rs.getRowId(columnIndex);
	}

	@Override
	public java.sql.Clob getClob(int columnIndex) throws java.sql.SQLException {
		return this.rs.getClob(columnIndex);
	}

	@Override
	public java.sql.Clob getClob(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getClob(columnLabel);
	}

	@Override
	public java.sql.NClob getNClob(int columnIndex) throws java.sql.SQLException {
		return this.rs.getNClob(columnIndex);
	}

	@Override
	public java.sql.NClob getNClob(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getNClob(columnLabel);
	}

	@Override
	public java.io.InputStream getAsciiStream(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getAsciiStream(columnLabel);
	}

	@Override
	public java.io.InputStream getAsciiStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getAsciiStream(columnIndex);
	}

	@Override
	public java.io.Reader getCharacterStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getCharacterStream(columnIndex);
	}

	@Override
	public java.io.Reader getCharacterStream(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getCharacterStream(columnLabel);
	}

	@Override
	public java.io.Reader getNCharacterStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getNCharacterStream(columnIndex);
	}

	@Override
	public java.io.Reader getNCharacterStream(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getNCharacterStream(columnLabel);
	}

	@Override
	@Deprecated
	public java.io.InputStream getUnicodeStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getUnicodeStream(columnIndex);
	}

	@Override
	@Deprecated
	public java.io.InputStream getUnicodeStream(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getUnicodeStream(columnLabel);
	}

	@Override
	public java.sql.Blob getBlob(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getBlob(columnLabel);
	}

	@Override
	public java.sql.Blob getBlob(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBlob(columnIndex);
	}

	@Override
	public java.io.InputStream getBinaryStream(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.getBinaryStream(columnLabel);
	}

	@Override
	public java.io.InputStream getBinaryStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBinaryStream(columnIndex);
	}

	@Override
	public int getConcurrency() throws java.sql.SQLException {
		return this.rs.getConcurrency();
	}

	@Override
	public java.lang.String getCursorName() throws java.sql.SQLException {
		return this.rs.getCursorName();
	}

	@Override
	public int getFetchDirection() throws java.sql.SQLException {
		return this.rs.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws java.sql.SQLException {
		return this.rs.getFetchSize();
	}

	@Override
	public int getHoldability() throws java.sql.SQLException {
		return this.rs.getHoldability();
	}

	@Override
	public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
		return this.rs.getMetaData();
	}

	@Override
	public int getRow() throws java.sql.SQLException {
		return this.rs.getRow();
	}

	@Override
	public java.sql.Statement getStatement() throws java.sql.SQLException {
		return this.rs.getStatement();
	}

	@Override
	public int getType() throws java.sql.SQLException {
		return this.rs.getType();
	}

	@Override
	public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
		return this.rs.getWarnings();
	}

	@Override
	public void updateBoolean(java.lang.String columnLabel, boolean x) throws java.sql.SQLException {
		this.rs.updateBoolean(columnLabel, x);
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws java.sql.SQLException {
		this.rs.updateBoolean(columnIndex, x);
	}

	@Override
	public void updateString(int columnIndex, java.lang.String x) throws java.sql.SQLException {
		this.rs.updateString(columnIndex, x);
	}

	@Override
	public void updateString(java.lang.String columnLabel, java.lang.String x) throws java.sql.SQLException {
		this.rs.updateString(columnLabel, x);
	}

	@Override
	public void updateNString(java.lang.String columnLabel, java.lang.String nString) throws java.sql.SQLException {
		this.rs.updateNString(columnLabel, nString);
	}

	@Override
	public void updateNString(int columnIndex, java.lang.String nString) throws java.sql.SQLException {
		this.rs.updateNString(columnIndex, nString);
	}

	@Override
	public void updateBytes(java.lang.String columnLabel, byte[] x) throws java.sql.SQLException {
		this.rs.updateBytes(columnLabel, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws java.sql.SQLException {
		this.rs.updateBytes(columnIndex, x);
	}

	@Override
	public void updateByte(java.lang.String columnLabel, byte x) throws java.sql.SQLException {
		this.rs.updateByte(columnLabel, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws java.sql.SQLException {
		this.rs.updateByte(columnIndex, x);
	}

	@Override
	public void updateShort(java.lang.String columnLabel, short x) throws java.sql.SQLException {
		this.rs.updateShort(columnLabel, x);
	}

	@Override
	public void updateShort(int columnIndex, short x) throws java.sql.SQLException {
		this.rs.updateShort(columnIndex, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws java.sql.SQLException {
		this.rs.updateInt(columnIndex, x);
	}

	@Override
	public void updateInt(java.lang.String columnLabel, int x) throws java.sql.SQLException {
		this.rs.updateInt(columnLabel, x);
	}

	@Override
	public void updateLong(java.lang.String columnLabel, long x) throws java.sql.SQLException {
		this.rs.updateLong(columnLabel, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws java.sql.SQLException {
		this.rs.updateLong(columnIndex, x);
	}

	@Override
	public void updateFloat(java.lang.String columnLabel, float x) throws java.sql.SQLException {
		this.rs.updateFloat(columnLabel, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws java.sql.SQLException {
		this.rs.updateFloat(columnIndex, x);
	}

	@Override
	public void updateDouble(java.lang.String columnLabel, double x) throws java.sql.SQLException {
		this.rs.updateDouble(columnLabel, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws java.sql.SQLException {
		this.rs.updateDouble(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) throws java.sql.SQLException {
		this.rs.updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(java.lang.String columnLabel, java.math.BigDecimal x) throws java.sql.SQLException {
		this.rs.updateBigDecimal(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(java.lang.String columnLabel, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(java.lang.String columnLabel, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateAsciiStream(java.lang.String columnLabel, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(java.lang.String columnLabel, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(java.lang.String columnLabel, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(java.lang.String columnLabel, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(java.lang.String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, java.io.Reader x) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(java.lang.String columnLabel, java.io.Reader reader, int length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateCharacterStream(java.lang.String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateNull(int columnIndex) throws java.sql.SQLException {
		this.rs.updateNull(columnIndex);
	}

	@Override
	public void updateNull(java.lang.String columnLabel) throws java.sql.SQLException {
		this.rs.updateNull(columnLabel);
	}

	@Override
	public void updateDate(int columnIndex, java.sql.Date x) throws java.sql.SQLException {
		this.rs.updateDate(columnIndex, x);
	}

	@Override
	public void updateDate(java.lang.String columnLabel, java.sql.Date x) throws java.sql.SQLException {
		this.rs.updateDate(columnLabel, x);
	}

	@Override
	public void updateTime(java.lang.String columnLabel, java.sql.Time x) throws java.sql.SQLException {
		this.rs.updateTime(columnLabel, x);
	}

	@Override
	public void updateTime(int columnIndex, java.sql.Time x) throws java.sql.SQLException {
		this.rs.updateTime(columnIndex, x);
	}

	@Override
	public void updateTimestamp(java.lang.String columnLabel, java.sql.Timestamp x) throws java.sql.SQLException {
		this.rs.updateTimestamp(columnLabel, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws java.sql.SQLException {
		this.rs.updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateObject(int columnIndex, java.lang.Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, java.lang.Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x, targetSqlType);
	}

	@Override
	public void updateObject(java.lang.String columnLabel, java.lang.Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void updateObject(java.lang.String columnLabel, java.lang.Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x, targetSqlType);
	}

	@Override
	public void updateObject(java.lang.String columnLabel, java.lang.Object x, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, java.lang.Object x) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x);
	}

	@Override
	public void updateObject(int columnIndex, java.lang.Object x, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x, scaleOrLength);
	}

	@Override
	public void updateObject(java.lang.String columnLabel, java.lang.Object x) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x);
	}

	@Override
	public void updateRow() throws java.sql.SQLException {
		this.rs.updateRow();
	}

	@Override
	public void updateRef(int columnIndex, java.sql.Ref x) throws java.sql.SQLException {
		this.rs.updateRef(columnIndex, x);
	}

	@Override
	public void updateRef(java.lang.String columnLabel, java.sql.Ref x) throws java.sql.SQLException {
		this.rs.updateRef(columnLabel, x);
	}

	@Override
	public void updateBlob(java.lang.String columnLabel, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
		this.rs.updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateBlob(java.lang.String columnLabel, java.io.InputStream inputStream) throws java.sql.SQLException {
		this.rs.updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateBlob(int columnIndex, java.io.InputStream inputStream) throws java.sql.SQLException {
		this.rs.updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(int columnIndex, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
		this.rs.updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(java.lang.String columnLabel, java.sql.Blob x) throws java.sql.SQLException {
		this.rs.updateBlob(columnLabel, x);
	}

	@Override
	public void updateBlob(int columnIndex, java.sql.Blob x) throws java.sql.SQLException {
		this.rs.updateBlob(columnIndex, x);
	}

	@Override
	public void updateClob(java.lang.String columnLabel, java.sql.Clob x) throws java.sql.SQLException {
		this.rs.updateClob(columnLabel, x);
	}

	@Override
	public void updateClob(int columnIndex, java.sql.Clob x) throws java.sql.SQLException {
		this.rs.updateClob(columnIndex, x);
	}

	@Override
	public void updateClob(int columnIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(int columnIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(java.lang.String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateClob(columnLabel, reader);
	}

	@Override
	public void updateClob(java.lang.String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateArray(int columnIndex, java.sql.Array x) throws java.sql.SQLException {
		this.rs.updateArray(columnIndex, x);
	}

	@Override
	public void updateArray(java.lang.String columnLabel, java.sql.Array x) throws java.sql.SQLException {
		this.rs.updateArray(columnLabel, x);
	}

	@Override
	public void updateRowId(int columnIndex, java.sql.RowId x) throws java.sql.SQLException {
		this.rs.updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(java.lang.String columnLabel, java.sql.RowId x) throws java.sql.SQLException {
		this.rs.updateRowId(columnLabel, x);
	}

	@Override
	public void updateNClob(java.lang.String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateNClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(int columnIndex, java.sql.NClob nClob) throws java.sql.SQLException {
		this.rs.updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(java.lang.String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(java.lang.String columnLabel, java.sql.NClob nClob) throws java.sql.SQLException {
		this.rs.updateNClob(columnLabel, nClob);
	}

	@Override
	public void updateNClob(int columnIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateSQLXML(java.lang.String columnLabel, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
		this.rs.updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public void updateSQLXML(int columnIndex, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
		this.rs.updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, java.io.Reader x) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(java.lang.String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateNCharacterStream(java.lang.String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnIndex, x, length);
	}

	@Override
	public boolean rowUpdated() throws java.sql.SQLException {
		return this.rs.rowUpdated();
	}

	@Override
	public void cancelRowUpdates() throws java.sql.SQLException {
		this.rs.cancelRowUpdates();
	}

	@Override
	public boolean wasNull() throws java.sql.SQLException {
		return this.rs.wasNull();
	}

	@Override
	public boolean isBeforeFirst() throws java.sql.SQLException {
		return this.rs.isBeforeFirst();
	}

	@Override
	public void beforeFirst() throws java.sql.SQLException {
		this.rs.beforeFirst();
	}

	@Override
	public void refreshRow() throws java.sql.SQLException {
		this.rs.refreshRow();
	}

	@Override
	public boolean absolute(int row) throws java.sql.SQLException {
		return this.rs.absolute(row);
	}

	@Override
	public void afterLast() throws java.sql.SQLException {
		this.rs.afterLast();
	}

	@Override
	public void clearWarnings() throws java.sql.SQLException {
		this.rs.clearWarnings();
	}

	@Override
	public void deleteRow() throws java.sql.SQLException {
		this.rs.deleteRow();
	}

	@Override
	public int findColumn(java.lang.String columnLabel) throws java.sql.SQLException {
		return this.rs.findColumn(columnLabel);
	}

	@Override
	public boolean first() throws java.sql.SQLException {
		return this.rs.first();
	}

	@Override
	public void insertRow() throws java.sql.SQLException {
		this.rs.insertRow();
	}

	@Override
	public boolean isAfterLast() throws java.sql.SQLException {
		return this.rs.isAfterLast();
	}

	@Override
	public boolean isClosed() throws java.sql.SQLException {
		return this.rs.isClosed();
	}

	@Override
	public boolean isFirst() throws java.sql.SQLException {
		return this.rs.isFirst();
	}

	@Override
	public boolean isLast() throws java.sql.SQLException {
		return this.rs.isLast();
	}

	@Override
	public boolean last() throws java.sql.SQLException {
		return this.rs.last();
	}

	@Override
	public void moveToCurrentRow() throws java.sql.SQLException {
		this.rs.moveToCurrentRow();
	}

	@Override
	public void moveToInsertRow() throws java.sql.SQLException {
		this.rs.moveToInsertRow();
	}

	@Override
	public boolean next() throws java.sql.SQLException {
		return this.rs.next();
	}

	@Override
	public boolean previous() throws java.sql.SQLException {
		return this.rs.previous();
	}

	@Override
	public boolean relative(int rows) throws java.sql.SQLException {
		return this.rs.relative(rows);
	}

	@Override
	public boolean rowDeleted() throws java.sql.SQLException {
		return this.rs.rowDeleted();
	}

	@Override
	public boolean rowInserted() throws java.sql.SQLException {
		return this.rs.rowInserted();
	}

	@Override
	public void setFetchDirection(int direction) throws java.sql.SQLException {
		this.rs.setFetchDirection(direction);
	}

	@Override
	public void setFetchSize(int rows) throws java.sql.SQLException {
		this.rs.setFetchSize(rows);
	}

	// java.sql.Wrapper

	@Override
	public boolean isWrapperFor(java.lang.Class<?> arg0) throws java.sql.SQLException {
		return this.rs.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(java.lang.Class<T> arg0) throws java.sql.SQLException {
		return this.rs.unwrap(arg0);
	}

	// java.lang.AutoCloseable

	@Override
	public void close() throws java.sql.SQLException {
		this.rs.close();
	}
}
