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
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import com.appslandia.common.base.Params;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EntityManagerAccessor implements EntityManager {

	private EntityManager em;

	public EntityManagerAccessor() {
	}

	public EntityManagerAccessor(EntityManager em) {
		this.em = em;
	}

	protected EntityManager getEm() {
		return this.em;
	}

	public void insert(Object entity) {
		getEm().persist(entity);
		getEm().flush();
	}

	public void insertRefresh(Object entity) {
		getEm().persist(entity);
		getEm().flush();
		getEm().refresh(entity);
	}

	public void removeByPk(Class<?> type, Object primaryKey) throws EntityNotFoundException {
		Object ref = getEm().getReference(type, primaryKey);
		getEm().remove(ref);
	}

	public boolean isInCache(Class<?> type, Object primaryKey) {
		Cache cache = getEm().getEntityManagerFactory().getCache();
		return (cache != null) && cache.contains(type, primaryKey);
	}

	public void evictCache() {
		Cache cache = getEm().getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evictAll();
		}
	}

	public void evictCache(Class<?> type) {
		Cache cache = getEm().getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evict(type);
		}
	}

	public void evictCache(Class<?> type, Object primaryKey) {
		Cache cache = getEm().getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evict(type, primaryKey);
		}
	}

	public <T> T findFetch(Class<T> entityClass, Object primaryKey, String graphName) {
		return getEm().find(entityClass, primaryKey, Params.of(JpaHints.HINT_JPA_FETCH_GRAPH, getEm().createEntityGraph(graphName)));
	}

	public <T> T findLoad(Class<T> entityClass, Object primaryKey, String graphName) {
		return getEm().find(entityClass, primaryKey, Params.of(JpaHints.HINT_JPA_LOAD_GRAPH, getEm().createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createQueryFetch(String qlString, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(
				getEm().createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH, getEm().createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createQueryLoad(String qlString, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(
				getEm().createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH, getEm().createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createNamedQueryFetch(String name, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(
				getEm().createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH, getEm().createEntityGraph(graphName)));
	}

	public <T> TypedQueryAccessor<T> createNamedQueryLoad(String name, Class<T> resultClass, String graphName) {
		return new TypedQueryAccessor<T>(
				getEm().createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH, getEm().createEntityGraph(graphName)));
	}

	@Override
	public void remove(Object entity) {
		getEm().remove(entity);
	}

	@Override
	public void lock(Object entity, javax.persistence.LockModeType lockMode) {
		getEm().lock(entity, lockMode);
	}

	@Override
	public void lock(Object entity, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
		getEm().lock(entity, lockMode, properties);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		getEm().setProperty(propertyName, value);
	}

	@Override
	public void clear() {
		getEm().clear();
	}

	@Override
	public boolean contains(Object entity) {
		return getEm().contains(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return getEm().find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return getEm().find(entityClass, primaryKey, properties);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return getEm().find(entityClass, primaryKey, lockMode);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return getEm().find(entityClass, primaryKey, lockMode, properties);
	}

	@Override
	public Map<String, Object> getProperties() {
		return getEm().getProperties();
	}

	@Override
	public void close() {
		getEm().close();
	}

	@Override
	public void flush() {
		getEm().flush();
	}

	@Override
	public <T> T merge(T entity) {
		return getEm().merge(entity);
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return getEm().unwrap(cls);
	}

	@Override
	public boolean isOpen() {
		return getEm().isOpen();
	}

	@Override
	public void detach(Object entity) {
		getEm().detach(entity);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QueryAccessor createNativeQuery(String sqlString, Class resultClass) {
		return new QueryAccessor(getEm().createNativeQuery(sqlString, resultClass));
	}

	@Override
	public QueryAccessor createNativeQuery(String sqlString) {
		return new QueryAccessor(getEm().createNativeQuery(sqlString));
	}

	@Override
	public QueryAccessor createNativeQuery(String sqlString, String resultSetMapping) {
		return new QueryAccessor(getEm().createNativeQuery(sqlString, resultSetMapping));
	}

	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return getEm().createNamedStoredProcedureQuery(name);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return getEm().createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return getEm().createStoredProcedureQuery(procedureName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return getEm().createStoredProcedureQuery(procedureName, resultClasses);
	}

	@Override
	public boolean isJoinedToTransaction() {
		return getEm().isJoinedToTransaction();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return getEm().getEntityManagerFactory();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return getEm().getCriteriaBuilder();
	}

	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return getEm().createEntityGraph(rootType);
	}

	@Override
	public EntityGraph<?> createEntityGraph(String graphName) {
		return getEm().createEntityGraph(graphName);
	}

	@Override
	public void persist(Object entity) {
		getEm().persist(entity);
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return getEm().getReference(entityClass, primaryKey);
	}

	@Override
	public void setFlushMode(javax.persistence.FlushModeType flushMode) {
		getEm().setFlushMode(flushMode);
	}

	@Override
	public FlushModeType getFlushMode() {
		return getEm().getFlushMode();
	}

	@Override
	public void refresh(Object entity, javax.persistence.LockModeType lockMode) {
		getEm().refresh(entity, lockMode);
	}

	@Override
	public void refresh(Object entity, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
		getEm().refresh(entity, lockMode, properties);
	}

	@Override
	public void refresh(Object entity) {
		getEm().refresh(entity);
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		getEm().refresh(entity, properties);
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return getEm().getLockMode(entity);
	}

	@Override
	public <T> TypedQueryAccessor<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return new TypedQueryAccessor<T>(getEm().createQuery(criteriaQuery));
	}

	@Override
	public <T> TypedQueryAccessor<T> createQuery(String qlString, Class<T> resultClass) {
		return new TypedQueryAccessor<T>(getEm().createQuery(qlString, resultClass));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QueryAccessor createQuery(CriteriaUpdate updateQuery) {
		return new QueryAccessor(getEm().createQuery(updateQuery));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QueryAccessor createQuery(CriteriaDelete deleteQuery) {
		return new QueryAccessor(getEm().createQuery(deleteQuery));
	}

	@Override
	public QueryAccessor createQuery(String qlString) {
		return new QueryAccessor(getEm().createQuery(qlString));
	}

	@Override
	public <T> TypedQueryAccessor<T> createNamedQuery(String name, Class<T> resultClass) {
		return new TypedQueryAccessor<T>(getEm().createNamedQuery(name, resultClass));
	}

	@Override
	public QueryAccessor createNamedQuery(String name) {
		return new QueryAccessor(getEm().createNamedQuery(name));
	}

	@Override
	public void joinTransaction() {
		getEm().joinTransaction();
	}

	@Override
	public Object getDelegate() {
		return getEm().getDelegate();
	}

	@Override
	public EntityTransaction getTransaction() {
		return getEm().getTransaction();
	}

	@Override
	public Metamodel getMetamodel() {
		return getEm().getMetamodel();
	}

	@Override
	public EntityGraph<?> getEntityGraph(String graphName) {
		return getEm().getEntityGraph(graphName);
	}

	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return getEm().getEntityGraphs(entityClass);
	}
}
