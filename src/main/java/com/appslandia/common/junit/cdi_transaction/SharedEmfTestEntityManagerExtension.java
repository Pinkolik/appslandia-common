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

package com.appslandia.common.junit.cdi_transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.appslandia.common.threading.ThreadLocalStorage;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class SharedEmfTestEntityManagerExtension implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback, AfterAllCallback {

	static final ThreadLocalStorage<EntityManager> emHolder = new ThreadLocalStorage<>();

	static volatile private EntityManagerFactory emf;
	static final Object MUTEX = new Object();

	protected abstract String getPUName();

	protected EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(getPUName());
	}

	protected abstract void initEach(ExtensionContext context, EntityManager txEm);

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		EntityManager em = emHolder.get();
		if (em != null) {
			return;
		}
		em = emf.createEntityManager();
		emHolder.set(em);

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			initEach(context, em);

			et.commit();
		} catch (Exception ex) {
			et.rollback();

			throw ex;
		}
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		EntityManager em = emHolder.remove();
		AssertUtils.assertStateNotNull(em);
		em.close();
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		if (emf == null) {
			synchronized (MUTEX) {
				if (emf == null) {
					emf = createEntityManagerFactory();
				}
			}
		}
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		if (emf != null) {
			synchronized (MUTEX) {
				if (emf != null) {
					emf.close();
					emf = null;
				}
			}
		}
	}

	public static EntityManager newEntityManager() {
		AssertUtils.assertStateNotNull(emf);

		EntityManager em = emf.createEntityManager();
		emHolder.set(em);
		return em;
	}
}
