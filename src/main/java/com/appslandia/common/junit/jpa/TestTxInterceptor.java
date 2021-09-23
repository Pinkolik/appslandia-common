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

package com.appslandia.common.junit.jpa;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.InvalidTransactionException;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class TestTxInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	protected TestEmfControl testEmfControl;

	public Object doTran(InvocationContext context) throws Exception {

		// @Transactional
		Transactional tx = context.getMethod().getAnnotation(Transactional.class);
		if (tx == null) {
			tx = context.getTarget().getClass().getAnnotation(Transactional.class);
		}
		TxType type = tx.value();

		// NEVER
		// if a transaction was started, raises an exception; otherwise works with no
		// transaction.
		if (type == TxType.NEVER) {
			EntityManager curEm = getEm();

			if ((curEm != null) && curEm.getTransaction().isActive()) {
				throw new InvalidTransactionException("TxType.NEVER - Transaction active found.");
			}

			// No TX
			setEm(null);
			try {
				return context.proceed();

			} catch (Exception ex) {
				throw ex;

			} finally {
				setEm(curEm);
			}
		}

		// MANDATORY
		// fails if no transaction was started; works within the existing transaction
		// otherwise.
		if (type == TxType.MANDATORY) {
			EntityManager curEm = getEm();

			if ((curEm == null) || !curEm.getTransaction().isActive()) {
				throw new InvalidTransactionException("TxType.MANDATORY - No transaction active found.");
			}

			// Join TX
			return context.proceed();
		}

		// SUPPORTS
		// if a transaction was started, joins it; otherwise works with no transaction.
		if (type == TxType.SUPPORTS) {
			EntityManager curEm = getEm();

			if ((curEm != null) && curEm.getTransaction().isActive()) {
				// Join TX
				return context.proceed();
			} else {

				// No TX
				setEm(null);
				try {
					return context.proceed();

				} catch (Exception ex) {
					throw ex;

				} finally {
					setEm(curEm);
				}
			}
		}

		// NOT_SUPPORTED
		// if a transaction was started, suspends it and works with no transaction for
		// the boundary of the method; otherwise works with no transaction.
		if (type == TxType.NOT_SUPPORTED) {
			EntityManager curEm = getEm();
			setEm(null);
			try {

				return context.proceed();

			} catch (Exception ex) {
				throw ex;

			} finally {
				setEm(curEm);
			}
		}

		// REQUIRES_NEW
		// starts a transaction if none was started; if an existing one was started,
		// suspends it and starts a new one for the boundary of that method.
		if (type == TxType.REQUIRES_NEW) {

			EntityManager curEm = getEm();

			EntityManager newEm = storeNewEm();
			EntityTransaction et = newEm.getTransaction();
			et.begin();

			try {
				Object obj = context.proceed();
				et.commit();

				return obj;

			} catch (Exception ex) {
				if (willRollbackOn(ex, tx.rollbackOn(), tx.dontRollbackOn())) {
					et.rollback();
				}

				throw ex;
			} finally {
				newEm.close();
				setEm(curEm);
			}
		}

		// TxType.REQUIRED
		// (default): starts a transaction if none was started, stays with the existing
		// one otherwise.
		EntityManager curEm = getEm();
		boolean startNew = (curEm == null);

		if (startNew) {
			curEm = storeNewEm();
		}

		EntityTransaction et = curEm.getTransaction();
		boolean inTrans = et.isActive();

		if (!inTrans)
			et.begin();

		try {
			Object obj = context.proceed();

			if (!inTrans)
				et.commit();

			return obj;

		} catch (Exception ex) {
			if (!inTrans) {
				if (willRollbackOn(ex, tx.rollbackOn(), tx.dontRollbackOn())) {
					et.rollback();
				}
			}

			throw ex;
		} finally {
			if (startNew)
				setEm(null);
		}
	}

	protected EntityManager getEm() {
		if (this.testEmfControl.isSharedEmf()) {
			return SharedEmfTestEntityManagerExtension.emHolder.get();
		} else {
			return TestEntityManagerExtension.emHolder.get();
		}
	}

	protected EntityManager storeNewEm() {
		if (this.testEmfControl.isSharedEmf()) {
			return SharedEmfTestEntityManagerExtension.newEntityManager();
		} else {
			return TestEntityManagerExtension.newEntityManager();
		}
	}

	protected void setEm(EntityManager em) {
		if (this.testEmfControl.isSharedEmf()) {
			SharedEmfTestEntityManagerExtension.emHolder.set(em);
		} else {
			TestEntityManagerExtension.emHolder.set(em);
		}
	}

	protected boolean willRollbackOn(Exception ex, Class<?>[] rollbackOn, Class<?>[] dontRollbackOn) {
		// Runtime Exception
		if (ex instanceof RuntimeException) {
			Class<?> dontRollbackOnClass = getClosestMatchOrNull(dontRollbackOn, ex.getClass());

			if (dontRollbackOnClass == null) {
				return true;
			}
			if (dontRollbackOnClass.equals(ex.getClass()) || dontRollbackOnClass.isAssignableFrom(ex.getClass())) {
				return false;
			}

			Class<?> rollbackOnClass = getClosestMatchOrNull(rollbackOn, ex.getClass());
			if (rollbackOnClass != null) {

				if (rollbackOnClass.isAssignableFrom(dontRollbackOnClass)) {
					return false;
				} else if (dontRollbackOnClass.isAssignableFrom(rollbackOnClass)) {
					return true;
				}
			}
			return true;
		}
		// Checked Exception
		else {
			Class<?> rollbackOnClass = getClosestMatchOrNull(rollbackOn, ex.getClass());
			if (rollbackOnClass == null) {
				return false;
			}

			Class<?> dontRollbackOnClass = getClosestMatchOrNull(dontRollbackOn, ex.getClass());
			if (dontRollbackOnClass != null) {

				if (rollbackOnClass.isAssignableFrom(dontRollbackOnClass)) {
					return false;
				} else if (dontRollbackOnClass.isAssignableFrom(rollbackOnClass)) {
					return true;
				}
			}

			if (rollbackOnClass.equals(ex.getClass()) || rollbackOnClass.isAssignableFrom(ex.getClass())) {
				return true;
			}
			return false;
		}
	}

	private Class<?> getClosestMatchOrNull(Class<?>[] exClasses, Class<?> exceptionClass) {
		Class<?> closestMatch = null;

		for (Class<?> exClass : exClasses) {
			if (exClass.equals(exceptionClass)) {
				return exClass;

			}
			if (exClass.isAssignableFrom(exceptionClass)) {
				if (closestMatch == null || closestMatch.isAssignableFrom(exClass))
					closestMatch = exClass;
			}
		}
		return closestMatch;
	}
}
