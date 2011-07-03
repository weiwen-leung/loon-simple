package org.loon.framework.javase.game.utils;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
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
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1
 */
public class ScreenUtils {

	final private static GraphicsEnvironment environment = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	final private static GraphicsDevice graphicsDevice = environment
			.getDefaultScreenDevice();

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

}
