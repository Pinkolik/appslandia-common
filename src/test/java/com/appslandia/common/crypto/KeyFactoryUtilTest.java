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

package com.appslandia.common.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class KeyFactoryUtilTest {

	final KeyFactoryUtil keyFactoryUtil = new KeyFactoryUtil("DSA");

	@Test
	public void test() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
			generator.initialize(1024, new SecureRandom());
			KeyPair keyPair = generator.generateKeyPair();

			String privateKeyPem = PKIUtils.toPemEncoded(keyPair.getPrivate());
			String publicKeyPem = PKIUtils.toPemEncoded(keyPair.getPublic());

			PrivateKey privateKey = keyFactoryUtil.toPrivateKey(privateKeyPem);
			PublicKey publicKey = keyFactoryUtil.toPublicKey(publicKeyPem);

			Assert.assertTrue(keyPair.getPrivate().equals(privateKey));
			Assert.assertTrue(keyPair.getPublic().equals(publicKey));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_copy() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
			generator.initialize(1024, new SecureRandom());
			KeyPair keyPair = generator.generateKeyPair();

			PrivateKey privateKey = keyFactoryUtil.copy(keyPair.getPrivate());
			PublicKey publicKey = keyFactoryUtil.copy(keyPair.getPublic());

			Assert.assertTrue(keyPair.getPrivate().equals(privateKey));
			Assert.assertTrue(keyPair.getPublic().equals(publicKey));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
