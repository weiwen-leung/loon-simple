package org.loon.framework.android.game.core.graphics;

import java.util.ArrayList;
import java.util.LinkedList;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.action.sprite.SpriteListener;
import org.loon.framework.android.game.action.sprite.Sprites;
import org.loon.framework.android.game.core.EmulatorButtons;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;
import org.loon.framework.android.game.core.timer.LTimerContext;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

/**
 * 
 * Copyright 2008 - 2010
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
 * @version 0.1.1
 */
public interface IScreen {

	public final static int SCREEN_NOT_REPAINT = 0;

	public final static int SCREEN_BITMAP_REPAINT = -1;

	public final static int SCREEN_CANVAS_REPAINT = -2;

	/**
	 * 取出第一个Screen并执行
	 * 
	 */
	public void runFirstScreen();

	/**
	 * 取出最后一个Screen并执行
	 * 
	 */
	public void runLastScreen();

	/**
	 * 运行指定位置的Screen
	 * 
	 * @param index
	 */
	public void runIndexScreen(int index);

	/**
	 * 运行当前Screen的前一个Screen
	 * 
	 */
	public void runPreviousScreen();

	/**
	 * 运行当前Screen的下一个Screen
	 * 
	 */
	public void runNextScreen();

	/**
	 * 向缓存中添加Screen数据，但是不立即执行
	 * 
	 * @param screen
	 */
	public void addScreen(IScreen screen);

	/**
	 * 获得保存的Screen列表
	 * 
	 * @return
	 */
	public LinkedList<IScreen> getScreens();

	/**
	 * 获得缓存的Screen总数
	 * 
	 * @return
	 */
	public int getScreenCount();

	/**
	 * 传递封装后手机的倾斜方向,以及x,y,z参数给用户
	 * 
	 * @param direction
	 * @param x
	 * @param y
	 * @param z
	 */
	public void onDirection(SensorDirection direction, float x, float y, float z);

	public void onAccuracyChanged(Sensor sensor, int accuracy);

	public void onSensorChanged(SensorEvent event);

	public void onResume();

	public void onPause();

	public boolean onCreateOptionsMenu(Menu menu);

	public boolean onOptionsItemSelected(MenuItem item);

	public void onOptionsMenuClosed(Menu menu);

	public boolean onTrackballEvent(MotionEvent e);

	public boolean onKeyDownEvent(int keyCode, KeyEvent e);

	public boolean onKeyUpEvent(int keyCode, KeyEvent e);

	public boolean onTouchEvent(MotionEvent e);

	/**
	 * 获得重力感应是否已与键盘联动（重力方向转为键盘方向）
	 * 
	 * @return
	 */
	public boolean isGravityToKey();

	/**
	 * 要求重力感应与键盘事件联动
	 * 
	 * @param isGravityToKey
	 */
	public void setGravityToKey(boolean isGravityToKey);

	/**
	 * 返回精灵监听
	 * 
	 * @return
	 */
	public SpriteListener getSprListerner();

	/**
	 * 监听Screen中精灵
	 * 
	 * @param sprListerner
	 */
	public void setSprListerner(SpriteListener sprListerner);

	/**
	 * 获得当前Screen类名
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 初始化时加载的数据
	 */
	public void onLoad();

	/**
	 * 判断是否点中指定精灵
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean onClick(ISprite sprite);

	/**
	 * 判断是否点中指定组件
	 * 
	 * @param component
	 * @return
	 */
	public boolean onClick(LComponent component);

	/**
	 * 设定模拟按钮监听器
	 */
	public void setEmulatorListener(EmulatorListener emulator);

	/**
	 * 设定模拟按钮组是否显示
	 * 
	 * @param visible
	 */
	public void emulatorButtonsVisible(boolean visible);

	/**
	 * 返回模拟按钮集合
	 * 
	 * @return
	 */
	public EmulatorButtons getEmulatorButtons();

	/**
	 * 广告是否显示
	 * 
	 * @return
	 */
	public abstract boolean isVisibleAD();

	/**
	 * 隐藏广告
	 */
	public abstract void hideAD();

	/**
	 * 显示广告
	 */
	public abstract void showAD();

	/**
	 * 选择的结果
	 * 
	 * @return
	 */
	public abstract int getAndroidSelect();

	/**
	 * 播放Assets中的音频文件
	 * 
	 * @param file
	 * @param loop
	 */
	public abstract void playtAssetsMusic(final String file, final boolean loop);

	/**
	 * 设置Assets中的音频文件音量
	 * 
	 * @param vol
	 */
	public void resetAssetsMusic(final int vol);

	/**
	 * 重置Assets中的音频文件
	 * 
	 */
	public abstract void resetAssetsMusic();

	/**
	 * 中断Assets中的音频文件
	 */
	public abstract void stopAssetsMusic();

	/**
	 * 中断Assets中指定索引的音频文件
	 * 
	 * @param index
	 */
	public abstract void stopAssetsMusic(int index);

	/**
	 * 移动坐标
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void move(double x, double y);

	/**
	 * 访问指定的网站页面
	 * 
	 * @param url
	 */
	public abstract void openBrowser(final String url);

	/**
	 * 弹出一个Android选框，用以显示指定的异常数据
	 * 
	 * @param ex
	 */
	public abstract void showAndroidExceptionMessageBox(final Exception ex);

	/**
	 * 弹出一个Android选框，用以显示指定的进度条
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public abstract AlertDialog showAndroidProgress(final String title,
			final String message);

	/**
	 * 弹出一个Android选框，用以显示指定的信息
	 * 
	 * @param message
	 */
	public abstract void showAndroidAlert(final String title,
			final String message);

	/**
	 * 弹出一个Android选择框，并返回选择结果
	 * 
	 * @param text
	 * @return
	 */
	public abstract int showAndroidSelectMessageBox(final String title,
			final String[] text);

	/**
	 * 注销资源
	 */
	public abstract void destroy();

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public abstract void setBackground(Bitmap screen);

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public abstract void setBackground(LImage screen);

	/**
	 * 设定背景图像
	 * 
	 * @param fileName
	 * @param transparency
	 */
	public abstract void setBackground(String fileName, boolean transparency);

	/**
	 * 设定背景图像
	 * 
	 * @param fileName
	 */
	public abstract void setBackground(String fileName);

	/**
	 * 设定背景颜色
	 * 
	 * @param color
	 */
	public void setBackground(LColor color);

	/**
	 * 返回背景图像
	 * 
	 * @return
	 */
	public abstract Bitmap getBackground();

	/**
	 * 设定刷新率
	 * 
	 * @param fps
	 */
	public abstract void setFPS(long fps);

	/**
	 * 返回当前刷新率
	 * 
	 * @return
	 */
	public abstract long getFPS();

	/**
	 * 返回最大刷新率
	 * 
	 * @return
	 */
	public abstract long getMaxFPS();

	/**
	 * 返回游戏组件桌面
	 * 
	 * @return
	 */
	public Desktop getDesktop();

	/**
	 * 返回与指定类匹配的组件
	 * 
	 * @param clazz
	 * @return
	 */
	public ArrayList<LComponent> getComponents(Class<? extends LComponent> clazz);

	/**
	 * 返回位于屏幕顶部的组件
	 * 
	 * @return
	 */
	public LComponent getTopComponent();

	/**
	 * 返回位于屏幕底部的组件
	 * 
	 * @return
	 */
	public LComponent getBottomComponent();

	/**
	 * 返回位于屏幕顶部的图层
	 * 
	 * @return
	 */
	public Layer getTopLayer();

	/**
	 * 返回位于屏幕底部的图层
	 * 
	 * @return
	 */
	public Layer getBottomLayer();

	/**
	 * 返回游戏精灵列表
	 * 
	 * @return
	 */
	public Sprites getSprites();

	/**
	 * 返回所有指定类产生的精灵
	 * 
	 * @param clazz
	 * @return
	 */
	public ArrayList<ISprite> getSprites(Class<? extends ISprite> clazz);

	/**
	 * 返回位于数组顶部的精灵
	 * 
	 * @return
	 */
	public ISprite getTopSprite();

	/**
	 * 返回位于数组底部的精灵
	 * 
	 * @return
	 */
	public ISprite getBottomSprite();

	/**
	 * 获得背景显示模式
	 */
	public int getRepaintMode();

	/**
	 * 设定背景刷新模式
	 * 
	 * @param mode
	 */
	public void setRepaintMode(int mode);

	/**
	 * 添加游戏组件
	 * 
	 * @param comp
	 */
	public abstract void add(LComponent comp);

	/**
	 * 添加游戏精灵
	 * 
	 * @param sprite
	 */
	public abstract void add(ISprite sprite);

	/**
	 * 删除组件
	 * 
	 * @param comp
	 */
	public abstract void remove(LComponent comp);

	/**
	 * 删除指定类产生的组件
	 * 
	 * @param clazz
	 */
	public abstract void removeComponent(Class<? extends LComponent> clazz);

	/**
	 * 删除精灵
	 * 
	 * @param sprite
	 */
	public abstract void remove(ISprite sprite);

	/**
	 * 删除指定的精灵类
	 * 
	 * @param clazz
	 */
	public abstract void removeSprite(Class<? extends ISprite> clazz);

	/**
	 * 清空所有组件与精灵
	 * 
	 */
	public abstract void removeAll();

	/**
	 * 触摸屏按下
	 * 
	 * @param e
	 * @return
	 */
	public abstract void onTouchDown(MotionEvent e);

	/**
	 * 触摸屏放开
	 * 
	 * @param e
	 * @return
	 */
	public abstract void onTouchUp(MotionEvent e);

	public abstract void onTouchMove(MotionEvent e);

	public boolean onKeyDown(int keyCode, KeyEvent e);

	public boolean onKeyUp(int keyCode, KeyEvent e);

	public abstract boolean next();

	/**
	 * 增减一个线程事件
	 * 
	 * @param runnable
	 */
	public abstract void callEvent(Runnable runnable);

	/**
	 * 暂停指定的线程事件
	 * 
	 * @param runnable
	 */
	public abstract void callEventWait(Runnable runnable);

	/**
	 * 中断所有线程事件
	 * 
	 */
	public abstract void callEventInterrupt();

	/**
	 * 运行线程事件
	 * 
	 */
	public abstract void callEvents();

	/**
	 * 运行线程事件
	 * 
	 */
	public abstract void callEvents(boolean execute);

	public abstract LImage getWebImage(String url, boolean transparency);

	public abstract LImage getImage(String fileName, boolean transparency);

	public abstract LImage[] getSplitImages(String fileName, int row, int col,
			boolean transparency);

	public abstract LImage[] getSplitImages(LImage image, int row, int col);

	public abstract void draw(LGraphics g);

	public abstract void alter(LTimerContext timer);

	public abstract void setScreen(IScreen screen);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getHalfWidth();

	public abstract int getHalfHeight();

	/**
	 * 对外的线程暂停器
	 * 
	 * @param timeMillis
	 */
	public abstract void pause(long timeMillis);

	public abstract Point getTouch();

	public abstract boolean isPaused();

	public abstract boolean isClick();

	public abstract int getTouchX();

	public abstract int getTouchY();

	public abstract int getTouchDX();

	public abstract int getTouchDY();

	public abstract int getTouchDirection();

	// 手机倾斜角度
	public abstract double getSensorInclination();

	// 手机型斜方向
	public SensorDirection getSensorDirection();

	// 重力倾斜的X坐标
	public abstract float getAccelX();

	// 重力倾斜的Y坐标
	public abstract float getAccelY();

	// 重力倾斜的Z坐标
	public abstract float getAccelZ();

	public abstract void createUI(LGraphics g);

	public abstract void runTimer(LTimerContext timer);

}
