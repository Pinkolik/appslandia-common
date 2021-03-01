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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CertificateFactoryUtil extends InitializeObject {

	public static final CertificateFactoryUtil X509 = new CertificateFactoryUtil("X.509");

	private String type;
	private String provider;
	private CertificateFactory certificateFactory;
	final Object mutex = new Object();

	public CertificateFactoryUtil() {
	}

	public CertificateFactoryUtil(String type) {
		this.type = type;
	}

	public CertificateFactoryUtil(String type, String provider) {
		this.type = type;
		this.provider = provider;
	}

	@Override
	protected void init() throws Exception {
		this.type = ValueUtils.valueOrAlt(this.type, "X.509");

		if (this.provider == null) {
			this.certificateFactory = CertificateFactory.getInstance(this.type);
		} else {
			this.certificateFactory = CertificateFactory.getInstance(this.type, provider);
		}
	}

	public X509Certificate toCertificate(InputStream certInDer) throws CryptoException {
		this.initialize();
		try {
			synchronized (this.mutex) {
				return (X509Certificate) this.certificateFactory.generateCertificate(certInDer);
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public X509Certificate toCertificate(String certInPem) throws CryptoException {
		this.initialize();
		byte[] der = PKIUtils.toDerEncoded(certInPem);
		try {
			synchronized (this.mutex) {
				return (X509Certificate) this.certificateFactory.generateCertificate(new ByteArrayInputStream(der));
			}
		} catch (GeneralSecurityException ex) {
			throw new CryptoException(ex);
		}
	}

	public CertificateFactoryUtil setType(String type) {
		assertNotInitialized();
		this.type = type;
		return this;
	}

	public CertificateFactoryUtil setProvider(String provider) {
		assertNotInitialized();
		this.provider = provider;
		return this;
	}
}
