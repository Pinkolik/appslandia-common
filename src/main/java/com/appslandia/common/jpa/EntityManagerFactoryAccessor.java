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

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EntityManagerFactoryAccessor implements EntityManagerFactory {

	private EntityManagerFactory emf;

	public EntityManagerFactoryAccessor() {
	}

	public EntityManagerFactoryAccessor(EntityManagerFactory emf) {
		this.emf = emf;
	}

	protected EntityManagerFactory getEmf() {
		return this.emf;
	}

	@Override
	public EntityManagerAccessor createEntityManager() {
		return new EntityManagerAccessor(getEmf().createEntityManager());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public EntityManagerAccessor createEntityManager(Map map) {
		return new EntityManagerAccessor(getEmf().createEntityManager(map));
	}

	@Override
	public EntityManagerAccessor createEntityManager(SynchronizationType synchronizationType) {
		return new EntityManagerAccessor(getEmf().createEntityManager(synchronizationType));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public EntityManagerAccessor createEntityManager(SynchronizationType synchronizationType, Map map) {
		return new EntityManagerAccessor(getEmf().createEntityManager(synchronizationType, map));
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return getEmf().getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return getEmf().getMetamodel();
	}

	@Override
	public boolean isOpen() {
		return getEmf().isOpen();
	}

	@Override
	public void close() {
		getEmf().close();
	}

	@Override
	public Map<String, Object> getProperties() {
		return getEmf().getProperties();
	}

	@Override
	public Cache getCache() {
		return getEmf().getCache();
	}

	@Override
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return getEmf().getPersistenceUnitUtil();
	}

	@Override
	public void addNamedQuery(String name, Query query) {
		getEmf().addNamedQuery(name, query);
	}

	@Override
	public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
		getEmf().addNamedEntityGraph(graphName, entityGraph);
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return getEmf().unwrap(cls);
	}
}
