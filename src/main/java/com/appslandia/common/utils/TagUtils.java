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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.appslandia.common.base.Out;
import com.appslandia.common.jdbc.LikeType;
import com.appslandia.common.jdbc.SqlLikeEscaper;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TagUtils {

	static final Pattern TAGS_PATTEN = Pattern.compile("[^,]+(\\s*,\\s*[^,]*)*", Pattern.CASE_INSENSITIVE);

	public static final String TBD_TAG = "#tbd";
	public static final String UNSORTED_TAG = "#unsorted";

	// #tag1, #tag2
	// tag1, #tag2
	// tag1, tag2
	public static List<String> toTags(String tags, Out<Boolean> isValid) {
		List<String> result = new ArrayList<>();

		isValid.value = true;
		if (tags == null) {
			return result;
		}

		if (TBD_TAG.equals(tags)) {
			result.add(TBD_TAG);
			return result;
		}
		if (UNSORTED_TAG.equals(tags)) {
			result.add(UNSORTED_TAG);
			return result;
		}

		if (!TAGS_PATTEN.matcher(tags).matches()) {
			isValid.value = false;
			return result;
		}

		// Parse tags
		final Out<Boolean> valid = new Out<>();
		for (String tag : SplitUtils.split(tags, ',')) {
			tag = toTag(tag, valid);

			if ((tag != null) && !result.contains(tag)) {
				result.add(tag);
			}
		}

		if (result.isEmpty()) {
			isValid.value = false;
		}

		// #tbd must be first
		// #unsorted resulting in removal of others
		boolean hasTbd = result.remove(TBD_TAG);

		if (result.contains(UNSORTED_TAG)) {
			result.removeIf(t -> !t.equals(UNSORTED_TAG));
		}
		if (hasTbd) {
			result.add(0, TBD_TAG);
		}
		return result;
	}

	// tag, #tag -> #tag
	public static String toTag(String tag, Out<Boolean> isValid) {
		isValid.value = true;
		if (tag == null) {
			return null;
		}
		if (TBD_TAG.equals(tag)) {
			return TBD_TAG;
		}
		if (UNSORTED_TAG.equals(tag)) {
			return UNSORTED_TAG;
		}

		tag = tag.toLowerCase(Locale.ROOT);
		String nmlTag = NormalizeUtils.normalizeLabel(tag);

		if (nmlTag == null) {
			isValid.value = false;
			return null;
		}
		return "#" + nmlTag;
	}

	// |tag1|#tag2|
	public static String[] toTags(String dbTags) {
		return SplitUtils.split(dbTags, '|');
	}

	// |#tag1|#tag2| -> #tag1, #tag2
	public static String toDispTags(String dbTags) {
		return String.join(", ", toTags(dbTags));
	}

	// #tag1 -> |#tag1|
	public static String wrapTag(String tag) {
		return "|" + tag + "|";
	}

	// |#tag1|#tag2|
	public static String toDbTags(Collection<String> tags) {
		return StringUtils.join('|', true, tags);
	}

	// #tag -> %|#tag|%
	public static String toTagLikeVal(String tag) {
		return SqlLikeEscaper.toLikePattern(wrapTag(tag), LikeType.CONTAINS);
	}
}
