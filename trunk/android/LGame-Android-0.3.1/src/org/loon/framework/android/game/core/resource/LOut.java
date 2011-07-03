package org.loon.framework.android.game.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
public class LOut extends OutputStream {
	
	public static final int DEFAULT_BUFF_SIZE = 4096;

	private byte[] prv_buff;

	private String prv_filename;

	private int prv_exSize;

	private int prv_index;

	public LOut(String fileName) {
		this.prv_buff = new byte[4096];
		this.prv_index = 0;
		this.prv_exSize = 4096;
		this.prv_filename = fileName;
	}

	public LOut(String paramString, int size) {
		this.prv_buff = new byte[size];
		this.prv_index = 0;
		this.prv_exSize = size;
		this.prv_filename = paramString;
	}

	public int getSize() {
		return this.prv_index;
	}

	public void extendMemory(int size) {
		byte[] arrayOfByte = new byte[this.prv_buff.length + size];
		System
				.arraycopy(this.prv_buff, 0, arrayOfByte, 0,
						this.prv_buff.length);
		this.prv_buff = arrayOfByte;
	}

	public void write(byte[] bytes) throws IOException {
		if (this.prv_buff == null) {
			throw new IOException();
		}
		if (this.prv_buff.length - this.prv_index < bytes.length) {
			extendMemory(this.prv_exSize);
		}
		System.arraycopy(bytes, 0, this.prv_buff, this.prv_index, bytes.length);
		this.prv_index += bytes.length;
	}

	public void write(byte[] bytes, int off, int len) throws IOException {
		if (this.prv_buff == null) {
			throw new IOException();
		}
		if (this.prv_buff.length - this.prv_index < len) {
			extendMemory(this.prv_exSize);
		}
		System.arraycopy(bytes, off, this.prv_buff, this.prv_index, len);
		this.prv_index += len;
	}

	public void write(int b) throws IOException {
		if (this.prv_buff == null) {
			throw new IOException();
		}
		if (this.prv_buff.length - this.prv_index == 0) {
			extendMemory(this.prv_exSize);
		}
		this.prv_buff[(this.prv_index++)] = (byte) (b & 0xFF);
	}

	public void flush() throws IOException {
		if (this.prv_buff == null) {
			throw new IOException();
		}
	}

	public void close() throws IOException {
		try {
			FileOutputStream out = new FileOutputStream(new File(
					this.prv_filename));
			out.write(this.prv_buff, 0, this.prv_index);
			out.flush();
			out.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			throw e;
		}
	}

}
