package org.loon.framework.game.simple.action.sprite.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.action.map.RectBox;
import org.loon.framework.game.simple.action.sprite.ISprite;
import org.loon.framework.game.simple.core.graphics.LColor;
import org.loon.framework.game.simple.core.timer.LTimer;
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class PShadowEffect implements ISprite {

	/**
	 * 图片（黑白）渐变特效
	 */
	private static final long serialVersionUID = 1L;

	private LTimer timer = new LTimer(10);

	private int x, y, width, height, indexD, indexW, block, layer;

	private BufferedImage oldimage, image;

	private boolean visible = true, flag = true;

	final static int[] deasilTrans = new int[256],
			widdershinTrans = new int[256];

	static {
		for (int i = 0; i < 256; i++) {
			deasilTrans[i] = LColor.getPixel(i, i, i);
		}
		int flag = 0;
		for (int i = 0; i < 256; i++) {
			widdershinTrans[flag++] = deasilTrans[i];
		}
	}

	public PShadowEffect(String fileName) {
		this(GraphicsUtils.loadImage(fileName));
	}

	public PShadowEffect(Image img) {
		this(GraphicsUtils.getBufferImage(img));
	}

	public PShadowEffect(BufferedImage img) {
		this(img, 0, 0);
	}

	public PShadowEffect(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName), x, y);
	}

	public PShadowEffect(Image img, int x, int y) {
		this(GraphicsUtils.getBufferImage(img), x, y, img.getWidth(null), img
				.getHeight(null));
	}

	public PShadowEffect(BufferedImage img, int x, int y) {
		this(img, x, y, img.getWidth(), img.getHeight());
	}

	public PShadowEffect(String fileName, int x, int y, int w, int h) {
		this(GraphicsUtils.getBufferImage(GraphicsUtils.loadImage(fileName)),
				x, y, w, h);
	}

	public PShadowEffect(BufferedImage img, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		if (img.getWidth() != w || img.getHeight() != h) {
			this.image = GraphicsUtils.getResize(img, w, h);
		} else {
			this.image = img;
		}
		this.oldimage = GraphicsUtils.getBufferImage(image);
		this.indexD = 255;
		this.indexW = 0;
		this.block = 4;
	}

	public void createUI(Graphics2D g) {
		if (visible && !isComplete()) {
			g.drawImage(image, x, y, null);
		}
	}

	public void reset() {
		this.indexD = 255;
		this.indexW = 0;
		this.visible = true;
		this.image = GraphicsUtils.getBufferImage(oldimage);
	}

	public void update(long elapsedTime) {
		if (visible && timer.action(elapsedTime) && !isComplete()) {
			if (flag) {
				for (int i = 0; i < block; i++) {
					GraphicsUtils.transparencyColor(image,
							widdershinTrans[indexW++]);
				}
			} else {
				for (int i = 0; i < block; i++) {
					GraphicsUtils.transparencyColor(image,
							deasilTrans[indexD--]);
				}
			}
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isComplete() {
		return flag ? (indexW >= 255) : (indexD <= 0);
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean isBlackToWhite() {
		return flag;
	}

	public void setBlackToWhite(boolean flag) {
		this.flag = flag;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public RectBox getCollisionBox() {
		return new RectBox(x,y,width,height);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public float getAlpha() {
		return 0;
	}
}
