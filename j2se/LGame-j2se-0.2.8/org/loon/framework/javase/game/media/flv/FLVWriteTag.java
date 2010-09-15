package org.loon.framework.javase.game.media.flv;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class FLVWriteTag {

	final static byte AUDIO = 8;

	final static byte VIDEO = 9;

	final static byte SCRIPT = 18;

	private byte tagType;

	private int dataSize;

	private long timer = 0;

	private final int streamID = 0;

	private DataWritter data;

	public FLVWriteTag(BufferedImage image, long timer) {
		this.tagType = VIDEO;
		this.timer = timer;
		this.data = new VideoData(image);
	}

	// FLV影片格式用类
	class VideoData implements DataWritter {

		final static byte KEYFRAME = 1 * ScreenVideoData.block;

		final static byte INTERFRAME = 2 * ScreenVideoData.block;

		final static byte DISPOSABLEINTERFRAME = 3 * ScreenVideoData.block;

		final static byte GENERATEDKEYFRAME = 4 * ScreenVideoData.block;

		final static byte INFOCOMMAND = 5 * ScreenVideoData.block;

		final static byte JPEG_CODEC = 1;

		final static byte H263_CODEC = 2;

		final static byte SCREENVIDEO_CODEC = 3;

		final static byte On2VP6_CODEC = 4;

		final static byte On2VP6_Alpha_CODEC = 5;

		final static byte SCREENVIDEO2_CODEC = 6;

		final static byte AVC = 7;

		private byte FrameAndCodec;

		private DataWritter videoData;

		public VideoData(BufferedImage image) {
			FrameAndCodec = KEYFRAME | SCREENVIDEO_CODEC;
			videoData = new ScreenVideoData(image, 5);
		}

		public void write(FLVDataOutputStream out) throws IOException {
			out.write(FrameAndCodec);
			videoData.write(out);
			out.flush();
		}
	}

	/**
	 * 输出FLV内容
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void write(FLVDataOutputStream out) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 1024);
		data.write(new FLVDataOutputStream(bos));
		dataSize = bos.size();
		out.writeByte(tagType);
		out.writeInteger(dataSize);
		out.writeInteger((int) timer & 0x00ffffff);
		out.write((byte) (timer >> 24 & 0xff));
		out.writeInteger(streamID);
		bos.writeTo(out);
		out.writeInt(dataSize + 11);
	}
}