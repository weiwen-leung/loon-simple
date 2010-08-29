package org.loon.framework.game.simple.media.flv;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
/**
 * 
 * Copyright 2008 - 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng  
 * @email：ceponline@yahoo.com.cn 
 * @version 0.1
 */
public class FLVWriteStream {

	final static byte VIDEO = 1;

	final static byte AUDIO = 4;

	final static byte FLV_1 = 1;

	private FLVDataOutputStream os;

	private FLVWriteTag tag;

	public FLVWriteStream(OutputStream os, byte type) throws IOException {
		this.os = new FLVDataOutputStream(os);
		writeHeader(type);
	}

	/**
	 * 输出BufferedImage
	 * 
	 * @param image
	 * @param timestamp
	 * @throws IOException
	 */
	public void writeImage(BufferedImage image, long timer) throws IOException {
		tag = new FLVWriteTag(image, timer);
		tag.write(os);
	}

	/**
	 * 写FLV头文件
	 * 
	 * @param type
	 * @throws IOException
	 */
	private void writeHeader(byte type) throws IOException {
		os.writeBytes("FLV");
		os.write(FLV_1);
		os.write(type);
		os.writeInt(9);
		os.writeInt(0);
		
	}
}