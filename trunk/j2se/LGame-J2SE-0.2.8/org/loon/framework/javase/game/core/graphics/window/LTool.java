package org.loon.framework.javase.game.core.graphics.window;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.loon.framework.javase.game.core.graphics.LComponent;

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

public class LTool extends LComponent {

	private boolean over, pressed;

	private int pressedTime;

	public LTool(int x, int y, int w, int h) {
		super(x, y, w, h);
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

	public void doClick() {
	}

	protected void processMouseDragged() {
		if (this.input.isMouseDown(MouseEvent.BUTTON1)) {
			this.over = this.pressed = this.intersects(
					this.input.getMouseX(), this.input.getMouseY());
		}
	}

	protected void processMouseClicked() {
		if (this.input.getMouseReleased() == MouseEvent.BUTTON1) {
			this.doClick();
		}
	}

	protected void processMousePressed() {
		if (this.input.getMousePressed() == MouseEvent.BUTTON1) {
			this.pressed = true;
		}
	}

	protected void processMouseReleased() {
		if (this.input.getMouseReleased() == MouseEvent.BUTTON1) {
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

	public String getUIName() {
		return "Tool";
	}

}
