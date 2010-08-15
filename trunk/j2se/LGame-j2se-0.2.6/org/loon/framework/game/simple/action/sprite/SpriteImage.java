package org.loon.framework.game.simple.action.sprite;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import org.loon.framework.game.simple.action.map.shapes.RectBox;
import org.loon.framework.game.simple.core.LObject;
import org.loon.framework.game.simple.core.graphics.LColor;
import org.loon.framework.game.simple.core.graphics.SerializablelImage;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.utils.GraphicsUtils;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * 
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * 
 * under the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class SpriteImage extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982900453464314946L;

	public boolean visible = true;

	public SerializablelImage serializablelImage;

	private boolean isOpaque = true;

	private int[] pixels;

	private Mask mask;

	protected int width, height;

	// 精灵透明度
	protected int alpha = 255;

	public SpriteImage(String fileName) {
		this(fileName, 0, 0);
	}

	public SpriteImage(Image img) {
		this(img, 0, 0);
	}

	public SpriteImage(Image img, int x, int y) {
		this(img, x, y, img.getWidth(null), img.getHeight(null));
	}

	public SpriteImage(Image img, int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.height = height;
		this.bind(img);
	}

	public SpriteImage(SpriteImage image) {
		this(image, 0, 0);
	}

	public SpriteImage(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName), x, y);
	}

	public SpriteImage(SpriteImage image, int x, int y) {
		this.setLocation(x, y);
		this.width = image.width;
		this.height = image.height;
		this.bind(image.serializablelImage.getImage());
	}

	public SpriteImage(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.height = height;
		this.bind(null, Color.RED);
	}

	private void bind(Image img) {
		bind(img, null);
	}

	private void bind(Image img, Color color) {
		int size = width * height;
		pixels = new int[width * height];
		PixelGrabber pixelGrabber = new PixelGrabber(img, 0, 0, width, height,
				pixels, 0, width);
		boolean result = false;
		try {
			result = pixelGrabber.grabPixels();
		} catch (InterruptedException ex) {
		}
		if (result) {
			int pixel;
			for (int i = 0; i < size; i++) {
				pixels[i] = LColor.premultiply(pixel = pixels[i]);
				if (isOpaque && (pixel >>> 24) < 255) {
					isOpaque = false;
				}
			}
		}
		BufferedImage awtImage = null;
		if (isOpaque) {
			awtImage = GraphicsUtils
					.newAwtRGBImage(pixels, width, height, size);
		} else {
			awtImage = GraphicsUtils.newAwtARGBImage(pixels, width, height,
					size);
		}
		makeMask();
		serializablelImage = new SerializablelImage(awtImage);
	}

	/**
	 * 生成像素掩码
	 * 
	 */
	private void makeMask() {
		boolean[][] alphas = new boolean[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				alphas[x][y] = isTransparent(x, y);
			}
		}
		mask = new Mask(alphas, width, height);
	}

	/**
	 * 判定指定像素点是否透明
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isTransparent(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return true;
		} else if (isOpaque) {
			return false;
		} else {
			return (pixels[x + width * y] & 0xff000000) == 0xff000000;
		}
	}

	/**
	 * 绘制图像到当前画布
	 */
	public void createUI(LGraphics g) {
		if (visible) {
			g.drawImage(serializablelImage.getImage(), x(), y());
		}
	}

	public void update(long timer) {

	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void move(int x, int y) {
		this.move(x, y);
	}

	public void setLocation(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 设定当前透明度(0-255)
	 * 
	 * @param alpha
	 */
	public void setAlphaValue(int alpha) {
		if (alpha < 0 || alpha > 255) {
			return;
		}
		if (isOpaque) {
			for (int i = 0; i < pixels.length; i++) {
				if (pixels[i] != 0xffffff) {
					pixels[i] = LColor.premultiply(pixels[i], alpha);
				}
			}
		} else {
			for (int i = 0; i < pixels.length; i++) {
				if (pixels[i] != 0xffffff) {
					int[] rgb = LColor.getRGBs(pixels[i]);
					pixels[i] = LColor.getARGB(rgb[0], rgb[1], rgb[2], alpha);
				}
			}
		}
		this.alpha = alpha;
	}

	/**
	 * 设定当前透明度(0.0F-1.0F)
	 * 
	 * @param alpha
	 */
	public void setAlpha(float alpha) {
		setAlphaValue((int) (255 * alpha));
	}

	/**
	 * 返回当前透明度(0-255)
	 * 
	 * @return
	 */
	public int getAlphaValue() {
		return alpha;
	}

	/**
	 * 返回当前透明度(0.0F-1.0F)
	 * 
	 * @return
	 */
	public float getAlpha() {
		return (alpha * 1.0f) / 255;
	}

	public int[] getData() {
		return pixels;
	}

	public SpriteImage copy() {
		return new SpriteImage(this);
	}

	public Mask getMask() {
		return mask;
	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), width, height);
	}

	public boolean isOpaque() {
		return isOpaque;
	}

}
