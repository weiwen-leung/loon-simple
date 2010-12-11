package org.loon.framework.javase.game.core.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.loon.framework.javase.game.GameManager;
import org.loon.framework.javase.game.action.Action;
import org.loon.framework.javase.game.action.IAction;
import org.loon.framework.javase.game.action.MoveAction;
import org.loon.framework.javase.game.action.sprite.ISprite;
import org.loon.framework.javase.game.action.sprite.ISpriteListener;
import org.loon.framework.javase.game.action.sprite.Sprites;
import org.loon.framework.javase.game.core.EmulatorButtons;
import org.loon.framework.javase.game.core.EmulatorListener;
import org.loon.framework.javase.game.core.IHandler;
import org.loon.framework.javase.game.core.LInput;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.window.awt.AWTInputDialog;
import org.loon.framework.javase.game.core.graphics.window.awt.AWTMessageDialog;
import org.loon.framework.javase.game.core.graphics.window.awt.AWTOpenDialog;
import org.loon.framework.javase.game.core.graphics.window.awt.AWTYesNoCancelDialog;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.media.flv.FLVEngine;
import org.loon.framework.javase.game.media.sound.SoundBox;
import org.loon.framework.javase.game.utils.FileUtils;
import org.loon.framework.javase.game.utils.GraphicsUtils;
import org.loon.framework.javase.game.utils.log.Level;
import org.loon.framework.javase.game.utils.log.Log;
import org.loon.framework.javase.game.utils.log.LogFactory;

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
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1.1
 */
public abstract class Screen extends SoundBox implements IScreen, LInput {

	private int mouseX, mouseY, lastMouseX, lastMouseY, mouseDX, mouseDY;

	private Thread videoThread;

	private FLVEngine flvVideo;

	private int mode;

	private boolean mouseExists, isNext, isVideo, isComponents;

	private static boolean[] mouseDown, keyDown;

	private static int[] mousePressed, mouseReleased;

	private static int[] keyPressed, keyReleased;

	private static Map keyMap, mouseMap;

	private int pressedMouse, releasedMouse;

	private int pressedKey, releasedKey;

	private LKeyTyped keyTyped;

	protected long fps = 0, elapsedTime;

	private Point mouse = new Point(0, 0);

	private LInput baseInput;

	private IHandler handler;

	// 精灵集合
	private Sprites sprites;

	// 桌面集合
	private Desktop desktop;

	// 背景屏幕
	private BufferedImage currentScreen;

	// 线程事件集合
	private final ArrayList runnables;

	private int tmp_width = 1, tmp_height = 1;

	private final Log log;

	private int id;

	private static class ThreadID {

		private static int nextThreadID = 1;

		private static ThreadLocal threadID = new ThreadLocal() {
			protected synchronized Object initialValue() {
				return new Integer(nextThreadID++);
			}
		};

		public static int get() {
			return ((Integer) (threadID.get())).intValue();
		}

	}

	static {
		keyDown = new boolean[255];
		keyPressed = keyReleased = new int[255];
		keyMap = new HashMap(255);
		mouseDown = new boolean[8];
		mousePressed = mouseReleased = new int[8];
		mouseMap = new HashMap(8);
	}

	/**
	 * 构造函数，初始化游戏屏幕
	 * 
	 */
	public Screen() {
		LSystem.AUTO_REPAINT = true;
		this.handler = GameManager.getSystemHandler();
		this.baseInput = this;
		this.log = LogFactory.getInstance(this.getClass());
		this.resize();
		this.setFPS(getMaxFPS());
		this.mode = SCREEN_CANVAS_REPAINT;
		this.runnables = new ArrayList(1);
		this.pressedKey = this.releasedKey = 0;
		this.keyTyped = new LKeyTyped(this);
		this.mouseExists = true;
		this.pressedMouse = this.releasedMouse = 0;
		this.mouseX = this.mouseY = this.lastMouseX = this.lastMouseY = this.mouseDX = this.mouseDY = 0;
		this.isNext = true;
	}

	/**
	 * 设定模拟按钮监听器
	 */
	public void setEmulatorListener(EmulatorListener emulator) {
		if (handler.getDeploy() != null) {
			handler.getDeploy().setEmulatorListener(emulator);
		}
	}

	/**
	 * 返回模拟按钮集合
	 * 
	 * @return
	 */
	public EmulatorButtons getEmulatorButtons() {
		if (handler.getDeploy() != null) {
			return handler.getDeploy().getEmulatorButtons();
		}
		return null;
	}

	/**
	 * 设定模拟按钮组是否显示
	 * 
	 * @param visible
	 */
	public void emulatorButtonsVisible(boolean visible) {
		if (handler.getDeploy() != null) {
			try {
				EmulatorButtons es = handler.getDeploy().getEmulatorButtons();
				es.setVisible(visible);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 增减一个线程事件
	 * 
	 * @param runnable
	 */
	public final void callEvent(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
		}
	}

	/**
	 * 暂停指定的线程事件
	 * 
	 * @param runnable
	 */
	public final void callEventWait(Runnable runnable) {
		synchronized (runnable) {
			synchronized (runnables) {
				runnables.add(runnable);
			}
			try {
				runnable.wait();
			} catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * 中断所有线程事件
	 * 
	 */
	public final void callEventInterrupt() {
		synchronized (runnables) {
			for (Iterator it = runnables.iterator(); it.hasNext();) {
				Object running = it.next();
				synchronized (running) {
					if (running instanceof Thread) {
						((Thread) running).setPriority(Thread.MIN_PRIORITY);
						((Thread) running).interrupt();
					}
				}
			}
		}
	}

	/**
	 * 运行线程事件
	 * 
	 */
	public final void callEvents() {
		callEvents(true);
	}

	/**
	 * 执行或中断指定的线程事件
	 * 
	 * @param execute
	 */
	private final void callEvents(boolean execute) {
		if (!execute) {
			synchronized (runnables) {
				runnables.clear();
			}
			return;
		}
		if (runnables.size() == 0) {
			return;
		}
		ArrayList runnableList;
		synchronized (runnables) {
			runnableList = new ArrayList(runnables);
			runnables.clear();
		}
		for (Iterator it = runnableList.iterator(); it.hasNext();) {
			Object running = it.next();
			synchronized (running) {
				try {
					if (running instanceof Thread) {
						Thread thread = (Thread) running;
						if (!thread.isAlive()) {
							thread.start();
						}

					} else {
						((Runnable) running).run();
					}
				} catch (Exception ex) {
				}
				running.notifyAll();
			}
		}
		runnableList = null;
	}

	/**
	 * 初始化时加载的数据
	 */
	public void onLoad() {

	}

	/**
	 * 获得当前Screen类名
	 */
	public String getName() {
		return FileUtils.getExtension(getClass().getName());
	}

	/**
	 * 返回当前窗体线程ID
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * 变更窗体匹配的图像组件大小
	 * 
	 * @param w
	 * @param h
	 */
	public void resize() {
		this.id = ThreadID.get();
		if (handler != null) {
			int w = handler.getWidth(), h = handler.getHeight();
			if (w < 1 || h < 1) {
				w = h = 1;
			}
			if (w != tmp_width || h != tmp_height) {
				tmp_width = w;
				tmp_height = h;
			} else {
				Thread.yield();
				return;
			}
		}
		this.setBackground(GraphicsUtils.createIntdexedImage(tmp_width,
				tmp_height));
		this.flvVideo = new FLVEngine(tmp_width, tmp_height);
		this.sprites = new Sprites(tmp_width, tmp_height);
		this.desktop = new Desktop(baseInput, tmp_width, tmp_height);
	}

	/**
	 * 注销占用的资源
	 * 
	 */
	public void destroy() {
		this.callEvents(false);
		this.isNext = false;
		this.videoThread = null;
		this.currentScreen = null;
		this.flvVideo = null;
		this.sprites = null;
		this.desktop = null;
		this.dispose();
	}

	/**
	 * 返回精灵监听
	 * 
	 * @return
	 */
	public ISpriteListener getSprListerner() {
		if (sprites == null) {
			return null;
		}
		return sprites.getSprListerner();
	}

	/**
	 * 监听Screen中精灵
	 * 
	 * @param sprListerner
	 */
	public void setSprListerner(ISpriteListener sprListerner) {
		if (sprites == null) {
			return;
		}
		sprites.setSprListerner(sprListerner);
	}

	/**
	 * 释放函数内资源
	 * 
	 */
	public void dispose() {

	}

	/**
	 * 记录info信息
	 * 
	 * @param message
	 */
	public void info(String message) {
		log.info(message);
	}

	/**
	 * 记录info信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void info(String message, Throwable tw) {
		log.info(message, tw);
	}

	/**
	 * 记录debug信息
	 * 
	 * @param message
	 */
	public void debug(String message) {
		log.debug(message);
	}

	/**
	 * 记录debug信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void debug(String message, Throwable tw) {
		log.debug(message, tw);
	}

	/**
	 * 记录warn信息
	 * 
	 * @param message
	 */
	public void warn(String message) {
		log.warn(message);
	}

	/**
	 * 记录warn信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void warn(String message, Throwable tw) {
		log.warn(message, tw);
	}

	/**
	 * 记录error信息
	 * 
	 * @param message
	 */
	public void error(String message) {
		log.error(message);
	}

	/**
	 * 记录error信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void error(String message, Throwable tw) {
		log.error(message, tw);
	}

	/**
	 * 输出日志
	 * 
	 * @param message
	 */
	public void log(String message) {
		log.log(message);
	}

	/**
	 * 输出日志
	 * 
	 * @param message
	 * @param tw
	 */
	public void log(String message, Throwable tw) {
		log.log(message, tw);
	}

	/**
	 * 设定是否在控制台显示日志
	 * 
	 * @param show
	 */
	public void logShow(boolean show) {
		log.setVisible(show);
	}

	/**
	 * 设定日志是否保存为文件
	 * 
	 * @param save
	 */
	public void logSave(boolean save) {
		log.setSave(save);
	}

	/**
	 * 日志保存地点
	 * 
	 * @param fileName
	 */
	public void logFileName(String fileName) {
		log.setFileName(fileName);
	}

	/**
	 * 设定日志等级
	 * 
	 * @param level
	 */
	public void logLevel(int level) {
		log.setLevel(level);
	}

	/**
	 * 设定日志等级
	 * 
	 * @param level
	 */
	public void logLevel(Level level) {
		log.setLevel(level);
	}

	/**
	 * 判断是否点中指定精灵
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean onClick(ISprite sprite) {
		return sprite.isVisible()
				&& (sprite.getCollisionBox().contains(mouseX, mouseY));
	}

	/**
	 * 判断是否点中指定组件
	 * 
	 * @param component
	 * @return
	 */
	public boolean onClick(LComponent component) {
		return component.isVisible()
				& (component.getCollisionBox().contains(mouseX, mouseY));
	}

	/**
	 * 将鼠标居中
	 * 
	 */
	public void mouseCenter() {
		try {
			GraphicsDevice device = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			DisplayMode mode = device.getDisplayMode();
			this.mouseX = this.lastMouseX = (mode.getWidth() / 2) - 10;
			this.mouseY = this.lastMouseY = (mode.getHeight() / 2) - 10;
			LSystem.RO_BOT.mouseMove(this.mouseX, this.mouseY);
		} catch (Exception e) {
		}
	}

	/**
	 * 弹出一个AWT输入框
	 * 
	 * @param title
	 * @param message
	 */
	public AWTInputDialog showAWTInputDialog(final String title,
			final String message) {
		final AWTInputDialog dialog = new AWTInputDialog(title, message);
		callEvent(new Thread() {
			public void run() {
				dialog.setVisible(true);
			}
		});
		return dialog;
	}

	/**
	 * 弹出一个AWT提示框
	 * 
	 * @param title
	 * @param message
	 */
	public AWTMessageDialog showAWTMessageDialog(final String title,
			final String message) {
		final AWTMessageDialog dialog = new AWTMessageDialog(title, message);
		callEvent(new Thread() {
			public void run() {
				dialog.setVisible(true);
			}
		});
		return dialog;
	}

	/**
	 * 弹出一个AWT选择框
	 * 
	 * @param title
	 * @param message
	 */
	public AWTYesNoCancelDialog showAWTYesNoCancelDialog(String title,
			String message) {
		final AWTYesNoCancelDialog dialog = new AWTYesNoCancelDialog(title,
				message);
		callEvent(new Thread() {
			public void run() {
				dialog.setVisible(true);
			}
		});
		return dialog;
	}

	/**
	 * 弹出一个AWT文件选择框
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public AWTOpenDialog showAWTOpenDialog(String title, String path) {
		final AWTOpenDialog dialog = new AWTOpenDialog(title, path);
		callEvent(new Thread() {
			public void run() {
				dialog.setVisible(true);
			}
		});
		return dialog;
	}

	/**
	 * 设定游戏句柄
	 */
	public synchronized void setupHandler(IHandler handler) {
		this.handler = handler;
	}

	/**
	 * 返回当前游戏句柄
	 * 
	 * @return
	 */
	public synchronized IHandler getHandler() {
		return handler;
	}

	/**
	 * 设置主窗体小图标
	 * 
	 * @param image
	 */
	public void setFrameIcon(Image icon) {
		if (handler != null) {
			handler.getScene().setIconImage(icon);
		}
	}

	/**
	 * 设置主窗体小图标
	 */
	public void setFrameIcon(String fileName) {
		if (handler != null) {
			handler.getScene().setIconImage(fileName);
		}
	}

	/**
	 * 设置主窗口标题
	 * 
	 * @param title
	 */
	public void setFrameTitle(String title) {
		if (handler != null) {
			handler.getScene().setTitle(title);
		}
	}

	/**
	 * 设定游戏屏幕
	 * 
	 * @param screen
	 */
	public synchronized void setScreen(IScreen screen) {
		if (handler != null) {
			this.destroy();
			screen.setupHandler(handler);
			this.handler.setScreen(screen);
		}
	}

	/**
	 * 设定刷新率
	 * 
	 * @param fps
	 */
	public void setFPS(long fps) {
		if (handler != null) {
			handler.getDeploy().getView().setFPS(fps);
		}
	}

	/**
	 * 返回刷新率
	 */
	public long getFPS() {
		if (handler != null) {
			return handler.getDeploy().getView().getCurrentFPS();
		}
		return 0;
	}

	/**
	 * 返回最大刷新率
	 */
	public long getMaxFPS() {
		if (handler != null) {
			return handler.getDeploy().getView().getMaxFPS();
		}
		return 0;
	}

	/**
	 * 添加键盘事件
	 * 
	 * @param keyCode
	 * @param action
	 */
	public void addKeyEvents(int keyCode, String key, IAction action) {
		addKeyEvents(keyCode, new Action(action, key));
	}

	/**
	 * 添加键盘事件
	 * 
	 * @param keyCode
	 * @param action
	 */
	public void addKeyEvents(int keyCode, Action action) {
		keyMap.put(String.valueOf(keyCode), action);
	}

	/**
	 * 删除键盘事件
	 * 
	 * @param keyCode
	 */
	public void removeKeyEvents(int keyCode) {
		keyMap.remove(String.valueOf(keyCode));
	}

	/**
	 * 清空键盘事件
	 * 
	 */
	public void clearKeyEvents() {
		keyMap.clear();
	}

	/**
	 * 添加鼠标事件
	 * 
	 * @param mouseCode
	 * @param action
	 */
	public void addMouseEvents(int mouseCode, String key, IAction action) {
		addMouseEvents(mouseCode, new Action(action, key));
	}

	/**
	 * 添加鼠标事件
	 * 
	 * @param mouseCode
	 * @param action
	 */
	public void addMouseEvents(int mouseCode, Action action) {
		mouseMap.put(String.valueOf(mouseCode), action);
	}

	/**
	 * 删除鼠标事件
	 * 
	 * @param mouseCode
	 */
	public void removeMouseEvents(int mouseCode) {
		mouseMap.remove(String.valueOf(mouseCode));
	}

	/**
	 * 清空鼠标事件
	 * 
	 */
	public void clearMouseEvents() {
		mouseMap.clear();
	}

	public int getWidth() {
		return tmp_width;
	}

	public int getHeight() {
		return tmp_height;
	}

	public LInput getInput() {
		return baseInput;
	}

	public void setInput(LInput input) {
		this.baseInput = input;
	}

	public Point getMouse() {
		mouse.setLocation(mouseX, mouseY);
		return mouse;
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public Sprites getSprites() {
		return sprites;
	}

	/**
	 * 添加游戏组件
	 * 
	 * @param comp
	 */
	public void add(LComponent comp) {
		if (desktop != null) {
			desktop.add(comp);
		}
	}

	/**
	 * 添加游戏精灵
	 * 
	 * @param sprite
	 */
	public void add(ISprite sprite) {
		if (sprites != null) {
			sprites.add(sprite);
		}
	}

	public synchronized void remove(LComponent comp) {
		if (desktop != null) {
			desktop.remove(comp);
		}
	}

	public synchronized void remove(ISprite sprite) {
		if (sprites != null) {
			sprites.remove(sprite);
		}
	}

	public synchronized void removeAll() {
		if (sprites != null) {
			sprites.removeAll();
		}
		if (desktop != null) {
			desktop.getContentPane().clear();
		}
	}

	public void centerOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				/ 2 - object.getHeight() / 2);
	}

	public void topOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, 0);
	}

	public void leftOn(final LObject object) {
		object.setLocation(0, getHeight() / 2 - object.getHeight() / 2);
	}

	public void rightOn(final LObject object) {
		object.setLocation(getWidth() - object.getWidth(), getHeight() / 2
				- object.getHeight() / 2);
	}

	public void bottomOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				- object.getHeight());
	}

	public boolean openBrowser(String url) {
		return LSystem.openBrowser(url);
	}

	public int getRepaintMode() {
		return mode;
	}

	public void setRepaintMode(int mode) {
		this.mode = mode;
	}

	/**
	 * 刷新鼠标及键盘数据
	 */
	public void update(long timer) {
		this.keyTyped.update(timer);
		this.pressedMouse = this.releasedMouse = this.pressedKey = this.releasedKey = 0;
		this.mouseDX = this.mouseX - this.lastMouseX;
		this.mouseDY = this.mouseY - this.lastMouseY;
		this.lastMouseX = this.mouseX;
		this.lastMouseY = this.mouseY;
	}

	/**
	 * 刷新基础设置
	 */
	public void refresh() {
		this.keyTyped.refresh();
		for (int i = 0; i < mouseDown.length; i++) {
			mouseDown[i] = false;
		}
		this.pressedMouse = this.releasedMouse = 0;
		this.mouseDX = this.mouseDY = 0;
		for (int i = 0; i < keyDown.length; i++) {
			keyDown[i] = false;
		}
		this.pressedKey = this.releasedKey = 0;
	}

	/**
	 * 鼠标移动
	 */
	public synchronized void mouseMove(int x, int y) {
		LSystem.RO_BOT.mouseMove(x, y);
	}

	public boolean isMouseExists() {
		return this.mouseExists;
	}

	public int getMouseX() {
		return this.mouseX;
	}

	public int getMouseY() {
		return this.mouseY;
	}

	public int getMouseDX() {
		return this.mouseDX;
	}

	public int getMouseDY() {
		return this.mouseDY;
	}

	public boolean[] getMouseDown() {
		return mouseDown;
	}

	public boolean isMouseDown(int button) {
		return mouseDown[button];
	}

	public boolean isClick() {
		return mouseDown[MouseEvent.BUTTON1];
	}

	public int getKeyPressed() {
		return (this.pressedKey > 0) ? keyPressed[0] : LInput.NO_KEY;
	}

	public boolean isKeyPressed(int keyCode) {
		for (int i = 0; i < this.pressedKey; i++) {
			if (keyPressed[i] == keyCode) {
				return true;
			}
		}
		return false;
	}

	public int getKeyReleased() {
		return (this.releasedKey > 0) ? keyReleased[0] : LInput.NO_KEY;
	}

	public boolean isKeyReleased(int keyCode) {
		for (int i = 0; i < this.releasedKey; i++) {
			if (keyReleased[i] == keyCode) {
				return true;
			}
		}
		return false;
	}

	public boolean[] getKeyDown() {
		return keyDown;
	}

	public boolean isKeyDown(int keyCode) {
		return keyDown[keyCode & 0xFF];
	}

	public int getKeyTyped() {
		return this.keyTyped.getKeyTyped();
	}

	public boolean isKeyTyped(int keyCode) {
		return this.keyTyped.isKeyTyped(keyCode);
	}

	public long getRepeatDelay() {
		return this.keyTyped.getRepeatDelay();
	}

	/**
	 * 设定重复延迟
	 * 
	 * @param delay
	 */
	public void setRepeatDelay(long delay) {
		this.keyTyped.setRepeatDelay(delay);
	}

	public long getRepeatRate() {
		return this.keyTyped.getRepeatRate();
	}

	/**
	 * 设定重复延迟比率
	 * 
	 * @param rate
	 */
	public void setRepeatRate(long rate) {
		this.keyTyped.setRepeatRate(rate);
	}

	/**
	 * 设定背景颜色
	 * 
	 * @param color
	 */
	public void setBackground(Color color) {
		int w = getWidth(), h = getHeight();
		BufferedImage image = GraphicsUtils.createIntdexedImage(w, h);
		Graphics2D g = image.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, w, h);
		g.dispose();
		this.setBackground(image);
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(BufferedImage screen) {
		if (screen != null) {
			if (screen.getWidth() != getWidth()
					|| screen.getHeight() != getHeight()) {
				screen = GraphicsUtils.getResize(screen, getWidth(),
						getHeight());
			}
			this.currentScreen = screen;
			this.setRepaintMode(SCREEN_BITMAP_REPAINT);
		}
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(Image screen) {
		this.setBackground(GraphicsUtils.getBufferImage(screen));
	}

	/**
	 * 设定背景图像
	 * 
	 * @param fileName
	 */
	public void setBackground(String fileName) {
		this.setBackground(GraphicsUtils.loadBufferedImage(fileName));
	}

	/**
	 * 根据运行时间进行事务刷新
	 */
	public void runTimer(LTimerContext timer) {
		this.elapsedTime = timer.getTimeSinceLastUpdate();
		if (sprites != null && this.sprites.size() > 0) {
			this.sprites.update(elapsedTime);
		}
		if (desktop != null
				&& this.desktop.getContentPane().getComponentCount() > 0) {
			this.desktop.update(elapsedTime);
		}
		this.updateAction(keyMap, 0, elapsedTime);
		this.updateAction(mouseMap, 1, elapsedTime);
		this.baseInput.update(elapsedTime);
		this.alter(timer);
	}

	/**
	 * 创建程序UI
	 */
	public synchronized void createUI(final LGraphics g) {
		draw(g);
		if (sprites != null) {
			sprites.createUI(g);
		}
		if (desktop != null) {
			desktop.createUI(g);
		}
	}

	/**
	 * 保存当前窗体显示图像到指定位置
	 * 
	 * @param fileName
	 */
	public void saveScreenImage(String fileName) {
		GraphicsUtils.saveImage(getScreenImage(), fileName);
	}

	/**
	 * 保存当前窗体显示图像
	 * 
	 */
	public void saveScreenImage() {
		GraphicsUtils.saveImage(getScreenImage(), LSystem.getLScreenFile());
	}

	/**
	 * 返回当前窗体画面图
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public BufferedImage getScreenImage() {
		if (handler == null) {
			return null;
		}
		return handler.getDeploy().getView().getAwtImage();
	}

	/**
	 * 返回指定大小的当前窗体画面图
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public BufferedImage getScreenImage(int w, int h) {
		return GraphicsUtils.getResize(getScreenImage(), w, h);
	}

	/**
	 * 获得Screen的画面边界
	 * 
	 * @return
	 */
	public Rectangle getBounds() {
		if (handler == null) {
			return null;
		}
		Window window = handler.getScene().getWindow();
		Rectangle bounds = window.getBounds();
		Insets insets = window.getInsets();
		return new Rectangle(bounds.x + insets.left, bounds.y + insets.top,
				bounds.width - (insets.left + insets.top), bounds.height
						- (insets.top + insets.bottom));
	}

	/**
	 * 设置是否录像当前Screen到指定位置(保存为FLV)
	 * 
	 */
	public synchronized void setVideo(String fileName, boolean video) {
		this.isVideo = video;
		if (!isVideo) {
			isVideo = false;
			flvVideo.close();
			videoThread = null;
		} else {
			if (isVideo && videoThread == null) {
				try {
					flvVideo.open(fileName);
					videoThread = new Thread(new Runnable() {
						public void run() {
							while (isVideo) {
								flvVideo.addFrame(getScreenImage());
							}
						}
					});
					videoThread.start();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 设置是否录像当前Screen(保存为FLV)
	 */
	public synchronized void setVideo(boolean video) {
		setVideo(LSystem.getLVideoFile(), video);
	}

	/**
	 * 验证是否使用了非框架组建,此情况下不能全屏
	 * 
	 */
	private void checkFullScreen() {
		if (isComponents) {
			throw new RuntimeException(
					"Using the AWT/Swing components can not be changed to full screen !");
		}
	}

	/**
	 * 全屏窗体为指定大小
	 * 
	 * @param d
	 */
	public void updateFullScreen(Dimension d) {
		updateFullScreen((int) d.getWidth(), (int) d.getHeight());
	}

	/**
	 * 全屏窗体为指定大小
	 * 
	 */
	public void updateFullScreen(int w, int h) {
		checkFullScreen();
		if (handler != null) {
			handler.getScene().updateFullScreen(w, h);
		}
	}

	/**
	 * 全屏窗体
	 */
	public void updateFullScreen() {
		checkFullScreen();
		if (handler != null) {
			handler.getScene().updateFullScreen();
		}
	}

	/**
	 * 还原窗体
	 * 
	 */
	public void updateNormalScreen() {
		checkFullScreen();
		if (handler != null) {
			handler.getScene().updateNormalScreen();
		}
	}

	/**
	 * 检查窗体默认对象中是否包含指定精灵
	 * 
	 */
	public boolean contains(ISprite sprite) {
		return sprites.contains(sprite);
	}

	/**
	 * 检查窗体默认对象中是否包含指定组件
	 * 
	 * @param comp
	 * @return
	 */
	public boolean contains(LComponent comp) {
		return desktop.getContentPane().contains(comp);
	}

	/**
	 * 设定指定精灵到图层最前
	 * 
	 * @param sprite
	 */
	public void sendSpriteToFront(ISprite sprite) {
		sprites.sendToFront(sprite);
	}

	/**
	 * 设定指定精灵到图层最后
	 * 
	 * @param sprite
	 */
	public void sendSpriteToBack(ISprite sprite) {
		sprites.sendToBack(sprite);
	}

	/**
	 * 设定是否允许进行下一步
	 * 
	 */
	public void setNext(boolean next) {
		this.isNext = next;
	}

	/**
	 * 是否允许进行下一步
	 * 
	 */
	public boolean next() {
		return isNext;
	}

	/**
	 * 设定当前Frame（未装）
	 */
	public void setFrame(int frame) {
	}

	/**
	 * 返回背景图片
	 */
	public Image getBackground() {
		return currentScreen;
	}

	/**
	 * 对外的线程暂停器
	 * 
	 * @param timeMillis
	 */
	public void pause(long timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted in pause !");
		}
	}

	/**
	 * 对外的刷新器
	 * 
	 * @param timer
	 */
	public void alter(LTimerContext timer) {

	}

	/**
	 * 触发指定事件
	 * 
	 * @param actions
	 * @param timer
	 */
	private void updateAction(Map actions, int type, long timer) {
		int length = actions.size();
		if (length == 0) {
			return;
		}
		Action keyAction = null;
		switch (type) {
		case 0:
			keyAction = (Action) actions.get(String
					.valueOf(keyPressed[this.pressedKey]));
			break;
		case 1:
			keyAction = (Action) actions.get(String
					.valueOf(mousePressed[this.pressedMouse]));
			break;
		}
		if (keyAction != null) {
			if (keyAction.isPressed()) {
				IAction action = keyAction.getIAction();
				action.doAction(timer);
				if (!(action instanceof MoveAction)) {
					keyAction.reset();
				}
			}
		}
	}

	/**
	 * 点击鼠标左键
	 * 
	 * @return
	 */
	public boolean leftClick() {
		return this.baseInput.isMousePressed(MouseEvent.BUTTON1);
	}

	/**
	 * 点击鼠标中间键(滚轴)
	 * 
	 * @return
	 */
	public boolean middleClick() {
		return this.baseInput.isMousePressed(MouseEvent.BUTTON2);
	}

	/**
	 * 点击鼠标右键
	 * 
	 * @return
	 */
	public boolean rightClick() {
		return this.baseInput.isMousePressed(MouseEvent.BUTTON3);
	}

	public boolean getKeyDown(int keyCode) {
		return this.baseInput.isKeyDown(keyCode);
	}

	public boolean getKeyPressed(int keyCode) {
		return this.baseInput.isKeyPressed(keyCode);
	}

	public void keyTyped(KeyEvent e) {
		e.consume();
	}

	public Map getKeyMap() {
		return keyMap;
	}

	public Map getMouseMap() {
		return mouseMap;
	}

	public int getMousePressed() {
		return (this.pressedMouse > 0) ? mousePressed[0] : LInput.NO_BUTTON;
	}

	public int getMouseReleased() {
		return (this.releasedMouse > 0) ? mouseReleased[0] : LInput.NO_BUTTON;
	}

	public boolean isMousePressed(int button) {
		if (!isMouseReleased(button)) {
			return false;
		}
		try {
			for (int i = 0; i < this.pressedMouse; i++) {
				if (mousePressed[i] == button) {
					return true;
				}
			}
			return mousePressed[0] == button;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean isMouseReleased(int button) {
		try {
			for (int i = 0; i < this.releasedMouse; i++) {
				if (mouseReleased[i] == button) {
					return true;
				}
			}
			return mouseReleased[0] == button;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 键盘按下
	 */
	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		if (e.getModifiers() == InputEvent.ALT_MASK
				&& e.getKeyCode() == KeyEvent.VK_F4) {
			if (handler != null) {
				handler.getScene().close();
			}
		}
		try {
			this.onKey(e);
			if (!keyDown[code & 0xFF]) {
				keyDown[code & 0xFF] = true;
				keyPressed[this.pressedKey] = code;
				this.pressedKey++;
				if (keyMap.size() > 0) {
					Action action = (Action) keyMap.get(String.valueOf(code));
					if (action != null) {
						action.press();
					}
				}
			}
			e.consume();
		} catch (Exception ex) {
			pressedKey = 0;
		}

	}

	/**
	 * 键盘放开
	 */
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();
		try {
			this.onKeyUp(e);
			keyDown[code & 0xFF] = false;
			keyReleased[this.releasedKey] = code;
			this.releasedKey++;
			if (keyMap.size() > 0) {
				Action action = (Action) keyMap.get(String.valueOf(code));
				if (action != null) {
					action.release();
				}
			}
			e.consume();
		} catch (Exception ex) {
			releasedKey = 0;
		}

	}

	/**
	 * 鼠标按下
	 * 
	 * @param e
	 */
	public void mouseDown(MouseEvent e) {

	}

	/**
	 * 鼠标放开
	 * 
	 * @param e
	 */
	public void mouseUp(MouseEvent e) {

	}

	/**
	 * 鼠标按下
	 */
	public void mousePressed(MouseEvent e) {

		int code = e.getButton();
		try {
			Screen.mouseDown[code] = true;
			Screen.mousePressed[this.pressedMouse] = code;
			this.pressedMouse++;
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				leftClick(e);
			}
			if ((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
				middleClick(e);
			}
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				rightClick(e);
			}
			if (mouseMap.size() > 0) {
				Action action = (Action) mouseMap.get(String.valueOf(code));
				if (action != null) {
					action.press();
				}
			}
			mouseDown(e);
			e.consume();
		} catch (Exception ex) {
			pressedMouse = 0;
		}

	}

	/**
	 * 鼠标放开
	 */
	public void mouseReleased(MouseEvent e) {
		int code = e.getButton();
		try {
			Screen.mouseDown[code] = false;
			Screen.mouseReleased[this.releasedMouse] = code;
			this.releasedMouse++;
			if (mouseMap.size() > 0) {
				Action action = (Action) mouseMap.get(String.valueOf(code));
				if (action != null) {
					action.release();
				}
			}
			mouseUp(e);
			e.consume();
		} catch (Exception ex) {
			releasedMouse = 0;
		}
	}

	public void mouseMove(MouseEvent e) {
	}

	public synchronized void mouseClicked(MouseEvent e) {
	}

	public synchronized void mouseEntered(MouseEvent e) {
		this.mouseExists = true;
	}

	public synchronized void mouseExited(MouseEvent e) {
		this.mouseExists = false;
		for (int i = 0; i < 4; i++) {
			mouseDown[i] = false;
		}
	}

	public synchronized void mouseDragged(MouseEvent e) {
		this.mouseX = e.getX();
		this.mouseY = e.getY();
		mouseMove(e);
	}

	public synchronized void mouseMoved(MouseEvent e) {
		this.mouseX = e.getX();
		this.mouseY = e.getY();
		mouseMove(e);
	}

	public void move(double x, double y) {
		this.mouseX = (int) x;
		this.mouseY = (int) y;
	}

	/**
	 * 触发输入事件
	 */
	public void changeText(String text) {
		desktop.changeText(text);
	}

	public void focusGained(FocusEvent e) {
		this.isNext = true;
	}

	public void focusLost(FocusEvent e) {
		this.isNext = false;
		this.refresh();
	}

	public void addComponent(final Component component, final int x,
			final int y, final int w, final int h) {
		if (handler != null) {
			if (handler.getDeploy().addComponent(x, y, w, h, component)) {
				isComponents = true;
			}
		} else {
			Thread componentThread = new Thread(new Runnable() {
				public void run() {
					while (handler == null) {
						Thread.yield();
					}
					if (handler != null) {
						if (handler.getDeploy().addComponent(x, y, w, h,
								component)) {
							isComponents = true;
						}
					}
				}
			});
			componentThread.start();
		}
	}

	public void addComponent(Component component, int x, int y) {
		addComponent(component, x, y, component.getWidth(), component
				.getHeight());
	}

	public void addComponent(Component component) {
		addComponent(component, 0, 0);
	}

	public void removeComponent(Component component) {
		if (handler != null) {
			handler.getDeploy().removeComponent(component);
		}
	}

	public void removeComponent(int index) {
		if (handler != null) {
			this.handler.getDeploy().removeComponent(index);
		}
	}

}
