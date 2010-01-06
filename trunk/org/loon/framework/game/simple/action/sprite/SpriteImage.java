package org.loon.framework.game.simple.action.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;

import org.loon.framework.game.simple.action.map.RectBox;
import org.loon.framework.game.simple.core.LObject;
import org.loon.framework.game.simple.core.graphics.SerializablelImage;
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

	public transient VolatileImage volatileImage;

	public transient Graphics2D graphics;

	private int[] pixels;

	private Mask mask;

	protected int width, height;

	// 精灵透明度
	protected int alpha = 255;

	// 是否使用VolatileImage缓冲图像
	protected boolean useVolatileImage;

	public SpriteImage(String fileName) {
		this(GraphicsUtils.loadImage(fileName));
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
		this.useVolatileImage = createVolatileImage();
	}

	public SpriteImage(SpriteImage image) {
		this(image, 0, 0);
	}

	public SpriteImage(String fileName, int x, int y) {
		this(new SpriteImage(fileName), x, y);
	}

	public SpriteImage(SpriteImage image, int x, int y) {
		this.setLocation(x, y);
		this.width = image.width;
		this.height = image.height;
		this.useVolatileImage = image.getVolatileImageUsage();
		this.bind(image.serializablelImage.getImage());
		createVolatileImage();
	}

	public SpriteImage(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.height = height;
		this.bind(null, Color.RED);
		this.useVolatileImage = createVolatileImage();
	}

	private void bind(Image img) {
		bind(img, null);
	}

	private void bind(Image img, Color color) {
		BufferedImage bufferImage = createBufferedImage();
		graphics = bufferImage.createGraphics();
		if (color != null) {
			graphics.setColor(color);
			graphics.fillRect(0, 0, this.width, this.height);
		}
		if (img != null) {
			graphics.drawImage(img, 0, 0, null);
		}
		makeMask(bufferImage);
		serializablelImage = new SerializablelImage(bufferImage);
	}

	public void makeMask(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		pixels = new int[width * height];
		raster.getDataElements(0, 0, width, height, pixels);
		boolean[][] alphas = new boolean[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((pixels[x + width * y] & 0xff000000) != 0xff000000) {
					alphas[x][y] = false;
				} else {
					alphas[x][y] = true;
				}
			}
		}
		mask = new Mask(alphas, width, height);
	}

	public void createUI(Graphics2D g) {
		if (visible) {
			g.drawImage(serializablelImage.getImage(), x(), y(), null);
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

	private BufferedImage createBufferedImage() {
		return GraphicsUtils.createImage(width, height, true);
	}

	private boolean createVolatileImage() {
		try {
			(volatileImage = GraphicsUtils.createVolatileImage(width, height))
					.getGraphics().drawImage(serializablelImage.getImage(), 0,
							0, null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void setVolatileImageUsage(boolean value) {
		useVolatileImage = value;
	}

	public void setBufferedImageUsage(boolean value) {
		useVolatileImage = !value;
	}

	public boolean getVolatileImageUsage() {
		if (useVolatileImage) {
			return true;
		}
		return false;
	}

	public boolean getBufferedImageUsage() {
		if (!useVolatileImage) {
			return true;
		}
		return false;
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

	public void matchWidthAndHeight() {
		width = this.serializablelImage.getImage().getWidth();
		height = this.serializablelImage.getImage().getHeight();
	}

	/**
	 * 设定当前透明度(0-255)
	 * 
	 * @param alpha
	 */
	public void setAlphaValue(int alpha) {
		useVolatileImage = false;
		GraphicsUtils.setAlphaImage(serializablelImage.getImage(), alpha);
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

	/**
	 * 透明化指定颜色
	 * 
	 * @param color
	 * @return
	 */
	public SpriteImage makeTransparency(Color color) {
		useVolatileImage = false;
		GraphicsUtils.makeTransparency(serializablelImage.getImage(), color
				.getRGB());
		return this;
	}

	/**
	 * 透明化指定颜色
	 * 
	 * @param color
	 * @return
	 */
	public SpriteImage makeTransparency(int color) {
		useVolatileImage = false;
		GraphicsUtils.makeTransparency(serializablelImage.getImage(), color);
		return this;
	}

	/**
	 * 透明化指定区域
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public SpriteImage makeTransparency(int x, int y) {
		makeTransparency(serializablelImage.getImage().getRGB(x, y));
		return this;
	}

	/**
	 * 以一维数组分解像素
	 * 
	 * @return
	 */
	public int[] getArrayColor1D() {
		return GraphicsUtils.getArrayColor1D(serializablelImage.getImage());
	}

	/**
	 * 以二维数组分解像素
	 * 
	 * @return
	 */
	public int[][] getArrayColor2D() {
		return GraphicsUtils.getArrayColor2D(serializablelImage.getImage());
	}

	public void setArrayColor(int array[]) {
		serializablelImage.getImage().setRGB(0, 0, width, height, array, 0,
				width);
		volatileImage.getGraphics().drawImage(serializablelImage.getImage(), 0,
				0, null);
	}

	public void setArrayColor(int array[][]) {
		GraphicsUtils.setArrayColor(serializablelImage.getImage(), array);
		volatileImage.getGraphics().drawImage(serializablelImage.getImage(), 0,
				0, null);
	}

	public int[] getArrayAlpha1D() {
		return GraphicsUtils.getArrayAlpha1D(serializablelImage.getImage());
	}

	public int[][] getArrayAlpha2D() {
		return GraphicsUtils.getArrayAlpha2D(serializablelImage.getImage());
	}

	public void setArrayAlpha(int array[]) {
		GraphicsUtils.setArrayAlpha(serializablelImage.getImage(), array);
		useVolatileImage = false;
	}

	public void setArrayAlpha(int array[][]) {
		GraphicsUtils.setArrayAlpha(serializablelImage.getImage(), array);
		useVolatileImage = false;
	}

	public SpriteImage copy() {
		return new SpriteImage(this);
	}

	public Mask getMask() {
		return mask;
	}

	public int[] getPixels() {
		return pixels;
	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), width, height);
	}

}
