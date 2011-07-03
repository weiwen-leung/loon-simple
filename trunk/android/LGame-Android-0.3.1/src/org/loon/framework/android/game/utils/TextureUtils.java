package org.loon.framework.android.game.utils;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.graphics.opengl.LTextures;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;

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
public class TextureUtils {

	public static LTexture filterColor(String res, GLColor col) {
		return TextureUtils.filterColor(res, col, Format.DEFAULT);
	}

	public static LTexture filterColor(String res, GLColor col, Format format) {
		int color = col.getRGB();
		LImage tmp = new LImage(res);
		LImage image = new LImage(tmp.getWidth(), tmp.getHeight(), true);
		LGraphics g = image.getLGraphics();
		g.drawImage(tmp, 0, 0);
		g.dispose();
		if (tmp != null) {
			tmp.dispose();
			tmp = null;
		}
		int[] pixels = image.getPixels();
		int size = pixels.length;
		for (int i = 0; i < size; i++) {
			if (pixels[i] == color) {
				pixels[i] = 0xffffff;
			}
		}
		image.setFormat(format);
		image.setPixels(pixels, image.getWidth(), image.getHeight());
		LTexture texture = image.getTexture();
		if (image != null) {
			image.dispose();
			image = null;
		}
		return texture;
	}

	public static LTexture filterLimitColor(String res, GLColor start,
			GLColor end) {
		return TextureUtils.filterLimitColor(res, start, end, Format.DEFAULT);
	}

	public static LTexture filterLimitColor(String res, GLColor start,
			GLColor end, Format format) {
		int sred = start.getRed();
		int sgreen = start.getGreen();
		int sblue = start.getBlue();
		int ered = end.getRed();
		int egreen = end.getGreen();
		int eblue = end.getBlue();
		LImage tmp = new LImage(res);
		LImage image = new LImage(tmp.getWidth(), tmp.getHeight(), true);
		LGraphics g = image.getLGraphics();
		g.drawImage(tmp, 0, 0);
		g.dispose();
		if (tmp != null) {
			tmp.dispose();
			tmp = null;
		}
		int[] pixels = image.getPixels();
		int size = pixels.length;
		for (int i = 0; i < size; i++) {
			int[] rgbs = ColorUtils.getRGBs(pixels[i]);
			if ((rgbs[0] >= sred && rgbs[1] >= sgreen && rgbs[2] >= sblue)
					&& (rgbs[0] <= ered && rgbs[1] <= egreen && rgbs[2] <= eblue)) {
				pixels[i] = 0xffffff;
			}
		}
		image.setFormat(format);
		image.setPixels(pixels, image.getWidth(), image.getHeight());
		LTexture texture = image.getTexture();
		if (image != null) {
			image.dispose();
			image = null;
		}
		return texture;
	}

	public static LTexture loadTexture(String fileName) {
		return LTextures.loadTexture(fileName).get();
	}

	public static LTexture[] getSplitTextures(String fileName, int row, int col) {
		return getSplitTextures(LTextures.loadTexture(fileName).get(), row, col);
	}

	public static LTexture[] getSplitTextures(LTexture image, int row, int col) {
		if (image == null) {
			return null;
		}
		if (image != null) {
			image.loadTexture();
		}
		int frame = 0;
		int wlength = image.getWidth() / row;
		int hlength = image.getHeight() / col;
		int total = wlength * hlength;
		LTexture[] images = new LTexture[total];
		for (int y = 0; y < hlength; y++) {
			for (int x = 0; x < wlength; x++) {
				images[frame] = image.getSubTexture((x * row), (y * col), row,
						col);
				frame++;
			}
		}
		return images;
	}

	public static LTexture[][] getSplit2Textures(String fileName, int row,
			int col) {
		return getSplit2Textures(LTextures.loadTexture(fileName).get(), row,
				col);
	}

	public static LTexture[][] getSplit2Textures(LTexture image, int row,
			int col) {
		if (image == null) {
			return null;
		}
		if (image != null) {
			image.loadTexture();
		}
		int wlength = image.getWidth() / row;
		int hlength = image.getHeight() / col;
		LTexture[][] textures = new LTexture[wlength][hlength];
		for (int y = 0; y < hlength; y++) {
			for (int x = 0; x < wlength; x++) {
				textures[x][y] = image.getSubTexture((x * row), (y * col), row,
						col);
			}
		}
		return textures;
	}

}
