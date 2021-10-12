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

package com.appslandia.common.jpa;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.appslandia.common.jdbc.LikeType;
import com.appslandia.common.jdbc.SqlLikeEscaper;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.TagUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class QueryAccessor implements Query {

	final Query q;

	public QueryAccessor(Query q) {
		this.q = q;
	}

	public <T> T getFirstOrNull() {
		List<T> results = setMaxResults(1).getList();
		return !results.isEmpty() ? results.get(0) : null;
	}

	public <T> T getSingleOrNull() {
		try {
			return ObjectUtils.cast(this.q.getSingleResult());
		} catch (NoResultException ex) {
			return null;
		}
	}

	public <T> List<T> getList() {
		return ObjectUtils.cast(getResultList());
	}

	public QueryAccessor setStartPos(int pos) {
		this.q.setFirstResult(pos);
		return this;
	}

	// Set LIKE Parameters
	// :name IS NULL OR name LIKE :name
	// :name = '' OR name LIKE :name

	public QueryAccessor setLikeTag(String parameterName, String tag) {
		return setLikePattern(parameterName, TagUtils.wrapTag(tag));
	}

	public QueryAccessor setLikePattern(String parameterName, String value) {
		this.q.setParameter(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.CONTAINS));
		return this;
	}

	public QueryAccessor setLikeStart(String parameterName, String value) {
		this.q.setParameter(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.STARTS_WITH));
		return this;
	}

	public QueryAccessor setLikeEnd(String parameterName, String value) {
		this.q.setParameter(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.ENDS_WITH));
		return this;
	}

	// javax.persistence.Query

	@Override
	public java.util.Map<java.lang.String, java.lang.Object> getHints() {
		return this.q.getHints();
	}

	@Override
	public int getFirstResult() {
		return this.q.getFirstResult();
	}

	@Override
	public javax.persistence.FlushModeType getFlushMode() {
		return this.q.getFlushMode();
	}

	@Override
	public javax.persistence.LockModeType getLockMode() {
		return this.q.getLockMode();
	}

	@Override
	public int getMaxResults() {
		return this.q.getMaxResults();
	}

	@Override
	public javax.persistence.Parameter<?> getParameter(int position) {
		return this.q.getParameter(position);
	}

	@Override
	public javax.persistence.Parameter<?> getParameter(java.lang.String name) {
		return this.q.getParameter(name);
	}

	@Override
	public <T> javax.persistence.Parameter<T> getParameter(int position, java.lang.Class<T> type) {
		return this.q.getParameter(position, type);
	}

	@Override
	public <T> javax.persistence.Parameter<T> getParameter(java.lang.String name, java.lang.Class<T> type) {
		return this.q.getParameter(name, type);
	}

	@Override
	public java.lang.Object getParameterValue(int position) {
		return this.q.getParameterValue(position);
	}

	@Override
	public java.lang.Object getParameterValue(java.lang.String name) {
		return this.q.getParameterValue(name);
	}

	@Override
	public <T> T getParameterValue(javax.persistence.Parameter<T> param) {
		return this.q.getParameterValue(param);
	}

	@Override
	public java.util.Set<javax.persistence.Parameter<?>> getParameters() {
		return this.q.getParameters();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public java.util.List getResultList() {
		return this.q.getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public java.util.stream.Stream getResultStream() {
		return this.q.getResultStream();
	}

	@Override
	public java.lang.Object getSingleResult() {
		return this.q.getSingleResult();
	}

	@Override
	public QueryAccessor setHint(java.lang.String hintName, java.lang.Object value) {
		this.q.setHint(hintName, value);
		return this;
	}

	@Override
	public QueryAccessor setFirstResult(int startPosition) {
		this.q.setFirstResult(startPosition);
		return this;
	}

	@Override
	public QueryAccessor setFlushMode(javax.persistence.FlushModeType flushMode) {
		this.q.setFlushMode(flushMode);
		return this;
	}

	@Override
	public QueryAccessor setLockMode(javax.persistence.LockModeType lockMode) {
		this.q.setLockMode(lockMode);
		return this;
	}

	@Override
	public QueryAccessor setMaxResults(int maxResult) {
		this.q.setMaxResults(maxResult);
		return this;
	}

	@Override
	public QueryAccessor setParameter(int position, java.lang.Object value) {
		this.q.setParameter(position, value);
		return this;
	}

	@Override
	public QueryAccessor setParameter(java.lang.String name, java.lang.Object value) {
		this.q.setParameter(name, value);
		return this;
	}

	@Override
	public <T> QueryAccessor setParameter(javax.persistence.Parameter<T> param, T value) {
		this.q.setParameter(param, value);
		return this;
	}

	@Override
	public QueryAccessor setParameter(int position, java.util.Calendar value, javax.persistence.TemporalType temporalType) {
		this.q.setParameter(position, value, temporalType);
		return this;
	}

	@Override
	public QueryAccessor setParameter(int position, java.util.Date value, javax.persistence.TemporalType temporalType) {
		this.q.setParameter(position, value, temporalType);
		return this;
	}

	@Override
	public QueryAccessor setParameter(java.lang.String name, java.util.Calendar value, javax.persistence.TemporalType temporalType) {
		this.q.setParameter(name, value, temporalType);
		return this;
	}

	@Override
	public QueryAccessor setParameter(java.lang.String name, java.util.Date value, javax.persistence.TemporalType temporalType) {
		this.q.setParameter(name, value, temporalType);
		return this;
	}

	@Override
	public QueryAccessor setParameter(javax.persistence.Parameter<java.util.Calendar> param, java.util.Calendar value,
			javax.persistence.TemporalType temporalType) {
		this.q.setParameter(param, value, temporalType);
		return this;
	}

	@Override
	public QueryAccessor setParameter(javax.persistence.Parameter<java.util.Date> param, java.util.Date value, javax.persistence.TemporalType temporalType) {
		this.q.setParameter(param, value, temporalType);
		return this;
	}

	@Override
	public int executeUpdate() {
		return this.q.executeUpdate();
	}

	@Override
	public boolean isBound(javax.persistence.Parameter<?> param) {
		return this.q.isBound(param);
	}

	@Override
	public <T> T unwrap(java.lang.Class<T> cls) {
		return this.q.unwrap(cls);
	}
}
