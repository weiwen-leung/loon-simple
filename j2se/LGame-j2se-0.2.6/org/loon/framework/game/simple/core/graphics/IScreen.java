package org.loon.framework.game.simple.core.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.loon.framework.game.simple.action.Action;
import org.loon.framework.game.simple.action.IAction;
import org.loon.framework.game.simple.action.sprite.ISprite;
import org.loon.framework.game.simple.action.sprite.Sprites;
import org.loon.framework.game.simple.core.IHandler;
import org.loon.framework.game.simple.core.LObject;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.timer.LTimerContext;
import org.loon.framework.game.simple.utils.collection.store.IStore;
import org.loon.framework.game.simple.utils.log.Level;

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
public interface IScreen extends MouseListener, MouseMotionListener,
		KeyListener, FocusListener {

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
	 * 返回当前窗体线程ID
	 * 
	 * @return
	 */
	public abstract int getID();

	/**
	 * 销毁本窗体占用的所有资源
	 * 
	 */
	public void destroy();

	/**
	 * 尝试释放函数内资源
	 * 
	 */
	public void dispose();

	/**
	 * 重新设定窗体大小
	 * 
	 */
	public void resize();

	/**
	 * 播放指定音乐
	 * 
	 * @param fileName
	 */
	public abstract void playSound(String fileName);

	/**
	 * 停止当前音乐播放
	 * 
	 */
	public abstract void stopSound();

	/**
	 * 设定当前音乐音量
	 * 
	 * @param volume
	 */
	public abstract void setSoundVolume(int volume);

	/**
	 * 判断是否点中指定精灵
	 * 
	 * @param sprite
	 * @return
	 */
	public abstract boolean onClick(ISprite sprite);

	/**
	 * 判断是否点中指定组件
	 * 
	 * @param component
	 * @return
	 */
	public abstract boolean onClick(LComponent component);

	/**
	 * 将鼠标居中
	 * 
	 */
	public abstract void mouseCenter();

	/**
	 * 绘图器抽象接口
	 * 
	 * @param g
	 */
	public abstract void draw(LGraphics g);

	/**
	 * 键盘按下抽象接口
	 * 
	 * @param e
	 */
	public abstract void onKey(KeyEvent e);

	/**
	 * 键盘放开抽象接口
	 * 
	 * @param e
	 */
	public abstract void onKeyUp(KeyEvent e);

	/**
	 * 鼠标左键按下抽象接口
	 * 
	 * @param e
	 */
	public abstract void leftClick(MouseEvent e);

	/**
	 * 鼠标中间键按下抽象接口
	 * 
	 * @param e
	 */
	public abstract void middleClick(MouseEvent e);

	/**
	 * 鼠标右键按下抽象接口
	 * 
	 * @param e
	 */
	public abstract void rightClick(MouseEvent e);
	
	/**
	 * 设定游戏句柄
	 */
	public abstract void setupHandler(IHandler handler);

	/**
	 * 设定游戏屏幕
	 * 
	 * @param screen
	 */
	public abstract void setScreen(IScreen screen);

	/**
	 * 返回Screen存储器
	 * 
	 * @return
	 */
	public abstract IStore getScreenStore();

	/**
	 * 直接实例化指定类为游戏屏幕
	 * 
	 * @param clazz
	 */
	public abstract void makeScreen(Class clazz);

	/**
	 * 直接以指定参数，实例化指定类为游戏屏幕
	 * 
	 * @param clazz
	 * @param args
	 */
	public abstract void makeScreen(Class clazz, Object[] args);

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
	 * 返回当前游戏窗体背景图片
	 * 
	 * @return
	 */
	public abstract Image getBackground();

	/**
	 * 获得Screen的画面边界
	 * 
	 * @return
	 */
	public abstract Rectangle getBounds();

	/**
	 * 程序运行时间
	 * 
	 * @param timer
	 */
	public abstract void runTimer(final LTimerContext lTimer);

	/**
	 * 对外线程暂停器
	 * 
	 * @param timeMillis
	 */
	public abstract void pause(long timeMillis);

	/**
	 * 对外刷新器
	 * 
	 * @param timer
	 */
	public abstract void alter(LTimerContext timer);

	/**
	 * 点击鼠标左键
	 * 
	 * @return
	 */
	public abstract boolean leftClick();
	
	/**
	 * 是否触发了点击事件
	 * 
	 * @return
	 */
	public boolean isClick();
	/**
	 * 点击鼠标中间键(滚轴)
	 * 
	 * @return
	 */
	public abstract boolean middleClick();

	/**
	 * 点击鼠标右键
	 * 
	 * @return
	 */
	public abstract boolean rightClick();

	/**
	 * 创建当前UI
	 * 
	 * @param g
	 */
	public abstract void createUI(final LGraphics g);

	/**
	 * 居中指定对象
	 * 
	 * @param object
	 */
	public abstract void centerOn(final LObject object);

	/**
	 * 置顶指定对象
	 * 
	 * @param object
	 */
	public abstract void topOn(final LObject object);

	/**
	 * 左移指定对象
	 * 
	 * @param object
	 */
	public abstract void leftOn(final LObject object);

	/**
	 * 右移指定对象
	 * 
	 * @param object
	 */
	public abstract void rightOn(final LObject object);

	/**
	 * 置底指定对象
	 * 
	 * @param object
	 */
	public abstract void bottomOn(final LObject object);

	/**
	 * 添加键盘事件
	 * 
	 * @param keyCode
	 * @param action
	 */
	public abstract void addKeyEvents(int keyCode, String key, IAction action);

	/**
	 * 添加键盘事件
	 * 
	 * @param keyCode
	 * @param action
	 */
	public abstract void addKeyEvents(int keyCode, Action action);

	/**
	 * 删除键盘事件
	 * 
	 * @param keyCode
	 */
	public abstract void removeKeyEvents(int keyCode);

	/**
	 * 清空键盘事件
	 * 
	 */
	public abstract void clearKeyEvents();

	/**
	 * 添加鼠标事件
	 * 
	 * @param mouseCode
	 * @param action
	 */
	public abstract void addMouseEvents(int mouseCode, String key,
			IAction action);

	/**
	 * 添加鼠标事件
	 * 
	 * @param mouseCode
	 * @param action
	 */
	public abstract void addMouseEvents(int mouseCode, Action action);

	/**
	 * 删除鼠标事件
	 * 
	 * @param mouseCode
	 */
	public abstract void removeMouseEvents(int mouseCode);

	/**
	 * 清空鼠标事件
	 * 
	 */
	public abstract void clearMouseEvents();

	/**
	 * 窗体宽
	 * 
	 * @return
	 */
	public abstract int getWidth();

	/**
	 * 窗体高
	 * 
	 * @return
	 */
	public abstract int getHeight();

	/**
	 * 返回游戏输出设备
	 * 
	 * @return
	 */
	public LInput getInput();

	/**
	 * 返回游戏组件桌面
	 * 
	 * @return
	 */
	public Desktop getDesktop();

	/**
	 * 返回游戏精灵列表
	 * 
	 * @return
	 */
	public Sprites getSprites();

	/**
	 * 返回屏幕背景震动率
	 * 
	 * @return
	 */
	public abstract int getShakeNumber();

	/**
	 * 处理下一步事务
	 * 
	 * @return
	 */
	public abstract boolean next();

	/**
	 * 设定是否处理下一步事务
	 * 
	 * @param next
	 */
	public abstract void setNext(boolean next);

	/**
	 * 跳转到指定桢
	 * 
	 * @param i
	 */
	public abstract void setFrame(final int frame);

	/**
	 * 自动的键盘事件
	 * 
	 * @return
	 */
	public abstract Map getKeyMap();

	/**
	 * 自动的鼠标事件
	 * 
	 * @return
	 */
	public abstract Map getMouseMap();

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
	 * 删除精灵
	 * 
	 * @param sprite
	 */
	public abstract void remove(ISprite sprite);

	/**
	 * 清空所有组件与精灵
	 * 
	 */
	public abstract void removeAll();

	/**
	 * 设置主窗体小图标
	 * 
	 * @param icon
	 */
	public abstract void setFrameIcon(Image icon);

	/**
	 * 设置主窗体小图标
	 * 
	 * @param fileName
	 */
	public abstract void setFrameIcon(String fileName);

	/**
	 * 设置主窗口标题
	 * 
	 * @param title
	 */
	public abstract void setFrameTitle(String title);

	/**
	 * 添加AWT标准组件
	 * 
	 * @param component
	 */
	public abstract void addComponent(Component component);

	/**
	 * 添加AWT标准组件
	 * 
	 * @param component
	 * @param x
	 * @param y
	 */
	public abstract void addComponent(Component component, int x, int y);

	/**
	 * 添加AWT标准组件
	 * 
	 * @param component
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public abstract void addComponent(Component component, int x, int y, int w,
			int h);

	/**
	 * 删除AWT标准组件
	 * 
	 * @param component
	 */
	public abstract void removeComponent(Component component);

	/**
	 * 删除AWT标准组件
	 * 
	 * @param index
	 */
	public abstract void removeComponent(int index);

	/**
	 * 命令操作系统当开指定网页
	 * 
	 * @param url
	 */
	public abstract boolean openBrowser(String url);

	/**
	 * 返回当前窗体画面图
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public abstract BufferedImage getScreenImage();

	/**
	 * 返回指定大小的当前窗体画面图
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public abstract BufferedImage getScreenImage(int w, int h);

	/**
	 * 保存当前Screen图像到指定位置
	 * 
	 * @param fileName
	 */
	public abstract void saveScreenImage(String fileName);

	/**
	 * 保存当前Screen图像
	 * 
	 */
	public abstract void saveScreenImage();

	/**
	 * 全屏窗体
	 * 
	 */
	public abstract void updateFullScreen();

	/**
	 * 全屏窗体为指定大小
	 * 
	 */
	public abstract void updateFullScreen(int w, int h);

	/**
	 * 全屏窗体为指定大小
	 * 
	 * @param d
	 */
	public abstract void updateFullScreen(Dimension d);

	/**
	 * 还原窗体
	 * 
	 */
	public abstract void updateNormalScreen();

	/**
	 * 设置是否录像当前Screen到指定位置(保存为FLV)
	 * 
	 */
	public abstract void setVideo(String fileName, boolean video);

	/**
	 * 设置是否录像当前Screen(保存为FLV)
	 * 
	 * @param video
	 */
	public abstract void setVideo(boolean video);

	/**
	 * 检查窗体默认对象中是否包含指定精灵
	 * 
	 */
	public abstract boolean contains(ISprite sprite);

	/**
	 * 检查窗体默认对象中是否包含指定组件
	 * 
	 * @param comp
	 * @return
	 */
	public abstract boolean contains(LComponent comp);

	/**
	 * 设定指定精灵到图层最前
	 * 
	 * @param sprite
	 */
	public abstract void sendSpriteToFront(ISprite sprite);

	/**
	 * 设定指定精灵到图层最后
	 * 
	 * @param sprite
	 */
	public abstract void sendSpriteToBack(ISprite sprite);

	/**
	 * 当触发文字输入事件
	 * 
	 * @param text
	 */
	public abstract void changeText(String text);

	/**
	 * 输出日志
	 * 
	 * @param message
	 */
	public abstract void log(String message);

	/**
	 * 输出日志
	 * 
	 * @param message
	 * @param tw
	 */
	public abstract void log(String message, Throwable tw);

	/**
	 * 记录info信息
	 * 
	 * @param message
	 */
	public abstract void info(String message);

	/**
	 * 记录info信息
	 * 
	 * @param message
	 * @param tw
	 */
	public abstract void info(String message, Throwable tw);

	/**
	 * 记录debug信息
	 * 
	 * @param message
	 */
	public abstract void debug(String message);

	/**
	 * 记录debug信息
	 * 
	 * @param message
	 * @param tw
	 */
	public abstract void debug(String message, Throwable tw);

	/**
	 * 记录warn信息
	 * 
	 * @param message
	 */
	public abstract void warn(String message);

	/**
	 * 记录warn信息
	 * 
	 * @param message
	 * @param tw
	 */
	public abstract void warn(String message, Throwable tw);

	/**
	 * 记录error信息
	 * 
	 * @param message
	 */
	public abstract void error(String message);

	/**
	 * 记录error信息
	 * 
	 * @param message
	 * @param tw
	 */
	public abstract void error(String message, Throwable tw);

	/**
	 * 设定是否在控制台显示日志
	 * 
	 * @param show
	 */
	public abstract void logShow(boolean show);

	/**
	 * 设定日志是否保存为文件
	 * 
	 * @param save
	 */
	public void logSave(boolean save);

	/**
	 * 日志保存地点
	 * 
	 * @param fileName
	 */
	public abstract void logFileName(String fileName);

	/**
	 * 设定日志等级
	 * 
	 * @param level
	 */
	public abstract void logLevel(int level);

	/**
	 * 设定日志等级
	 * 
	 * @param level
	 */
	public abstract void logLevel(Level level);

}
