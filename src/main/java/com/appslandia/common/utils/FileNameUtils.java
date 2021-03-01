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

import java.util.Date;

import com.appslandia.common.base.UUIDGenerator;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FileNameUtils {

	public static String insertNow(String fileName) {
		return insert(fileName, DateUtils.format(new Date(), "yyyyMMdd_HHmmss"));
	}

	public static String insertUUID(String fileName) {
		return insert(fileName, UUIDGenerator.INSTANCE.generate());
	}

	public static String insert(String fileName, Object info) {
		int idx = fileName.lastIndexOf('.');
		if (idx <= 0) {
			return fileName + "_" + info;
		}
		return fileName.substring(0, idx) + "_" + info + fileName.substring(idx);
	}

	public static String uuidAsName(String fileName) {
		String uuid = UUIDGenerator.INSTANCE.generate();
		int idx = fileName.lastIndexOf('.');
		if (idx < 0) {
			return uuid;
		}
		if (idx == 0) {
			return "." + uuid;
		}
		return uuid + fileName.substring(idx);
	}

	public static String toUnixPath(String path) {
		if (path == null || path.indexOf('\\') == -1) {
			return path;
		}
		return path.replace('\\', '/');
	}
}
