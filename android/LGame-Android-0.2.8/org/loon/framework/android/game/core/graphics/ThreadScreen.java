package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.media.sound.AssetsSound;

import android.graphics.Bitmap.Config;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public abstract class ThreadScreen extends Screen implements Runnable {

	protected int bench, centerX, centerY;

	private int benchcount, runPriority;

	private long benchtime;

	private long time, time1, time2;

	private long speed, sleepTime;

	private Thread gameThread;

	private LGraphics screenGraphics;

	protected LImage screen;

	protected boolean running, synchro, match;

	private Draw draw;

	private int max_fps;

	private int extWidth, extHeight;

	private int ascent;

	public ThreadScreen() {
		init(getWidth(), getHeight());
	}

	public ThreadScreen(int width, int height) {
		init(width, height);
	}

	private void init(int width, int height) {
		LSystem.AUTO_REPAINT = false;
		this.running = true;
		this.match = true;
		this.setSynchroFPS(20);
		this.setShakeNumber(0);
		this.screen = LImage.createImage(width, height, Config.RGB_565);
		this.screenGraphics = screen.getLGraphics();
		this.ascent = (int) screenGraphics.getFont().getAscent();
		this.centerX = this.getWidth() / 2 - width / 2;
		this.centerY = this.getHeight() / 2 - height / 2;
		this.gameThread = new Thread(this);
		this.runPriority = Thread.NORM_PRIORITY;
		this.gameThread.setPriority(runPriority);
		this.gameThread.start();
	}

	public void text(String s, int x, int y) {
		screenGraphics.drawString(s, x, y + ascent);
	}

	public void text(int x, int y, String s) {
		color(0, 0, 0);
		text(s, x + y, y + 1);
		text(s, x, y + 1);
		text(s, x + 1, y);
		color(255, 255, 255);
		text(s, x, y);
	}

	public void text(String s, int y) {
		try {
			LFont fontmetrics = screenGraphics.getFont();
			int f = fontmetrics.stringWidth(s);
			int x = (screen.getWidth() - f) / 2;
			text(x, y, s);
		} catch (Exception ex) {
		}
	}

	public void draw(LImage img, int x, int y) {
		screenGraphics.drawImage(img, x, y);
	}

	public void color(int r, int g, int b) {
		screenGraphics.setColor(new LColor(r, g, b));
	}

	public void rect(int x, int y, int w, int h) {
		screenGraphics.drawRect(x, y, w, h);
	}

	public void fill(int x, int y, int w, int h) {
		screenGraphics.fillRect(x, y, w, h);
	}

	public void resizeScreen(int w, int h) {
		this.extWidth = w;
		this.extHeight = h;
		this.match = (extWidth == screen.getWidth() && extHeight == screen
				.getHeight());
		if (!match) {
			this.centerX = this.getWidth() / 2 - extWidth / 2;
			this.centerY = this.getHeight() / 2 - extHeight / 2;
		}
	}

	public AssetsSound loadSound(String fileName) {
		return new AssetsSound(fileName);
	}

	public void dispose() {
		try {
			running = false;
			gameThread = null;
		} catch (Exception e) {
		}
	}

	public int getBenchCount() {
		return bench;
	}

	private void task() {
		if (benchtime < System.currentTimeMillis()) {
			benchtime = System.currentTimeMillis() + 1000L;
			bench = benchcount;
			benchcount = 0;
		}
		benchcount++;
	}

	private void synchro() {
		try {
			time2 = time1;
			time1 = System.currentTimeMillis();
			time = speed - (time1 - time2);
			if (time >= 0L) {
				Thread.sleep(time);
			}
		} catch (Exception ex) {
		}
	}

	public void sleep(long i) {
		while (System.currentTimeMillis() < sleepTime + i) {
			;
		}
		sleepTime = System.currentTimeMillis();
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public int getSynchroFPS() {
		return max_fps;
	}

	public void setSynchroFPS(int fps) {
		max_fps = fps;
		speed = 1000 / fps;
	}

	public synchronized Draw getDraw() {
		if (draw == null) {
			draw = new DrawImpl(this);
		}
		return draw;
	}

	public LImage getImage() {
		return screen;
	}

	public LGraphics getLGraphics() {
		return screenGraphics;
	}

	public void initialize() {

	}

	final public void draw(LGraphics g) {

	}

	public abstract void drawScreen(LGraphics g);

	public synchronized void repaint() {
		if (!running) {
			return;
		}
		drawScreen(screenGraphics);
		if (match) {
			LSystem.repaintLocation(screen, centerX, centerY);
		} else {
			LSystem.repaintFull(screen, extWidth, extHeight);
		}

	}

	public abstract void gameLoop();

	public boolean isSynchro() {
		return synchro;
	}

	public void setSynchro(boolean synchro) {
		this.synchro = synchro;
	}

	public void setRunning(boolean isRunning) {
		this.running = isRunning;
	}

	public boolean isRunning() {
		return running;
	}

	public void run() {
		initialize();
		do {
			
			if (LSystem.isPaused) {
				pause(500);
				continue;
			}
			if (synchro) {
				task();
			}
			gameLoop();
			if (synchro) {
				synchro();
			}
		} while (running);
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getExtWidth() {
		return extWidth;
	}

	public int getExtHeight() {
		return extHeight;
	}

	public int getRunPriority() {
		return runPriority;
	}

	public void setRunPriority(int runPriority) {
		if (runPriority >= 0 && runPriority <= 10) {
			this.runPriority = runPriority;
			if (gameThread != null) {
				try {
					gameThread.setPriority(runPriority);
				} catch (Exception e) {
				}
			}
		}
	}

}
