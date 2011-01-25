package org.loon.framework.android.game;

import java.util.LinkedList;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.geom.Dimension;
import org.loon.framework.android.game.media.sound.AssetsSoundManager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class Android2DHandler implements IAndroid2DHandler {

	private int width, height;

	private LGameAndroid2DActivity activity;

	private Context context;

	private Window window;

	private WindowManager windowManager;

	private AssetsSoundManager asm;

	private View view;

	private final RectBox rect;

	private IScreen currentControl;

	private final LinkedList<IScreen> screens;

	public Android2DHandler(LGameAndroid2DActivity activity,
			LGameAndroid2DView view, boolean landscape) {
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
		this.screens = new LinkedList<IScreen>();

		Dimension d = activity.getScreenDimension();

		LSystem.SCREEN_LANDSCAPE = landscape;

		if (landscape && (d.getWidth() > d.getHeight())) {
			this.width = d.getWidth();
			this.height = d.getHeight();
		} else if (landscape && (d.getWidth() < d.getHeight())) {
			this.height = d.getWidth();
			this.width = d.getHeight();
		} else if (!landscape && (d.getWidth() < d.getHeight())) {
			this.width = d.getWidth();
			this.height = d.getHeight();
		} else if (!landscape && (d.getWidth() > d.getHeight())) {
			this.height = d.getWidth();
			this.width = d.getHeight();
		}

		if (landscape) {
			this.width = width >= LSystem.MAX_SCREEN_WIDTH ? LSystem.MAX_SCREEN_WIDTH
					: width;
			this.height = height >= LSystem.MAX_SCREEN_HEIGHT ? LSystem.MAX_SCREEN_HEIGHT
					: height;
		} else {
			this.width = width >= LSystem.MAX_SCREEN_HEIGHT ? LSystem.MAX_SCREEN_HEIGHT
					: width;
			this.height = height >= LSystem.MAX_SCREEN_WIDTH ? LSystem.MAX_SCREEN_WIDTH
					: height;
		}

		this.rect = new RectBox(0, 0, width, height);

		Log.i("Android2DSize", width + "," + height);
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
				setScreen((IScreen) o, false);
			}
		}
	}

	public void runLastScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getLast();
			if (o != currentControl) {
				setScreen((IScreen) o, false);
			}
		}
	}

	public void runPreviousScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i - 1 > -1) {
						setScreen((IScreen) screens.get(i - 1), false);
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
						setScreen((IScreen) screens.get(i + 1), false);
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
				setScreen((IScreen) screens.get(index), false);
			}
		}
	}

	public void addScreen(final IScreen screen) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [IScreen] instance !");
		}
		screens.add(screen);
	}

	public LinkedList<IScreen> getScreens() {
		return screens;
	}

	public int getScreenCount() {
		return screens.size();
	}

	public void setScreen(final IScreen screen) {
		setScreen(screen, true);
	}

	public void setScreen(final IScreen screen, boolean put) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [IScreen] instance !");
		}
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
			synchronized (currentControl) {
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
							screen.onLoad();
						}
					};
					load.start();
				} catch (Exception ex) {
					Log.d(currentControl.getName() + " onLoad", ex
									.getMessage());
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
		return rect;
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

}
