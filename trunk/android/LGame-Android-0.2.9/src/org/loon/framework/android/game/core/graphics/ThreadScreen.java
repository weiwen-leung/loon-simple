package org.loon.framework.android.game.core.graphics;

import java.util.LinkedList;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.geom.Line2D;
import org.loon.framework.android.game.core.graphics.geom.Point;
import org.loon.framework.android.game.core.graphics.geom.Polygon;
import org.loon.framework.android.game.media.sound.AssetsSound;

import android.graphics.Paint;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1.2
 */
public abstract class ThreadScreen extends Screen implements Runnable {

	private int bench, centerX, centerY;

	private int benchcount, runPriority;

	private long benchtime;

	private long time, time1, time2;

	private long speed, sleepTime;

	private Thread gameThread;

	private LGraphics drawGraphics;

	private LImage screen;

	final static private int MAX_BUFFER_FPS = 5;

	private boolean running, synchro, match;

	private boolean centralizando;

	private Draw draw;

	private int max_fps;

	private int extWidth, extHeight, mapWidth, mapHeight;

	private Point screenCenter;

	private Point origin;

	private Point goal;

	private LinkedList<LImage>[] firstLayer;

	private LinkedList<Point>[] firstLayerPts;

	private LinkedList<Float>[] firstLayerOpacity;

	private int ascent;

	private RectBox focus;

	public ThreadScreen() {
		init(getWidth(), getHeight());
	}

	public ThreadScreen(int width, int height) {
		init(width, height);
	}

	private void init(int width, int height) {
		LSystem.AUTO_REPAINT = false;
		this.setRepaintMode(IScreen.SCREEN_NOT_REPAINT);
		this.running = true;
		this.match = true;
		this.setFPS(MAX_BUFFER_FPS);
		this.setSynchroFPS(30);
		this.screen = LImage.createImage(width, height, false);
		this.drawGraphics = screen.getLGraphics();
		this.ascent = (int) drawGraphics.getFont().getAscent();
		this.centerX = this.getWidth() / 2 - width / 2;
		this.centerY = this.getHeight() / 2 - height / 2;
		this.focus = new RectBox(centerX, centerY, width, height);
	}

	final public void onLoad() {
		LSystem.AUTO_REPAINT = false;
		this.InitializeVars();
		this.gameThread = new Thread(this);
		this.runPriority = Thread.NORM_PRIORITY;
		this.gameThread.setPriority(runPriority);
		this.gameThread.start();
	}

	@SuppressWarnings("unchecked")
	private void InitializeVars() {
		this.firstLayer = new LinkedList[2];
		this.firstLayerPts = new LinkedList[2];
		this.firstLayerOpacity = new LinkedList[2];
		this.screenCenter = new Point();
		this.origin = new Point();
		this.goal = new Point();
		this.inicializaLayer();
	}

	public void inicializaLayer() {
		for (int c = 0; c < this.firstLayer.length; ++c) {
			this.firstLayer[c] = new LinkedList<LImage>();
			this.firstLayerPts[c] = new LinkedList<Point>();
			this.firstLayerOpacity[c] = new LinkedList<Float>();
		}

	}

	public void centerScreenAt(Point p) {
		this.focus.setLocation(p.x - this.focus.getWidth() / 2, p.y
				- this.focus.getHeight() / 2);
	}

	public void centerScreenAt(int x, int y) {
		this.focus.setLocation(x - this.focus.getWidth() / 2, y
				- this.focus.getHeight() / 2);
	}

	public void clearBuffer() {
		this.drawGraphics
				.clearRect(0, 0, screen.getWidth(), screen.getHeight());
	}

	/**
	 * 提交图像到游戏画面（随中心点偏移）
	 * 
	 * @param img
	 * @param x
	 * @param y
	 */
	public void putImage(LImage img, int x, int y) {
		drawGraphics.drawImage(img, x - this.focus.getX(), y
				- this.focus.getY());
	}

	/**
	 * 插入图像到指定图层
	 * 
	 * @param img
	 * @param layer
	 * @param x
	 * @param y
	 * @param opacity
	 */
	public void addLayer(LImage img, int layer, int x, int y, float opacity) {
		if (this.focus.intersects(x, y, img.getWidth(), img.getHeight())) {
			this.firstLayer[layer].add(img);
			this.firstLayerPts[layer].add(new Point(x, y));
			this.firstLayerOpacity[layer].add(opacity);
		}
	}

	/**
	 * 显示指定图层的画像
	 * 
	 * @param layer
	 */
	public void putLayer(int layer) {
		while (!this.firstLayer[layer].isEmpty()) {
			this
					.putImage(this.firstLayer[layer].get(0),
							( this.firstLayerPts[layer].get(0)).x,
							(this.firstLayerPts[layer].get(0)).y,
							(this.firstLayerOpacity[layer].get(0))
									.floatValue());
			this.firstLayer[layer].remove(0);
			this.firstLayerPts[layer].remove(0);
			this.firstLayerOpacity[layer].remove(0);
		}

	}

	public void putImage(LImage img, int x, int y, float t) {
		if (this.focus.intersects(x, y, img.getWidth(), img.getHeight())) {
			if (t < 1.0D) {
				this.drawGraphics.setAlpha(t);
				this.drawGraphics.drawImage(img, x - this.focus.getX(), y
						- this.focus.getY());
				this.drawGraphics.setAlpha(1.0f);
			} else {
				this.putImage(img, x, y);
			}
		}
	}

	public void putLine(int x1, int y1, int x2, int y2) {
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		if ((new Line2D.Float(p1, p2)).intersects(this.focus.getRectangle2D())) {
			this.drawGraphics.drawLine(x1 - this.focus.getX(), y1
					- this.focus.getY(), x2 - this.focus.getX(), y2
					- this.focus.getY());
		}

	}

	public void putOval(int x, int y, int width, int height) {
		if (this.focus.intersects(x, y, width, height)) {
			this.drawGraphics.drawOval(x - this.focus.getX(), y
					- this.focus.getY(), width, height);
		}

	}

	public void putPolygon(Polygon p) {
		if (this.focus.getRectangle2D().intersects(p.getBounds())) {
			Polygon aux = new Polygon(new int[] { 1, 2, 3, 4 }, new int[] { 1,
					2, 3, 4 }, 4);

			int c;
			for (c = 0; c < p.xpoints.length; ++c) {
				aux.xpoints[c] = p.xpoints[c] - this.focus.getX();
			}

			for (c = 0; c < p.ypoints.length; ++c) {
				aux.ypoints[c] = p.ypoints[c] - this.focus.getY();
			}

			this.drawGraphics
					.drawPolygon(aux.xpoints, aux.ypoints, aux.npoints);
		}

	}

	public void putRect(int x, int y, int width, int height) {
		if (this.focus.intersects(x, y, width, height)) {
			this.drawGraphics.drawRect(x - this.focus.getX(), y
					- this.focus.getY(), width, height);
		}

	}

	public void putText(String t, int x, int y) {
		this.drawGraphics.drawString(t, x - this.focus.getX(), y
				- this.focus.getY());
	}

	public void moveCenterScreenAt(Point p) {
		if (!this.centralizando) {
			this.screenCenter = new Point(this.focus.getX()
					+ this.focus.getWidth() / 2, this.focus.getY()
					+ this.focus.getHeight() / 2);
			this.origin = (Point) this.screenCenter.clone();
			this.centralizando = true;
			this.goal = p;
		}

	}

	public void moveCenterScreenAt(int x, int y) {
		this.screenCenter = new Point(this.focus.getX() + this.focus.getWidth()
				/ 2, this.focus.getY() + this.focus.getHeight() / 2);
		this.origin = (Point) this.screenCenter.clone();
		this.centralizando = true;
		this.goal.x = x;
		this.goal.y = y;
	}

	public void putFillPolygon(Polygon p) {
		if (this.focus.getRectangle2D().intersects(p.getBounds())) {
			Polygon aux = new Polygon(new int[] { 1, 2, 3, 4 }, new int[] { 1,
					2, 3, 4 }, 4);

			int c;
			for (c = 0; c < p.xpoints.length; ++c) {
				aux.xpoints[c] = p.xpoints[c] - this.focus.getX();
			}

			for (c = 0; c < p.ypoints.length; ++c) {
				aux.ypoints[c] = p.ypoints[c] - this.focus.getY();
			}

			this.drawGraphics
					.fillPolygon(aux.xpoints, aux.ypoints, aux.npoints);
		}

	}

	public void putFillPolygon(Polygon p, float t) {
		if (this.focus.getRectangle2D().intersects(p.getBounds())) {
			Polygon aux = new Polygon(new int[] { 1, 2, 3, 4 }, new int[] { 1,
					2, 3, 4 }, 4);

			this.drawGraphics.setAlpha(t);

			int c;
			for (c = 0; c < p.xpoints.length; ++c) {
				aux.xpoints[c] = p.xpoints[c] - this.focus.getX();
			}

			for (c = 0; c < p.ypoints.length; ++c) {
				aux.ypoints[c] = p.ypoints[c] - this.focus.getY();
			}

			this.drawGraphics
					.fillPolygon(aux.xpoints, aux.ypoints, aux.npoints);

			this.drawGraphics.setAlpha(1.0f);
		}

	}

	public void putFillRect(int x, int y, int width, int height) {
		if (this.focus.intersects(x, y, width, height)) {
			this.drawGraphics.fillRect(x - this.focus.getX(), y
					- this.focus.getY(), width, height);
		}

	}

	public void scrollToGoal(int frames) {
		int speed = (Math.abs(this.origin.x - this.goal.x) >= Math
				.abs(this.origin.y - this.goal.y) ? Math.abs(this.origin.x
				- this.goal.x) : Math.abs(this.origin.y - this.goal.y))
				/ frames;
		if (speed == 0) {
			speed = 1;
		}

		if (this.screenCenter.x < this.goal.x) {
			this.screenCenter.x = Math.abs(this.screenCenter.x - this.goal.x) > speed ? this.screenCenter.x
					+ speed
					: this.goal.x;
		} else if (this.screenCenter.x > this.goal.x) {
			this.screenCenter.x = Math.abs(this.screenCenter.x - this.goal.x) > speed ? this.screenCenter.x
					- speed
					: this.goal.x;
		}

		if (this.screenCenter.y < this.goal.y) {
			this.screenCenter.y = Math.abs(this.screenCenter.y - this.goal.y) > speed ? this.screenCenter.y
					+ speed
					: this.goal.y;
		} else if (this.screenCenter.y > this.goal.y) {
			this.screenCenter.y = Math.abs(this.screenCenter.y - this.goal.y) > speed ? this.screenCenter.y
					- speed
					: this.goal.y;
		}

		this.centerScreenAt((Point) this.screenCenter.clone());
		if (this.screenCenter.x == this.goal.x
				&& this.screenCenter.y == this.goal.y) {
			this.centralizando = false;
		}

	}

	public void text(String s, int x, int y) {
		drawGraphics.drawString(s, x, y + ascent);
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
			LFont fontmetrics = drawGraphics.getLFont();
			int f = fontmetrics.stringWidth(s);
			int x = (screen.getWidth() - f) / 2;
			text(x, y, s);
		} catch (Exception ex) {
		}
	}

	public void draw(LImage img, int x, int y) {
		drawGraphics.drawImage(img, x, y);
	}

	public void color(int r, int g, int b) {
		drawGraphics.setColor(new LColor(r, g, b));
	}

	public void rect(int x, int y, int w, int h) {
		drawGraphics.drawRect(x, y, w, h);
	}

	public void fill(int x, int y, int w, int h) {
		drawGraphics.fillRect(x, y, w, h);
	}

	public void resizeScreen(int w, int h) {
		this.extWidth = w;
		this.extHeight = h;
		this.match = (extWidth == screen.getWidth() && extHeight == screen
				.getHeight());
		if (!match) {
			this.centerX = this.getWidth() / 2 - extWidth / 2;
			this.centerY = this.getHeight() / 2 - extHeight / 2;
			if (focus == null) {
				this.focus = new RectBox(centerX, centerY, w, h);
			} else {
				this.focus.setBounds(centerX, centerY, w, h);
			}
		}
	}

	public AssetsSound loadSound(String fileName) {
		return new AssetsSound(fileName);
	}

	public void dispose() {
		try {
			running = false;
			if (draw != null) {
				draw = null;
			}
			if (screen != null) {
				screen = null;
			}
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
		return drawGraphics;
	}

	abstract public void initialize();

	final public void draw(LGraphics g) {

	}

	public void drawScreen(LGraphics g) {

	}

	public synchronized void repaint() {
		if (!running) {
			return;
		}
		drawScreen(drawGraphics);
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
			if (synchro) {
				task();
			}
			gameLoop();
			if (synchro) {
				synchro();
			}
			repaint();
		} while (running);
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
		if (focus != null) {
			focus.setY(centerY);
		}
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
		if (focus != null) {
			focus.setX(centerX);
		}
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

	public void setFocusedArea(RectBox r) {
		this.focus = r;
	}

	public void setFocusedArea(int x, int y, int w, int h) {
		this.focus.setX(x);
		this.focus.setY(y);
		this.focus.setWidth(w);
		this.focus.setHeight(h);
	}

	public void setGraphics2DPaint(Paint p) {
		this.drawGraphics.setPaint(p);
	}

	public void setGraphicsColor(LColor c) {
		this.drawGraphics.setColor(c);
	}

	public void setMapSize(int width, int height) {
		this.mapWidth = width;
		this.mapHeight = height;
	}

	public void setScreenSize(int width, int height) {
		if (screen != null) {
			screen.dispose();
			screen = null;
		}
		this.screen = LImage.createImage(width, height, false);
		this.drawGraphics = screen.getLGraphics();
		this.focus = new RectBox(0, 0, width, height);
	}

	public RectBox getFocusedArea() {
		return this.focus;
	}

	public Point getGoal() {
		return this.goal;
	}

	public int getMapHeight() {
		return this.mapHeight;
	}

	public int getMapWidth() {
		return this.mapWidth;
	}

	public Point getOrigin() {
		return this.origin;
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
