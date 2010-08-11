package org.loon.framework.game.simple.core.graphics.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.graphics.LComponent;
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
public class LProgress extends LComponent {

	private float percentage;

	private boolean showMessage;

	private static final Color defaultRedColor = new Color(145, 78, 59);

	private static final Color defaultBlueColor = new Color(61, 98, 135);

	private static final Color defaultYellowColor = new Color(175, 134, 82);

	private Color fillColor;

	private Color fontColor;

	private String message;

	private Font font;

	private BufferedImage screenImage, backgroundImage;

	public LProgress(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.setMessage(null);
		this.font = GraphicsUtils.getFont("Dialog", Font.BOLD, 12);
		this.fontColor = Color.WHITE;
		this.showMessage = true;
		this.customRendering = true;
		this.elastic = true;
		this.visible = true;
		this.progressBlue();
		this.backgroundImage = GraphicsUtils.createButtonBackground(width,
				height, Color.BLACK, Color.WHITE);
	}

	public void progressColor(Color color) {
		this.screenImage = GraphicsUtils.createButtonImage(color, false,
				getWidth(), getHeight());
	}

	public void progressRed() {
		progressColor(this.fillColor = defaultRedColor);
	}

	public void progressYellow() {
		progressColor(this.fillColor = defaultYellowColor);
	}

	public void progressBlue() {
		progressColor(this.fillColor = defaultBlueColor);
	}

	public void updatePercent(int percentage) {
		this.percentage = (float) percentage / 100;

	}

	public void updateBar(float percentage) {
		this.percentage = percentage;

	}

	protected void createCustomUI(Graphics2D g, int x, int y, int w, int h) {
		if (visible) {
			Color oldColor = g.getColor();
			Font oldFont = g.getFont();
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics();
			g.setClip(x, y, Math.round(percentage * w), h);
			g.drawImage(screenImage, x, y, null);
			g.setColor(fontColor);
			String mes = null;
			if (message == null & showMessage) {
				mes = Integer.toString((int) (percentage * 100)) + "%";
			} else if (showMessage) {
				mes = message;
			} else {
				mes = "";
			}
			if (this.showMessage) {
				GraphicsUtils
						.drawStyleString(g, mes, x
								+ (w - metrics.stringWidth(mes)) / 2 + 6, y
								+ (h + metrics.getAscent()) / 2, fontColor,
								Color.BLACK);
			}
			g.setClip(x + Math.round(percentage * w), y, w
					- Math.round(percentage * w), h);

			g.drawImage(backgroundImage, x, y, null);
			if (this.showMessage) {
				GraphicsUtils
						.drawStyleString(g, mes, x
								+ (w - metrics.stringWidth(mes)) / 2 + 6, y
								+ (h + metrics.getAscent()) / 2, fontColor,
								Color.BLACK);
			}
			g.setFont(oldFont);
			g.setColor(oldColor);

		}
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public float getPercentage() {
		return percentage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUIName() {
		return "Progress";
	}

}
