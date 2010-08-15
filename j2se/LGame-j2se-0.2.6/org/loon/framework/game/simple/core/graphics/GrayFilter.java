package org.loon.framework.game.simple.core.graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

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

public class GrayFilter extends RGBImageFilter {

	private boolean brighter;

	private int percent;

	public static Image createDisabledImage(Image i) {
		GrayFilter filter = new GrayFilter(true, 50);
		ImageProducer prod = new FilteredImageSource(i.getSource(), filter);
		Image grayImage = Toolkit.getDefaultToolkit().createImage(prod);
		return grayImage;
	}

	public GrayFilter(boolean b, int p) {
		brighter = b;
		percent = p;
		canFilterIndexColorModel = true;
	}

	public int filterRGB(int x, int y, int rgb) {
		int gray = (int) ((0.299 * ((rgb >> 16) & 0xff) + 0.587
				* ((rgb >> 8) & 0xff) + 0.114 * (rgb & 0xff)) / 3);
		if (brighter) {
			gray = (255 - ((255 - gray) * (100 - percent) / 100));
		} else {
			gray = (gray * (100 - percent) / 100);
		}
		if (gray < 0) {
			gray = 0;
		}
		if (gray > 255) {
			gray = 255;
		}
		return (rgb & 0xff000000) | (gray << 16) | (gray << 8) | (gray << 0);
	}
}
