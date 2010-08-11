package org.loon.framework.game.simple.core.graphics;

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.loon.framework.game.simple.core.store.IStore;
import org.loon.framework.game.simple.core.store.StoreEntry;
import org.loon.framework.game.simple.core.store.StoreFactory;
import org.loon.framework.game.simple.utils.StringUtils;
import org.loon.framework.game.simple.utils.ioc.reflect.Reflector;

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
public class ScreenManager {

	final private static IStore screenStore = StoreFactory.getInstance();

	final public static GraphicsEnvironment environment = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	final public static GraphicsDevice graphicsDevice = environment
			.getDefaultScreenDevice();

	final public static GraphicsConfiguration graphicsConfiguration = graphicsDevice
			.getDefaultConfiguration();

	/**
	 * 查询可用的屏幕设备
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public final static DisplayMode searchFullScreenModeDisplay(int width,
			int height) {
		return searchFullScreenModeDisplay(graphicsDevice, width, height);
	}

	/**
	 * 查询可用的屏幕设备
	 * 
	 * @param device
	 * @param width
	 * @param height
	 * @return
	 */
	public final static DisplayMode searchFullScreenModeDisplay(
			GraphicsDevice device, int width, int height) {
		DisplayMode displayModes[] = device.getDisplayModes();
		int currentDisplayPoint = 0;
		DisplayMode fullScreenMode = null;
		DisplayMode normalMode = device.getDisplayMode();
		DisplayMode adisplaymode[] = displayModes;
		int i = 0, length = adisplaymode.length;
		for (int j = length; i < j; i++) {
			DisplayMode mode = adisplaymode[i];
			if (mode.getWidth() == width && mode.getHeight() == height) {
				int point = 0;
				if (normalMode.getBitDepth() == mode.getBitDepth()) {
					point += 40;
				} else {
					point += mode.getBitDepth();
				}
				if (normalMode.getRefreshRate() == mode.getRefreshRate()) {
					point += 5;
				}
				if (currentDisplayPoint < point) {
					fullScreenMode = mode;
					currentDisplayPoint = point;
				}
			}
		}
		return fullScreenMode;
	}

	/**
	 * 将指定类实例化为IScreen
	 * 
	 * @param clazz
	 * @return
	 */
	public synchronized static IScreen makeScreen(Class clazz) {
		return makeScreen(clazz, null);
	}

	/**
	 * 将指定类，以指定参数实例化为IScreen
	 * 
	 * @param clazz
	 * @param args
	 * @return
	 */
	public synchronized static IScreen makeScreen(Class clazz, Object[] args) {
		Reflector reflector = Reflector.getReflector(clazz);
		if (reflector.isImplInterface(IScreen.class)) {
			StringBuffer ret = new StringBuffer();
			ret.append(clazz.getName());
			ret.append(":");
			ret.append(StringUtils.arrayToString(args));
			String key = ret.toString();
			StoreEntry entry = screenStore.getEntry(key);
			IScreen screen;
			if (entry == null) {
				screenStore.put(key, screen = (IScreen) reflector
						.newInstance(args));
			} else {
				screen = (IScreen) entry.getData();
			}
			return screen;
		} else {
			throw new RuntimeException(
					(clazz.getName() + " not Implemented IScreen ！").intern());
		}
	}

	/**
	 * 返回Screen存储器
	 * 
	 * @return
	 */
	public static IStore getScreenStore() {
		return screenStore;
	}

	/**
	 * 获得存储的游戏屏幕总数
	 * 
	 * @return
	 */
	public static int getScreenStoreCount() {
		return screenStore.size();
	}

}
