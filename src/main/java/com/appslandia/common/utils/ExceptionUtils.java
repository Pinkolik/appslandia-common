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

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import com.appslandia.common.base.StringWriter;
import com.appslandia.common.base.UncheckedException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ExceptionUtils {

	public static Exception tryUnwrap(InvocationTargetException ex) {
		Throwable te = ex.getTargetException();
		if (te instanceof Exception) {
			return (Exception) te;
		}
		return ex;
	}

	public static String toStackTrace(Throwable ex) {
		StringWriter out = new StringWriter();
		PrintWriter w = new PrintWriter(out);
		ex.printStackTrace(w);
		w.close();
		return out.toString();
	}

	public static String buildMessage(Throwable ex) {
		StringBuilder sb = new StringBuilder();
		sb.append(ex.getClass().getName());
		sb.append('(');
		sb.append("message=").append(ex.getMessage());
		sb.append("; cause=");
		if (ex.getCause() != null) {
			sb.append(ex.getCause().getClass().getName());
		} else {
			sb.append("null");
		}
		sb.append(')');
		return sb.toString();
	}

	public static RuntimeException toUncheckedException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			return (RuntimeException) ex;
		}
		return new UncheckedException(ex);
	}
}
