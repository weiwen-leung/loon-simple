package org.loon.framework.game.simple.core.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.graphics.device.LGraphicsJava2D;
import org.loon.framework.game.simple.utils.GraphicsUtils;

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

	protected int bench, centerX, centerY, extWidth, extHeight;

	private int benchcount;

	private long benchtime;

	private long time, time1, time2;

	private long speed, sleepTime;

	private Thread gameThread;

	private LGraphics screenGraphics;

	protected BufferedImage screen;

	protected boolean running, synchro;

	protected Draw draw;

	private int max_fps;

	protected boolean match;

	private int ascent;

	public ThreadScreen() {
		init(getWidth(), getHeight());
	}

	public ThreadScreen(int width, int height) {
		init(width, height);
	}

	private void init(int width, int height) {
		this.running = true;
		this.match = true;
		this.setSynchroFPS(20);
		this.setShakeNumber(0);
		this.screen = GraphicsUtils.createImage(width, height, false);
		this.screenGraphics = new LGraphicsJava2D(screen);
		this.ascent = screenGraphics.getFontMetrics().getAscent();
		this.centerX = this.getWidth() / 2 - width / 2;
		this.centerY = this.getHeight() / 2 - height / 2;
		this.gameThread = new Thread(this);
		this.gameThread.setPriority(Thread.MAX_PRIORITY);
		this.gameThread.start();
	}

	public void text(String s, int x, int y) {
		screenGraphics.drawString(s, x, y + ascent);
	}

	public void text(int x, int j, String s) {
		color(0, 0, 0);
		text(s, x + 1, j + 1);
		text(s, x, j + 1);
		text(s, x + 1, j);
		color(255, 255, 255);
		text(s, x, j);
	}

	public void text(String s, int y) {
		try {
			FontMetrics fontmetrics = screenGraphics.getFontMetrics();
			int f = fontmetrics.stringWidth(s);
			int x = (screen.getWidth() - f) / 2;
			text(x, y, s);
		} catch (Exception ex) {
		}
	}

	public void draw(Image img, int x, int y) {
		screenGraphics.drawImage(img, x, y);
	}

	public void color(int r, int g, int b) {
		screenGraphics.setColor(new Color(r, g, b));
	}

	public void color(Graphics gs, int r, int g, int b) {
		gs.setColor(new Color(r, g, b));
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

	public void dispose() {
		try {
			running = false;
			if (draw != null) {
				draw.dispose();
				draw = null;
			}
			if (screenGraphics != null) {
				screenGraphics.dispose();
				screen = null;
			}
			gameThread = null;
		} catch (Exception e) {
		}
	}

	public int getBenchCount() {
		return bench;
	}
	
	public Clip loadSound(String fileName) {
		Clip clip = null;
		try {
			AudioInputStream in = AudioSystem
					.getAudioInputStream(LSystem.classLoader
							.getResourceAsStream(fileName));
			javax.sound.sampled.DataLine.Info info = new javax.sound.sampled.DataLine.Info(
					Clip.class, in.getFormat());
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(in);
		} catch (Exception e) {

		}
		return clip;
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

	public Image getImage() {
		return screen;
	}

	public LGraphics getLGraphics() {
		return screenGraphics;
	}

	public void initialize() {

	}

	public void drawScreen(LGraphics g){
		
	}


	public synchronized void repaint() {
		drawScreen(screenGraphics);
	}

	public abstract void gameLoop();

	public void setRunning(boolean isRunning) {
		this.running = isRunning;
	}

	public boolean isRunning() {
		return running;
	}

	public void run() {
		initialize();
		do {
			if (synchro) {
				task();
			}
			gameLoop();
			if (synchro) {
				synchro();
			}
		} while (running);
	}

	public void draw(LGraphics g) {
		if (match) {
			g.drawImage(screen, centerX, centerY);
		} else {
			g.drawImage(screen, centerX, centerY, extWidth, extHeight);
		}
	}

	public boolean isSynchro() {
		return synchro;
	}

	public void setSynchro(boolean synchro) {
		this.synchro = synchro;
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

}
