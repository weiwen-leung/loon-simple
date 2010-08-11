package org.loon.framework.android.game;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.geom.Dimension;
import org.loon.framework.android.game.media.sound.AssetsSoundManager;

import android.content.Context;
import android.content.pm.ActivityInfo;

import android.util.Log;
import android.view.KeyEvent;
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

	private IScreen control;

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
		Dimension d = activity.getScreenDimension();

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

		Log.i("Android2DSize", width + "," + height);
	}

	/**
	 * 返回AssetsSoundManager
	 * 
	 * @return
	 */
	public AssetsSoundManager getAssetsSound() {
		if (this.asm == null) {
			this.asm = AssetsSoundManager.getInstance(activity);
		}
		return asm;
	}

	public void initScreen() {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		try {
			window.setBackgroundDrawable(null);
		} catch (Exception e) {
		}
	}

	public void setScreen(final IScreen screen) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [IScreen] instance !");
		}
		LGameAndroid2DView.control = screen;
		this.control = screen;
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
			return control.onTouchEvent(e);
		} catch (Exception ex) {

		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		try {
			return control.onDownUpEvent(keyCode, e);
		} catch (Exception ex) {

		}
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		try {
			return control.onKeyUp(keyCode, e);
		} catch (Exception ex) {

		}
		return false;
	}

	public void destroy() {
		if (control != null) {
			control.destroy();
		}
	}
}
