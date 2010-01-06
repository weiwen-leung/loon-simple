package org.loon.framework.game.simple.core.graphics.window;

import java.awt.Point;

import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.utils.StringUtils;

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
public class LToolTip extends LComponent {

	// 默认悬浮时间
	private int initialDelay = 60;

	// 默认关闭时间
	private int dismissDelay = 250;

	// 中间延迟
	private int reshowDelay = 30;

	public int initial, dismiss, reshow, dismissTime;

	private LComponent tooltip;

	private String tipText = "";

	private boolean tooltipChanged;

	private final Point spacing = new Point(0, 22);

	public LToolTip() {
		super(0, 0, 0, 0);
		this.setLayer(Integer.MAX_VALUE - 100);
		this.setVisible(false);
	}

	public void update(long timer) {
		if (this.isVisible()) {
			if (this.tooltip != null && !this.tooltipChanged) {
				if (++this.dismiss >= this.dismissTime) {
					this.setToolTipComponent(null);
					this.setVisible(false);
					this.reshow = 0;
				}
			} else {
				this.setVisible(false);
			}

		} else {
			if (this.reshow > 0) {
				this.reshow--;
			}

			if (this.tooltip != null
					&& (this.reshow > 0 || ++this.initial >= this.initialDelay)) {
				this.show(this.input.getMouseX(), this.input.getMouseY());
			}
		}
	}

	public String getUIName() {
		return "ToolTip";
	}

	public void show(int x, int y) {
		if (this.tooltip == null) {
			return;
		}
		this.setVisible(true);
		this.initial = 0;
		this.dismiss = 0;
		this.reshow = this.reshowDelay;
		this.tooltipChanged = false;

		if (this.tooltip != null) {
			if (this.ui == null
					|| !this.tooltip.getToolTipText().equals(this.tipText)) {
				this.createUI();
				this.tipText = this.tooltip.getToolTipText();
				String[] componentTipText = StringUtils
						.parseString(this.tipText);
				this.dismissTime = (this.dismissDelay * componentTipText.length);
			}
		}
		x += this.spacing.x;
		y += this.spacing.y;

		if (x + this.ui[0].getWidth() + 20 > this.desktop.getWidth()) {
			x -= this.ui[0].getWidth() + this.spacing.x;
		}
		if (y + this.ui[0].getHeight() + 20 > this.desktop.getHeight()) {
			y -= this.ui[0].getHeight() + this.spacing.y + 8;
		}

		this.setLocation(x, y);
	}

	public LComponent getToolTipComponent() {
		return this.tooltip;
	}

	public void setToolTipComponent(LComponent tooltip) {
		if (tooltip != null) {
			if (tooltip.getToolTipParent() != null) {
				tooltip = tooltip.getToolTipParent();
			}

			if (tooltip.getToolTipText() == null) {
				tooltip = null;
			}
		}
		if (this.tooltip == tooltip) {
			return;
		}

		this.tooltip = tooltip;
		this.tooltipChanged = true;

		if (!this.isVisible()) {
			this.initial = 0;
		}
	}

	public int getInitialDelay() {
		return this.initialDelay;
	}

	public void setInitialDelay(int i) {
		this.initialDelay = i;
	}

	public int getDismissDelay() {
		return this.dismissDelay;
	}

	public void setDismissDelay(int i) {
		this.dismissDelay = i;
	}

	public int getReshowDelay() {
		return this.reshowDelay;
	}

	public void setReshowDelay(int i) {
		this.reshowDelay = i;
	}

	public Point getSpacing() {
		return this.spacing;
	}

	public void setSpacing(int x, int y) {
		this.spacing.x = x;
		this.spacing.y = y;
	}

}
