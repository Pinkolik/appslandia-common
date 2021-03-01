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

package com.appslandia.common.validators;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ValidateTest {

	static {
		ModelValidator.addValidator("confirmPasswordValidator", new ModelValidator<ResetPasswordModel>() {

			@Override
			public boolean validate(ResetPasswordModel model) {
				if (model.password == null || model.confirmPassword == null) {
					return true;
				}
				return model.confirmPassword.equals(model.password);
			}
		});
	}

	@Test
	public void test() {
		ResetPasswordModel m = new ResetPasswordModel();
		m.password = "123456";
		m.confirmPassword = "123457";

		Set<?> errors = ValidatorUtils.getValidator().validate(m);
		Assert.assertTrue(!errors.isEmpty());

	}

	@Validate(modelValidator = "confirmPasswordValidator", message = "{confirmPasswordValidator.message}", reportProperty = "confirmPassword")
	private static class ResetPasswordModel {

		public String password;

		public String confirmPassword;
	}
}
