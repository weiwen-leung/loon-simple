package org.loon.framework.game.simple.action.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
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
final public class SpriteImageUtils {

	/**
	 * 变更指定精灵图像大小
	 * 
	 * @param img
	 * @param x
	 * @param y
	 * @return
	 */
	public static SpriteImage resize(SpriteImage spriteImage, double x, double y) {
		SpriteImage copy = spriteImage.copy();
		BufferedImage img;
		img = new BufferedImage((int) (spriteImage.getWidth() * x),
				(int) (spriteImage.getHeight() * y),
				BufferedImage.TYPE_INT_ARGB);
		(copy.graphics = (Graphics2D) img.getGraphics()).drawImage(
				spriteImage.serializablelImage.getImage(), 0, 0,
				(int) (spriteImage.getWidth() * x), (int) (spriteImage
						.getHeight() * y), null);
		copy.serializablelImage.setImage(img);
		copy.matchWidthAndHeight();
		return copy;
	}

	/**
	 * 水平翻转指定精灵图像
	 * 
	 * @param spriteImage
	 * @param v
	 * @param h
	 * @return
	 */
	public static SpriteImage flipHorizontalImage(SpriteImage spriteImage,
			boolean v, boolean h) {
		if (spriteImage == null) {
			return null;
		}
		SpriteImage copy = spriteImage.copy();
		if (v && h) {
			int val[] = spriteImage.getArrayColor1D();
			int val2[] = new int[val.length];
			for (int i = val.length - 1; i > 0; i--)
				val2[val.length - 1 - i] = val[i];
			copy.setArrayColor(val2);
			return copy;
		} else if (v && !h) {
			int val[][] = spriteImage.getArrayColor2D();
			int val2[][] = new int[spriteImage.getWidth()][spriteImage
					.getHeight()];
			int dimx, dimy;
			dimx = spriteImage.getWidth();
			dimy = spriteImage.getHeight();
			for (int y = 0; y < dimy; y++) {
				for (int x = 0; x < dimx; x++) {
					val2[x][y] = val[dimx - x - 1][y];
				}
			}
			copy.setArrayColor(val2);
			return copy;
		} else if (!v && h) {
			int val[][] = spriteImage.getArrayColor2D();
			int val2[][] = new int[spriteImage.getWidth()][spriteImage
					.getHeight()];
			int dimx, dimy;
			dimx = spriteImage.getWidth();
			dimy = spriteImage.getHeight();
			for (int x = 0; x < dimx; x++) {
				for (int y = 0; y < dimy; y++) {
					val2[x][y] = val[x][dimy - 1 - y];
				}
			}
			copy.setArrayColor(val2);
			return copy;
		}
		return spriteImage;
	}

}
