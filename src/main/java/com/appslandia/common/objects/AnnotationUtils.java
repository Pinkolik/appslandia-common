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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Qualifier;

import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AnnotationUtils {

	public static boolean equals(Annotation[] anns1, Annotation[] anns2) {
		if (anns2.length != anns1.length) {
			return false;
		}
		if (anns2.length == 0) {
			return true;
		}
		return Arrays.stream(anns2).allMatch(ann2 -> Arrays.stream(anns1).anyMatch(ann1 -> ann1.equals(ann2)));
	}

	public static boolean hasAnnotations(Annotation[] src, Annotation[] annotations) {
		if (annotations.length == 0) {
			return src.length == 0;
		}
		return Arrays.stream(annotations).allMatch(ann -> Arrays.stream(src).anyMatch(srcAnn -> srcAnn.equals(ann)));
	}

	public static Annotation[] parseQualifiers(Annotation[] annotations) {
		List<Annotation> qualifiers = new ArrayList<>(3);
		for (Annotation ann : annotations) {
			if (ann.annotationType().getDeclaredAnnotation(Qualifier.class) != null) {
				qualifiers.add(ann);
			}
		}
		return !qualifiers.isEmpty() ? qualifiers.toArray(new Annotation[qualifiers.size()]) : ReflectionUtils.EMPTY_ANNOTATIONS;
	}

	public static Annotation[] parseQualifiers(AnnotatedElement element) {
		return parseQualifiers(element.getDeclaredAnnotations());
	}

	public static Annotation[] parseQualifiers(ObjectProducer<?> producer) {
		Method m = ReflectionUtils.getDeclaredMethod(producer.getClass(), "produce", ObjectFactory.class);
		return parseQualifiers(m);
	}

	public static ObjectScope parseScope(ObjectProducer<?> producer) {
		Method m = ReflectionUtils.getDeclaredMethod(producer.getClass(), "produce", ObjectFactory.class);
		return parseScope(m);
	}

	public static ObjectScope parseScope(AnnotatedElement element) {
		if (element.getDeclaredAnnotation(ApplicationScoped.class) != null) {
			return ObjectScope.SINGLETON;
		}
		if (element.getDeclaredAnnotation(Dependent.class) != null) {
			return ObjectScope.PROTOTYPE;
		}
		if (element.getDeclaredAnnotation(SessionScoped.class) != null || element.getDeclaredAnnotation(RequestScoped.class) != null) {
			throw new IllegalArgumentException("SessionScoped/RequestScoped is unsupported.");
		}
		return ObjectScope.SINGLETON;
	}
}
