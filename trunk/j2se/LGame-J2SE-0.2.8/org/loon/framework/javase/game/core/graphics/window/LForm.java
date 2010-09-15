package org.loon.framework.javase.game.core.graphics.window;

import org.loon.framework.javase.game.core.graphics.LComponent;
import org.loon.framework.javase.game.core.graphics.LContainer;

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

public class LForm extends LContainer {

	private TTitleBar titleBar;

	private LContainer contentPane;

	private boolean boolFlag;

	private int distance = -2;

	public LForm(String title, boolean closable, boolean iconifiable, int x,
			int y, int w, int h) {
		super(x, y, w, h);

		this.titleBar = new TTitleBar(title, closable, iconifiable, 0, 0, w, 25);
		this.contentPane = new LPanel(0, 0, w, h - 25 - this.distance);

		this.addAccessory(this.titleBar);
		this.addAccessory(this.contentPane);
		this.relayout();

		this.setElastic(true);
		this.setLayer(100);
	}

	public void add(LComponent comp) {
		this.contentPane.add(comp);
	}

	public void addAccessory(LComponent comp) {
		super.add(comp);
	}

	public String getTitle() {
		return this.titleBar.getTitle();
	}

	public void setTitle(String title) {
		this.titleBar.setTitle(title);
	}

	public void relayout() {
		this.titleBar.setLocation(0, 0);
		this.contentPane.setLocation(0, this.titleBar.getHeight()
				+ this.distance);
		this.setSize(this.contentPane.getWidth(), this.titleBar.getHeight()
				+ this.distance + this.contentPane.getHeight());
	}

	protected void validateSize() {
		super.validateSize();
		this.titleBar.setSize(this.getWidth(), this.titleBar.getHeight());

		if (this.boolFlag) {
			// 不显示
			this.contentPane.setVisible(false);
		} else {
			// 显示
			this.contentPane.setVisible(true);
			this.contentPane.setSize(this.getWidth(), this.getHeight()
					- (this.titleBar.getHeight() + this.distance));
		}
	}

	public boolean isForm() {
		return this.boolFlag;
	}

	/**
	 * 是否显示浮动面板
	 * 
	 * @param b
	 */
	public void setForm(boolean b) {
		if (this.boolFlag == b) {
			return;
		}

		this.boolFlag = b;

		int w = this.getWidth(), h = this.titleBar.getHeight();
		if (!this.boolFlag) {
			h += this.contentPane.getHeight() + this.distance;
		}

		this.setSize(w, h);

	}

	public int getPaneDistance() {
		return this.distance;
	}

	public void setPaneDistance(int i) {
		this.distance = i;
		this.relayout();
	}

	public TTitleBar getTitleBar() {
		return this.titleBar;
	}

	public void setTitleBar(TTitleBar bar) {
		this.replace(this.titleBar, bar);
		this.titleBar = bar;
		this.relayout();
	}

	public LContainer getContentPane() {
		return this.contentPane;
	}

	public void setContentPane(LContainer container) {
		this.replace(this.contentPane, container);
		this.contentPane = container;
		this.relayout();
	}

	public String getUIName() {
		return "Form";
	}

	public class TTitleBar extends LContainer {

		private String title = "";

		private TTitleBarButton close, iconified;

		public TTitleBar(String title, boolean closable, boolean iconifiable,
				int x, int y, int w, int h) {
			super(x, y, w, h);
			this.title = title;
			this.close = new TTitleBarButton(TTitleBarButton.CLOSE_BUTTON,
					0, 0, 20, h - 5);
			this.iconified = new TTitleBarButton(
					TTitleBarButton.ICONIFIED_BUTTON, 0, 0, 20, h - 5);
			if (!closable) {
				this.close.setVisible(false);
			}
			if (!iconifiable) {
				this.iconified.setVisible(false);
			}

			this.add(this.close);
			this.add(this.iconified);
			this.relayout();
		}

		protected void processMouseDragged() {
			System.out.println(this.input.getMouseX());
			//if (this.input.isMouseDown(MouseEvent.BUTTON1)) {
			if (LForm.this.getContainer() != null) {
				LForm.this.getContainer().sendToFront(LForm.this);
			}
			LForm.this.move(this.input.getMouseDX(), this.input
					.getMouseDY());
			//}
		}

		public void relayout() {
			this.close.setLocation(this.getWidth() - 22, 2);
			// 当关闭按钮不存在时
			this.iconified.setLocation(
					this.close.isVisible() ? this.getWidth() - 43 : this
							.getWidth() - 22, 2);
		}

		protected void validateSize() {
			super.validateSize();

			this.close.setSize(20, this.getHeight() - 5);
			this.iconified.setSize(20, this.getHeight() - 5);
		}

		public boolean isClosable() {
			return this.close.isVisible();
		}

		public void setClosable(boolean b) {
			this.close.setVisible(b);
			this.relayout();
		}

		public boolean isIconifiable() {
			return this.iconified.isVisible();
		}

		public void setIconifiable(boolean b) {
			this.iconified.setVisible(b);
			this.relayout();
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String st) {
			this.title = st;
			this.createUI();
		}

		public TTitleBarButton getCloseButton() {
			return this.close;
		}

		public void setCloseButton(TTitleBarButton btn) {
			this.replace(this.close, btn);
			this.close = btn;
			this.relayout();
		}

		public TTitleBarButton getIconifiedButton() {
			return this.iconified;
		}

		public void setIconifiedButton(TTitleBarButton btn) {
			this.replace(this.iconified, btn);
			this.iconified = btn;
			this.relayout();
		}

		public String getUIName() {
			return "Panel.TitleBar";
		}

		public class TTitleBarButton extends LTool {

			public static final int CLOSE_BUTTON = 1;

			public static final int ICONIFIED_BUTTON = 2;

			private int action;

			public TTitleBarButton(int action, int x, int y,
					int w, int h) {
				super(x, y, w, h);

				this.action = action;

				switch (action) {
				case CLOSE_BUTTON:
					this.setToolTipText("关闭窗体");
					break;
				case ICONIFIED_BUTTON:
					this.setToolTipText((LForm.this.isForm()) ? "还原窗体"
							: "隐藏窗体");
					break;
				}
			}

			public void doClick() {
				switch (this.action) {
				case CLOSE_BUTTON:
					LForm.this.setVisible(false);
					break;
				case ICONIFIED_BUTTON:
					LForm.this.setForm(!LForm.this.isForm());
					this.createUI();
					break;
				}
			}

			public int getAction() {
				return this.action;
			}

			public String getUIName() {
				return "Tool.TitleBarButton";
			}
		}
	}

}
