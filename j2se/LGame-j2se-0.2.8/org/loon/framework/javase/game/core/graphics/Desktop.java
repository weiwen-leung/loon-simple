package org.loon.framework.javase.game.core.graphics;

import java.awt.event.MouseEvent;

import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.window.LPanel;
import org.loon.framework.javase.game.core.graphics.window.LToolTip;
import org.loon.framework.javase.game.core.graphics.window.LUI;
import org.loon.framework.javase.game.core.graphics.window.UIConfig;
import org.loon.framework.javase.game.core.graphics.window.UIFactory;

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
public class Desktop {

	// 空桌面布局
	public static final Desktop EMPTY_DESKTOP = new Desktop();

	// 输入设备监听
	protected final LInput input;

	private LContainer contentPane;

	private LToolTip tooltip;

	private LComponent modal;

	private LComponent hoverComponent;

	private LComponent selectedComponent;

	private LComponent[] clickComponent = new LComponent[3];

	private UIConfig uiConfig;

	/**
	 * 构造一个可用桌面
	 * 
	 * @param input
	 * @param width
	 * @param height
	 */
	public Desktop(LInput input, int width, int height) {
		this.contentPane = new LPanel(0, 0, width, height);
		this.input = input;
		this.uiConfig = new LUI();
		this.tooltip = new LToolTip();
		this.contentPane.add(this.tooltip);
		this.setDesktop(this.contentPane);
	}

	/**
	 * 空桌面布局
	 * 
	 */
	private Desktop() {
		this.contentPane = new LPanel(0, 0, 1, 1);
		this.input = null;
		this.uiConfig = new UIConfig();
		this.setDesktop(this.contentPane);
	}

	public void add(LComponent comp) {
		if (comp == null) {
			return;
		}
		if (comp.isFull) {
			this.input.setShakeNumber(0);
		} else {
			this.input.setShakeNumber(-1);
		}
		this.contentPane.add(comp);
		this.processMouseMotionEvent();
	}

	public int remove(LComponent comp) {
		int removed = this.removeComponent(this.contentPane, comp);
		if (removed != -1) {
			this.processMouseMotionEvent();
		}
		return removed;
	}

	private int removeComponent(LContainer container, LComponent comp) {
		int removed = container.remove(comp);
		LComponent[] components = container.getComponents();
		int i = 0;
		while (removed == -1 && i < components.length - 1) {
			if (components[i].isContainer()) {
				removed = this
						.removeComponent((LContainer) components[i], comp);
			}
			i++;
		}

		return removed;
	}

	/**
	 * 刷新当前桌面
	 * 
	 */
	public void update(long timer) {
		if (!this.contentPane.isVisible()) {
			return;
		}
		this.processEvents();
		// 刷新桌面中子容器组件
		this.contentPane.update(timer);
	}

	public void changeText(String text) {
		if (!this.contentPane.isVisible()) {
			return;
		}
		this.contentPane.changeText(text);
	}

	public void createUI(LGraphics g) {
		this.contentPane.createUI(g);
	}

	/**
	 * 事件监听
	 * 
	 */
	private void processEvents() {
		// 鼠标滑动
		this.processMouseMotionEvent();
		// 鼠标事件
		if (this.hoverComponent != null && this.hoverComponent.isEnabled()) {
			this.processMouseEvent();
		}
		// 键盘事件
		if (this.selectedComponent != null
				&& this.selectedComponent.isEnabled()) {
			this.processKeyEvent();
		}
	}

	/**
	 * 鼠标运动事件
	 * 
	 */
	private void processMouseMotionEvent() {
		if ((this.hoverComponent != null && this.hoverComponent.isEnabled())
				&& (this.input.isMouseDown(MouseEvent.BUTTON1)
						|| this.input.isMouseDown(MouseEvent.BUTTON2) || this.input
						.isMouseDown(MouseEvent.BUTTON3))) {

			if (this.input.getMouseDX() != 0 || this.input.getMouseDY() != 0) {
				this.hoverComponent.processMouseDragged();
			}

		} else {
			// 获得当前窗体下鼠标坐标
			LComponent comp = this.findComponent(this.input.getMouseX(),
					this.input.getMouseY());

			if (comp != null) {
				if (this.input.getMouseDX() != 0
						|| this.input.getMouseDY() != 0) {
					comp.processMouseMoved();
					// 刷新提示
					this.tooltip.dismiss = 0;
				}

				if (this.hoverComponent == null) {
					this.tooltip.setToolTipComponent(comp);
					comp.processMouseEntered();

				} else if (comp != this.hoverComponent) {
					this.tooltip.setToolTipComponent(comp);
					this.hoverComponent.processMouseExited();
					comp.processMouseEntered();
				}

				// 如果没有对应的悬停提示数据
			} else {
				this.tooltip.setToolTipComponent(null);
				if (this.hoverComponent != null) {
					this.hoverComponent.processMouseExited();
				}
			}

			// 设置组件悬停提示状态
			this.hoverComponent = comp;
		}
	}

	/**
	 * 鼠标按下事件
	 * 
	 */
	private void processMouseEvent() {
		int pressed = this.input.getMousePressed(), released = this.input
				.getMouseReleased();
		if (pressed != LInput.NO_BUTTON) {
			this.tooltip.setToolTipComponent(null);
			this.tooltip.reshow = 0;
			this.tooltip.initial = 0;
			this.hoverComponent.processMousePressed();
			this.clickComponent[pressed - 1] = this.hoverComponent;
			// 设定悬停状态
			if (this.hoverComponent.isFocusable()) {
				if (pressed == MouseEvent.BUTTON1
						&& this.hoverComponent != this.selectedComponent) {
					this.selectComponent(this.hoverComponent);
				}
			}
		}
		if (released != LInput.NO_BUTTON) {
			this.hoverComponent.processMouseReleased();
			// 当释放鼠标时，点击事件生效
			if (this.clickComponent[released - 1] == this.hoverComponent) {
				this.hoverComponent.processMouseClicked();
			}
		}
	}

	/**
	 * 触发键盘事件
	 * 
	 */
	private void processKeyEvent() {
		if (this.input.getKeyPressed() != LInput.NO_KEY) {
			this.selectedComponent.keyPressed();
		}

		if (this.input.getKeyReleased() != LInput.NO_KEY
				&& this.selectedComponent != null) {
			this.selectedComponent.processKeyReleased();
		}
	}

	/**
	 * 查找指定坐标点成员
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private LComponent findComponent(int x, int y) {
		if (this.modal != null && !this.modal.isContainer()) {
			return null;
		}
		// 返回子容器
		LContainer panel = (this.modal == null) ? this.contentPane
				: ((LContainer) this.modal);
		LComponent comp = panel.findComponent(x, y);

		return comp;
	}

	public void clearFocus() {
		this.deselectComponent();
	}

	void deselectComponent() {
		if (this.selectedComponent == null) {
			return;
		}
		this.selectedComponent.setSelected(false);
		this.selectedComponent = null;
	}

	/**
	 * 查找指定容器
	 * 
	 * @param comp
	 * @return
	 */
	boolean selectComponent(LComponent comp) {
		if (!comp.isVisible() || !comp.isFocusable() || !comp.isEnabled()) {
			return false;
		}

		// 清除最后部分
		this.deselectComponent();

		// 设定选中状态
		comp.setSelected(true);
		this.selectedComponent = comp;

		return true;
	}

	void setDesktop(LComponent comp) {
		if (comp.isContainer()) {
			LComponent[] child = ((LContainer) comp).getComponents();
			for (int i = 0; i < child.length; i++) {
				this.setDesktop(child[i]);
			}
		}
		comp.setDesktop(this);
	}

	void setComponentStat(LComponent comp, boolean active) {
		if (this == Desktop.EMPTY_DESKTOP) {
			return;
		}

		if (active == false) {
			if (this.hoverComponent == comp) {
				this.processMouseMotionEvent();
			}

			if (this.selectedComponent == comp) {
				this.deselectComponent();
			}

			for (int i = 0; i < this.clickComponent.length; i++) {
				if (this.clickComponent[i] == comp) {
					this.clickComponent[i] = null;
					break;
				}
			}

			if (this.modal == comp) {
				this.modal = null;
			}

		} else {
			this.processMouseMotionEvent();
		}

		if (comp.isContainer()) {
			LComponent[] components = ((LContainer) comp).getComponents();
			int size = ((LContainer) comp).getComponentCount();
			for (int i = 0; i < size; i++) {
				this.setComponentStat(components[i], active);
			}
		}
	}

	void clearComponentsStat(LComponent[] comp) {
		if (this == Desktop.EMPTY_DESKTOP) {
			return;
		}

		boolean checkMouseMotion = false;
		for (int i = 0; i < comp.length; i++) {
			if (this.hoverComponent == comp[i]) {
				checkMouseMotion = true;
			}

			if (this.selectedComponent == comp[i]) {
				this.deselectComponent();
			}

			for (int j = 0; j < this.clickComponent.length; j++) {
				if (this.clickComponent[j] == comp[i]) {
					this.clickComponent[j] = null;
					break;
				}
			}
		}

		if (checkMouseMotion) {
			this.processMouseMotionEvent();
		}
	}

	public void validateUI() {
		this.validateContainer(this.contentPane);
	}

	final void validateContainer(LContainer container) {
		if (container.getUIResource().size() > 0) {
			container.createUI();
		}

		LComponent[] components = container.getComponents();
		int size = container.getComponentCount();
		for (int i = 0; i < size; i++) {
			if (components[i].getUIResource().size() > 0) {
				components[i].createUI();
			}
			if (components[i].isContainer()) {
				this.validateContainer((LContainer) components[i]);
			}
		}
	}

	public int getWidth() {
		return this.contentPane.getWidth();
	}

	public int getHeight() {
		return this.contentPane.getHeight();
	}

	public void setSize(int w, int h) {
		this.contentPane.setSize(w, h);
	}

	public LContainer getContentPane() {
		return this.contentPane;
	}

	public void setContentPane(LContainer pane) {
		pane.setBounds(0, 0, this.getWidth(), this.getHeight());

		this.contentPane = pane;
		this.setDesktop(this.contentPane);
	}

	public LComponent getHoverComponent() {
		return this.hoverComponent;
	}

	public LComponent getSelectedComponent() {
		return this.selectedComponent;
	}

	public LComponent getModal() {
		return this.modal;
	}

	public void setModal(LComponent comp) {
		if (comp != null && !comp.isVisible()) {
			throw new RuntimeException(
					"Can't set invisible component as modal component!");
		}
		this.modal = comp;
	}

	public LComponent get() {
		return this.contentPane.get();
	}

	public UIConfig getUIConfig() {
		return this.uiConfig;
	}

	public void installUI(UIConfig newConfig) {
		UIFactory[] ui = this.uiConfig.getInstalledUI();
		for (int i = 0; i < ui.length; i++) {
			if (newConfig.getUIFactory(ui[i].getUIName()) == null
					|| ui[i].immutable) {
				// 重新部署ui配置
				newConfig.setupUI(ui[i]);
			}
		}

		this.uiConfig = newConfig;

		this.installUI(this.contentPane);
	}

	private void installUI(LComponent comp) {
		comp.setUIRenderer(this.uiConfig.createUIFactory(comp.getUIName()));
		if (comp.isContainer()) {
			LComponent[] childs = ((LContainer) comp).getComponents();
			for (int i = 0; i < childs.length; i++) {
				this.installUI(childs[i]);
			}
		}
	}

	public LToolTip getToolTip() {
		return this.tooltip;
	}

	public void setToolTip(LToolTip tip) {
		this.contentPane.replace(this.tooltip, tip);
		this.tooltip = tip;
	}

	protected void finalize() throws Throwable {
		super.finalize();
	}

}
