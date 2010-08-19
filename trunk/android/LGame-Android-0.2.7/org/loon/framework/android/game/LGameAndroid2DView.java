package org.loon.framework.android.game;

import java.util.concurrent.TimeUnit;

import org.loon.framework.android.game.core.EmulatorButtons;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.core.timer.SystemTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
public class LGameAndroid2DView extends SurfaceView implements
		SurfaceHolder.Callback, Android2DSurface {

	private final static long MAX_INTERVAL = 1000L;

	final static private Matrix tmp_matrix = new Matrix();

	final static private LFont fpsFont = LFont.getFont("Dialog", 0, 20);

	final static private int fpsX = 5;

	final static private int fpsY = 20;

	private boolean isFPS, isDebug, isLogo, paused, running;

	private int width, height, drawPriority;

	private long maxFrames, before, curTime, startTime, offsetTime, curFPS,
			calcInterval;

	private double frameCount;

	private Bitmap currentScreen;

	private SurfaceHolder surfaceHolder;

	private CanvasThread mainLoop;

	private volatile LImage logo;

	private ViewAnimator animator;

	private LGraphics gl;

	private IAndroid2DHandler handler;

	private LGameAndroid2DActivity activity;

	private Rect rect;

	private Canvas canvas;

	private LImage canvasImage;

	private int number, shake;

	protected static IScreen control;

	private EmulatorListener emulatorListener;

	private EmulatorButtons emulatorButtons;

	public LGameAndroid2DView(LGameAndroid2DActivity activity,
			boolean isLandscape) {
		super(activity.getApplicationContext());
		try {
			LSystem.setupHandler(activity, this, isLandscape);
			this.handler = (IAndroid2DHandler) LSystem.getSystemHandler();
			this.handler.initScreen();
			this.activity = handler.getLGameActivity();
			this.createScreen();
		} catch (Exception e) {
		}
	}

	protected void onAttachedToWindow() {
		if (animator != null) {
			animator.start();
		}
		super.onAttachedToWindow();
	}

	protected void onDetachedFromWindow() {
		if (animator != null) {
			animator.stop();
		}
		super.onDetachedFromWindow();
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
		LSystem.isPaused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * 创建游戏窗体载体
	 */
	private void createScreen() {
		this.canvasImage = new LImage(width = handler.getWidth(),
				height = handler.getHeight(), false);
		this.gl = canvasImage.getLGraphics();
		this.currentScreen = canvasImage.getBitmap();
		this.rect = new Rect(0, 0, width, height);
		this.surfaceHolder = getHolder();
		this.isLogo = true;
		int mode = 0;
		try {
			mode = 1;
			surfaceHolder
					.setType(android.view.SurfaceHolder.SURFACE_TYPE_HARDWARE);
		} catch (Exception e) {
			try {
				mode = 2;
				surfaceHolder
						.setType(android.view.SurfaceHolder.SURFACE_TYPE_GPU);
			} catch (Exception e2) {
				surfaceHolder
						.setType(android.view.SurfaceHolder.SURFACE_TYPE_NORMAL);
			}
		}

		switch (mode) {
		case 1:
			Log.i("Android2DView", "Hardware surface");
			break;
		case 2:
			Log.i("Android2DView", "GPU surface");
			break;
		default:
			Log.i("Android2DView", "No hardware acceleration available");
		}
		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setFormat(PixelFormat.RGB_565);
		this.setFPS(LSystem.DEFAULT_MAX_FPS);
		this.setClickable(false);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setHapticFeedbackEnabled(false);
		this.setKeepScreenOn(true);
		this.setLongClickable(false);
		this.setDrawingCacheEnabled(false);
		this.setWillNotCacheDrawing(true);
		this.setWillNotDraw(true);
	}

	/**
	 * 设定模拟按钮监听器
	 * 
	 * @param emulatorListener
	 */
	public void setEmulatorListener(EmulatorListener emulator) {
		this.emulatorListener = emulator;
		if (emulatorListener != null) {
			if (emulatorButtons == null) {
				emulatorButtons = new EmulatorButtons(emulatorListener, width,
						height);
			} else {
				emulatorButtons.setEmulatorListener(emulator);
			}
		} else {
			emulatorButtons = null;
		}
	}

	/**
	 * 获得模拟器监听
	 * 
	 * @return
	 */
	public EmulatorListener getEmulatorListener() {
		return emulatorListener;
	}

	/**
	 * 获得模拟器按钮
	 * 
	 * @return
	 */
	public EmulatorButtons getEmulatorButtons() {
		return emulatorButtons;
	}

	public void setScreen(IScreen screen) {
		this.handler.setScreen(screen);
	}

	public final Bitmap getAndroid2DBitmap() {
		return currentScreen;
	}

	public final LImage getAndroid2DImage() {
		return canvasImage;
	}

	public void destroyView() {
		try {
			synchronized (this) {

				handler.getAssetsSound().stopSoundAll();
				handler.destroy();
				LSystem.destroy();
				releaseResources();
				notifyAll();

			}
		} catch (Exception e) {
		}
	}

	/**
	 * 更新游戏画布
	 * 
	 */
	public void update() {
		try {
			if (!running) {
				return;
			}
			if (currentScreen == null) {
				return;
			}
			canvas = surfaceHolder.lockCanvas(rect);
			if (canvas != null) {
				synchronized (surfaceHolder) {
					canvas.drawBitmap(currentScreen, 0, 0, null);
				}
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 刷新图像到指定位置
	 * 
	 * @param bit
	 * @param x
	 * @param y
	 */
	public void updateLocation(Bitmap bit, int x, int y) {
		if (!running) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas = surfaceHolder.lockCanvas(rect);
		if (canvas != null) {
			synchronized (surfaceHolder) {
				canvas.drawBitmap(bit, x, y, null);
				if (emulatorButtons != null) {
					emulatorButtons.draw(canvas);
				}
			}
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 刷新图像到指定位置
	 * 
	 * @param img
	 * @param x
	 * @param y
	 */
	public void updateLocation(LImage img, int x, int y) {
		if (img == null) {
			return;
		}
		updateLocation(img.getBitmap(), x, y);
	}

	/**
	 * 更新游戏画布
	 * 
	 * @param img
	 */
	public void update(LImage img) {
		if (img == null) {
			return;
		}
		update(img.getBitmap());
	}

	/**
	 * 更新游戏画布
	 * 
	 * @param img
	 */
	public void update(Bitmap bit) {
		if (!running) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas = surfaceHolder.lockCanvas(rect);
		if (canvas != null) {
			synchronized (surfaceHolder) {
				canvas.drawBitmap(bit, 0, 0, null);
				if (emulatorButtons != null) {
					emulatorButtons.draw(canvas);
				}
			}
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 更新游戏画布
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public void update(LImage img, int w, int h) {
		if (img == null) {
			return;
		}
		update(img.getBitmap(), w, h);
	}

	/**
	 * 更新游戏画布
	 * 
	 * @param bit
	 * @param w
	 * @param h
	 */
	public void update(Bitmap bit, int w, int h) {
		if (!running) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas = surfaceHolder.lockCanvas(rect);
		if (canvas != null) {
			synchronized (surfaceHolder) {
				canvas.drawBitmap(bit, width / 2 - w / 2, height / 2 - h / 2,
						null);
				if (emulatorButtons != null) {
					emulatorButtons.draw(canvas);
				}
			}
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 更新游戏画布，成比例调整显示位置（画面变更为指定大小）
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public void updateFull(LImage img, int w, int h) {
		if (img == null) {
			return;
		}
		updateFull(img.getBitmap(), w, h);
	}

	/**
	 * 更新游戏画布，成比例调整显示位置（画面变更为指定大小）
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public void updateFull(Bitmap bit, int w, int h) {
		if (!running) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas = surfaceHolder.lockCanvas(rect);
		if (canvas != null) {
			int nw = bit.getWidth();
			int nh = bit.getHeight();
			if (nw == w && nh == h) {
				synchronized (surfaceHolder) {
					canvas.drawBitmap(bit, width / 2 - w / 2, height / 2 - h
							/ 2, null);
					if (emulatorButtons != null) {
						emulatorButtons.draw(canvas);
					}
				}
				surfaceHolder.unlockCanvasAndPost(canvas);
				return;
			}
			if (LSystem.isOverrunOS21()) {
				float scaleWidth = ((float) w) / nw;
				float scaleHeight = ((float) h) / nh;
				tmp_matrix.reset();
				tmp_matrix.postScale(scaleWidth, scaleHeight);
				tmp_matrix.postTranslate(width / 2 - w / 2, height / 2 - h / 2);
				synchronized (surfaceHolder) {
					canvas.drawBitmap(bit, tmp_matrix, null);
					if (emulatorButtons != null) {
						emulatorButtons.draw(canvas);
					}
				}
				surfaceHolder.unlockCanvasAndPost(canvas);
			} else {
				int x = width / 2 - w / 2;
				int y = height / 2 - h / 2;
				Rect srcR = new Rect(0, 0, w, h);
				Rect dstR = new Rect(x, y, x + w, y + h);
				synchronized (surfaceHolder) {
					canvas.drawBitmap(bit, srcR, dstR, null);
					if (emulatorButtons != null) {
						emulatorButtons.draw(canvas);
					}
				}
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	private long innerClock() {
		long now = System.currentTimeMillis();
		long ret = now - before;
		before = now;
		return ret;
	}

	final private class CanvasThread extends Thread {

		public CanvasThread() {
			running = true;
			setName("CanvasThread");
		}

		/**
		 * 显示游戏logo
		 */
		private void showLogo() {
			try {
				long elapsedTime;
				int cx = 0, cy = 0;
				double delay;
				try {
					if (logo == null) {
						logo = GraphicsUtils.loadNotCacheImage(
								"system/images/logo.png", true);
					}
					cx = LGameAndroid2DView.this.getWidth() / 2
							- logo.getWidth() / 2;
					cy = LGameAndroid2DView.this.getHeight() / 2
							- logo.getHeight() / 2;
				} catch (Exception e) {

				}
				float alpha = 0.0f;
				boolean firstTime = true;
				elapsedTime = innerClock();
				gl.setAntiAlias(true);
				while (alpha < 1.0f) {
					gl.drawClear();
					gl.setAlpha(alpha);
					gl.drawImage(logo, cx, cy);
					if (firstTime) {
						firstTime = false;
					}
					elapsedTime = innerClock();
					delay = 0.00065 * elapsedTime;
					if (delay > 0.22) {
						delay = 0.22 + (delay / 6);
					}
					alpha += delay;
					update();
				}
				while (number < 3000) {
					number += innerClock();
					update();
				}
				alpha = 1.0f;
				while (alpha > 0.0f) {
					gl.drawClear();
					gl.setAlpha(alpha);
					gl.drawImage(logo, cx, cy);
					elapsedTime = innerClock();
					delay = 0.00055 * elapsedTime;
					if (delay > 0.15) {
						delay = 0.15 + ((delay - 0.04) / 2);
					}
					alpha -= delay;
					update();
				}
				gl.setAntiAlias(false);
			} catch (Throwable e) {
			} finally {
				isLogo = false;
			}
		}

		/**
		 * 游戏窗体主循环
		 */
		public void run() {
			if (isLogo) {
				showLogo();
			}
			try {
				final LTimerContext timerContext = new LTimerContext();
				final SystemTimer timer = LSystem.getSystemTimer();
				boolean reapint = false;
				timerContext.setTimeMillis(startTime = timer.getTimeMillis());
				do {
					if (paused || !isFocusable()) {
						pause(500);
						continue;
					}
					if (!control.next()) {
						continue;
					}
					reapint = LSystem.AUTO_REPAINT;
					if (reapint) {
						canvas = surfaceHolder.lockCanvas(rect);
						shake = control.getShakeNumber();
						if (shake > 0) {
							gl.drawBitmap(control.getBackground(), shake / 2
									- LSystem.random.nextInt(shake), shake / 2
									- LSystem.random.nextInt(shake));
						} else if (shake == -1) {
							gl.drawBitmap(control.getBackground(), 0, 0);
						}
					}
					curTime = timer.getTimeMillis();
					timerContext.timeSinceLastUpdate = curTime
							- timerContext.millisTime;
					timerContext.millisSleepTime = (offsetTime - timerContext.timeSinceLastUpdate)
							- timerContext.millisOverSleepTime;
					if (timerContext.millisSleepTime > 0) {
						pause(timerContext.millisSleepTime);
						timerContext.millisOverSleepTime = ((timer
								.getTimeMillis() - curTime) - timerContext.millisSleepTime);
					} else {
						timerContext.millisOverSleepTime = 0L;
					}
					timerContext.millisTime = timer.getTimeMillis();
					control.callEvents(true);
					control.runTimer(timerContext);
					if (reapint) {
						control.createUI(gl);
						if (isFPS) {
							tickFrames();
							gl.setAntiAlias(true);
							gl.setFont(fpsFont);
							gl.setColor(LColor.white);
							gl.drawString("FPS:" + curFPS, fpsX, fpsY);
						}
						if (canvas != null) {
							synchronized (surfaceHolder) {
								if (emulatorButtons != null) {
									emulatorButtons.draw(gl);
								}
								canvas.drawBitmap(currentScreen, tmp_matrix,
										null);
							}
							surfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				} while (running);
			} catch (Exception ex) {
				// ex.printStackTrace();
				if (isDebug) {
					try {
						// 弹出异常
						activity.showAndroidException(ex);
					} catch (Exception e) {
					}
				}
			} finally {
				destroyView();
			}
		}

		private final void pause(long sleep) {
			try {
				TimeUnit.MILLISECONDS.sleep(sleep);
			} catch (InterruptedException e) {
			}
		}

		private void tickFrames() {
			frameCount++;
			calcInterval += offsetTime;
			if (calcInterval >= MAX_INTERVAL) {
				long timeNow = System.currentTimeMillis();
				long realElapsedTime = timeNow - startTime;
				curFPS = (long) ((frameCount / realElapsedTime) * MAX_INTERVAL);
				frameCount = 0L;
				calcInterval = 0L;
				startTime = timeNow;
			}
		}

	}

	public boolean isShowLogo() {
		return isLogo;
	}

	public void setShowLogo(boolean showLogo) {
		this.isLogo = showLogo;
	}

	public void setLogo(LImage img) {
		logo = img;
	}

	public LImage getLogo() {
		return logo;
	}

	public void setDebug(boolean debug) {
		this.isDebug = debug;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public Thread getMainLoop() {
		return mainLoop;
	}

	public void setFPS(long frames) {
		this.maxFrames = frames;
		this.offsetTime = (long) (1.0 / maxFrames * MAX_INTERVAL);
	}

	public long getMaxFPS() {
		return this.maxFrames;
	}

	public long getCurrentFPS() {
		return this.curFPS;
	}

	public void setShowFPS(boolean isFPS) {
		this.isFPS = isFPS;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (!running) {
				this.setRunning(true);
				if (mainLoop == null) {
					this.mainLoop = new CanvasThread();
					this.drawPriority = Thread.NORM_PRIORITY;
					this.mainLoop.setPriority(drawPriority);
					this.mainLoop.start();
				}
			}
		} catch (Exception e) {

		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (!paused) {
				if (mainLoop != null) {
					this.setRunning(false);
					this.mainLoop = null;
				}
			}
		} catch (Exception e) {

		}
	}

	private void stopThread() {
		try {
			if (mainLoop != null) {
				boolean result = true;
				setRunning(false);
				while (result) {
					try {
						mainLoop.join();
						result = false;
					} catch (InterruptedException e) {
					}
				}

			}
		} catch (Exception e) {

		}
	}

	private void releaseResources() {
		try {
			if (surfaceHolder != null) {
				surfaceHolder.removeCallback(this);
				surfaceHolder = null;
			}
			stopThread();
		} catch (Exception e) {

		}
	}

	public int getDrawPriority() {
		return drawPriority;
	}

	public void setDrawPriority(int drawPriority) {
		if (drawPriority >= 0 && drawPriority <= 10) {
			this.drawPriority = drawPriority;
			if (mainLoop != null) {
				try {
					mainLoop.setPriority(drawPriority);
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean onTouchEvent(MotionEvent e) {
		boolean result = false;
		result = activity.onTouchEvent(e);
		if (emulatorButtons != null) {
			emulatorButtons.onEmulatorButtonEvent(e);
		}
		try {
			Thread.sleep(16);
		} catch (Exception ex) {
		}
		return result;
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		boolean result = false;
		result = activity.onKeyDown(keyCode, e);
		return result;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		boolean result = false;
		result = activity.onKeyUp(keyCode, e);
		return result;
	}

	public IAndroid2DHandler getGameHandler() {
		return handler;
	}

}
