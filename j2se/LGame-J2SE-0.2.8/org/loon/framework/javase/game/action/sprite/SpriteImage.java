package org.loon.framework.javase.game.action.sprite;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.graphics.LColor;
import org.loon.framework.javase.game.core.graphics.SerializablelImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.utils.CollectionUtils;
import org.loon.framework.javase.game.utils.GraphicsUtils;

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

	private RectBox rect;

	private Mask mask;

	private CollisionMask collisionMask;

	protected int transform;

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
		this(GraphicsUtils.loadNotCacheImage(fileName), x, y);
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
		transform = Sprite.TRANS_NONE;
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
		serializablelImage = new SerializablelImage(awtImage);
	}

	/**
	 * 变更掩码中数据为指定角度
	 * 
	 * @param transform
	 */
	public Mask updateMask(int t) {
		this.transform = t;
		if (transform == Sprite.TRANS_NONE) {
			return createMask(pixels, width, height);
		}
		int th;
		int tw;
		if ((transform & 4) != 0) {
			th = width;
			tw = height;
		} else {
			th = height;
			tw = width;
		}

		int[] trans = new int[pixels.length];
		if (transform != 0) {
			int sp = 0;
			for (int sy = 0; sy < height; sy++) {
				int tx;
				int ty;
				int td;
				switch (transform) {
				case LGraphics.TRANS_ROT90:
					tx = tw - sy - 1;
					ty = 0;
					td = tw;
					break;
				case LGraphics.TRANS_ROT180:
					tx = tw - 1;
					ty = th - sy - 1;
					td = -1;
					break;
				case LGraphics.TRANS_ROT270:
					tx = sy;
					ty = th - 1;
					td = -tw;
					break;
				case LGraphics.TRANS_MIRROR:
					tx = tw - 1;
					ty = sy;
					td = -1;
					break;
				case LGraphics.TRANS_MIRROR_ROT90:
					tx = tw - sy - 1;
					ty = th - 1;
					td = -tw;
					break;
				case LGraphics.TRANS_MIRROR_ROT180:
					tx = 0;
					ty = th - sy - 1;
					td = 1;
					break;
				case LGraphics.TRANS_MIRROR_ROT270:
					tx = sy;
					ty = 0;
					td = tw;
					break;
				default:
					throw new RuntimeException("Illegal transformation: "
							+ transform);
				}

				int tp = ty * tw + tx;
				for (int sx = 0; sx < width; sx++) {
					trans[tp] = pixels[sp++];
					tp += td;
				}
			}
		}

		return createMask(trans, tw, th);
	}

	/**
	 * 生成像素掩码
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @return
	 */
	private Mask createMask(int[] pixels, int w, int h) {
		int width = w;
		int height = h;
		Mask data = new Mask(width, height);
		boolean[][] mask = new boolean[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				mask[y][x] = (pixels[x + w * y] & 0xff000000) == 0xff000000;
			}
		}
		data.setData(mask);
		return data;
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

	public BufferedImage getImage() {
		return serializablelImage.getImage();
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
		return CollectionUtils.copyOf(pixels);
	}

	public SpriteImage copy() {
		return new SpriteImage(this);
	}

	public CollisionMask getMask(int trans, int x, int y) {
		if (mask == null || transform != trans) {
			mask = updateMask(trans);
		}
		if (collisionMask == null) {
			collisionMask = new CollisionMask(mask);
		} else {
			collisionMask.set(mask, x, y, mask.getWidth(), mask.getHeight());
		}
		return collisionMask;
	}

	public RectBox getCollisionBox() {
		if (rect == null) {
			rect = new RectBox(x(), y(), width, height);
		} else {
			rect.setBounds(x(), y(), width, height);
		}
		return rect;
	}

	public boolean isOpaque() {
		return isOpaque;
	}

}
