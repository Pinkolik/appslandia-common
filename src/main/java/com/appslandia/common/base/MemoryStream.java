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

package com.appslandia.common.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.StringFormat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MemoryStream extends OutputStream implements Serializable {
	private static final long serialVersionUID = 1L;

	private int blockSize;
	private NodeList nodeList;

	private long count;
	private int lastLen;

	public MemoryStream() {
		this(512);
	}

	public MemoryStream(int blockSize) {
		AssertUtils.assertTrue(blockSize > 0);

		this.blockSize = blockSize;
		this.nodeList = new NodeList(new byte[this.blockSize]);
	}

	@Override
	public void write(int b) throws IOException {
		byte[] lastBuf = this.nodeList.last.buf;
		int lenAv = lastBuf.length - this.lastLen;
		if (lenAv >= 1) {
			lastBuf[this.lastLen] = (byte) b;
			this.lastLen += 1;
		} else {
			this.nodeList.insert(new byte[this.blockSize]);
			this.nodeList.last.buf[0] = (byte) b;
			this.lastLen = 1;
		}
		this.count += 1;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		byte[] lastBuf = this.nodeList.last.buf;
		int lenAv = lastBuf.length - this.lastLen;

		if (lenAv >= len) {
			System.arraycopy(b, off, lastBuf, this.lastLen, len);
			this.lastLen += len;

		} else {
			int addLen = len - lenAv;
			int addBlk = addLen / (this.blockSize);
			if (addBlk * this.blockSize < addLen) {
				addBlk++;
			}
			this.nodeList.insert(new byte[addBlk * this.blockSize]);

			if (lenAv == 0) {
				System.arraycopy(b, off, this.nodeList.last.buf, 0, len);
				this.lastLen = len;
			} else {
				System.arraycopy(b, off, lastBuf, this.lastLen, lenAv);
				System.arraycopy(b, off + lenAv, this.nodeList.last.buf, 0, len - lenAv);
				this.lastLen = len - lenAv;
			}
		}
		this.count += len;
	}

	public void writeTo(OutputStream out) throws IOException {
		Node n = this.nodeList.first;
		while (n != null) {
			if (n != this.nodeList.last) {
				out.write(n.buf, 0, n.buf.length);
			} else {
				out.write(n.buf, 0, this.lastLen);
			}
			n = n.next;
		}
	}

	public byte[] toByteArray() {
		byte[] bytes = new byte[(int) this.count];
		int destPos = 0;
		Node n = this.nodeList.first;
		while (n != null) {
			if (n != this.nodeList.last) {
				System.arraycopy(n.buf, 0, bytes, destPos, n.buf.length);
				destPos += n.buf.length;
			} else {
				System.arraycopy(n.buf, 0, bytes, destPos, this.lastLen);
				destPos += this.lastLen;
			}
			n = n.next;
		}
		return bytes;
	}

	public int getNodeCount() {
		int count = 0;
		Node n = this.nodeList.first;
		while (n != null) {
			count++;
			n = n.next;
		}
		return count;
	}

	public byte[] digest(String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		Node n = this.nodeList.first;
		while (n != null) {
			if (n != this.nodeList.last) {
				md.update(n.buf, 0, n.buf.length);
			} else {
				md.update(n.buf, 0, this.lastLen);
			}
			n = n.next;
		}
		return md.digest();
	}

	public void iterate(NodeIterator iterator) throws IOException {
		Node n = this.nodeList.first;
		while (n != null) {
			if (n != this.nodeList.last) {
				iterator.nextNode(n.buf, n.buf.length);
			} else {
				iterator.nextNode(n.buf, this.lastLen);
			}
			n = n.next;
		}
	}

	public String toString(Charset charset) {
		return new String(toByteArray(), 0, (int) this.count, charset);
	}

	public String toString(String charset) throws UnsupportedEncodingException {
		return new String(toByteArray(), 0, (int) this.count, charset);
	}

	@Override
	public String toString() {
		return StringFormat.fmt("{}: size={}, blockSize={}, nodeCount={}", ObjectUtils.toHashInfo(this), this.count, this.blockSize, this.getNodeCount());
	}

	public int getBlockSize() {
		return this.blockSize;
	}

	public long size() {
		return this.count;
	}

	public boolean isEmpty() {
		return this.count == 0;
	}

	public void reset() {
		this.nodeList.first.next = null;
		this.nodeList.last = this.nodeList.first;

		this.count = 0;
		this.lastLen = 0;
	}

	// Implements Serializable

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(this.blockSize);
		out.writeLong(this.count);
		out.writeInt(this.lastLen);
		out.writeInt(getNodeCount());

		Node n = this.nodeList.first;
		while (n != null) {

			out.writeInt(n.buf.length);
			out.write(n.buf, 0, n.buf.length);

			n = n.next;
		}
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.blockSize = in.readInt();
		this.count = in.readLong();
		this.lastLen = in.readInt();

		int nodeCount = in.readInt();
		while (nodeCount > 0) {

			byte[] buf = new byte[in.readInt()];
			in.readFully(buf, 0, buf.length);

			if (this.nodeList == null) {
				this.nodeList = new NodeList(buf);
			} else {
				this.nodeList.insert(buf);
			}
			nodeCount--;
		}
	}

	public interface NodeIterator {
		void nextNode(byte[] buf, int len) throws IOException;
	}

	private static class NodeList {
		final Node first;
		Node last;

		public NodeList(byte[] buf) {
			this.last = this.first = new Node(buf);
		}

		public void insert(byte[] buf) {
			Node n = new Node(buf);
			this.last.next = n;
			this.last = n;
		}
	}

	private static class Node {
		final byte[] buf;
		Node next;

		public Node(byte[] buf) {
			this.buf = buf;
		}
	}
}
