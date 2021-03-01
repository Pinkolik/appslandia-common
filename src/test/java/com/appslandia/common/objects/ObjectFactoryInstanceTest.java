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

package com.appslandia.common.objects;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Qualifier;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectFactoryInstanceTest {

	@Test
	public void test_testService() {
		try {
			ObjectFactory factory = new ObjectFactory();
			factory.register(TestDao.class, TestDao.class);
			factory.register(TestDao.class, TestDao1.class);
			factory.register(TestDao.class, TestDao21.class);
			factory.register(TestDao.class, TestDao22.class);
			factory.register(TestService.class, TestService.class);

			TestService service = factory.getObject(TestService.class);
			Assert.assertNotNull(service.testDaos);

			InstanceImpl<TestDao> sub = ObjectUtils.cast(service.testDaos.select());
			Assert.assertTrue(sub.getCount() == 1);

			sub = ObjectUtils.cast(service.testDaos.select(TestQualifier1.IMPL));
			Assert.assertTrue(sub.getCount() == 0);

		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void test_testService1() {
		try {
			ObjectFactory factory = new ObjectFactory();
			factory.register(TestDao.class, TestDao.class);
			factory.register(TestDao.class, TestDao1.class);
			factory.register(TestDao.class, TestDao21.class);
			factory.register(TestDao.class, TestDao22.class);

			factory.register(TestService1.class, TestService1.class);

			TestService1 service = factory.getObject(TestService1.class);
			Assert.assertNotNull(service.testDaos);

			InstanceImpl<TestDao> sub = ObjectUtils.cast(service.testDaos.select());
			Assert.assertTrue(sub.getCount() == 3);

			sub = ObjectUtils.cast(service.testDaos.select(TestDao.class));
			Assert.assertTrue(sub.getCount() == 3);

			sub = ObjectUtils.cast(service.testDaos.select(TestQualifier21.IMPL));
			Assert.assertTrue(sub.getCount() == 1);

			sub = ObjectUtils.cast(service.testDaos.select(TestQualifier22.IMPL));
			Assert.assertTrue(sub.getCount() == 1);

			sub = ObjectUtils.cast(service.testDaos.select(TestDao21.class));
			Assert.assertTrue(sub.getCount() == 1);

			sub = ObjectUtils.cast(service.testDaos.select(TestDao21.class, TestQualifier22.IMPL));
			Assert.assertTrue(sub.getCount() == 0);

			sub = ObjectUtils.cast(service.testDaos.select(TestDao22.class));
			Assert.assertTrue(sub.getCount() == 1);

			sub = ObjectUtils.cast(service.testDaos.select(TestDao22.class, TestQualifier21.IMPL));
			Assert.assertTrue(sub.getCount() == 0);

		} catch (Exception ex) {
			Assert.fail();
		}
	}

	static class TestService {

		@Inject
		Instance<TestDao> testDaos;
	}

	static class TestService1 {

		@Inject
		@TestQualifier1
		Instance<TestDao> testDaos;
	}

	static class TestDao {
	}

	@TestQualifier1
	static class TestDao1 extends TestDao {
	}

	@TestQualifier1
	@TestQualifier21
	static class TestDao21 extends TestDao {
	}

	@TestQualifier1
	@TestQualifier22
	static class TestDao22 extends TestDao {
	}

	@Qualifier
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.FIELD })
	@Documented
	public @interface TestQualifier1 {
		public static final TestQualifier1 IMPL = new ImplLiteral();

		@SuppressWarnings("all")
		static class ImplLiteral extends AnnotationLiteral<TestQualifier1> implements TestQualifier1 {
			private static final long serialVersionUID = 1L;
		}
	}

	@Qualifier
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.FIELD })
	@Documented
	public @interface TestQualifier21 {
		public static final TestQualifier21 IMPL = new ImplLiteral();

		@SuppressWarnings("all")
		static class ImplLiteral extends AnnotationLiteral<TestQualifier21> implements TestQualifier21 {
			private static final long serialVersionUID = 1L;
		}
	}

	@Qualifier
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.FIELD })
	@Documented
	public @interface TestQualifier22 {
		public static final TestQualifier22 IMPL = new ImplLiteral();

		@SuppressWarnings("all")
		static class ImplLiteral extends AnnotationLiteral<TestQualifier22> implements TestQualifier22 {
			private static final long serialVersionUID = 1L;
		}
	}
}
