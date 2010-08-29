package org.loon.framework.game.simple.core.graphics.window;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.core.graphics.filter.ImageFilterFactory;
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

public class LButton extends LComponent {

	private String text = "";

	private boolean over, pressed, exception;

	private int pressedTime;

	public LButton(String fileName) {
		this(fileName, null, 0, 0);
	}

	public LButton(String fileName, String text, int x, int y) {
		this(GraphicsUtils.loadBufferedImage(fileName), text, x, y);
	}

	public LButton(BufferedImage img, String text, int x, int y) {
		this(img, text, img.getWidth(), img.getHeight(), x, y);
	}

	public LButton(String fileName, int row, int col) {
		this(fileName, null, row, col, 0, 0);
	}

	public LButton(String fileName, String text, int row, int col, int x, int y) {
		this(GraphicsUtils.loadBufferedImage(fileName), text, row, col, x, y);
	}

	public LButton(BufferedImage img, String text, int row, int col, int x,
			int y) {
		this(GraphicsUtils.getSplitBufferedImages(img, row, col), text, row,
				col, x, y);
	}

	public LButton(BufferedImage[] img, String text, int row, int col, int x,
			int y) {
		super(x, y, row, col);
		this.setImages(img);
		this.text = text;
	}

	public LButton(String text, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.text = text;

	}

	public void setImages(BufferedImage[] images) {
		BufferedImage[] buttons = new BufferedImage[4];
		if (images != null) {
			int size = images.length;
			switch (size) {
			case 1:
				buttons[0] = ImageFilterFactory.getGray(images[0]);
				buttons[1] = images[0];
				buttons[2] = images[0];
				buttons[3] = images[0];
				break;
			case 2:
				buttons[0] = images[0];
				buttons[1] = images[1];
				buttons[2] = images[0];
				buttons[3] = images[0];
				break;
			case 3:
				buttons[0] = images[0];
				buttons[1] = images[1];
				buttons[2] = images[2];
				buttons[3] = images[0];
				break;
			case 4:
				buttons = images;
				break;
			default:
				exception = true;
				break;
			}
		}
		if (!exception) {
			this.setImageUI(buttons, true);
		}

	}

	public void update(long timer) {
		if (this.pressedTime > 0 && --this.pressedTime <= 0) {
			this.pressed = false;
		}
	}

	public boolean isMouseOver() {
		return this.over;
	}

	public boolean isMousePressed() {
		return this.pressed;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String st) {
		this.text = st;
		this.createUI();
	}

	protected void processMouseDragged() {
		if (this.input.isMouseDown(MouseEvent.BUTTON1)) {
			this.over = this.pressed = this.intersects(this.input.getMouseX(),
					this.input.getMouseY());
		}
	}

	/**
	 * 处理点击事件（请重载实现）
	 * 
	 */
	public void doClick() {
	}
	
	public void downClick(){
		
	}

	public void upClick(){
		
	}
	
	public boolean isPressed(){
		return pressed;
	}

	protected void processMouseClicked() {
		if (this.input.getMouseReleased() == MouseEvent.BUTTON1) {
			this.doClick();
		}
	}

	protected void processMousePressed() {
		if (this.input.getMousePressed() == MouseEvent.BUTTON1) {
			downClick();
			this.pressed = true;
		}
	}

	protected void processMouseReleased() {
		if (this.input.getMouseReleased() == MouseEvent.BUTTON1) {
			upClick();
			this.pressed = false;
		}
	}

	protected void processMouseEntered() {
		this.over = true;
	}

	protected void processMouseExited() {
		this.over = this.pressed = false;
	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.VK_ENTER) {
			this.pressedTime = 5;
			this.pressed = true;
			this.doClick();
		}
	}

	protected void processKeyReleased() {
		if (this.isSelected()
				&& this.input.getKeyReleased() == KeyEvent.VK_ENTER) {
			this.pressed = false;
		}
	}

	public boolean isException() {
		return exception;
	}

	public String getUIName() {
		return "Button";
	}

}
