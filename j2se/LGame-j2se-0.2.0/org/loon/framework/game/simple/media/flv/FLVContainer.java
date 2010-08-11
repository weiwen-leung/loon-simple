package org.loon.framework.game.simple.media.flv;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.loon.framework.game.simple.utils.GraphicsUtils;
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
public class FLVContainer {

	private FLVWriteStream worker;

	private Dimension size;

	public FLVContainer(OutputStream out, int width, int height)
			throws IOException {
		this(out, new Dimension(width, height));
	}

	public FLVContainer(OutputStream out, Dimension caputreSize)
			throws IOException {
		if (caputreSize.width % ScreenVideoData.block != 0 || caputreSize.height % ScreenVideoData.block != 0) {
			throw new RuntimeException(
					"Specified size can not be converted to flv !");
		}
		worker = new FLVWriteStream(out, FLVWriteStream.VIDEO);
		size = caputreSize;
	}

	/**
	 * 产生一个BufferedImage
	 * 
	 * @return
	 */
	public BufferedImage nextFrame() {
		return GraphicsUtils.createImage(size.width, size.height);
	}

	/**
	 * 输出BufferedImage
	 * 
	 * @param image
	 * @param timestamp
	 * @throws IOException
	 */
	public void writeFrame(BufferedImage image, long timer) throws IOException {
		worker.writeImage(image, timer);
	}
}
