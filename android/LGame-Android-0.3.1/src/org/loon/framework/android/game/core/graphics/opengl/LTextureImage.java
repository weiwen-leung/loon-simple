package org.loon.framework.android.game.core.graphics.opengl;

import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
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
public class LTextureImage implements LRelease {

	private Format format = Format.DEFAULT;

	private int width, height;

	LTexture cache;

	LImage image;

	LGraphics graphics;

	public LTextureImage(int width, int height, boolean hasAlpha) {
		this(width, height, hasAlpha, Format.DEFAULT);
	}

	public LTextureImage(int width, int height, boolean hasAlpha, Format format) {
		this.image = new LImage(width, height, hasAlpha);
		this.format = format;
	}

	public int[] getPixels() {
		if (image == null) {
			return null;
		}
		return image.getPixels();
	}

	public LGraphics getGraphics() {
		if (image == null) {
			return null;
		}
		if (graphics == null) {
			graphics = image.getLGraphics();
		} else {
			graphics.setColor(0, 0, 0, 0);
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(LColor.white);
		}
		return graphics;
	}

	public LTexture getTexture() {
		if (cache == null) {
			newTexture();
		}
		return cache;
	}

	public LTexture newTexture() {
		if (image == null) {
			return null;
		}
		disposeTexture();
		image.setFormat(format);
		cache = image.getTexture();
		return cache;
	}

	public void update() {
		if (image == null) {
			return;
		}
		image.setFormat(format);
		if (cache != null) {
			LTextureData data = GLLoader.getTextureData(image);
			LTextureData tmp = cache.imageData;
			cache.imageData = data;
			cache.format = format;
			cache.width = data.width;
			cache.height = data.height;
			cache.texWidth = data.texWidth;
			cache.texHeight = data.texHeight;
			cache.hasAlpha = data.hasAlpha;
			cache.reload();
			if (tmp != null) {
				tmp.source = null;
				tmp.pixmap = null;
				tmp.fileName = null;
				tmp = null;
			}
		} else {
			cache = image.getTexture();
		}
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void disposeTexture() {
		if (cache != null) {
			cache.destroy();
			cache = null;
		}
	}

	public void dispose() {
		if (graphics != null) {
			graphics.dispose();
			graphics = null;
		}
		if (image != null) {
			image.dispose();
			image = null;
		}
		disposeTexture();
	}

}
