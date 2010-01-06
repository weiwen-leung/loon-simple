package org.loon.framework.game.simple.media.flv;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * Copyright 2008 - 2009
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
public class FLVDataOutputStream extends DataOutputStream {

	public FLVDataOutputStream(OutputStream out) {
		super(new BufferedOutputStream(out));
	}

	/**
	 * 输入byte数据
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void write(byte data) throws IOException {
		writeByte(data);
	}

	/**
	 * 输入整型数据数据
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void writeInteger(int data) throws IOException {
		writeByte((byte) (data >> 16 & 0xff));
		writeByte((byte) (data >> 8 & 0xff));
		writeByte((byte) (data >> 0 & 0xff));
	}
}