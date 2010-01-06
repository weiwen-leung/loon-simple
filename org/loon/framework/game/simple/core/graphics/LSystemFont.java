package org.loon.framework.game.simple.core.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

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
public class LSystemFont implements LFont {

	private final Font font;

	private final FontMetrics metrics;

	private int height;

	private int width;

	private Color color;

	public LSystemFont(Font font, Color color) {
		this.font = font;
		this.color = color;
		Graphics2D graphics2d = GraphicsUtils.createImage(1, 1)
				.createGraphics();
		metrics = graphics2d.getFontMetrics(font);
		height = metrics.getMaxAscent() + metrics.getMaxDescent()
				+ metrics.getLeading();
		width = height - metrics.getDescent();
		graphics2d.dispose();
	}

	public LSystemFont(Font font) {
		this(font, null);
	}

	public int drawString(Graphics2D g2d, String s, int i, int j) {
		if (g2d.getFont() != this.font) {
			g2d.setFont(this.font);
		}
		int left = j + width;
		g2d.setColor(Color.GRAY);
		g2d.drawString(s, i + 1, left);
		g2d.drawString(s, i - 1, left);
		g2d.drawString(s, i, left + 1);
		g2d.drawString(s, i, left - 1);
		if (color != null) {
			g2d.setColor(color);
		} else {
			g2d.setColor(Color.WHITE);
		}
		g2d.drawString(s, i, left);
		return i + getWidth(s);
	}

	public int drawString(Graphics2D graphics2d, String s, int i, int j, int k,
			int l) {
		if (i == 1)
			return drawString(graphics2d, s, j, k);
		if (i == 3)
			return drawString(graphics2d, s, (j + l / 2) - getWidth(s) / 2, k);
		if (i == 2)
			return drawString(graphics2d, s, (j + l) - getWidth(s), k);
		if (i == 4) {
			int i1;
			if ((i1 = l - getWidth(s)) <= 0)
				return drawString(graphics2d, s, j, k);
			int j1 = s.length();
			int k1 = 0;
			for (int l1 = 0; l1 < j1;) {
				if (s.charAt(l1++) == ' ') {
					k1++;
				}
			}
			if (k1 > 0) {
				k1 = (i1 + k1 * getWidth(' ')) / k1;
			}
			int j2;
			for (int i2 = 0; i2 < j1; i2 = j2 + 1) {
				if ((j2 = s.indexOf(' ', i2)) == -1) {
					j2 = j1;
				}
				String s1 = s.substring(i2, j2);
				drawString(graphics2d, s1, j, k);
				j += getWidth(s1) + k1;
			}

			return j;
		} else {
			return 0;
		}
	}

	public int drawText(Graphics2D g2d, String s, int i, int j, int k, int l,
			int i1, int j1) {
		boolean flag = true;
		int j2 = s.length();
		int k2 = j1;
		int k1;
		int l1;
		int i2;
		for (k1 = l1 = i2 = 0; k1 < j2;) {
			char c1 = s.charAt(k1++);
			k2 += getWidth(c1);
			if (c1 - 32 == 0)
				i2 = k1 - 1;
			if (k2 >= l) {
				if (flag) {
					drawString(g2d, s.substring(l1, i2), i,
							i != 2 ? j + j1 : j, k, l - j1);
					flag = false;
				} else {
					drawString(g2d, s.substring(l1, i2), i, j, k, l);
				}
				k += getHeight() + i1;
				k2 = 0;
				l1 = k1 = i2 + 1;
			}
		}
		if (flag) {
			drawString(g2d, s.substring(l1, k1), i, i != 2 ? j + j1 : j, k, l
					- j1);
		} else {
			if (k2 != 0) {
				drawString(g2d, s.substring(l1, k1), i != 2 ? 1 : 2, j, k, l);
			}
		}
		return k + getHeight();
	}

	public Font getFont() {
		return this.font;
	}

	public FontMetrics getFontMetrics() {
		return metrics;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getWidth(String s) {
		return metrics.stringWidth(s);
	}

	public int getWidth(char c1) {
		return metrics.charWidth(c1);
	}

	public int getHeight() {
		return height;
	}

	public boolean isAvailable(char c1) {
		return true;
	}

}
