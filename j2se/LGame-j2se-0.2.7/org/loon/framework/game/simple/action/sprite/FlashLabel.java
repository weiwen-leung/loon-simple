package org.loon.framework.game.simple.action.sprite;

import java.awt.Color;

import org.loon.framework.game.simple.action.map.shapes.RectBox;
import org.loon.framework.game.simple.core.LObject;
import org.loon.framework.game.simple.core.graphics.LFont;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.timer.LTimer;

/**
 * Copyright 2008 - 2010
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
public class FlashLabel extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int DIR_NONE = 0;

	public static final int DIR_UP = 1;

	public static final int DIR_DOWN = 2;

	public static final int DIR_LEFT = 4;

	public static final int DIR_RIGHT = 8;

	public static final int DIR_IN = 16;

	public static final int DIR_OUT = 32;

	private String message;

	private boolean visible;

	private Color messageColor;

	private LFont messageFont;

	private int flashR;

	private int flashG;

	private int flashB;

	private int flashNum;

	private int dFontSize;

	private int flashAlphaDiff;

	private int fadeoutStartTime;

	private int fadeoutDirection;

	private int dFadeoutAlpha;

	private int xOffset, yOffset;

	private int xOffsetBase, yOffsetBase;

	private double flashSpeedX, flashSpeedY;

	private boolean isFlashing;

	private boolean isFadeout;

	private boolean isMessageBelow;

	private boolean isShadow;

	private Color shadowColor;

	private int index;

	private int count;

	private LTimer time;

	public FlashLabel(String s, int x, int y, Color c) {
		this(s, x, y, LFont.getDefaultFont(), c);
	}

	public FlashLabel(String s, int x, int y) {
		this(s, x, y, LFont.getDefaultFont(), Color.white);
	}

	public FlashLabel(String s, int x, int y, LFont font, Color color) {
		this.visible = true;
		this.message = s;
		this.time = new LTimer(500);
		this.setLocation(x, y);
		this.messageFont = font;
		this.messageColor = color;
		this.setFlashNum(3);
		this.dFontSize = 12;
		this.dFadeoutAlpha = 3;
		this.isFadeout = true;
		this.isFlashing = true;
		this.setFadeoutDirection(16);
		this.flashR = color.getRed();
		this.flashG = color.getGreen();
		this.flashB = color.getBlue();
	}

	public void setFlashColor(int i, int j, int k) {
		flashR = i;
		flashG = j;
		flashB = k;
	}

	public void setFlashNum(int i) {
		flashNum = i;
		flashAlphaDiff = 255 / (flashNum + 1);
	}

	public void setFadeoutDirection(int i) {
		fadeoutDirection = i;
	}

	public void setFlashMoveSpeed(double d, double d1) {
		flashSpeedX = d;
		flashSpeedY = d1;
	}

	public void setFlashOffset(int i, int j) {
		xOffsetBase = i;
		yOffsetBase = j;
	}

	public void setFadeoutStartTime(int i) {
		fadeoutStartTime = i;
	}

	public void setDFontSize(int i) {
		dFontSize = i;
	}

	public void setDFadeoutAlpha(int i) {
		dFadeoutAlpha = i;
	}

	public void setShadowColor(Color color) {
		shadowColor = color;
	}

	public void setShadow(boolean flag) {
		isShadow = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void move() {
		count++;
		xOffset = xOffsetBase + (int) (flashSpeedX * (double) count);
		yOffset = yOffsetBase + (int) (flashSpeedY * (double) count);
	}

	public void createUI(LGraphics g) {
		if (!visible) {
			return;
		}
		if (isMessageBelow) {
			g.setFont(messageFont);
			if (isShadow) {
				g.setColor(shadowColor);
				g.drawCenterString(message, x() + 3, y() + 3);
			}
			g.setColor(messageColor);
			g.drawCenterString(message, x(), y());
		}
		if (isFlashing) {
			String s = messageFont.getFontName();
			int i = messageFont.getStyle();
			int j = messageFont.getSize();
			for (int k = 0; k < index; k++) {
				int l = 0;
				if (isFadeout) {
					if (fadeoutDirection == 32) {
						l = flashAlphaDiff * (k + 1);
					} else if (fadeoutDirection == 16) {
						l = 255 - flashAlphaDiff * (k + 1);
					}
					if (count >= fadeoutStartTime) {
						l -= (count - fadeoutStartTime) * dFadeoutAlpha;
					}
				}
				if (l <= 0) {
					continue;
				}
				if (l > 255) {
					l = 255;
				}
				g.setColor(new Color(flashR, flashG, flashB, l));
				g.setFont(new LFont(s, i, j + dFontSize * (k + 1)));
				g.drawCenterString(message, x() + xOffset * (k + 1), y()
						+ yOffset * (k + 1));
			}
		}
		if (!isMessageBelow && fadeoutDirection == 16) {
			g.setFont(messageFont);
			if (isShadow) {
				g.setColor(shadowColor);
				g.drawCenterString(message, x() + 3, y() + 3);
			}
			g.setColor(messageColor);
			g.drawCenterString(message, x(), y());
		}
	}

	public int getHeight() {
		return messageFont.getHeight();
	}

	public int getWidth() {
		return messageFont.stringWidth(message);
	}

	public void setIndex(int i) {
		this.index = i;
	}

	public void update(long elapsedTime) {
		if (time.action(elapsedTime)) {
			if (index < flashNum) {
				index++;
			} else {
				index = 0;
			}
		}
	}

	public float getAlpha() {
		return 0;
	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), messageFont.stringWidth(message),
				messageFont.getHeight());
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
