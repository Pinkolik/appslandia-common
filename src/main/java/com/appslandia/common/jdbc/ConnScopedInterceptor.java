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

import java.io.Serializable;

import javax.interceptor.InvocationContext;
import javax.sql.DataSource;

import com.appslandia.common.jdbc.ConnScoped.ConnType;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ConnScopedInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;

	public Object intercept(InvocationContext context) throws Exception {

		// @ConnScoped
		ConnScoped connScoped = context.getMethod().getAnnotation(ConnScoped.class);
		ConnType type = connScoped.value();

		// REQUIRES_NEW
		if (type == ConnType.REQUIRES_NEW) {

			try (ConnectionImpl newConn = new ConnectionImpl(getDataSource(connScoped.ds()))) {
				return context.proceed();
			}
		}

		// REQUIRED
		if (ConnectionImpl.hasCurrent()) {

			// Same DS
			if (connScoped.ds().equals(ConnectionImpl.getCurrent().getDsName())) {
				return context.proceed();

			} else {
				// Different DS
				try (ConnectionImpl newConn = new ConnectionImpl(getDataSource(connScoped.ds()))) {
					return context.proceed();
				}
			}
		} else {

			try (ConnectionImpl newConn = new ConnectionImpl(getDataSource(connScoped.ds()))) {
				return context.proceed();
			}
		}
	}

	protected abstract DataSource getDataSource(String name);
}
