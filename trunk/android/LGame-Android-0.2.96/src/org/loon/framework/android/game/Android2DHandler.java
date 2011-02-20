package org.loon.framework.android.game;

import java.util.LinkedList;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.geom.Dimension;
import org.loon.framework.android.game.media.sound.AssetsSoundManager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
 * @version 0.1.2
 */
public class Android2DHandler implements IAndroid2DHandler {

	private int width, height,maxWidth,maxHeight;

	private LGameAndroid2DActivity activity;

	private Context context;

	private Window window;

	private WindowManager windowManager;

	private AssetsSoundManager asm;

	private View view;

	private Screen currentControl;

	private final LinkedList<Screen> screens;

	public Android2DHandler(LGameAndroid2DActivity activity,
			LGameAndroid2DView view, boolean landscape, Mode mode) {
		this.activity = activity;
		if (landscape) {
			activity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			activity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		this.context = activity.getApplicationContext();
		this.window = activity.getWindow();
		this.windowManager = activity.getWindowManager();
		this.view = view;
		this.screens = new LinkedList<Screen>();

		Dimension d = getScreenDimension();

		LSystem.SCREEN_LANDSCAPE = landscape;

		this.maxWidth = d.getWidth();
		this.maxHeight = d.getHeight();

		if (landscape && (d.getWidth() > d.getHeight())) {
			maxWidth = d.getWidth();
			maxHeight = d.getHeight();
		} else if (landscape && (d.getWidth() < d.getHeight())) {
			maxHeight = d.getWidth();
			maxWidth = d.getHeight();
		} else if (!landscape && (d.getWidth() < d.getHeight())) {
			maxWidth = d.getWidth();
			maxHeight = d.getHeight();
		} else if (!landscape && (d.getWidth() > d.getHeight())) {
			maxHeight = d.getWidth();
			maxWidth = d.getHeight();
		}

		if (landscape) {
			this.width = maxWidth >= LSystem.MAX_SCREEN_WIDTH ? LSystem.MAX_SCREEN_WIDTH
					: maxWidth;
			this.height = maxHeight >= LSystem.MAX_SCREEN_HEIGHT ? LSystem.MAX_SCREEN_HEIGHT
					: maxHeight;
		} else {
			this.width = maxWidth >= LSystem.MAX_SCREEN_HEIGHT ? LSystem.MAX_SCREEN_HEIGHT
					: maxWidth;
			this.height = maxHeight >= LSystem.MAX_SCREEN_WIDTH ? LSystem.MAX_SCREEN_WIDTH
					: maxHeight;
		}

		if (mode == Mode.Fill) {
			LSystem.scaleWidth = ((float) maxWidth) / width;
			LSystem.scaleHeight = ((float) maxHeight) / height;

		} else {
			LSystem.scaleWidth = 1;
			LSystem.scaleHeight = 1;
		}

		LSystem.screenRect = new RectBox(0, 0, width, height);

		Log.i("Android2DSize", width + "," + height);
	}

	/**
	 * 判断当前游戏屏幕是否需要拉伸
	 * 
	 * @return
	 */
	public boolean isScale() {
		return LSystem.scaleWidth != 1 || LSystem.scaleHeight != 1;
	}

	/**
	 * 返回AssetsSoundManager
	 * 
	 * @return
	 */
	public AssetsSoundManager getAssetsSound() {
		if (this.asm == null) {
			this.asm = AssetsSoundManager.getInstance();
		}
		return asm;
	}

	/**
	 * 获得窗体实际坐标
	 * 
	 * @return
	 */
	public Dimension getScreenDimension() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new Dimension((int) dm.xdpi, (int) dm.ydpi,
				(int) dm.widthPixels, (int) dm.heightPixels);

	}

	public Screen getCurrentScreen() {
		return currentControl;
	}

	public void initScreen() {
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(android.view.Window.FEATURE_NO_TITLE);
		try {
			window.setBackgroundDrawable(null);
		} catch (Exception e) {
		}
	}

	public void runFirstScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getFirst();
			if (o != currentControl) {
				setScreen((Screen) o, false);
			}
		}
	}

	public void runLastScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getLast();
			if (o != currentControl) {
				setScreen((Screen) o, false);
			}
		}
	}

	public void runPreviousScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i - 1 > -1) {
						setScreen((Screen) screens.get(i - 1), false);
						return;
					}
				}
			}
		}
	}

	public void runNextScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i + 1 < size) {
						setScreen((Screen) screens.get(i + 1), false);
						return;
					}
				}
			}
		}
	}

	public void runIndexScreen(int index) {
		int size = screens.size();
		if (size > 0 && index > -1 && index < size) {
			Object o = screens.get(index);
			if (currentControl != o) {
				setScreen((Screen) screens.get(index), false);
			}
		}
	}

	public void addScreen(final Screen screen) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [Screen] instance !");
		}
		screens.add(screen);
	}

	public LinkedList<Screen> getScreens() {
		return screens;
	}

	public int getScreenCount() {
		return screens.size();
	}

	public void setScreen(final Screen screen) {
		setScreen(screen, true);
	}

	public void setScreen(final Screen screen, boolean put) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [Screen] instance !");
		}
		screen.setOnLoadState(false);
		synchronized (this) {
			if (LGameAndroid2DView.currentControl == null) {
				LGameAndroid2DView.currentControl = screen;
				currentControl = screen;
			} else {
				synchronized (LGameAndroid2DView.currentControl) {
					if (LGameAndroid2DView.currentControl != null) {
						LGameAndroid2DView.currentControl.destroy();
						LGameAndroid2DView.currentControl = screen;
						currentControl = screen;
					}
				}
			}
			synchronized (screen) {
				if (screen instanceof EmulatorListener) {
					if (view instanceof LGameAndroid2DView) {
						LGameAndroid2DView l2d = (LGameAndroid2DView) view;
						l2d.update();
						l2d.setEmulatorListener((EmulatorListener) screen);
					}
				} else {
					if (view instanceof LGameAndroid2DView) {
						((LGameAndroid2DView) view).setEmulatorListener(null);
					}
				}
				Thread load = null;
				try {
					load = new Thread() {
						public void run() {
							for (; LSystem.isLogo;) {
								try {
									Thread.sleep(60);
								} catch (InterruptedException e) {
								}
							}
							screen.onLoad();
							screen.setOnLoadState(true);
						}
					};
					load.setPriority(Thread.NORM_PRIORITY - 1);
					load.start();
				} catch (Exception ex) {
					Log.d(screen.getName() + " onLoad", ex.getMessage());
				} finally {
					load = null;
				}
				if (put) {
					screens.add(screen);
				}
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public View getView() {
		return view;
	}

	public RectBox getScreenBox() {
		return LSystem.screenRect;
	}

	public LGameAndroid2DActivity getLGameActivity() {
		return activity;
	}

	public Context getContext() {
		return context;
	}

	public Window getWindow() {
		return window;
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}

	public boolean onTouchEvent(MotionEvent e) {
		try {
			return currentControl.onTouchEvent(e);
		} catch (Exception ex) {

		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		try {
			return currentControl.onKeyDownEvent(keyCode, e);
		} catch (Exception ex) {

		}
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		try {
			return currentControl.onKeyUpEvent(keyCode, e);
		} catch (Exception ex) {

		}
		return false;
	}

	public boolean onTrackballEvent(MotionEvent e) {
		try {
			return currentControl.onTrackballEvent(e);
		} catch (Exception ex) {

		}
		return false;
	}

	public void destroy() {
		if (currentControl != null) {
			currentControl.destroy();
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (currentControl != null) {
			currentControl.onAccuracyChanged(sensor, accuracy);
		}
	}

	public void onSensorChanged(SensorEvent event) {
		if (currentControl != null) {
			currentControl.onSensorChanged(event);
		}
	}

	public void onPause() {
		if (currentControl != null) {
			currentControl.onPause();
		}
	}

	public void onResume() {
		if (currentControl != null) {
			currentControl.onResume();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (currentControl != null) {
			return currentControl.onCreateOptionsMenu(menu);
		}
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (currentControl != null) {
			return currentControl.onOptionsItemSelected(item);
		}
		return false;
	}

	public void onOptionsMenuClosed(Menu menu) {
		if (currentControl != null) {
			currentControl.onOptionsMenuClosed(menu);
		}
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

}
