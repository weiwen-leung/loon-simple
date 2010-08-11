package org.loon.framework.game.simple.core.graphics;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.MemoryImageSource;

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
public class ImageLazy extends MemoryImageSource {

	public ImageConsumer consumer;

	public ImageLazy(int x, int y, ColorModel colormodel, int[] pixels, int w,
			int h) {
		super(x, y, colormodel, pixels, w, h);
	}

	public synchronized void addConsumer(ImageConsumer imageconsumer) {
		this.consumer = imageconsumer;
		super.addConsumer(imageconsumer);
	}

}
