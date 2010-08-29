package org.loon.framework.game.simple.core.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import org.loon.framework.game.simple.core.graphics.LColor;
import org.loon.framework.game.simple.core.graphics.ThreadScreen;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.utils.GraphicsUtils;
/**
 * 
 * Copyright 2008 - 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng  
 * @email：ceponline@yahoo.com.cn 
 * @version 0.1
 */
class DrawImpl implements Draw {

	private int xPoints[] = new int[4];

	private int yPoints[] = new int[4];

	private Image offsetScreen;

	private LGraphics offsetGraphics;

	private Font font;

	public DrawImpl(ThreadScreen screen) {
		this.offsetScreen = screen.getAwtImage();
		this.offsetGraphics = screen.getLGraphics();
		this.font = new Font("Monospaced", 0, 20);
	}

	public int rgbToPixel(int r, int g, int b) {
		return LColor.getPixel(r, g, b);
	}

	public void setFont(String fontName, int style, int size) {
		offsetGraphics.setFont(GraphicsUtils.getFont(fontName, style, size));
	}

	public void drawText(String message, int x, int y, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.drawString(message, x, y);
	}

	public void drawText(String message, int x, int y, Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.drawString(message, x, y);
	}

	public void line(int x, int y, int w, int h, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.drawLine(x, y, w, h);
	}

	public void drawImage(Image img, int x, int y) {
		offsetGraphics.drawImage(img, x, y);
	}

	public void drawImage(Image img, int x, int y, int w, int h) {
		offsetGraphics.drawImage(img, x, y, w, h);
	}

	public void drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2) {
		offsetGraphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
	}

	public void drawImage(Image image, int x, int y, int w, int h, int w1,
			int w2) {
		offsetGraphics.drawImage(image, x, y, x + w, y + h, w1, w2, w1 + w, w2
				+ h);
	}

	public void drawImage(Image image, int x, int y, int d) {
		if(image==null){
			return;
		}
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		switch (d) {
		case 0:
			offsetGraphics.drawImage(image, x, y);
			break;

		case 1:
			offsetGraphics.drawImage(image, x + w, y, x, y + h, 0, 0, w, h);
			break;

		case 2:
			offsetGraphics.drawImage(image, x, y + h, x + w, y, 0, 0, w, h);
			break;

		case 3:
			offsetGraphics.drawImage(image, x + w, y + h, x, y, 0, 0, w, h);
			break;
		}
	}

	public void line(int x, int y, int w, int h, Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.drawLine(x, y, w, h);
	}

	public void circleFill(int x, int y, int w, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.fillOval(x - w / 2, y - w / 2, w, w);
	}

	public void circleFill(int x, int y, int w, Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.fillOval(x - w / 2, y - w / 2, w, w);
	}

	public void circle(int x, int y, int w, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.drawArc(x - w / 2, y - w / 2, w, w, 0, 360);
	}

	public void circle(int x, int y, int w, Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.drawArc(x - w / 2, y - w / 2, w, w, 0, 360);

	}

	public void rect(int x, int y, int w, int h, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.drawRect(x, y, w, h);
	}

	public void rect(int x, int y, int w, int h, Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.drawRect(x, y, w, h);
	}

	public void fillRect(int x, int y, int w, int h, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		xPoints[0] = x;
		yPoints[0] = y;
		xPoints[1] = x + w;
		yPoints[1] = y;
		xPoints[2] = x + w;
		yPoints[2] = y + h;
		xPoints[3] = x;
		yPoints[3] = y + h;
		offsetGraphics.fillPolygon(xPoints, yPoints, 4);
	}

	public void fillPolygon(int xPoints[], int yPoints[], int nPoints, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	public void fillPolygon(int xPoints[], int yPoints[], int nPoints,
			Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	public void drawPolygon(int xPoints[], int yPoints[], int nPoints,
			Color color) {
		offsetGraphics.setColor(color);
		offsetGraphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	public void drawPolygon(int xPoints[], int yPoints[], int nPoints, int pixel) {
		offsetGraphics.setColor(LColor.getColor(pixel));
		offsetGraphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	public void surfaceCopy(Graphics g) {
		g.drawImage(offsetScreen, 0, 0, null);
	}

	public Image loadImage(String fileName) {
		return GraphicsUtils.loadImage(fileName);
	}

	public int getFontSize() {
		if (font != null) {
			return font.getSize();
		} else {
			return 0;
		}
	}

	public int getHeight() {
		return offsetScreen.getHeight(null);
	}

	public int getWidth() {
		return offsetScreen.getWidth(null);
	}

	public void dispose() {
		if (offsetGraphics != null) {
			offsetGraphics.dispose();
			offsetScreen = null;
		}
	}

}
