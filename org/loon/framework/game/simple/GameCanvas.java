package org.loon.framework.game.simple;

import java.awt.Canvas;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;


import sun.awt.InputMethodSupport;

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
public class GameCanvas extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StringBuffer committedText = new StringBuffer();

	private transient TextLayout textLayout = null;

	private transient boolean validTextLayout = false;

	public GameCanvas() {
		super();
		try {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			boolean shouldEnable = false;
			// 验证当前环境是否支持输入法调用
			if (toolkit instanceof InputMethodSupport) {
				shouldEnable = ((InputMethodSupport) toolkit)
						.enableInputMethodsForTextComponent();
			}
			enableInputMethods(shouldEnable);
		} catch (Exception e) {
		}

	}

	/**
	 * 转换当前待发数据为AttributedCharacterIterator
	 * 
	 * @return
	 */
	public AttributedCharacterIterator getCommittedText() {
		AttributedString string = new AttributedString(committedText.toString());
		return string.getIterator();
	}

	/**
	 * 转换指定长度的待发数据为AttributedCharacterIterator
	 * 
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public AttributedCharacterIterator getCommittedText(int beginIndex,
			int endIndex) {
		AttributedString string = new AttributedString(committedText.toString());
		return string.getIterator(null, beginIndex, endIndex);
	}

	/**
	 * 获得当前待发数据长度
	 * 
	 * @return
	 */
	public int getCommittedTextLength() {
		return committedText.length();
	}

	public AttributedCharacterIterator getDisplayText() {
		AttributedString string = new AttributedString(committedText.toString());
		if (committedText.length() > 0) {
			string.addAttribute(TextAttribute.FONT, getFont());
		}
		return string.getIterator();
	}

	/**
	 * 获得当前的TextLayout
	 * 
	 * @return
	 */
	public synchronized TextLayout getTextLayout() {
		if (!validTextLayout) {
			textLayout = null;
			AttributedCharacterIterator text = getDisplayText();
			if (text.getEndIndex() > text.getBeginIndex()) {
				FontRenderContext context = ((Graphics2D) getGraphics())
						.getFontRenderContext();
				textLayout = new TextLayout(text, context);
			}
		}
		validTextLayout = true;
		return textLayout;
	}

	/**
	 * 令TextLayout验证无效化
	 * 
	 */
	public synchronized void invalidateTextLayout() {
		validTextLayout = false;
	}

	/**
	 * 返回文字输入的显示点坐标
	 * 
	 * @return
	 */
	public Point getTextOrigin() {
		return GameManager.getSystemHandler().getTextOrigin();
	}

	/**
	 * 返回当前插入点Rectangle
	 * 
	 * @return
	 */
	public Rectangle getCaretRectangle() {
		TextHitInfo caret = getCaret();
		if (caret == null) {
			return null;
		}
		return getCaretRectangle(caret);
	}

	/**
	 * 返回当前文本索引的插入位置
	 * 
	 * @param caret
	 * @return
	 */
	public Rectangle getCaretRectangle(TextHitInfo caret) {
		TextLayout textLayout = getTextLayout();
		int caretLocation;
		if (textLayout != null) {
			caretLocation = Math.round(textLayout.getCaretInfo(caret)[0]);
		} else {
			caretLocation = 0;
		}
		FontMetrics metrics = getGraphics().getFontMetrics();
		return new Rectangle(
				GameManager.getSystemHandler().getTextOrigin().x
						+ caretLocation, GameManager.getSystemHandler()
						.getTextOrigin().y
						- metrics.getAscent(), 0, metrics.getAscent()
						+ metrics.getDescent());
	}

	public TextHitInfo getCaret() {
		return TextHitInfo.trailing(committedText.length() - 1);
	}

	public void insertCharacter(char c) {
		committedText.append(c);
		invalidateTextLayout();
	}

	public void clear() {
		this.committedText.delete(0, committedText.length());
	}

	public String getCommittedString() {
		return this.committedText.toString();
	}

}
