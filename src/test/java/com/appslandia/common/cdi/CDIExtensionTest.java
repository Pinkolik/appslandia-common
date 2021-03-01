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

package com.appslandia.common.cdi;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.CollectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CDIExtensionTest {

	@Test
	public void test_willExcludeClasses() {
		Set<Class<?>> excludedClasses = CollectionUtils.toSet(UserService.class);

		boolean excluded = CDIExtension.willExcludeClasses(excludedClasses, UserService.class);
		Assert.assertTrue(excluded);

		excluded = CDIExtension.willExcludeClasses(excludedClasses, UserServiceWithQualifier.class);
		Assert.assertFalse(excluded);

		excluded = CDIExtension.willExcludeClasses(excludedClasses, UserServiceFactory.class);
		Assert.assertTrue(excluded);

		excluded = CDIExtension.willExcludeClasses(excludedClasses, UserServiceFactoryWithQualifier.class);
		Assert.assertTrue(excluded);

		excluded = CDIExtension.willExcludeClasses(excludedClasses, UserServiceFactoryWithQualifierOnProduce.class);
		Assert.assertTrue(excluded);
	}

	@Test
	public void test_willExcludeAnnotations() {
		Set<Class<? extends Annotation>> excludedAnnotations = CollectionUtils.toSet(TestQualifier.class);
		boolean excluded = CDIExtension.willExcludeAnnotations(excludedAnnotations, UserService.class);
		Assert.assertFalse(excluded);

		excluded = CDIExtension.willExcludeAnnotations(excludedAnnotations, UserServiceWithQualifier.class);
		Assert.assertTrue(excluded);

		excluded = CDIExtension.willExcludeAnnotations(excludedAnnotations, UserServiceFactory.class);
		Assert.assertFalse(excluded);

		excluded = CDIExtension.willExcludeAnnotations(excludedAnnotations, UserServiceFactoryWithQualifier.class);
		Assert.assertTrue(excluded);

		excluded = CDIExtension.willExcludeAnnotations(excludedAnnotations, UserServiceFactoryWithQualifierOnProduce.class);
		Assert.assertTrue(excluded);
	}

	static class UserService {
	}

	@TestQualifier
	static class UserServiceWithQualifier extends UserService {

	}

	static class UserServiceFactory implements CDIFactory<UserService> {

		@Produces
		@ApplicationScoped
		@Override
		public UserService produce() {
			return new UserService();
		}

		@Override
		public void dispose(@Disposes UserService t) {
		}
	}

	@TestQualifier
	static class UserServiceFactoryWithQualifier implements CDIFactory<UserService> {

		@Produces
		@ApplicationScoped
		@Override
		public UserService produce() {
			return new UserService();
		}

		@Override
		public void dispose(@Disposes UserService t) {
		}
	}

	static class UserServiceFactoryWithQualifierOnProduce implements CDIFactory<UserService> {

		@Produces
		@ApplicationScoped
		@TestQualifier
		@Override
		public UserService produce() {
			return new UserService();
		}

		@Override
		public void dispose(@Disposes @TestQualifier UserService t) {
		}
	}

	@Qualifier
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
	@Documented
	public @interface TestQualifier {
	}
}
