package org.loon.framework.game.simple.core.graphics.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.core.graphics.GameFont;
import org.loon.framework.game.simple.core.graphics.LSystemFont;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.utils.GraphicsUtils;

/**
 * 
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

public class LText extends LComponent {

	protected transient TextListener textListener;

	protected transient TextEvent textEvent;

	private static final String NULL_STRING = new String();

	private static final GameFont myfont = new LSystemFont(new Font(LSystem.FONT,
			Font.PLAIN, 15));

	private final StringBuffer buff = new StringBuffer();

	private String text;

	private int caretPosition, caret;

	private int maxLength = -1;

	private boolean editable;

	protected int visiblePosition;

	private BufferedImage textUI;

	private GameFont font = LText.myfont;

	private long lastTicks;

	private boolean showCursor;

	public LText(String text, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.text = text;
		this.buff.append(text);
		this.caret = 5;
		this.editable = true;
		this.createTextUI();
	}

	public GameFont getFont() {
		return this.font;
	}

	public void setFont(GameFont font) {
		this.font = font;
		this.moveCaretPosition(0);
		this.createTextUI();
	}

	/**
	 * 鼠标点击事件
	 * 
	 */
	public void doClick() {
	}

	public void update(long timer) {
		if (this.isSelected() && this.editable) {
			if (System.currentTimeMillis() - this.lastTicks > 500) {
				this.lastTicks = System.currentTimeMillis();
				this.showCursor = !this.showCursor;
			}

		} else if (this.showCursor) {
			this.showCursor = false;
		}
	}

	public void setEnabled(boolean b) {
		if (this.isEnabled() == b) {
			return;
		}
		super.setEnabled(b);

		this.createTextUI();
	}

	protected void createTextUI() {
		this.textUI = GraphicsUtils.createImage(this.getWidth(), this
				.getHeight(), Transparency.BITMASK);

		Graphics2D g = this.textUI.createGraphics();

		g.setClip(5, 2, this.getWidth() - 10, this.getHeight() - 4);
		g.setColor((this.isEnabled()) ? Color.BLACK : new Color(172, 168, 153));

		this.font.drawString(g, this.getText(), 5 - this.visiblePosition, (this
				.getHeight() / 2)
				- (this.font.getHeight() / 2));

		g.dispose();
	}

	public void createUI(LGraphics g) {
		super.createUI(g);
		this.renderText(g, this.getScreenX(), this.getScreenY(), this
				.getWidth(), this.getHeight());
	}

	protected void renderText(LGraphics g, int x, int y, int w, int h) {
		g.drawImage(this.textUI, x, y);
		if (this.editable && this.showCursor) {
			int yfont = y + (h >> 1) - (this.font.getHeight() >> 1);
			g.setColor(Color.BLACK);
			g.drawLine(x + this.caret, yfont, x + this.caret, yfont
					+ this.font.getHeight());
		}
	}

	public String getText() {
		return this.text;
	}

	public void setText(String st) {
		this.buff.setLength(0);
		this.buff.append(st);
		if (this.maxLength >= 0 && this.buff.length() > this.maxLength) {
			this.buff.setLength(this.maxLength);
		}
		this.text = this.buff.toString();
		this.setCaretPosition(this.buff.length());
		this.createTextUI();
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public void setMaxLength(int i) {
		this.maxLength = i;
		if (this.maxLength >= 0 && this.buff.length() > this.maxLength) {
			this.buff.setLength(this.maxLength);
			this.text = this.buff.toString();
			this.createTextUI();
		}
	}

	public int getCaretPosition() {
		return this.caretPosition;
	}

	public void setCaretPosition(int i) {
		this.caretPosition = i;

		if (this.caretPosition < 0) {
			this.caretPosition = 0;
		}
		if (this.caretPosition > this.buff.length()) {
			this.caretPosition = this.buff.length();
		}

		boolean invalidCaret = true, createText = false;
		while (invalidCaret) {
			invalidCaret = false;
			this.caret = this.font.getWidth(this.getText().substring(0,
					this.caretPosition))
					+ 5 - this.visiblePosition;
			if (this.caret < 5) {
				int len = this.font.getWidth(this.getText().substring(
						Math.max(this.caretPosition - 5, 0),
						Math.max(this.caretPosition - 1, 0)));
				this.visiblePosition -= len;
				if (this.visiblePosition < 0 || this.caretPosition <= 4) {
					this.visiblePosition = 0;
				}
				invalidCaret = true;
				createText = true;

			} else if (this.caret >= this.getWidth() - 5) {
				int len = this.font.getWidth(this.getText().substring(
						Math.min(this.caretPosition - 1, this.getText()
								.length()),
						Math.min(this.caretPosition + 3, this.getText()
								.length())));
				this.visiblePosition += len;
				invalidCaret = true;
				createText = true;
			}
		}

		if (createText) {
			this.createTextUI();
		}
	}

	public void moveCaretPosition(int i) {
		this.setCaretPosition(this.caretPosition + i);
	}

	public boolean isEditable() {
		return this.editable;
	}

	public void setEditable(boolean b) {
		if (this.editable == b) {
			return;
		}

		this.editable = b;
		this.createTextUI();
	}

	public boolean insertString(int offset, String string) {
		if (this.maxLength >= 0
				&& this.buff.length() + string.length() > this.maxLength) {
			return false;
		}

		this.buff.insert(offset, string);
		this.text = this.buff.toString();
		this.createTextUI();
		return true;
	}

	public void delete(int index) {
		if (index < 0 || index > this.buff.length() - 1) {
			return;
		}

		this.buff.deleteCharAt(index);
		this.text = this.buff.toString();

		if (this.caretPosition > this.buff.length()) {
			this.setCaretPosition(this.buff.length());
		}
		this.createTextUI();
	}

	protected void processMouseClicked() {
		super.processMouseClicked();
	
	}
	
	public void changeText(String text){
		if (text.length() > 0) {
			insertString(caretPosition,text);
		}
	}

	protected void processKeyPressed() {
		super.processKeyPressed();
		// 不可编辑
		if (!this.editable) {
			return;
		}
		// 分别按键事件
		switch (this.input.getKeyPressed()) {
		case KeyEvent.VK_LEFT:
			if (this.input.isKeyDown(KeyEvent.VK_CONTROL)) {
				int pos = this.getText().lastIndexOf(' ',
						this.caretPosition - 2);
				if (pos != -1) {
					this.setCaretPosition(pos + 1);
				} else {
					this.setCaretPosition(0);
				}
			} else {
				this.moveCaretPosition(-1);
			}
			break;

		case KeyEvent.VK_RIGHT:
			if (this.input.isKeyDown(KeyEvent.VK_CONTROL)) {
				int pos = this.getText().indexOf(' ', this.caretPosition);
				if (pos != -1) {
					this.setCaretPosition(pos + 1);
				} else {
					this.setCaretPosition(this.getText().length());
				}

			} else {
				this.moveCaretPosition(1);
			}
			break;

		case KeyEvent.VK_HOME:
			this.setCaretPosition(0);
			break;

		case KeyEvent.VK_END:
			this.setCaretPosition(this.getText().length());
			break;

		case KeyEvent.VK_ENTER:
			this.doClick();
			break;

		case KeyEvent.VK_BACK_SPACE:
			if (this.caretPosition > 0) {
				this.moveCaretPosition(-1);
				this.delete(this.caretPosition);
			}
			break;

		case KeyEvent.VK_DELETE:
			this.delete(this.caretPosition);
			break;

		default:
			boolean upperCase = this.input.isKeyDown(KeyEvent.VK_SHIFT);
			boolean capsLock = false;
			try {
				capsLock = Toolkit.getDefaultToolkit().getLockingKeyState(
						KeyEvent.VK_CAPS_LOCK);
			} catch (Exception e) {
			}

			String textString = LText.NULL_STRING;
			int keyCode = this.input.getKeyPressed();

			switch (keyCode) {
			case KeyEvent.VK_BACK_QUOTE:
				textString = (upperCase) ? "~" : "`";
				break;
			case KeyEvent.VK_1:
				textString = (upperCase) ? "!" : "1";
				break;
			case KeyEvent.VK_2:
				textString = (upperCase) ? "@" : "2";
				break;
			case KeyEvent.VK_3:
				textString = (upperCase) ? "#" : "3";
				break;
			case KeyEvent.VK_4:
				textString = (upperCase) ? "$" : "4";
				break;
			case KeyEvent.VK_5:
				textString = (upperCase) ? "%" : "5";
				break;
			case KeyEvent.VK_6:
				textString = (upperCase) ? "^" : "6";
				break;
			case KeyEvent.VK_7:
				textString = (upperCase) ? "&" : "7";
				break;
			case KeyEvent.VK_8:
				textString = (upperCase) ? "*" : "8";
				break;
			case KeyEvent.VK_9:
				textString = (upperCase) ? "(" : "9";
				break;
			case KeyEvent.VK_0:
				textString = (upperCase) ? ")" : "0";
				break;
			case KeyEvent.VK_MINUS:
				textString = (upperCase) ? "_" : "-";
				break;
			case KeyEvent.VK_EQUALS:
				textString = (upperCase) ? "+" : "=";
				break;
			case KeyEvent.VK_BACK_SLASH:
				textString = (upperCase) ? "|" : "\\";
				break;
			case KeyEvent.VK_OPEN_BRACKET:
				textString = (upperCase) ? "{" : "[";
				break;
			case KeyEvent.VK_CLOSE_BRACKET:
				textString = (upperCase) ? "}" : "]";
				break;
			case KeyEvent.VK_SEMICOLON:
				textString = (upperCase) ? ":" : ";";
				break;
			case KeyEvent.VK_QUOTE:
				textString = (upperCase) ? "\"" : "'";
				break;
			case KeyEvent.VK_COMMA:
				textString = (upperCase) ? "<" : ",";
				break;
			case KeyEvent.VK_PERIOD:
				textString = (upperCase) ? ">" : ".";
				break;
			case KeyEvent.VK_SLASH:
				textString = (upperCase) ? "?" : "/";
				break;
			case KeyEvent.VK_DIVIDE:
				textString = "/";
				break;
			case KeyEvent.VK_MULTIPLY:
				textString = "*";
				break;
			case KeyEvent.VK_SUBTRACT:
				textString = "-";
				break;
			case KeyEvent.VK_ADD:
				textString = "+";
				break;
			case KeyEvent.VK_DECIMAL:
				textString = ".";
				break;
			case KeyEvent.VK_SPACE:
				textString = " ";
				break;
			default:
				textString = KeyEvent.getKeyText(keyCode).toLowerCase();
				if (textString.length() == 0 || textString.length() > 1) {
					// 无效的关键字
					return;
				}
				break;
			}

			if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
				if (upperCase && !capsLock || !upperCase && capsLock) {
					// 转化为大写字母
					textString = textString.toUpperCase();
				}
			}

			if (textString == LText.NULL_STRING) {
				return;
			}

			if (this.insertString(this.caretPosition, textString)) {
				this.moveCaretPosition(1);
			}
			
			break;
		}
	}

	public String getUIName() {
		return "Text";
	}

}
