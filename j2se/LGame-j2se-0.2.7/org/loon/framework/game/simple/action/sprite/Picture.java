package org.loon.framework.game.simple.action.sprite;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.action.map.shapes.RectBox;
import org.loon.framework.game.simple.core.LObject;
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Picture extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982153514439690901L;

	private boolean visible;

	private int width, height;

	private BufferedImage image;

	public Picture(String fileName) {
		this(fileName, 0, 0);
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
		this.setImage(image);
		this.setLocation(x, y);
		this.visible = true;
	}

	public void createUI(LGraphics g) {
		if (visible) {
			g.drawImage(image, x(), y());
		}
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

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(String fileName) {
		this.image = GraphicsUtils.loadBufferedImage(fileName);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), width, height);
	}

	public float getAlpha() {
		return 0;
	}

}
