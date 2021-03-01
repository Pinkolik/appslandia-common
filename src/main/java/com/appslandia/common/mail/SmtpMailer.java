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

package com.appslandia.common.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import com.appslandia.common.base.Config;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.SecureProps;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ExceptionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SmtpMailer extends InitializeObject {

	protected SecureProps props;
	protected Session session;

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.props, "props is required.");

		this.session = Session.getInstance(this.props);
	}

	public MailerMessage newMessage() throws AddressException {
		MailerMessage msg = new MailerMessage();

		String msgFrom = this.props.get("mail.smtp.msg.from");
		if (msgFrom != null) {
			msg.from(msgFrom);
		}
		return msg;
	}

	public void send(MailerMessage message) throws MessagingException {
		send(CollectionUtils.toList(new ArrayList<>(1), message));
	}

	public void send(List<MailerMessage> messages) throws MessagingException {
		initialize();
		AssertUtils.assertNotNull(messages);

		try (Transport transport = this.session.getTransport("smtp")) {

			String user = AssertUtils.assertNotNull(this.props.get("mail.smtp.user"), "mail.smtp.user is required.");
			String password = AssertUtils.assertNotNull(this.props.get("mail.smtp.password"), "mail.smtp.password is required.");
			transport.connect(user, password);

			String debugToEmails = null;
			if (Config.isTrueValue(this.props.get("mail.smtp.debug.enabled"))) {
				debugToEmails = AssertUtils.assertNotNull(this.props.get("mail.smtp.debug.to_emails"), "mail.smtp.debug.to_emails is required.");
			}

			for (MailerMessage mailerMessage : messages) {
				MimeMessage message = mailerMessage.toMimeMessage(this, debugToEmails);
				message.saveChanges();

				transport.sendMessage(message, message.getAllRecipients());
			}
		}
	}

	public void sendAsync(MailerMessage message, Executor executor, Consumer<Exception> consumer) {
		sendAsync(CollectionUtils.toList(new ArrayList<>(1), message), executor, consumer);
	}

	public void sendAsync(List<MailerMessage> messages, Executor executor, Consumer<Exception> consumer) {
		initialize();
		AssertUtils.assertNotNull(messages);
		AssertUtils.assertNotNull(executor);

		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					send(messages);

				} catch (Exception ex) {
					if (consumer != null)
						consumer.accept(ex);

					throw ExceptionUtils.toUncheckedException(ex);
				}
			}
		});
	}

	public SmtpMailer setProps(SecureProps props) {
		assertNotInitialized();

		if (props != null) {
			this.props = props.copy();
		}
		return this;
	}
}
