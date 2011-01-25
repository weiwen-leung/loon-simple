package org.loon.framework.android.game;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;

import org.apache.http.HttpException;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.geom.Dimension;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
 * @version 0.1.0
 */
public abstract class LGameAndroid2DActivity extends Activity implements
		SensorEventListener {

	private static final String dealutKeywords = "game play video mobile app buy love down world";

	private SensorManager sensorManager;

	private Sensor sensorAccelerometer;

	private Sensor sensorMagnetic;

	private boolean visible, keyboardOpen, isLandscape, isBackLocked;

	private int androidSelect, orientation;

	// PS:由于Android线程机制缘故，所以应用到线程的组件需置于主线程外。
	private AlertDialog alert;

	// 此为针对部分用户提到的长按键盘掉FPS问题，增加的一个按键时间监听，
	// 以防止键盘事件被长时间连续执行。
	// （话说,长按键盘本质上等于不停执行for,不减速才怪……）
	private long keyTimeMillis;

	private LGameAndroid2DView view;

	private AdView adview;

	private IAndroid2DHandler gameHandler;

	private FrameLayout frameLayout;

	private boolean setupSensors;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		LSystem.gc();
		Log.i("Android2DActivity", "LGame 2D Engine Start");
		this.androidSelect = -2;
		this.frameLayout = new FrameLayout(LGameAndroid2DActivity.this);
		this.onMain();
	}

	/**
	 * 设定为支持重力感应
	 */
	public void setupGravity() {
		this.setupSensors = true;
	}

	private void initSensors() {
		try {
			if (sensorManager == null) {
				sensorManager = (SensorManager) this
						.getSystemService(SENSOR_SERVICE);
			}

			if (sensorAccelerometer == null) {
				sensorAccelerometer = sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

			}

			if (sensorMagnetic == null) {
				sensorMagnetic = sensorManager
						.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

			}

			boolean accelSupported = sensorManager.registerListener(this,
					sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);

			// 当不支持Accelerometer时卸载此监听
			if (!accelSupported) {
				sensorManager.unregisterListener(this, sensorAccelerometer);
			}

			this.sensorManager.registerListener(this, sensorMagnetic,
					SensorManager.SENSOR_DELAY_GAME);
		} catch (Exception ex) {

		}
	}

	private void stopSensors() {
		try {
			if (sensorManager != null) {
				this.sensorManager.unregisterListener(this);
				this.sensorManager = null;
			}
		} catch (Exception ex) {

		}
	}

	public abstract void onMain();

	public void initialization() {
		initialization(false, false, null);
	}

	public void initialization(boolean landscape) {
		initialization(landscape, false, null);
	}

	public void initialization(boolean landscape, boolean openAD,
			String publisherId) {
		initialization(landscape, openAD, Location.BOTTOM, publisherId);
	}

	public void initialization(boolean landscape, String publisherId) {
		initialization(landscape, true, Location.BOTTOM, publisherId);
	}

	public void initialization(Location lad, String publisherId) {
		initialization(false, true, lad, publisherId);
	}

	public void initialization(Bundle icicle, boolean landscape, Location lad,
			String publisherId) {
		initialization(landscape, true, lad, publisherId);
	}

	public void initialization(boolean landscape, boolean openAD, Location lad,
			String publisherId) {
		initialization(landscape, openAD, lad, publisherId, dealutKeywords);
	}

	public void initialization(Location lad, String publisherId,
			int requestInterval) {
		initialization(false, lad, publisherId, requestInterval);
	}

	public void initialization(boolean landscape, Location lad,
			String publisherId, int requestInterval) {
		initialization(landscape, true, lad, publisherId, dealutKeywords,
				requestInterval);
	}

	public void initialization(boolean landscape, boolean openAD, Location lad,
			String publisherId, String keywords) {
		initialization(landscape, openAD, lad, publisherId, keywords, 60);
	}

	public void initialization(boolean landscape, Location lad,
			String publisherId, String keywords) {
		initialization(landscape, true, lad, publisherId, keywords, 60);
	}

	public void initializationNotKeywords(boolean landscape, Location lad,
			String publisherId) {
		initialization(landscape, true, lad, publisherId, null, 60);
	}

	public void initialization(IScreen screen, int maxFps) {
		this.initialization();
		this.setScreen(screen);
		this.setFPS(maxFps);
		this.setShowFPS(false);
	}

	public void initialization(IScreen screen) {
		this.initialization(screen, LSystem.DEFAULT_MAX_FPS);
	}

	public void initialization(final boolean landscape, final boolean openAD,
			final Location lad, final String publisherId,
			final String keywords, final int requestInterval) {
		if (openAD) {
			// setVolumeControlStream(AudioManager.STREAM_MUSIC);
			AdManager.setPublisherId(publisherId);
			AdManager.setTestDevices(new String[] { "" });
			AdManager.setAllowUseOfLocation(true);
			view = new LGameAndroid2DView(LGameAndroid2DActivity.this,
					landscape);
			IHandler handler = view.getGameHandler();
			RelativeLayout mainLayout = new RelativeLayout(
					LGameAndroid2DActivity.this);
			RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(
					handler.getWidth(), handler.getHeight());
			if (landscape) {
				mainParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
			} else {
				mainParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
			}
			mainLayout.addView(view, mainParams);

			adview = new AdView(LGameAndroid2DActivity.this);
			if (keywords != null) {
				adview.setKeywords(keywords);
			}
			adview.setRequestInterval(requestInterval);
			adview.setGravity(Gravity.NO_GRAVITY);
			RelativeLayout rl = new RelativeLayout(LGameAndroid2DActivity.this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			if (lad == Location.LEFT) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
			} else if (lad == Location.RIGHT) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			} else if (lad == Location.TOP) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			} else {
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
			}
			// adview.setVisibility(View.GONE);

			visible = true;
			frameLayout.addView(mainLayout);
			rl.addView(adview, lp);
			frameLayout.addView(rl);
		} else {
			view = new LGameAndroid2DView(LGameAndroid2DActivity.this,
					landscape);
			IHandler handler = view.getGameHandler();
			RelativeLayout mainLayout = new RelativeLayout(
					LGameAndroid2DActivity.this);
			RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(
					handler.getWidth(), handler.getHeight());
			if (landscape) {
				mainParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
			} else {
				mainParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
			}
			mainLayout.addView(view, mainParams);
			frameLayout.addView(mainLayout);
		}
		if (setupSensors) {
			// 启动重力感应
			this.initSensors();
		}
		if (view != null) {
			gameHandler = view.getGameHandler();
		}
	}

	/**
	 * 取出第一个Screen并执行
	 * 
	 */
	public void runFirstScreen() {
		if (gameHandler != null) {
			gameHandler.runFirstScreen();
		}
	}

	/**
	 * 取出最后一个Screen并执行
	 */
	public void runLastScreen() {
		if (gameHandler != null) {
			gameHandler.runLastScreen();
		}
	}

	/**
	 * 运行指定位置的Screen
	 * 
	 * @param index
	 */
	public void runIndexScreen(int index) {
		if (gameHandler != null) {
			gameHandler.runIndexScreen(index);
		}
	}

	/**
	 * 运行自当前Screen起的上一个Screen
	 */
	public void runPreviousScreen() {
		if (gameHandler != null) {
			gameHandler.runPreviousScreen();
		}
	}

	/**
	 * 运行自当前Screen起的下一个Screen
	 */
	public void runNextScreen() {
		if (gameHandler != null) {
			gameHandler.runNextScreen();
		}
	}

	/**
	 * 向缓存中添加Screen数据，但是不立即执行
	 * 
	 * @param screen
	 */
	public void addScreen(IScreen screen) {
		if (gameHandler != null) {
			gameHandler.addScreen(screen);
		}
	}

	/**
	 * 切换当前窗体为指定Screen
	 * 
	 * @param screen
	 */
	public void setScreen(IScreen screen) {
		if (gameHandler != null) {
			this.gameHandler.setScreen(screen);
		}
	}

	/**
	 * 获得保存的Screen列表
	 * 
	 * @return
	 */
	public LinkedList<IScreen> getScreens() {
		if (gameHandler != null) {
			return gameHandler.getScreens();
		}
		return null;
	}

	/**
	 * 获得缓存的Screen总数
	 */
	public int getScreenCount() {
		if (gameHandler != null) {
			return gameHandler.getScreenCount();
		}
		return 0;
	}

	/**
	 * 假广告ID注入地址之一，用以蒙蔽不懂编程的工具破解者，对实际开发无意义。
	 * 
	 * @param ad
	 * @return
	 */
	public int setAD(String ad) {
		int result = 0;
		try {
			Class<LGameAndroid2DActivity> clazz = LGameAndroid2DActivity.class;
			Field[] field = clazz.getDeclaredFields();
			if (field != null) {
				result = field.length;
			}
		} catch (Exception e) {
		}
		return result + ad.length();
	}

	/**
	 * 设定游戏窗体最大值
	 */
	public void maxScreen(int w, int h) {
		LSystem.MAX_SCREEN_WIDTH = w;
		LSystem.MAX_SCREEN_HEIGHT = h;
	}

	/**
	 * 显示游戏窗体
	 */
	public void showScreen() {
		setContentView(frameLayout);
		try {
			getWindow().setBackgroundDrawable(null);
		} catch (Exception e) {

		}
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

	/**
	 * 返回布局器
	 * 
	 * @return
	 */
	public FrameLayout getFrameLayout() {
		return frameLayout;
	}

	/**
	 * 获得当前环境版本信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		try {
			String packName = getPackageName();
			return getPackageManager().getPackageInfo(packName, 0);
		} catch (Exception ex) {

		}
		return null;
	}

	/**
	 * 获得当前版本名
	 * 
	 * @return
	 */
	public String getVersionName() {
		PackageInfo info = getPackageInfo();
		if (info != null) {
			return info.versionName;
		}
		return null;
	}

	/**
	 * 获得当前程序版本号
	 * 
	 * @return
	 */
	public int getVersionCode() {
		PackageInfo info = getPackageInfo();
		if (info != null) {
			return info.versionCode;
		}
		return -1;
	}

	/**
	 * 广告是否显示
	 * 
	 * @return
	 */
	public boolean isVisibleAD() {
		return visible;
	}

	/**
	 * 隐藏广告
	 */
	public void hideAD() {
		try {
			if (adview != null) {
				Runnable runnable = new Runnable() {
					public void run() {
						adview.setVisibility(View.GONE);
						visible = false;
					}
				};
				runOnUiThread(runnable);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 显示广告
	 */
	public void showAD() {
		try {
			if (adview != null) {
				Runnable runnable = new Runnable() {
					public void run() {
						adview.setVisibility(View.VISIBLE);
						adview.requestFreshAd();
						visible = true;
					}
				};
				runOnUiThread(runnable);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 界面同步的浏览器访问
	 * 
	 * @param url
	 */
	public void openBrowser(final String url) {
		runOnUiThread(new Runnable() {
			public void run() {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}

	/**
	 * 选择的结果
	 * 
	 * @return
	 */
	public int getAndroidSelect() {
		return androidSelect;
	}

	/**
	 * 界面同步的Progress弹出
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public AlertDialog showAndroidProgress(final String title,
			final String message) {
		if (alert == null || !alert.isShowing()) {
			Runnable showProgress = new Runnable() {
				public void run() {
					alert = ProgressDialog.show(LGameAndroid2DActivity.this,
							title, message);
				}
			};
			runOnUiThread(showProgress);
		}
		return alert;
	}

	/**
	 * 界面同步的Alter弹出
	 * 
	 * @param message
	 */
	public void showAndroidAlert(final String title, final String message) {
		Runnable showAlert = new Runnable() {
			public void run() {
				final AlertDialog alert = new AlertDialog.Builder(
						LGameAndroid2DActivity.this).create();
				alert.setTitle(title);
				alert.setMessage(message);
				alert.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						alert.dismiss();
					}
				});
				alert.show();
			}
		};
		runOnUiThread(showAlert);
	}

	/**
	 * 界面同步的Select弹出
	 * 
	 * @param title
	 * @param text
	 * @return
	 */
	public int showAndroidSelect(final String title, final String text[]) {
		Runnable showSelect = new Runnable() {
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						LGameAndroid2DActivity.this);
				builder.setTitle(title);
				builder.setItems(text, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						androidSelect = item;
					}
				});
				builder
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								androidSelect = -1;
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		};
		runOnUiThread(showSelect);
		return androidSelect;
	}

	/**
	 * 界面同步的Exception弹出
	 * 
	 * @param ex
	 */
	public void showAndroidException(final Exception ex) {
		runOnUiThread(new Runnable() {
			public void run() {
				androidException(ex);
			}
		});
	}

	/**
	 * 以对话框方式显示一个Android异常
	 * 
	 * @param exception
	 */
	private void androidException(Exception exception) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		try {
			throw exception;
		} catch (IOException e) {
			if (e.getMessage().startsWith("Network unreachable")) {
				builder.setTitle("No network");
				builder
						.setMessage("LGame-Android Remote needs local network access. Please make sure that your wireless network is activated. You can click on the Settings button below to directly access your network settings.");
				builder.setNeutralButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							}
						});
			} else {
				builder.setTitle("Unknown I/O Exception");
				builder.setMessage(e.getMessage().toString());
			}
		} catch (HttpException e) {
			if (e.getMessage().startsWith("401")) {
				builder.setTitle("HTTP 401: Unauthorized");
				builder
						.setMessage("The supplied username and/or password is incorrect. Please check your settings.");
				builder.setNeutralButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent());
							}
						});
			}
		} catch (RuntimeException e) {
			builder.setTitle("RuntimeException");
			builder.setMessage(e.getStackTrace().toString());
		} catch (Exception e) {
			builder.setTitle("Exception");
			builder.setMessage(e.getMessage());
		} finally {
			exception.printStackTrace();
			builder.setCancelable(true);
			builder.setNegativeButton("Close",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			final AlertDialog alert = builder.create();
			try {
				alert.show();
			} catch (Throwable e) {
			} finally {
				LSystem.destroy();
			}
		}
	}

	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		orientation = config.orientation;
		keyboardOpen = config.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO;
		isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	public void setShowFPS(boolean flag) {
		if (view != null) {
			this.view.setShowFPS(flag);
		}
	}

	public void setFPS(long frames) {
		if (view != null) {
			this.view.setFPS(frames);
		}
	}

	public void setEmulatorListener(EmulatorListener emulator) {
		if (view != null) {
			view.setEmulatorListener(emulator);
		}
	}

	public boolean isShowLogo() {
		if (view != null) {
			return view.isShowLogo();
		}
		return false;
	}

	public void setShowLogo(boolean showLogo) {
		if (view != null) {
			view.setShowLogo(showLogo);
		}
	}

	public void setLogo(LImage img) {
		if (view != null) {
			this.view.setLogo(img);
		}
	}

	public void setDebug(boolean debug) {
		if (view != null) {
			this.view.setDebug(true);
		}
	}

	public boolean isDebug() {
		if (view != null) {
			return this.view.isDebug();
		}
		return false;
	}

	public LGameAndroid2DView view() {
		return view;
	}

	public AdView adView() {
		return adview;
	}

	/**
	 * 键盘是否已显示
	 * 
	 * @return
	 */
	public boolean isKeyboardOpen() {
		return keyboardOpen;
	}

	/**
	 * 是否使用了横屏
	 * 
	 * @return
	 */
	public boolean isLandscape() {
		return isLandscape;
	}

	/**
	 * 当前窗体方向
	 * 
	 * @return
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * 退出当前应用
	 */
	public void close() {
		finish();
	}

	public boolean onTouchEvent(MotionEvent e) {
		if (gameHandler == null) {
			return false;
		}
		gameHandler.onTouchEvent(e);
		return false;
	}

	public boolean onTrackballEvent(MotionEvent e) {
		if (gameHandler != null) {
			synchronized (gameHandler) {
				return gameHandler.onTrackballEvent(e);
			}
		}
		return super.onTrackballEvent(e);
	}

	public boolean isBackLocked() {
		return isBackLocked;
	}

	/**
	 * 设定锁死BACK事件不处理
	 * 
	 * @param isBackLocked
	 */
	public void setBackLocked(boolean isBackLocked) {
		this.isBackLocked = isBackLocked;
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		long curTime = System.currentTimeMillis();
		// 让每次执行键盘事件，至少间隔1/5秒
		if ((curTime - keyTimeMillis) > LSystem.SECOND / 5) {
			keyTimeMillis = curTime;
			if (gameHandler != null) {
				synchronized (gameHandler) {
					if (!isBackLocked) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							LSystem.exit();
							return true;
						}
					}
					if (keyCode == KeyEvent.KEYCODE_MENU) {
						return super.onKeyDown(keyCode, e);
					}
					if (gameHandler.onKeyDown(keyCode, e)) {
						return true;
					}
					// 在事件提交给Android前再次间隔，防止连发
					try {
						Thread.sleep(16);
					} catch (Exception ex) {
					}
					return super.onKeyDown(keyCode, e);
				}
			}
		}
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		if (gameHandler != null) {
			synchronized (gameHandler) {
				if (keyCode == KeyEvent.KEYCODE_MENU) {
					return super.onKeyUp(keyCode, e);
				}
				if (gameHandler.onKeyUp(keyCode, e)) {
					return true;
				}
				return super.onKeyUp(keyCode, e);
			}
		}
		return true;
	}

	protected void onPause() {
		if (gameHandler != null) {
			gameHandler.onPause();
		}
		if (view != null) {
			view.setPaused(true);
		}
		super.onPause();
		if (setupSensors) {
			// 停止重力感应
			stopSensors();
		}
	}

	protected void onResume() {
		if (gameHandler != null) {
			gameHandler.onResume();
		}
		if (view != null) {
			view.setPaused(false);
		}
		super.onResume();
		if (setupSensors) {
			// 恢复重力感应
			initSensors();
		}
	}

	protected void onDestroy() {
		try {
			Log.i("Android2DActivity", "LGame 2D Engine Shutdown");
			if (view != null) {
				view.setRunning(false);
				if (adview != null) {
					adview = null;
				}
				Thread.sleep(16);
			}
			super.onDestroy();
			// 双保险
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {

		}
	}

	protected void onStop() {
		try {
			if (view != null) {
				this.view.setPaused(true);
			}
			super.onStop();
		} catch (Exception e) {

		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		if (gameHandler != null) {
			if (gameHandler.onCreateOptionsMenu(menu)) {
				return true;
			}
		}
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = super.onOptionsItemSelected(item);
		if (gameHandler != null) {
			if (gameHandler.onOptionsItemSelected(item)) {
				return true;
			}
		}
		return result;
	}

	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		if (gameHandler != null) {
			gameHandler.onOptionsMenuClosed(menu);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (gameHandler != null) {
			gameHandler.onAccuracyChanged(sensor, accuracy);
		}

	}

	public void onSensorChanged(SensorEvent event) {
		if (gameHandler != null) {
			gameHandler.onSensorChanged(event);
		}

	}

}