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

package com.appslandia.common.easyrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.appslandia.common.base.ToStringBuilder;
import com.appslandia.common.easyrecord.FieldValidator.FieldError;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FieldValidators {

	final Map<String, FieldValidator> validators = new HashMap<>();

	public FieldValidators() {
		initialize();
	}

	protected void initialize() {
		putValidator("required", new RequiredValidator());
		putValidator("bitValues", new BitValuesValidator());
		putValidator("maxLength", new MaxLengthValidator());
		putValidator("validValues", new ValidValuesValidator());
		putValidator("pattern", new PatternValidator());
	}

	public FieldValidator getValidator(String name) throws IllegalArgumentException {
		FieldValidator validator = this.validators.get(name);
		if (validator == null) {
			throw new IllegalArgumentException("Validator is required (name=" + name + ")");
		}
		return validator;
	}

	public void putValidator(String name, FieldValidator validator) {
		this.validators.put(name, validator);
	}

	public void validateField(Field field, Object value, List<FieldError> errors) {
		for (Entry<String, Object> constraint : field.getConstraints().entrySet()) {
			FieldError error = getValidator(constraint.getKey()).validate(value, field.getConstraint(constraint.getKey()));
			if (error != null) {
				errors.add(error);
			}
		}
	}

	public void validateRecord(Record record, Table table, Callback callback) {
		List<FieldError> errors = new ArrayList<>(2);
		for (Field field : table.getFields()) {
			validateField(field, record.get(field.getName()), errors);
			if (!errors.isEmpty()) {
				callback.onError(field.getName(), errors);
			}
		}
	}

	public void validateKey(Record key, Table table, Callback callback) {
		List<FieldError> errors = new ArrayList<>(2);
		for (Field field : table.getFields()) {
			if (field.isKey()) {
				validateField(field, key.get(field.getName()), errors);
				if (!errors.isEmpty()) {
					callback.onError(field.getName(), errors);
				}
			}
		}
	}

	public void validateRecord(Record record, Table table) throws ValidatorException {
		List<FieldError> errors = new ArrayList<>(2);
		for (Field field : table.getFields()) {
			validateField(field, record.get(field.getName()), errors);
			if (!errors.isEmpty()) {
				throw new ValidatorException("field=" + field.getName() + ", errors=" + new ToStringBuilder(3).toString(errors));
			}
		}
	}

	public void validateKey(Record key, Table table) throws ValidatorException {
		List<FieldError> errors = new ArrayList<>(2);
		for (Field field : table.getFields()) {
			if (field.isKey()) {
				validateField(field, key.get(field.getName()), errors);
				if (!errors.isEmpty()) {
					throw new ValidatorException("field=" + field.getName() + ", errors=" + new ToStringBuilder(3).toString(errors));
				}
			}
		}
	}

	public interface Callback {
		void onError(String fieldName, List<FieldError> errors);
	}
}
