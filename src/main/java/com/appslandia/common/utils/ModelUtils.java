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

package com.appslandia.common.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.function.Function;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ModelUtils {

	public static <T> void copy(T dest, T src, Function<String, Boolean> forProps) throws ReflectionException {
		try {
			for (PropertyDescriptor dpd : Introspector.getBeanInfo(dest.getClass()).getPropertyDescriptors()) {
				if (!forProps.apply(dpd.getName())) {
					continue;
				}

				AssertUtils.assertNotNull(dpd.getWriteMethod());
				AssertUtils.assertNotNull(dpd.getReadMethod());

				dpd.getWriteMethod().invoke(dest, dpd.getReadMethod().invoke(src));
			}
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);

		} catch (Exception ex) {
			throw ExceptionUtils.toUncheckedException(ex);
		}
	}

	public static <T> void copy(T dest, T src, String... forProps) throws ReflectionException {
		copy(dest, src, prop -> Arrays.stream(forProps).anyMatch(p -> p.equals(prop)));
	}

	public static <D, S> void copyProps(D dest, S src, Function<String, Boolean> forProps) throws ReflectionException {
		try {
			for (PropertyDescriptor dpd : Introspector.getBeanInfo(dest.getClass()).getPropertyDescriptors()) {
				if (!forProps.apply(dpd.getName())) {
					continue;
				}
				AssertUtils.assertNotNull(dpd.getWriteMethod());

				PropertyDescriptor spd = null;
				for (PropertyDescriptor dp : Introspector.getBeanInfo(src.getClass()).getPropertyDescriptors()) {
					if (dp.getName().equals(dpd.getName())) {
						spd = dp;
						break;
					}
				}
				AssertUtils.assertNotNull(spd);
				AssertUtils.assertNotNull(spd.getReadMethod());

				dpd.getWriteMethod().invoke(dest, spd.getReadMethod().invoke(src));
			}
		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);

		} catch (Exception ex) {
			throw ExceptionUtils.toUncheckedException(ex);
		}
	}

	public static <D, S> void copyProps(D dest, S src, String... forProps) throws ReflectionException {
		copyProps(dest, src, prop -> Arrays.stream(forProps).anyMatch(p -> p.equals(prop)));
	}
}
