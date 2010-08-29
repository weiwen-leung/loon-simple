package org.loon.framework.game.simple.action.sprite.effect;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.GameManager;
import org.loon.framework.game.simple.action.map.shapes.RectBox;
import org.loon.framework.game.simple.action.sprite.ISprite;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.utils.GraphicsUtils;

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
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class CardFadeEffect implements ISprite {

	/**
	 * 卡片过滤特效
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage nowImage, nextImage;

	private int x, y, width, height, maxCount, sleep, block, layer;

	private double div;

	private boolean visible = true;

	public CardFadeEffect(String fileName) {
		this(fileName, 0, 0);
	}

	public CardFadeEffect(String fileName, int x, int y) {
		this(fileName, x, y, GameManager.getSystemHandler().getWidth(),
				GameManager.getSystemHandler().getHeight());
	}

	public CardFadeEffect(String fileName, int x, int y, int w, int h) {
		this(null, GraphicsUtils.loadImage(fileName), 40, 18, 40, x, y, w, h);
	}

	public CardFadeEffect(Image nowImage, Image nextImage, int maxCount,
			int div, int block, int x, int y, int width, int height) {
		if (nowImage != null) {
			if (nowImage.getWidth(null) == width
					&& nowImage.getHeight(null) == height) {
				this.nowImage = GraphicsUtils.getBufferImage(nowImage);
			} else {
				this.nowImage = GraphicsUtils.getBufferImage(GraphicsUtils
						.getResize(nowImage, width, height));
			}
		}
		if (nextImage != null) {
			if (nextImage.getWidth(null) == width
					&& nextImage.getHeight(null) == height) {
				this.nextImage = GraphicsUtils.getBufferImage(nextImage);
			} else {
				this.nextImage = GraphicsUtils.getBufferImage(GraphicsUtils
						.getResize(nextImage, width, height));
			}
		}
		this.maxCount = maxCount;
		this.div = div;
		this.block = block;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void reset() {
		sleep = 0;
	}

	public boolean isComplete() {
		return sleep >= maxCount;
	}

	public void createUI(LGraphics g) {
		if (visible) {
			if (nextImage == null) {
				return;
			}
			if (isComplete()) {
				g.drawImage(nextImage, x, y);
				return;
			}
			if (nowImage != null) {
				g.drawImage(nowImage, x, y);
			}
			int max = maxCount;
			Graphics2D g2 = (Graphics2D) g;
			Composite pastComposite = g2.getComposite();
			double div = this.div * 2D, lange, value;
			double langeSup = ((double) max - div / 2D)
					/ Math.pow(height, 1 / 3D);
			try {
				for (int i = 0; i < width; i += block) {
					for (int j = 0; j < height; j += block) {
						lange = Math.pow((i / block) * (i / block)
								+ ((height - j) / block)
								* ((height - j) / block), 1 / 3D)
								* langeSup;
						value = ((double) sleep - lange) / div;
						if (value > 0.5D) {
							value = 0.5D;
						}
						if (value < 0.0D) {
							value = 0.0D;
						}
						AffineTransform affineTransform = AffineTransform
								.getTranslateInstance(i, j);
						affineTransform.translate(x, y);
						affineTransform.concatenate(new AffineTransform(
								value + 0.5D, ((0.5D - value) * block) / block,
								((0.5D - value) * block) / block, value + 0.5D,
								0.0D, 0.0D));
						g2.setComposite(AlphaComposite.getInstance(3,
								(float) value * 2.0F));
						g2.drawImage(nextImage.getSubimage(i, j, block, block),
								affineTransform, null);
						g2.setComposite(pastComposite);
					}
				}
			} catch (Exception e) {
			}
			g2.setComposite(pastComposite);
			sleep++;
		}
	}

	public void update(long elapsedTime) {
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public RectBox getCollisionBox() {
		return new RectBox(x, y, width, height);
	}

	public float getAlpha() {
		return 0;
	}
}
