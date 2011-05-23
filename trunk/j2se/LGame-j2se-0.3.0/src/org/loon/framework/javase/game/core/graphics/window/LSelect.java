package org.loon.framework.javase.game.core.graphics.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import org.loon.framework.javase.game.core.graphics.LContainer;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;
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
public class LSelect extends LContainer {

	private Font messageFont = GraphicsUtils.getFont("黑体", 20);

	private Color fontColor = Color.WHITE;

	private int left, top, type, nTop;

	private int sizeFont, doubleSizeFont, tmpOffset, messageLeft, nLeft,
			messageTop, selectSize, selectFlag;

	private float autoAlpha;

	private LTimer delay;

	private String[] selects;

	private String message, result;

	private Image cursor, buoyage;

	private boolean isAutoAlpha, isSelect;

	public LSelect(int x, int y, int width, int height) {
		this((BufferedImage) null, x, y, width, height);
	}

	public LSelect(String fileName) {
		this(fileName, 0, 0);
	}

	public LSelect(String fileName, int x, int y) {
		this(GraphicsUtils.loadBufferedImage(fileName), x, y);
	}

	public LSelect(Image formImage) {
		this(formImage, 0, 0);
	}

	public LSelect(Image formImage, int x, int y) {
		this(GraphicsUtils.getBufferImage(formImage), x, y);
	}

	public LSelect(BufferedImage formImage, int x, int y) {
		this(formImage, x, y, formImage.getWidth(), formImage.getHeight());
	}

	public LSelect(BufferedImage formImage, int x, int y, int width, int height) {
		super(x, y, width, height);
		if (formImage == null) {
			this.setBackground(createformImage(width, height));
			this.setAlpha(0.3F);
		} else {
			this.setBackground(formImage);
		}
		this.customRendering = true;
		this.selectFlag = 1;
		this.tmpOffset = -(width / 10);
		this.delay = new LTimer(150);
		this.autoAlpha = 0.25F;
		this.isAutoAlpha = true;
		this.setCursor("assets/creese.png");
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public void setLeftOffset(int left) {
		this.left = left;
	}

	public void setTopOffset(int top) {
		this.top = top;
	}

	public int getLeftOffset() {
		return left;
	}

	public int getTopOffset() {
		return top;
	}

	public int getResultIndex() {
		return selectFlag - 1;
	}

	public void setDelay(long timer) {
		delay.setDelay(timer);
	}

	public long getDelay() {
		return delay.getDelay();
	}

	public String getResult() {
		return result;
	}

	private static String[] getListToStrings(List list) {
		if (list == null || list.size() == 0)
			return null;
		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) list.get(i);
		}
		return result;
	}

	public void setMessage(String message, List list) {
		setMessage(message, getListToStrings(list));
	}

	public void setMessage(String[] selects) {
		setMessage(null, selects);
	}

	public void setMessage(String message, String[] selects) {
		this.message = message;
		this.selects = selects;
		this.selectSize = selects.length;
	}

	private static BufferedImage createformImage(int width, int height) {
		return GraphicsUtils.createImage(width, height, Transparency.OPAQUE);
	}

	public void update(long elapsedTime) {
		if (!visible) {
			return;
		}
		super.update(elapsedTime);
		if (isAutoAlpha && buoyage != null) {
			if (delay.action(elapsedTime)) {
				if (autoAlpha < 0.95F) {
					autoAlpha += 0.05F;
				} else {
					autoAlpha = 0.25F;
				}
			}
		}
	}

	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (!visible) {
			return;
		}
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(fontColor);
		g.setFont(messageFont);
		sizeFont = messageFont.getSize();
		doubleSizeFont = sizeFont * 2;
		messageLeft = (x + doubleSizeFont + sizeFont / 2) + tmpOffset + left
				+ doubleSizeFont;
		g.setAntiAlias(true);
		if (message != null) {
			messageTop = y + doubleSizeFont + top - 10;
			g.drawString(message, messageLeft, messageTop);
		} else {
			messageTop = y + top;
		}
		nTop = messageTop;
		if (selects != null) {
			nLeft = messageLeft - sizeFont / 4;
			for (int i = 0; i < selects.length; i++) {
				nTop += 30;
				type = i + 1;
				isSelect = (type == (selectFlag > 0 ? selectFlag : 1));
				if ((buoyage != null) && isSelect) {
					g.setAlpha(autoAlpha);
					g.drawImage(buoyage, nLeft, nTop
							- (int) (buoyage.getHeight(null) / 1.5));
					g.setAlpha(1.0F);
				}
				g.drawString(selects[i], messageLeft, nTop);
				if ((cursor != null) && isSelect) {
					g.drawImage(cursor, nLeft, nTop - cursor.getHeight(null)
							/ 2);
				}

			}
		}
		g.setAntiAlias(false);
		g.setColor(oldColor);
		g.setFont(oldFont);

	}

	/**
	 * 处理点击事件（请重载实现）
	 * 
	 */
	public void doClick() {

	}

	protected void processMouseClicked() {
		if (this.input.getMouseReleased() == MouseEvent.BUTTON1) {
			if ((selects != null) && (selectFlag > 0)) {
				this.result = selects[selectFlag - 1];
			}
			this.doClick();
		}
	}

	protected synchronized void processMouseMoved() {
		if (selects != null) {
			if (doubleSizeFont == 0) {
				doubleSizeFont = 20;
			}
			selectFlag = selectSize
					- (((nTop + 30) - input.getMouseY()) / doubleSizeFont);
			if (selectFlag < 1) {
				selectFlag = 0;
			}
			if (selectFlag > selectSize) {
				selectFlag = selectSize;
			}
		}
	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.VK_ENTER) {
			this.doClick();
		}
	}

	protected void processMouseDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getMouseDX(), this.input.getMouseDY());
		}
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Font getMessageFont() {
		return messageFont;
	}

	public void setMessageFont(Font messageFont) {
		this.messageFont = messageFont;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	protected void validateSize() {
		super.validateSize();
	}

	public Image getCursor() {
		return cursor;
	}

	public void setNotCursor() {
		this.cursor = null;
	}

	public void setCursor(Image cursor) {
		this.cursor = cursor;
	}

	public void setCursor(String fileName) {
		setCursor(GraphicsUtils.loadImage(fileName));
	}

	public Image getBuoyage() {
		return buoyage;
	}

	public void setNotBuoyage() {
		this.cursor = null;
	}

	public void setBuoyage(Image buoyage) {
		this.buoyage = buoyage;
	}

	public void setBuoyage(String fileName) {
		setBuoyage(GraphicsUtils.loadImage(fileName));
	}

	public boolean isFlashBuoyage() {
		return isAutoAlpha;
	}

	public void setFlashBuoyage(boolean flashBuoyage) {
		this.isAutoAlpha = flashBuoyage;
	}

	public String getUIName() {
		return "Select";
	}
}
