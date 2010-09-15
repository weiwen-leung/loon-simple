package org.loon.framework.javase.game;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.loon.framework.javase.game.core.Handler;
import org.loon.framework.javase.game.core.IHandler;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.AwtScene;
import org.loon.framework.javase.game.core.graphics.Deploy;
import org.loon.framework.javase.game.core.graphics.IScene;
import org.loon.framework.javase.game.core.graphics.Model;
import org.loon.framework.javase.game.core.graphics.ScreenManager;
import org.loon.framework.javase.game.core.graphics.SwingScene;
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
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1
 */
public class GameScene {

	/**
	 * 
	 */
	private static final long serialVersionUID = 198284399945549558L;

	private Dimension screenSize;

	private IScene scene;

	private IHandler handler;

	private Thread screenThread;

	private Deploy deploy;

	private boolean isFullScreen;

	private DisplayMode normalMode;

	/**
	 * 构造GameScene(默认使用AWT容器)
	 * 
	 * @param titleName
	 * @param width
	 * @param height
	 */
	public GameScene(String titleName, int width, int height) {
		this(titleName, true, Model.Awt, width, height);
	}

	/**
	 * 构造GameScene
	 * 
	 * @param titleName
	 * @param type
	 * @param width
	 * @param height
	 */
	public GameScene(String titleName, int type, int width, int height) {
		this(titleName, true, type, width, height);
	}

	/**
	 * 构造GameScene(默认使用AWT容器)
	 * 
	 * @param titleName
	 * @param border
	 * @param width
	 * @param height
	 */
	public GameScene(String titleName, boolean border, int width, int height) {
		this(titleName, border, Model.Awt, width, height);
	}

	/**
	 * 构造GameScene,type == 0 使用AWT容器,type == 1 使用Swing容器
	 * 
	 * @param titleName
	 * @param border
	 * @param type
	 * @param width
	 * @param height
	 */
	public GameScene(String titleName, boolean border, int type, int width,
			int height) {
		if (width < 1 || height < 1) {
			throw new RuntimeException("Width and Height must be positive !");
		}
		System.setProperty("sun.java2d.translaccel", "true");
		System.setProperty("sun.java2d.ddscale", "true");
		if (LSystem.isWindows()) {
			System.setProperty("sun.java2d.ddoffscreen", "true");
			System.setProperty("sun.java2d.noddraw", "true");
			System.setProperty("sun.java2d.d3d", "true");
		} else if (LSystem.isMacOS()) {
			System.setProperty("apple.awt.showGrowBox", "false");
			System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
			System.setProperty("apple.awt.graphics.EnableLazyDrawing", "true");
			System.setProperty(
					"apple.awt.window.position.forceSafeUserPositioning",
					"true");
			System.setProperty("apple.awt.window.position.forceSafeCreation",
					"true");
			System.setProperty("com.apple.hwaccel", "true");
			System.setProperty("com.apple.forcehwaccel", "true");
			System.setProperty("com.apple.macos.smallTabs", "true");
			System.setProperty("com.apple.macos.use-file-dialog-packages",
					"true");
		} else {
			System.setProperty("sun.java2d.opengl", "true");
		}
		switch (type) {
		case 0:
			scene = new AwtScene(titleName);
			break;
		case 1:
			scene = new SwingScene(titleName);
			break;
		default:
			throw new RuntimeException("GameScene start type is invalid !");
		}
		if (!border) {
			scene.setUndecorated(true);
		}
		this.handler = new Handler(this, width, height);
		scene.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		scene.setIgnoreRepaint(true);
		scene.setResizable(false);
		scene.requestFocus();
		if (!isApplet()) {
			setIconImage("system/image/icon.png");
			setSize(width, height);
		}
		this.deploy = new Deploy(handler);
		this.updateDisplayMode();
		this.centerFrame();

	}

	/**
	 * 关闭框架运行
	 * 
	 */
	public synchronized void close() {
		try {
			scene.setVisible(false);
			deploy.getView().setRunning(false);
			scene.dispose();
			System.exit(0);
		} catch (Throwable e) {
		}
	}

	/**
	 * 是否正在使用Applet
	 * 
	 * @return
	 */
	public synchronized boolean isApplet() {
		return LSystem.isApplet;
	}

	/**
	 * 设置主窗体小图标
	 * 
	 * @param fileName
	 */
	public synchronized void setIconImage(String fileName) {
		scene.setIconImage(GraphicsUtils.loadImage(fileName));
	}

	public synchronized void setIconImage(Image icon) {
		scene.setIconImage(icon);
	}

	/**
	 * 居中Frame
	 * 
	 */
	public synchronized void centerFrame() {
		if (!isApplet()) {
			if (LSystem.isOverrunJdk14()) {
				scene.setLocationRelativeTo(null);
			} else {
				screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension frameSize = scene.getSize();
				if (frameSize.height > screenSize.height) {
					frameSize.height = screenSize.height;
				}
				if (frameSize.width > screenSize.width) {
					frameSize.width = screenSize.width;
				}
				scene.setLocation((screenSize.width - frameSize.width) / 2,
						(screenSize.height - frameSize.height) / 2);
			}
		}
	}

	/**
	 * 设定Frame游标
	 * 
	 * @param fileName
	 */
	public synchronized void setCursor(String fileName) {
		scene.setCursor(GameCursor.getCursor(fileName));
	}

	/**
	 * 设定Frame游标
	 * 
	 * @param cursor
	 */
	public synchronized void setCursor(Cursor cursor) {
		scene.setCursor(cursor);
	}

	/**
	 * 当前窗体是否处于全屏模式
	 * 
	 * @return
	 */
	public synchronized boolean isFullScreen() {
		return isFullScreen;
	}

	/**
	 * 设定当前窗体全屏
	 * 
	 */
	public synchronized void updateFullScreen() {
		updateFullScreen(handler.getWidth(), handler.getHeight());
	}

	/**
	 * 设定当前窗体全屏为指定大小
	 * 
	 * @param w
	 * @param h
	 */
	public synchronized void updateFullScreen(final int w, final int h) {
		if (isFullScreen) {
			return;
		}
		try {
			screenThread = new Thread(new Runnable() {
				public void run() {
					DisplayMode useDisplayMode = ScreenManager
							.searchFullScreenModeDisplay(w, h);
					if (useDisplayMode == null) {
						return;
					}
					isFullScreen = true;
					deploy.getView().endPaint();
					normalMode = ScreenManager.graphicsDevice.getDisplayMode();
					scene.setVisible(false);
					scene.removeNotify();
					scene.setUndecorated(true);
					ScreenManager.graphicsDevice.setFullScreenWindow(scene
							.getWindow());
					if (useDisplayMode != null
							&& ScreenManager.graphicsDevice
									.isDisplayChangeSupported()) {
						try {
							ScreenManager.graphicsDevice
									.setDisplayMode(useDisplayMode);
						} catch (IllegalArgumentException ex) {
						}
						setSuperSize(useDisplayMode.getWidth(), useDisplayMode
								.getHeight());
					}
					scene.addNotify();
					scene.setVisible(true);
					scene.pack();
					scene.requestFocus();
					deploy.getView().requestFocus();
					updateDisplayMode();
					deploy.getView().startPaint();
				}
			});
			screenThread.start();
		} catch (Exception ex) {
			this.updateNormalScreen();
		}

	}

	/**
	 * 还原当前窗体为默认模式
	 * 
	 */
	public synchronized void updateNormalScreen() {
		if (!isFullScreen) {
			return;
		} else {
			try {
				screenThread = new Thread(new Runnable() {
					public void run() {
						deploy.getView().endPaint();
						isFullScreen = false;
						scene.setVisible(false);
						ScreenManager.graphicsDevice.setDisplayMode(normalMode);
						scene.removeNotify();
						scene.setUndecorated(false);
						ScreenManager.graphicsDevice.setFullScreenWindow(null);
						scene.addNotify();
						scene.pack();
						setSize(handler.getWidth(), handler.getHeight());
						scene.setVisible(true);
						scene.validate();
						scene.requestFocus();
						deploy.getView().requestFocus();
						updateDisplayMode();
						deploy.getView().startPaint();
					}
				});
				screenThread.start();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 变更BufferStrategy为指定缓冲方式
	 * 
	 */
	private synchronized void updateDisplayMode() {
		scene.createBufferStrategy(2);
	}

	/**
	 * 显示窗体
	 * 
	 */
	public synchronized void showFrame() {
		scene.setVisible(true);
	}

	/**
	 * 隐藏窗体
	 * 
	 */
	public synchronized void hideFrame() {
		scene.setVisible(false);
	}

	/**
	 * 以默认Frame方式设定窗体大小
	 * 
	 * @param width
	 * @param height
	 */
	public synchronized void setSuperSize(final int width, final int height) {
		scene.setSize(width, height);
	}

	/**
	 * 设定GameFrame窗体大小
	 * 
	 */
	public synchronized void setSize(final int width, final int height) {
		scene.pack();
		Insets insets = scene.getInsets();
		setSuperSize(width + insets.left, height + insets.top);
	}

	/**
	 * 返回当前Frame配置器
	 * 
	 * @return
	 */
	public synchronized Deploy getDeploy() {
		return deploy;
	}

	/**
	 * 设定配置器
	 * 
	 * @param deploy
	 */
	public synchronized void setDeploy(Deploy deploy) {
		this.deploy = deploy;
	}

	/**
	 * 设定标题
	 * 
	 * @param title
	 */
	public synchronized void setTitle(String title) {
		scene.setTitle(title);
	}

	/**
	 * 返回当前Frame操作句柄
	 * 
	 * @return
	 */
	public synchronized IHandler getHandler() {
		return handler;
	}

	/**
	 * 返回实际使用的IScene
	 * 
	 * @return
	 */
	public synchronized IScene getIScene() {
		return scene;
	}

	/**
	 * 设定当前Frame操作句柄
	 * 
	 * @param handler
	 */
	public synchronized void setHandler(IHandler handler) {
		this.handler = handler;
	}

	/**
	 * 返回Window
	 * 
	 * @return
	 */
	public synchronized Window getWindow() {
		return scene.getWindow();
	}

}
