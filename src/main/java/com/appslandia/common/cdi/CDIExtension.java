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
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import com.appslandia.common.base.DeployEnv;
import com.appslandia.common.base.EnableEnv;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CDIExtension extends InitializeObject implements Extension {

	final Set<Class<?>> excludedClasses = new HashSet<>();
	final Set<String> excludedPackages = new HashSet<>();
	final Set<Class<? extends Annotation>> excludedAnnotations = new HashSet<>();

	@Override
	protected void init() throws Exception {
	}

	protected CDIExtension excludeClasses(Class<?>... beanClasses) {
		CollectionUtils.toSet(this.excludedClasses, beanClasses);
		return this;
	}

	protected CDIExtension excludePackages(String... packages) {
		CollectionUtils.toSet(this.excludedPackages, packages);
		return this;
	}

	protected CDIExtension excludePackages(Class<?>... beanClasses) {
		for (Class<?> clazz : beanClasses) {
			this.excludedPackages.add(clazz.getPackage().getName());
		}
		return this;
	}

	protected CDIExtension excludeAnnotations(Class<?>... annotationClasses) {
		for (Class<?> clazz : annotationClasses) {
			this.excludedAnnotations.add(ObjectUtils.cast(clazz));
		}
		return this;
	}

	protected boolean willExcludeClass(Class<?> beanClass) {
		return false;
	}

	protected void onExcludeClass(Class<?> beanClass) {
	}

	protected void onRegisterClass(Class<?> beanClass) {
	}

	public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
		this.initialize();
		Class<?> beanClass = event.getAnnotatedType().getJavaClass();

		if (willExcludeClasses(this.excludedClasses, beanClass)) {
			event.veto();
			onExcludeClass(beanClass);
			return;
		}
		if (willExcludePackages(this.excludedPackages, beanClass)) {
			event.veto();
			onExcludeClass(beanClass);
			return;
		}
		if (willExcludeAnnotations(this.excludedAnnotations, beanClass)) {
			event.veto();
			onExcludeClass(beanClass);
			return;
		}

		// @EnableEnv
		EnableEnv enableEnv = getEnableEnv(beanClass);
		if ((enableEnv != null) && !DeployEnv.getCurrent().isAny(enableEnv.value())) {
			event.veto();
			onExcludeClass(beanClass);
			return;
		}

		// Another Chance
		if (willExcludeClass(beanClass)) {
			event.veto();
			onExcludeClass(beanClass);
			return;
		}
		onRegisterClass(beanClass);
	}

	public static boolean willExcludeClasses(Set<Class<?>> excludedClasses, Class<?> beanClass) {
		if (excludedClasses.isEmpty()) {
			return false;
		}
		if (excludedClasses.contains(beanClass)) {
			return true;
		}

		// CDIFactory
		if (CDIFactory.class.isAssignableFrom(beanClass)) {
			Method factoryMethod = CDIUtils.getFactoryMethod(ObjectUtils.cast(beanClass));
			if (excludedClasses.contains(factoryMethod.getReturnType())) {
				return true;
			}
		}
		return false;
	}

	public static boolean willExcludePackages(Set<String> excludedPackages, Class<?> beanClass) {
		if (excludedPackages.isEmpty()) {
			return false;
		}
		for (String pkg : excludedPackages) {
			if (beanClass.getPackage().getName().equals(pkg)) {
				return true;
			}
		}

		// CDIFactory
		if (CDIFactory.class.isAssignableFrom(beanClass)) {
			Method factoryMethod = CDIUtils.getFactoryMethod(ObjectUtils.cast(beanClass));
			for (String pkg : excludedPackages) {
				if (factoryMethod.getReturnType().getPackage().getName().equals(pkg)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean willExcludeAnnotations(Set<Class<? extends Annotation>> excludedAnnotations, Class<?> beanClass) {
		if (excludedAnnotations.isEmpty()) {
			return false;
		}
		for (Class<? extends Annotation> annotationClass : excludedAnnotations) {
			if (beanClass.getDeclaredAnnotation(annotationClass) != null) {
				return true;
			}
		}

		// CDIFactory
		if (CDIFactory.class.isAssignableFrom(beanClass)) {
			Method factoryMethod = CDIUtils.getFactoryMethod(ObjectUtils.cast(beanClass));
			for (Class<? extends Annotation> annotationClass : excludedAnnotations) {
				if (factoryMethod.getDeclaredAnnotation(annotationClass) != null) {
					return true;
				}
			}
		}
		return false;
	}

	public static EnableEnv getEnableEnv(Class<?> beanClass) {
		EnableEnv enableEnv = beanClass.getDeclaredAnnotation(EnableEnv.class);
		if (enableEnv != null) {
			return enableEnv;
		}

		// CDIFactory
		if (CDIFactory.class.isAssignableFrom(beanClass)) {
			Method factoryMethod = CDIUtils.getFactoryMethod(ObjectUtils.cast(beanClass));
			return factoryMethod.getDeclaredAnnotation(EnableEnv.class);
		}
		return null;
	}
}
