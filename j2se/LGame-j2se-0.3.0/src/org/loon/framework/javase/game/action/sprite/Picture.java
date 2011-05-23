package org.loon.framework.javase.game.action.sprite;

import java.awt.Image;
import java.awt.image.BufferedImage;
import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.utils.GraphicsUtils;

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
public class Picture extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982153514439690901L;

	private boolean visible;

	private float alpha;

	private int width, height;

	private BufferedImage image;

	private RectBox rect;

	public Picture(String fileName) {
		this(fileName, 0, 0);
	}

	public Picture(int x, int y) {
		this((Image) null, x, y);
	}

	public Picture(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName), x, y);
	}

	public Picture(Image image) {
		this(image, 0, 0);
	}

	public Picture(Image image, int x, int y) {
		this(GraphicsUtils.getBufferImage(image), x, y);
	}

	public Picture(BufferedImage image) {
		this(image, 0, 0);
	}

	public Picture(BufferedImage image, int x, int y) {
		if (image != null) {
			this.setImage(image);
			this.width = image.getWidth();
			this.height = image.getHeight();
		}
		this.setLocation(x, y);
		this.visible = true;
	}

	public void createUI(LGraphics g) {
		if (visible) {
			if (alpha >= 0.1 && alpha <= 1.0) {
				g.setAlpha(alpha);
			}
			g.drawImage(image, x(), y());
			if (alpha != 0) {
				g.setAlpha(1.0f);
			}
		}
	}

	public boolean equals(Picture p) {
		if (this.width == p.width && this.height == p.height) {
			if (GraphicsUtils.hashImage(image) == GraphicsUtils
					.hashImage(p.image)) {
				return true;
			}
		}
		return false;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void update(long timer) {
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void dispose() {
		if (image != null) {
			image.flush();
			image = null;
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(String fileName) {
		this.image = GraphicsUtils.loadBufferedImage(fileName);
	}

	public void setImage(Image image) {
		this.image = GraphicsUtils.getBufferImage(image);
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public RectBox getCollisionBox() {
		if (rect == null) {
			rect = new RectBox(x(), y(), width, height);
		} else {
			rect.setBounds(x(), y(), width, height);
		}
		return rect;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Image getBitmap() {
		return image;
	}

}
