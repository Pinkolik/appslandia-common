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

package com.appslandia.common.base;

import java.util.Locale;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DeployEnv {

	public static final DeployEnv DEVELOPMENT = new DeployEnv("Development");
	public static final DeployEnv TESTING = new DeployEnv("Testing");
	public static final DeployEnv STAGING = new DeployEnv("Staging");
	public static final DeployEnv PRODUCTION = new DeployEnv("Production");

	final String name;

	private DeployEnv(String name) {
		this.name = AssertUtils.assertNotNull(name);
	}

	public boolean isStagingOrProduction() {
		return this.equals(STAGING) || this.equals(PRODUCTION);
	}

	public boolean isDevelopment() {
		return this.equals(DEVELOPMENT) || this.name.toLowerCase(Locale.ENGLISH).startsWith("development");
	}

	public boolean isTesting() {
		return this.equals(TESTING);
	}

	public boolean isStaging() {
		return this.equals(STAGING);
	}

	public boolean isProduction() {
		return this.equals(PRODUCTION);
	}

	public boolean isAny(String... environments) {
		for (String env : environments) {
			if (this.name.equalsIgnoreCase(env)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "DeployEnv: " + this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DeployEnv)) {
			return false;
		}
		DeployEnv another = (DeployEnv) obj;
		return this.name.equalsIgnoreCase(another.name);
	}

	private static volatile DeployEnv __current;
	private static final Object MUTEX = new Object();

	public static DeployEnv getCurrent() {
		DeployEnv obj = __current;
		if (obj == null) {
			synchronized (MUTEX) {
				if ((obj = __current) == null) {
					__current = obj = initDeployEnv();
				}
			}
		}
		return obj;
	}

	public static void setCurrent(DeployEnv env) {
		if (__current == null) {
			synchronized (MUTEX) {
				if (__current == null) {
					__current = env;
					return;
				}
			}
		}
		throw new IllegalStateException("DeployEnv.__current must be null.");
	}

	public static void setCurrent(String env) {
		setCurrent(getDeployEnv(env));
	}

	private static DeployEnv initDeployEnv() {
		String env = SYS.resolveExpr("${deploy_env,env.DEPLOY_ENV:Development}", new Out<>());
		return getDeployEnv(env);
	}

	private static DeployEnv getDeployEnv(String env) {
		if (DEVELOPMENT.name.equalsIgnoreCase(env)) {
			return DEVELOPMENT;
		}
		if (TESTING.name.equalsIgnoreCase(env)) {
			return TESTING;
		}
		if (STAGING.name.equalsIgnoreCase(env)) {
			return STAGING;
		}
		if (PRODUCTION.name.equalsIgnoreCase(env)) {
			return PRODUCTION;
		}
		return new DeployEnv(env);
	}
}
