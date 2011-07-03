package org.loon.framework.android.game.core.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class LIn extends InputStream {
	
	private byte[] prv_data;

	private int prv_size;

	private int prv_pt;

	private int prv_mark;

	public LIn(InputStream in) throws IOException {
		byte[] bytes = new byte[65536];
		byte[] save = new byte[2621440];
		this.prv_pt = 0;
		this.prv_mark = 0;
		try {
			int i = 0;
			int j;
			while ((j = in.read(bytes)) >= 0) {
				if (j > 0) {
					System.arraycopy(bytes, 0, save, i, j);
				}
				i += j;
			}
			this.prv_data = new byte[i];
			System.arraycopy(save, 0, this.prv_data, 0, i);
			this.prv_size = i;
		} catch (IOException ex) {
			throw ex;
		}
	}

	public int available() throws IOException {
		return this.prv_size;
	}

	public void close() throws IOException {
		this.prv_pt = 0;
	}

	public void mark(int mark) {
		this.prv_mark = this.prv_pt;
	}

	public boolean markSupported() {
		return true;
	}

	public int read() throws IOException {
		if (this.prv_pt >= this.prv_size) {
			return -1;
		}
		return this.prv_data[(this.prv_pt++)] & 0xFF;
	}

	public int read(byte[] bytes) throws IOException {
		if (this.prv_pt >= this.prv_size) {
			return -1;
		}
		int i;
		if (this.prv_size - this.prv_pt < bytes.length) {
			i = this.prv_size - this.prv_pt;
		} else {
			i = bytes.length;
		}
		System.arraycopy(this.prv_data, this.prv_pt, bytes, 0, i);
		return i;
	}

	public int read(byte[] bytes, int off, int len) throws IOException {
		if (len == 0) {
			return 0;
		}
		if (this.prv_pt >= this.prv_size) {
			return -1;
		}
		int i = this.prv_size - this.prv_pt;
		if (len < i) {
			i = len;
		}
		System.arraycopy(this.prv_data, this.prv_pt, bytes, off, i);
		this.prv_pt += i;
		return i;
	}

	public void reset() throws IOException {
		this.prv_pt = this.prv_mark;
	}

	public void first() {
		this.prv_pt = 0;
	}

	public long skip(long n) throws IOException {
		if (this.prv_pt >= this.prv_size) {
			return 0L;
		}
		if (n < 0L) {
			return 0;
		}
		if (n + this.prv_pt > this.prv_size) {
			this.prv_pt = this.prv_size;
			return this.prv_size - this.prv_pt;
		}
		this.prv_pt += (int) n;
		return n;
	}
}
