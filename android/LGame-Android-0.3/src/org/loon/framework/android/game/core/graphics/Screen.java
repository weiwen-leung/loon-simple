package org.loon.framework.android.game.core.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.loon.framework.android.game.IAndroid2DHandler;
import org.loon.framework.android.game.Location;
import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.action.sprite.SpriteListener;
import org.loon.framework.android.game.action.sprite.Sprites;
import org.loon.framework.android.game.core.EmulatorButtons;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LEvent;
import org.loon.framework.android.game.core.LInput;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.media.sound.PlaySound;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.MultitouchUtils;

import android.graphics.Bitmap;
import android.graphics.Point;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * Copyright 2008 - 2011
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.3
 */
public abstract class Screen implements LInput, LRelease {

	public final static int SCREEN_NOT_REPAINT = 0;

	public final static int SCREEN_BITMAP_REPAINT = -1;

	public final static int SCREEN_CANVAS_REPAINT = -2;
	// 线程事件集合
	private final ArrayList<Runnable> runnables;

	private double landscapeUpdate;

	private int touchX, touchY, lastTouchX, lastTouchY, touchDX, touchDY,
			touchDirection;

	private float offsetTouchX, offsetMoveX, offsetTouchY, offsetMoveY;

	private float currentX, currentY, currentZ, currenForce;

	private float lastX, lastY, lastZ;

	public long elapsedTime;

	private final static boolean[] touchDown, keyDown;

	private final static int[] touchPressed, touchReleased;

	private int pressedTouch, releasedTouch;

	private final static int[] keyPressed, keyReleased;

	private float accelOffset = 0.0F;

	private int pressedKey, releasedKey;

	boolean isNext, isMoving, isGravityToKey;

	private int mode, frame;

	private long lastUpdate;

	private Bitmap currentScreen;

	private IAndroid2DHandler handler;

	private int width, height, halfWidth, halfHeight;

	private SensorDirection direction = SensorDirection.NONE;

	private LInput baseInput;

	// 精灵集合
	private Sprites sprites;
	// 桌面集合
	private Desktop desktop;

	private final Touch touchEvent = new Touch();

	private Point touch = new Point(0, 0);

	private boolean isLoad, isLock, isClose;

	static {
		keyDown = new boolean[128];
		keyPressed = keyReleased = new int[128];
		touchDown = new boolean[15];
		touchPressed = touchReleased = new int[15];
	}

	public Screen() {
		LSystem.AUTO_REPAINT = true;
		this.mode = SCREEN_CANVAS_REPAINT;
		this.setFPS(getMaxFPS());
		this.runnables = new ArrayList<Runnable>(1);
		this.handler = (IAndroid2DHandler) LSystem.getSystemHandler();
		this.width = handler.getWidth();
		this.height = handler.getHeight();
		this.halfWidth = getWidth() / 2;
		this.halfHeight = getHeight() / 2;
		this.currentScreen = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		this.baseInput = this;
		this.touchX = touchY = lastTouchX = lastTouchY = touchDX = touchDY = 0;
		this.sprites = new Sprites(width, height);
		this.desktop = new Desktop(baseInput, width, height);
		this.isNext = true;
		this.isMoving = false;
	}

	/**
	 * 获得当前游戏事务运算时间是否被锁定
	 * 
	 * @return
	 */
	public boolean isLock() {
		return isLock;
	}

	/**
	 * 锁定游戏事务运算时间
	 * 
	 * @param lock
	 */
	public void setLock(boolean lock) {
		this.isLock = lock;
	}

	/**
	 * 关闭游戏
	 * 
	 * @param close
	 */
	public void setClose(boolean close) {
		this.isClose = close;
	}

	/**
	 * 判断游戏是否被关闭
	 * 
	 * @return
	 */
	public boolean isClose() {
		return isClose;
	}

	/**
	 * 设定当前帧
	 * 
	 * @param frame
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}

	/**
	 * 返回当前帧
	 * 
	 * @return
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * 移动当前帧
	 * 
	 * @return
	 */
	public synchronized boolean next() {
		this.frame++;
		return isNext;
	}

	/**
	 * 暂停当前Screen指定活动帧数
	 * 
	 * @param i
	 */
	public synchronized void waitFrame(int i) {
		for (int wait = frame + i; frame < wait;) {
			try {
				super.wait();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 暂停当前Screen指定时间
	 * 
	 * @param i
	 */
	public synchronized void waitTime(long i) {
		for (long time = System.currentTimeMillis() + i; System
				.currentTimeMillis() < time;)
			try {
				super.wait(time - System.currentTimeMillis());
			} catch (Exception ex) {
			}
	}

	/**
	 * 初始化时加载的数据
	 */
	public void onLoad() {

	}

	/**
	 * 初始化加载完毕
	 * 
	 */
	public void onLoaded() {

	}

	/**
	 * 改变资源加载状态
	 */
	public void setOnLoadState(boolean flag) {
		this.isLoad = flag;
	}

	/**
	 * 获得当前资源加载是否完成
	 */
	public boolean isOnLoadComplete() {
		return isLoad;
	}

	/**
	 * 取出第一个Screen并执行
	 * 
	 */
	public void runFirstScreen() {
		if (handler != null) {
			handler.runFirstScreen();
		}
	}

	/**
	 * 取出最后一个Screen并执行
	 */
	public void runLastScreen() {
		if (handler != null) {
			handler.runLastScreen();
		}
	}

	/**
	 * 运行指定位置的Screen
	 * 
	 * @param index
	 */
	public void runIndexScreen(int index) {
		if (handler != null) {
			handler.runIndexScreen(index);
		}
	}

	/**
	 * 运行自当前Screen起的上一个Screen
	 */
	public void runPreviousScreen() {
		if (handler != null) {
			handler.runPreviousScreen();
		}
	}

	/**
	 * 运行自当前Screen起的下一个Screen
	 */
	public void runNextScreen() {
		if (handler != null) {
			handler.runNextScreen();
		}
	}

	/**
	 * 向缓存中添加Screen数据，但是不立即执行
	 * 
	 * @param screen
	 */
	public void addScreen(Screen screen) {
		if (handler != null) {
			handler.addScreen(screen);
		}
	}

	/**
	 * 获得保存的Screen列表
	 * 
	 * @return
	 */
	public LinkedList<Screen> getScreens() {
		if (handler != null) {
			return handler.getScreens();
		}
		return null;
	}

	/**
	 * 获得缓存的Screen总数
	 */
	public int getScreenCount() {
		if (handler != null) {
			return handler.getScreenCount();
		}
		return 0;
	}

	/**
	 * 返回精灵监听
	 * 
	 * @return
	 */
	public SpriteListener getSprListerner() {
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
	public void setSprListerner(SpriteListener sprListerner) {
		if (sprites == null) {
			return;
		}
		sprites.setSprListerner(sprListerner);
	}

	/**
	 * 获得当前Screen类名
	 */
	public String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * 设定模拟按钮监听器
	 */
	public void setEmulatorListener(EmulatorListener emulator) {
		if (LSystem.getSurface2D() != null) {
			LSystem.getSurface2D().setEmulatorListener(emulator);
		}
	}

	/**
	 * 返回模拟按钮集合
	 * 
	 * @return
	 */
	public EmulatorButtons getEmulatorButtons() {
		if (LSystem.getSurface2D() != null) {
			return LSystem.getSurface2D().getEmulatorButtons();
		}
		return null;
	}

	/**
	 * 设定模拟按钮组是否显示
	 * 
	 * @param visible
	 */
	public void emulatorButtonsVisible(boolean visible) {
		if (LSystem.getSurface2D() != null) {
			try {
				EmulatorButtons es = LSystem.getSurface2D()
						.getEmulatorButtons();
				es.setVisible(visible);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 添加指定的View到游戏界面
	 * 
	 * @param view
	 */
	public void addView(final View view) {
		if (handler != null) {
			handler.getLGameActivity().addView(view);
		}
	}

	/**
	 * 从游戏界面中删除指定的View
	 * 
	 * @param view
	 */
	public void removeView(final View view) {
		if (handler != null) {
			handler.getLGameActivity().removeView(view);
		}
	}

	/**
	 * 添加指定的View到游戏界面的指定位置
	 * 
	 * @param view
	 * @param location
	 */
	public void addView(final View view, Location location) {
		if (handler != null) {
			handler.getLGameActivity().addView(view, location);
		}
	}

	/**
	 * 添加指定的View到游戏界面的指定位置，并将View设置为指定大小
	 * 
	 * @param view
	 * @param location
	 */
	public void addView(final View view, int w, int h, Location location) {
		if (handler != null) {
			handler.getLGameActivity().addView(view, w, h, location);
		}
	}

	/**
	 * 弹出一个Android选框，用以显示指定的信息
	 * 
	 * @param message
	 */
	public void showAndroidAlert(final ClickListener listener,
			final String title, final String message) {
		if (handler != null) {
			handler.getLGameActivity().showAndroidAlert(listener, title,
					message);
		}
	}

	/**
	 * 弹出一个Android信息框，用以显示指定的HTML文档
	 * 
	 * @param title
	 * @param assetsFileName
	 */
	public void showAndroidOpenHTML(final ClickListener listener,
			final String title, final String url) {
		if (handler != null) {
			handler.getLGameActivity()
					.showAndroidOpenHTML(listener, title, url);
		}
	}

	/**
	 * 弹出一个Android选择框，并返回选择结果
	 * 
	 * @param text
	 * @return
	 */
	public void showAndroidSelect(final SelectListener listener,
			final String title, final String[] text) {
		if (handler != null) {
			handler.getLGameActivity().showAndroidSelect(listener, title, text);
		}
	}

	/**
	 * 弹出一个Android输入框，并返回选择结果
	 * 
	 * @param text
	 * @return
	 */
	public void showAndroidTextInput(final TextListener listener,
			final String title, final String text) {
		if (handler != null) {
			handler.getLGameActivity().showAndroidTextInput(listener, title,
					text);
		}
	}

	/**
	 * 添加一个与指定资源索引相对应的PlaySound音频文件
	 * 
	 * @param resId
	 */
	public PlaySound addPlaySound(final int resId) {
		if (handler != null) {
			return handler.getPlaySound().addPlaySound(resId);
		}
		return null;
	}

	/**
	 * 添加一个与指定资源索引相对应的PlaySound音频文件
	 * 
	 * @param resId
	 * @param vol
	 */
	public PlaySound addPlaySound(final int resId, final int vol) {
		if (handler != null) {
			return handler.getPlaySound().addPlaySound(resId, vol);
		}
		return null;
	}

	/**
	 * 播放Assets中的音频文件
	 * 
	 * @param file
	 * @param loop
	 */
	public void playAssetsMusic(final String file, final boolean loop) {
		if (handler != null) {
			handler.getAssetsSound().playSound(file, loop);
		}
	}

	/**
	 * 设置Assets中的音频文件音量
	 * 
	 * @param vol
	 */
	public void resetAssetsMusic(final int vol) {
		if (handler != null) {
			handler.getAssetsSound().setSoundVolume(vol);
		}
	}

	/**
	 * 重置Assets中的音频文件
	 * 
	 */
	public void resetAssetsMusic() {
		if (handler != null) {
			handler.getAssetsSound().resetSound();
		}
	}

	/**
	 * 中断Assets中的音频文件
	 */
	public void stopAssetsMusic() {
		if (handler != null) {
			handler.getAssetsSound().stopSound();
		}
	}

	/**
	 * 中断Assets中指定索引的音频文件
	 */
	public void stopAssetsMusic(int index) {
		if (handler != null) {
			handler.getAssetsSound().stopSound(index);
		}
	}

	/**
	 * 设定常规图像加载方法的扩大值
	 * 
	 * @param sampleSize
	 */
	public void setSizeImage(int sampleSize) {
		LSystem.setPoorImage(sampleSize);
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(Bitmap screen) {
		if (screen != null) {
			setRepaintMode(SCREEN_BITMAP_REPAINT);
			if (screen.getWidth() != getWidth()
					|| screen.getHeight() != getHeight()) {
				screen = GraphicsUtils.getResize(screen, getWidth(),
						getHeight());
			}
			Bitmap tmp = currentScreen;
			currentScreen = screen;
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

		}
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(LImage screen) {
		setBackground(screen.getBitmap());
	}

	/**
	 * 设定背景图像
	 */
	public void setBackground(String fileName, boolean transparency) {
		this.setBackground(getImage(fileName, transparency));
	}

	/**
	 * 设定背景图像
	 * 
	 * @param fileName
	 */
	public void setBackground(String fileName) {
		this.setBackground(getImage(fileName, false));
	}

	/**
	 * 返回背景图像
	 * 
	 * @return
	 */
	public Bitmap getBackground() {
		return currentScreen;
	}

	/**
	 * 设定刷新率
	 * 
	 * @param fps
	 */
	public void setFPS(long fps) {
		LSystem.setFPS(fps);
	}

	/**
	 * 返回当前刷新率
	 * 
	 * @return
	 */
	public long getFPS() {
		return LSystem.getFPS();
	}

	/**
	 * 返回最大刷新率
	 * 
	 * @return
	 */
	public long getMaxFPS() {
		return LSystem.getMaxFPS();
	}

	public void setBackground(LColor color) {
		setRepaintMode(SCREEN_BITMAP_REPAINT);
		LImage image = new LImage(currentScreen);
		LGraphics g = image.getLGraphics();
		g.setBackground(color);
		g.dispose();
	}

	public final void destroy() {
		synchronized (this) {
			isClose = true;
			callEvents(false);
			isNext = false;
			sprites = null;
			desktop = null;
			currentScreen = null;
			dispose();
		}
	}

	/**
	 * 释放函数内资源
	 * 
	 */
	public void dispose() {

	}

	public Desktop getDesktop() {
		return desktop;
	}

	public Sprites getSprites() {
		return sprites;
	}

	/**
	 * 返回与指定类匹配的组件
	 */
	public ArrayList<LComponent> getComponents(Class<? extends LComponent> clazz) {
		if (desktop != null) {
			return desktop.getComponents(clazz);
		}
		return null;
	}

	/**
	 * 返回位于屏幕顶部的组件
	 * 
	 * @return
	 */
	public LComponent getTopComponent() {
		if (desktop != null) {
			return desktop.getTopComponent();
		}
		return null;
	}

	/**
	 * 返回位于屏幕底部的组件
	 * 
	 * @return
	 */
	public LComponent getBottomComponent() {
		if (desktop != null) {
			return desktop.getBottomComponent();
		}
		return null;
	}

	/**
	 * 返回位于屏幕顶部的图层
	 */
	public Layer getTopLayer() {
		if (desktop != null) {
			return desktop.getTopLayer();
		}
		return null;
	}

	/**
	 * 返回位于屏幕底部的图层
	 */
	public Layer getBottomLayer() {
		if (desktop != null) {
			return desktop.getBottomLayer();
		}
		return null;
	}

	/**
	 * 返回所有指定类产生的精灵
	 * 
	 */
	public ArrayList<ISprite> getSprites(Class<? extends ISprite> clazz) {
		if (sprites != null) {
			return sprites.getSprites(clazz);
		}
		return null;
	}

	/**
	 * 返回位于数据顶部的精灵
	 * 
	 */
	public ISprite getTopSprite() {
		if (sprites != null) {
			return sprites.getTopSprite();
		}
		return null;
	}

	/**
	 * 返回位于数据底部的精灵
	 * 
	 */
	public ISprite getBottomSprite() {
		if (sprites != null) {
			return sprites.getBottomSprite();
		}
		return null;
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

	public void remove(ISprite sprite) {
		if (sprites != null) {
			sprites.remove(sprite);
		}
	}

	public void removeSprite(Class<? extends ISprite> clazz) {
		if (sprites != null) {
			sprites.remove(clazz);
		}
	}

	public void remove(LComponent comp) {
		if (desktop != null) {
			desktop.remove(comp);
		}
	}

	public void removeComponent(Class<? extends LComponent> clazz) {
		if (desktop != null) {
			desktop.remove(clazz);
		}
	}

	public void removeAll() {
		if (sprites != null) {
			sprites.removeAll();
		}
		if (desktop != null) {
			desktop.getContentPane().clear();
		}
	}

	/**
	 * 判断是否点中指定精灵
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean onClick(ISprite sprite) {
		if (sprite == null) {
			return false;
		}
		if (sprite.isVisible()) {
			RectBox rect = sprite.getCollisionBox();
			if (rect.contains(touchX, touchY)
					|| rect.intersects(touchX, touchY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否点中指定组件
	 * 
	 * @param component
	 * @return
	 */
	public boolean onClick(LComponent component) {
		if (component == null) {
			return false;
		}
		if (component.isVisible()) {
			RectBox rect = component.getCollisionBox();
			if (rect.contains(touchX, touchY)
					|| rect.intersects(touchX, touchY)) {
				return true;
			}
		}
		return false;
	}

	public void centerOn(final LObject object) {
		LObject.centerOn(object, getWidth(), getHeight());
	}

	public void topOn(final LObject object) {
		LObject.topOn(object, getWidth(), getHeight());
	}

	public void leftOn(final LObject object) {
		LObject.leftOn(object, getWidth(), getHeight());
	}

	public void rightOn(final LObject object) {
		LObject.rightOn(object, getWidth(), getHeight());
	}

	public void bottomOn(final LObject object) {
		LObject.bottomOn(object, getWidth(), getHeight());
	}

	/**
	 * 获得背景显示模式
	 */
	public int getRepaintMode() {
		return mode;
	}

	/**
	 * 设定背景刷新模式
	 * 
	 * @param mode
	 */
	public void setRepaintMode(int mode) {
		this.mode = mode;
	}

	/**
	 * 增减一个线程事件
	 * 
	 * @param event
	 */
	public void callEvent(final LEvent event) {
		if (event == null) {
			return;
		}
		Thread runnable = new Thread() {
			public void run() {
				event.call();
			}
		};
		callEvent(runnable);
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
			for (Iterator<Runnable> it = runnables.iterator(); it.hasNext();) {
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
	public final void callEvents(boolean execute) {
		if (!execute) {
			synchronized (runnables) {
				runnables.clear();
			}
			return;
		}
		if (runnables.size() == 0) {
			return;
		}
		ArrayList<Runnable> runnableList;
		synchronized (runnables) {
			runnableList = new ArrayList<Runnable>(runnables);
			runnables.clear();
		}
		for (Iterator<Runnable> it = runnableList.iterator(); it.hasNext();) {
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

	public LImage getWebImage(String url, boolean transparency) {
		return GraphicsUtils.loadWebImage(url, transparency);
	}

	public LImage getImage(String fileName, boolean transparency) {
		return GraphicsUtils.loadImage(fileName, transparency);
	}

	public LImage[] getSplitImages(String fileName, int row, int col,
			boolean transparency) {
		return getSplitImages(getImage(fileName, transparency), row, col);
	}

	public LImage[] getSplitImages(LImage image, int row, int col) {
		return GraphicsUtils.getSplitImages(image, row, col);
	}

	public synchronized void createUI(LGraphics g) {
		if (isClose) {
			return;
		}
		draw(g);
		if (sprites != null) {
			sprites.createUI(g);
		}
		if (desktop != null) {
			desktop.createUI(g);
		}
	}

	public abstract void draw(LGraphics g);

	public void runTimer(final LTimerContext timer) {
		if (isClose) {
			return;
		}
		this.elapsedTime = timer.getTimeSinceLastUpdate();
		if (sprites != null && this.sprites.size() > 0) {
			this.sprites.update(elapsedTime);
		}
		if (desktop != null
				&& this.desktop.getContentPane().getComponentCount() > 0) {
			this.desktop.update(elapsedTime);
		}
		this.pressedTouch = releasedTouch = pressedKey = releasedKey = 0;
		this.touchDX = touchX - lastTouchX;
		this.touchDY = touchY - lastTouchY;
		this.lastTouchX = touchX;
		this.lastTouchY = touchY;
		this.alter(timer);
	}

	public void setNext(boolean next) {
		this.isNext = next;
	}

	public void alter(LTimerContext timer) {

	}

	/**
	 * 设定游戏窗体
	 */
	public void setScreen(Screen screen) {
		if (handler != null) {
			this.handler.setScreen(screen);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 刷新基础设置
	 */
	public void refresh() {
		for (int i = 0; i < touchDown.length; i++) {
			touchDown[i] = false;
		}
		touchDX = touchDY = 0;
		for (int i = 0; i < keyDown.length; i++) {
			keyDown[i] = false;
		}
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
		}
	}

	public Point getTouch() {
		touch.set(touchX, touchY);
		return touch;
	}

	public boolean isPaused() {
		return LSystem.isPaused;
	}

	public boolean isClick() {
		return touchDown[MotionEvent.ACTION_DOWN];
	}

	public boolean isTouchClick() {
		return baseInput.isTouchPressed(MotionEvent.ACTION_DOWN);
	}

	public boolean isTouchClickUp() {
		return baseInput.isTouchPressed(MotionEvent.ACTION_UP);
	}

	public int getTouchPressed() {
		return (pressedTouch > 0) ? touchPressed[pressedTouch - 1]
				: LInput.NO_BUTTON;
	}

	public int getTouchReleased() {
		return (releasedTouch > 0) ? touchReleased[releasedTouch - 1]
				: LInput.NO_BUTTON;
	}

	public boolean isTouchPressed(int button) {
		if (!isTouchReleased(button)) {
			return false;
		}
		try {
			for (int i = 0; i < this.pressedTouch; i++) {
				if (touchPressed[i] == button) {
					return true;
				}
			}
			return touchPressed[0] == button;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isTouchReleased(int button) {
		try {
			for (int i = 0; i < this.releasedTouch; i++) {
				if (touchReleased[i] == button) {
					return true;
				}
			}
			return touchReleased[0] == button;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getKeyDown(int keyCode) {
		return baseInput.isKeyDown(keyCode);
	}

	public boolean getKeyPressed(int keyCode) {
		return baseInput.isKeyPressed(keyCode);
	}

	public int getTouchX() {
		return touchX;
	}

	public int getTouchY() {
		return touchY;
	}

	public int getTouchDX() {
		return touchDX;
	}

	public int getTouchDY() {
		return touchDY;
	}

	public boolean[] getTouchDown() {
		return touchDown;
	}

	public boolean isTouchDown(int button) {
		return touchDown[button];
	}

	public int getKeyPressed() {
		return (pressedKey > 0) ? keyPressed[pressedKey - 1] : LInput.NO_KEY;
	}

	public boolean isKeyPressed(int keyCode) {
		for (int i = 0; i < pressedKey; i++) {
			if (keyPressed[i] == keyCode) {
				return true;
			}
		}
		return false;
	}

	public int getKeyReleased() {
		return (releasedKey > 0) ? keyReleased[releasedKey - 1] : LInput.NO_KEY;
	}

	public boolean isKeyReleased(int keyCode) {
		for (int i = 0; i < releasedKey; i++) {
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
		return keyDown[keyCode];
	}

	public boolean onTrackballEvent(MotionEvent e) {
		return false;
	}

	public final boolean onKeyDownEvent(int keyCode, KeyEvent e) {
		if (isLock || isClose) {
			return true;
		}
		try {
			keyDown[keyCode] = true;
			keyPressed[pressedKey] = keyCode;
			pressedKey++;
		} catch (Exception ex) {
			pressedKey = 0;
			Log.d(getName(), "on Key Down !", ex);
		}
		return onKeyDown(keyCode, e);
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		return true;
	}

	public void setKeyDown(int code) {
		try {
			keyDown[code] = true;
			keyPressed[this.pressedKey] = code;
			pressedKey++;
		} catch (Exception e) {
		}
	}

	public final boolean onKeyUpEvent(int keyCode, KeyEvent e) {
		if (isLock || isClose) {
			return true;
		}
		try {
			keyDown[keyCode] = false;
			keyReleased[releasedKey] = keyCode;
			releasedKey++;
		} catch (Exception ex) {
			releasedKey = 0;
			Log.d(getName(), "on Key Up !", ex);
		}
		return onKeyUp(keyCode, e);
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		return true;
	}

	public void setKeyUp(int code) {
		try {
			keyDown[code] = false;
			keyReleased[this.releasedKey] = code;
			releasedKey++;
		} catch (Exception e) {
		}
	}

	public final boolean onTouchEvent(MotionEvent e) {
		if (isLock || isClose) {
			return true;
		}
		try {
			int code = e.getAction();
			int pointerCount = MultitouchUtils.getPointerCount(e);
			if (MultitouchUtils.isMultitouch() && pointerCount > 1) {
				int id = 0;
				for (int idx = 0; idx < pointerCount; idx++) {
					id = MultitouchUtils.getPointerId(e, idx);
					touchX = Math.round(MultitouchUtils.getX(e, id)
							/ LSystem.scaleWidth);
					touchY = Math.round(MultitouchUtils.getY(e, id)
							/ LSystem.scaleHeight);
					touchEvent.x = touchX;
					touchEvent.y = touchY;
					touchEvent.action = code;
					touchEvent.pointerCount = pointerCount;
					switch (code) {
					case MotionEvent.ACTION_DOWN:
						if (idx == 0) {
							isMoving = false;
							offsetTouchX = touchX;
							offsetTouchY = touchY;
							touchDown[code] = true;
							touchPressed[pressedTouch] = code;
							touchDX = lastTouchX = touchX;
							touchDY = lastTouchY = touchY;
							pressedTouch++;
							if ((touchX < halfWidth) && (touchY < halfHeight)) {
								this.touchDirection = LInput.UPPER_LEFT;
							} else if ((touchX >= halfWidth)
									&& (touchY < halfHeight)) {
								this.touchDirection = LInput.UPPER_RIGHT;
							} else if ((touchX < halfWidth)
									&& (touchY >= halfHeight)) {
								this.touchDirection = LInput.LOWER_LEFT;
							} else {
								this.touchDirection = LInput.LOWER_RIGHT;
							}
							onTouchDown(touchEvent);
							return true;
						}
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_OUTSIDE:
					case MotionEvent.ACTION_CANCEL:
						if (idx == 0) {
							isMoving = false;
							touchDown[code] = false;
							touchReleased[releasedTouch] = code;
							touchDX = lastTouchX = touchX;
							touchDY = lastTouchY = touchY;
							releasedTouch++;
							onTouchUp(touchEvent);
							return true;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (idx == 0) {
							offsetMoveX = touchX;
							offsetMoveY = touchY;
							if (Math.abs(offsetTouchX - offsetMoveX) > 5
									|| Math.abs(offsetTouchY - offsetMoveY) > 5) {
								isMoving = true;
								onTouchMove(touchEvent);
								return true;
							}
						}
						break;
					case MultitouchUtils.ACTION_POINTER_1_DOWN:
						if (idx == 0) {
							onTouchDown(touchEvent);
							return true;
						}
						break;
					case MultitouchUtils.ACTION_POINTER_1_UP:
						if (idx == 0) {
							onTouchUp(touchEvent);
							return true;
						}
						break;
					case MultitouchUtils.ACTION_POINTER_2_DOWN:
						if (idx == 1) {
							onTouchDown(touchEvent);
							return true;
						}
						break;
					case MultitouchUtils.ACTION_POINTER_2_UP:
						if (idx == 1) {
							onTouchUp(touchEvent);
							return true;
						}
						break;
					case MultitouchUtils.ACTION_POINTER_3_DOWN:
						if (idx == 2) {
							onTouchDown(touchEvent);
							return true;
						}
						break;
					case MultitouchUtils.ACTION_POINTER_3_UP:
						if (idx == 2) {
							onTouchUp(touchEvent);
							return true;
						}
						break;
					}
				}
			} else {
				touchX = Math.round(e.getX() / LSystem.scaleWidth);
				touchY = Math.round(e.getY() / LSystem.scaleHeight);
				touchEvent.x = touchX;
				touchEvent.y = touchY;
				touchEvent.action = code;
				touchEvent.pointerCount = 0;
				switch (code) {
				case MotionEvent.ACTION_DOWN:
					isMoving = false;
					offsetTouchX = touchX;
					offsetTouchY = touchY;
					touchDown[code] = true;
					touchPressed[pressedTouch] = code;
					touchDX = lastTouchX = touchX;
					touchDY = lastTouchY = touchY;
					pressedTouch++;
					if ((touchX < halfWidth) && (touchY < halfHeight)) {
						this.touchDirection = LInput.UPPER_LEFT;
					} else if ((touchX >= halfWidth) && (touchY < halfHeight)) {
						this.touchDirection = LInput.UPPER_RIGHT;
					} else if ((touchX < halfWidth) && (touchY >= halfHeight)) {
						this.touchDirection = LInput.LOWER_LEFT;
					} else {
						this.touchDirection = LInput.LOWER_RIGHT;
					}
					onTouchDown(touchEvent);
					return true;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
					isMoving = false;
					touchDown[code] = false;
					touchReleased[releasedTouch] = code;
					touchDX = lastTouchX = touchX;
					touchDY = lastTouchY = touchY;
					releasedTouch++;
					onTouchUp(touchEvent);
					return true;
				case MotionEvent.ACTION_MOVE:
					offsetMoveX = touchX;
					offsetMoveY = touchY;
					if (Math.abs(offsetTouchX - offsetMoveX) > 5
							|| Math.abs(offsetTouchY - offsetMoveY) > 5) {
						isMoving = true;
						onTouchMove(touchEvent);
						return true;
					}
				}
			}
		} catch (Exception ex) {
			pressedTouch = 0;
			releasedTouch = 0;
			Log.d(getName(), "on Touch !", ex);
		}
		return false;

	}

	/**
	 * 触摸屏按下
	 * 
	 * @param e
	 * @return
	 */
	public abstract void onTouchDown(Touch e);

	/**
	 * 触摸屏放开
	 * 
	 * @param e
	 * @return
	 */
	public abstract void onTouchUp(Touch e);

	/**
	 * 在触摸屏上移动
	 * 
	 * @param e
	 */
	public abstract void onTouchMove(Touch e);

	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	public void onOptionsMenuClosed(Menu menu) {

	}

	public void onResume() {

	}

	public void onPause() {

	}

	private void gravityToKey(SensorDirection d) {
		setKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
		setKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
		setKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
		setKeyUp(KeyEvent.KEYCODE_DPAD_UP);

		if (d == SensorDirection.LEFT) {
			setKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
		} else if (d == SensorDirection.RIGHT) {
			setKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);
		} else if (d == SensorDirection.UP) {
			setKeyDown(KeyEvent.KEYCODE_DPAD_UP);
		} else if (d == SensorDirection.DOWN) {
			setKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
		}
	}

	/**
	 * 手机震动值
	 * 
	 * @param force
	 */
	public void onShakeChanged(float force) {

	}

	public void onSensorChanged(android.hardware.SensorEvent e) {
		if (isLock || isClose) {
			return;
		}

		if (e.sensor.getType() != android.hardware.Sensor.TYPE_ACCELEROMETER) {
			return;
		}
		synchronized (this) {

			long curTime = System.currentTimeMillis();

			if ((curTime - lastUpdate) > 30) {

				long diffTime = (curTime - lastUpdate);

				lastUpdate = curTime;
				currentX = e.values[0];
				currentY = e.values[1];
				currentZ = e.values[2];

				currenForce = Math.abs(currentX + currentY + currentZ - lastX
						- lastY - lastZ)
						/ diffTime * 10000;

				if (currenForce > 500) {
					onShakeChanged(currenForce);
				}

				float absx = Math.abs(currentX);
				float absy = Math.abs(currentY);
				float absz = Math.abs(currentZ);

				if (LSystem.SCREEN_LANDSCAPE) {

					// PS:横屏时上下朝向非常容易同左右混淆,暂未发现有效的解决方案(不是无法区分,
					// 而是横屏操作游戏时用户难以保持水平,而一晃就是【左摇右摆】……) ,所以现阶段横屏只取左右
					double type = Math.atan(currentY / currentZ);
					if (type <= -accelOffset) {
						if (landscapeUpdate > -accelOffset) {
							direction = SensorDirection.LEFT;
						}
					} else if (type >= accelOffset) {
						if (landscapeUpdate < accelOffset) {
							direction = SensorDirection.RIGHT;
						}
					} else {
						if (landscapeUpdate <= -accelOffset) {
							direction = SensorDirection.LEFT;
						} else if (landscapeUpdate >= accelOffset) {
							direction = SensorDirection.RIGHT;
						}
					}
					landscapeUpdate = type;

				} else {
					if (absx > absy && absx > absz) {
						if (currentX > accelOffset) {
							direction = SensorDirection.LEFT;

						} else {
							direction = SensorDirection.RIGHT;
						}
					} else if (absz > absx && absz > absy) {
						if (currentY < -accelOffset) {
							direction = SensorDirection.DOWN;
						} else {
							direction = SensorDirection.UP;
						}
					}
				}
				lastX = currentX;
				lastY = currentY;
				lastZ = currentZ;

				if (isGravityToKey) {
					gravityToKey(direction);
				}

				onDirection(direction, currentX, currentY, currentZ);
			}

		}
	}

	public void onDirection(SensorDirection direction, float x, float y, float z) {

	}

	public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

	}

	public void move(double x, double y) {
		this.touchX = (int) x;
		this.touchY = (int) y;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getHalfWidth() {
		return halfWidth;
	}

	public int getHalfHeight() {
		return halfHeight;
	}

	public int getTouchDirection() {
		return touchDirection;
	}

	public SensorDirection getSensorDirection() {
		return direction;
	}

	public float getAccelOffset() {
		return accelOffset;
	}

	public void setAccelOffset(float accelOffset) {
		this.accelOffset = accelOffset;
	}

	public float getLastAccelX() {
		return lastX;
	}

	public float getLastAccelY() {
		return lastY;
	}

	public float getLastAccelZ() {
		return lastZ;
	}

	public float getAccelX() {
		return currentX;
	}

	public float getAccelY() {
		return currentY;
	}

	public float getAccelZ() {
		return currentZ;
	}

	public boolean isGravityToKey() {
		return isGravityToKey;
	}

	public void setGravityToKey(boolean isGravityToKey) {
		this.isGravityToKey = isGravityToKey;
	}

}
