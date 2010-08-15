package org.loon.framework.game.simple.action.sprite;

import java.awt.Color;
import java.awt.Font;

import org.loon.framework.game.simple.action.map.RectBox;
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
public class Label extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Font font;

	private boolean visible;

	private int width, height;

	private Color color;

	private float alpha;

	private String label;

	public Label(String label, int x, int y) {
		this(GraphicsUtils.getFont(12), label, x, y);
	}

	public Label(String label, String font, int type, int size, int x, int y) {
		this(GraphicsUtils.getFont(font, type, size), label, x, y);
	}

	public Label(Font font, String label, int x, int y) {
		this.font = font;
		this.label = label;
		this.color = Color.BLACK;
		this.visible = true;
		this.setLocation(x, y);
	}

	public void setFont(String fontName, int type, int size) {
		setFont(GraphicsUtils.getFont(fontName, type, size));
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void createUI(LGraphics g) {
		if (visible) {
			Font oldFont = g.getFont();
			Color oldColor = g.getColor();
			g.setFont(font);
			g.setColor(color);
			this.width = g.getFontMetrics().stringWidth(label);
			this.height = font.getSize();
			g.setAntialias(true);
			if (alpha > 0 && alpha <= 1.0) {
				g.setAlpha(alpha);
				g.drawString(label, x(), y());
				g.setAlpha(1.0F);
			} else {
				g.drawString(label, x(), y());
			}
			g.setAntialias(false);
			g.setFont(oldFont);
			g.setColor(oldColor);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void update(long timer) {

	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), width, height);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getLabel() {
		return label;
	}
	
	public void setLabel(int label) {
		setLabel(String.valueOf(label));
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
