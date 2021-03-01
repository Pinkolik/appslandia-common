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
public abstract class TestEntityManagerExtension implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback, AfterAllCallback {

	static final ThreadLocalStorage<EntityManagerFactory> emfHolder = new ThreadLocalStorage<>();
	static final ThreadLocalStorage<EntityManager> emHolder = new ThreadLocalStorage<>();

	protected abstract String getPUName();

	protected EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(getPUName());
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		EntityManagerFactory emf = emfHolder.get();
		if (emf == null) {

			emf = createEntityManagerFactory();
			emfHolder.set(emf);
		}

		EntityManager em = emHolder.get();
		if (em != null) {
			return;
		}
		emHolder.set(emf.createEntityManager());
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		EntityManager em = emHolder.remove();
		AssertUtils.assertStateNotNull(em);
		em.close();

		EntityManagerFactory emf = emfHolder.remove();
		AssertUtils.assertStateNotNull(emf);
		emf.close();
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
	}

	public static EntityManager newEntityManager() {
		EntityManager em = emfHolder.val().createEntityManager();
		emHolder.set(em);
		return em;
	}
}
