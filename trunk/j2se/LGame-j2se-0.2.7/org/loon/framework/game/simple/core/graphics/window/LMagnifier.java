package org.loon.framework.game.simple.core.graphics.window;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.GameManager;
import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.LColor;
import org.loon.framework.game.simple.core.graphics.LContainer;
import org.loon.framework.game.simple.core.graphics.PixelsImage;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.resource.ZipResource;
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
public class LMagnifier extends LContainer implements Runnable {

	private boolean running, matching, isLoop, isMove;

	private Thread zoomThread;

	private int size;

	private PixelsImage zoom1;

	private PixelsImage zoom2;

	private int count;

	private double nx1, ny1;

	private double offset1, offset2;

	private int posx, posy, index;

	private int quality;

	private boolean on;

	private int nx, ny, nw, nh;

	private boolean ac;

	private BufferedImage image;

	private Graphics2D graphics;

	public LMagnifier(String fileName) {
		this();
		this.loadImage(fileName);
	}

	public LMagnifier() {
		this(0, 0, GameManager.getSystemHandler().getWidth(), GameManager
				.getSystemHandler().getHeight());

	}

	public LMagnifier(int x, int y, int w, int h) {
		super(x, y, w, h);
		System.gc();
		this.setBackground(Color.WHITE);
		this.quality = 1;
		this.customRendering = true;
		this.isMove = true;
		this.setElastic(true);
		this.setLayer(100);
		this.setAlpha(0.75F);
	}

	/**
	 * 释放资源
	 */
	public void dispose() {
		super.dispose();
		destroy();
	}

	/**
	 * 缩放模式1
	 * 
	 * @param x1
	 * @param y1
	 * @param w1
	 * @param h1
	 * @param b1
	 * @param b2
	 */
	public static void resize(int x1, int y1, int w, int h, PixelsImage b1,
			PixelsImage b2) {
		int w1 = b2.getWidth();
		int h1 = b2.getHeight();
		w = Math.min(b1.getWidth() - 2, w);
		h = Math.min(b1.getHeight() - 2, h);
		float f1 = (float) (w - x1) / (float) w1;
		float f2 = (float) (h - y1) / (float) h1;
		float f3 = y1;
		int k2 = (int) (f1 * 65536F);
		int zoom1[] = b1.getPixels();
		int zoom2[] = b2.getPixels();
		int l2 = b1.getWidth();
		for (int i6 = 0; i6 < h1; i6++) {
			int i3 = (int) f3 * l2;
			int j3 = i6 * w1;
			int j6 = (int) (f3 * 256F) & 0xff;
			int k6 = 255 - j6;
			int l6 = y1 << 16;
			for (int i7 = 0; i7 < w1; i7++) {
				int j7 = i3 + (l6 >> 16);
				int l3;
				int k3 = l3 = zoom1[j7];
				int l4;
				int k4 = l4 = zoom1[j7 + l2];
				int j4;
				int i4 = j4 = zoom1[++j7];
				int j5;
				int i5 = j5 = zoom1[j7 + l2];
				int l5 = l6 >> 8 & 0xff;
				int k5 = 255 - l5;
				k3 &= 0xff00ff;
				l3 &= 0xff00;
				i4 &= 0xff00ff;
				j4 &= 0xff00;
				k4 &= 0xff00ff;
				l4 &= 0xff00;
				i5 &= 0xff00ff;
				j5 &= 0xff00;
				k3 *= k5;
				l3 *= k5;
				i4 *= l5;
				j4 *= l5;
				k4 *= k5;
				l4 *= k5;
				i5 *= l5;
				j5 *= l5;
				k3 = k3 >> 8 & 0xff00ff;
				l3 = l3 >> 8 & 0xff00;
				i4 = i4 >> 8 & 0xff00ff;
				j4 = j4 >> 8 & 0xff00;
				k4 = k4 >> 8 & 0xff00ff;
				l4 = l4 >> 8 & 0xff00;
				i5 = i5 >> 8 & 0xff00ff;
				j5 = j5 >> 8 & 0xff00;
				k3 += i4;
				l3 += j4;
				k4 += i5;
				l4 += j5;
				k3 *= k6;
				l3 *= k6;
				k4 *= j6;
				l4 *= j6;
				k3 = k3 >> 8 & 0xff00ff;
				l3 = l3 >> 8 & 0xff00;
				k4 = k4 >> 8 & 0xff00ff;
				l4 = l4 >> 8 & 0xff00;
				zoom2[j3++] = k3 + l3 + k4 + l4;
				l6 += k2;
			}

			f3 += f2;
		}
		zoom1 = null;
		zoom2 = null;
	}

	/**
	 * 缩放模式2
	 * 
	 * @param i1
	 * @param j1
	 * @param k1
	 * @param l1
	 * @param b1
	 * @param b2
	 */
	private void resize1(int x1, int y1, int w, int h, PixelsImage b1,
			PixelsImage b2) {
		int w1 = b1.getWidth();
		int h1 = b1.getHeight();
		int w2 = b2.getWidth();
		int h2 = b2.getHeight();
		if (x1 < 0) {
			x1 = 0;
		}
		if (x1 > w1) {
			x1 = w1;
		}
		if (w < 0) {
			w = 0;
		}
		if (w > w1) {
			w = w1;
		}
		if (y1 < 0) {
			y1 = 0;
		}
		if (y1 > h1) {
			y1 = h1;
		}
		if (h < 0) {
			h = 0;
		}
		if (h > h1) {
			h = h1;
		}
		if (x1 > w) {
			int i3 = x1;
			x1 = w;
			w = i3;
		}
		if (y1 > h) {
			int j3 = y1;
			y1 = h;
			h = j3;
		}
		int k3 = 0;
		int pixels1[] = b1.getPixels();
		int pixels2[] = b2.getPixels();

		int width = (w - x1 << 8) / w2;
		int height = (h - y1 << 8) / h2;
		int type = y1 << 8;
		for (int i = 0; i < h2; i++) {
			int j = (type >> 8) * w1 + x1 << 8;
			for (int k = 0; k < w2; k++) {
				pixels2[k3++] = pixels1[j >> 8];
				j += width;
			}
			type += height;
		}
		if (offset1 < 0.25D) {
			if (offset1 < 0.1D) {
				for (int k = 0; k < pixels2.length; k = k + 3) {
					pixels2[k + 0] = pixels2[k + 2];
					pixels2[k + 1] = pixels2[k + 1];
					pixels2[k + 2] = pixels2[k + 0];
				}
			}
			LColor.sharpen(pixels2, getWidth(), getHeight(), 0.75F);
		}
		pixels1 = null;
		pixels2 = null;
	}

	private boolean draw(Graphics2D g, int x1, int y1, int w1, int h1,
			boolean flag) {
		boolean flag1 = false;
		boolean flag2 = false;
		if (x1 != nx || w1 != nw || y1 != ny || h1 != nh) {
			flag1 = true;
			nx = x1;
			nw = w1;
			ny = y1;
			nh = h1;
		}
		if (flag != ac) {
			flag2 = true;
			ac = flag;
		}
		if (!flag1 && !flag2) {
			return false;
		}
		resize1(x1, y1, w1, h1, zoom2, zoom1);
		if (g == null) {
			return false;
		}
		Image image = zoom1.get();
		g.drawImage(image, 0, 0, null);
		image.flush();
		image = null;
		return flag1;
	}

	private static int match(double d) {
		if (d >= 0.0D) {
			return (int) (d + 0.5);
		} else {
			return (int) (d - 0.5D);
		}
	}

	/**
	 * 绘制缩放图
	 * 
	 * @param g
	 * @return
	 */
	private boolean drawImage(Graphics2D g) {
		double x1 = posx - (getScreenX() + getWidth() / 2);
		double y1 = posy - (getScreenY() + getHeight() / 2);
		double angle = Math.sqrt(x1 * x1 + y1 * y1);
		if (angle > 0.0D) {
			double d4 = 1.0D / angle;
			x1 *= d4;
			y1 *= d4;
			angle = Math.max(0.0D, angle - 26D);
			x1 *= angle;
			y1 *= angle;
			x1 /= 22D;
			y1 /= 22D;
		}
		double w = (double) zoom2.getWidth() * offset1;
		double h = (double) zoom2.getHeight() * offset1;
		offset1 = Math.max(Math.min(offset1 * offset2, 1.0D),
				0.040000000000000001D);
		double d8 = (double) zoom2.getWidth() * offset1;
		double d9 = (double) zoom2.getHeight() * offset1;
		nx1 += (w - d8) / 2D;
		ny1 += (h - d9) / 2D;
		double d10 = (double) size / (double) getWidth();
		nx1 += x1 * d10;
		ny1 += y1 * d10;
		if (nx1 < 0.0D) {
			nx1 = 0.0D;
		}
		if (ny1 < 0.0D) {
			ny1 = 0.0D;
		}
		int i1 = match(nx1);
		int j1 = match(ny1);
		int k1 = i1 + match((double) zoom2.getWidth() * offset1);
		int l1 = j1 + match((double) zoom2.getHeight() * offset1);
		if (k1 > zoom2.getWidth()) {
			k1 = zoom2.getWidth();
			i1 = match((double) k1 - (double) zoom2.getWidth() * offset1);
			nx1 = i1;
		}
		if (l1 > zoom2.getHeight()) {
			l1 = zoom2.getHeight();
			j1 = match((double) l1 - (double) zoom2.getHeight() * offset1);
			ny1 = j1;
		}
		return draw(g, i1, j1, k1, l1, on);
	}

	public void destroy() {
		this.setAlpha(1.0F);
		if (zoomThread != null) {
			this.isLoop = false;
			this.running = false;
			this.zoomThread = null;
		}
		if (this.graphics != null) {
			this.graphics.dispose();
			this.graphics = null;
		}
		this.zoom1 = null;
		this.zoom2 = null;
		System.gc();
	}

	public void zoom() {
		offset2 = 0.95999999999999996D;
	}

	public void narrow() {
		offset2 = 1.04D;
	}

	public void loadImage(String fileName) {
		this.loadImage(GraphicsUtils.loadImage(fileName));
	}

	public void loadImage(byte[] bytes) {
		this.loadImage(GraphicsUtils.loadImage(bytes));
	}

	public void loadImage(String name, byte[] bytes) {
		this.loadImage(GraphicsUtils.loadImage(name, bytes));
	}

	public void loadImage(String name, ZipResource loader) {
		this.loadImage(loader.loadData(name));
	}

	public void loadImage(Image image) {
		if (image == null) {
			return;
		}
		this.destroy();
		if (matching) {
			this.setWidth(image.getWidth(null));
			this.setHeight(image.getHeight(null));
		}
		this.offset1 = 1.0D;
		this.offset2 = 1.0D;
		this.index = 0;
		this.quality = 1;
		this.on = false;
		this.zoom1 = new PixelsImage(getWidth(), getHeight());
		this.zoom2 = new PixelsImage(image);
		try {
			if (quality == 1 && !matching) {
				PixelsImage cacheImage1 = new PixelsImage(zoom2.getWidth() * 2,
						zoom2.getHeight() * 2);
				resize(0, 0, cacheImage1.getWidth(), cacheImage1.getHeight(),
						zoom2, cacheImage1);
				zoom2 = new PixelsImage(cacheImage1.getWidth(), cacheImage1
						.getHeight());
				PixelsImage.match(cacheImage1, zoom2);
				cacheImage1.destroy();
				cacheImage1 = null;
			}
		} catch (OutOfMemoryError ex) {
			destroy();
			LSystem.destroy();
			LSystem.gc();
			return;
		}
		this.size = zoom2.getWidth();
		this.running = true;
		this.isLoop = true;
		if (zoomThread == null) {
			zoomThread = new Thread(this);
			zoomThread.start();
		}
	}

	public void doClick() {
	}

	private void match() {
		posx = this.input.getMouseX();
		posy = this.input.getMouseY();
	}

	protected void processMouseClicked() {
		if (isMove) {
			match();
		}
		this.doClick();
	}

	protected void processMouseExited() {
		if (isMove) {
			posx = getWidth() / 2;
			posy = getHeight() / 2;
		}
	}

	protected void processMouseDragged() {
		if (isMove) {
			posx = this.input.getMouseX();
			posy = this.input.getMouseY();
		}
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getMouseDX(), this.input.getMouseDY());
		}
	}

	protected void processMouseMoved() {
		if (isMove) {
			match();
		}
	}

	protected void processMousePressed() {
		if (isMove) {
			if (this.input.leftClick()) {
				zoom();
			} else if (this.input.rightClick()) {
				narrow();
			}
			match();
		}
	}

	protected void processMouseReleased() {
		if (isMove) {
			offset2 = 1.0D;
			match();
		}
	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.VK_ENTER) {
			this.doClick();
		}
	}

	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (!running) {
			String message = "Loading...";
			FontMetrics fontmetrics = g.getFontMetrics();
			g.drawString(message, x + (w / 2)
					- (fontmetrics.stringWidth(message) / 2), y + (h / 2));
			if (image != null) {
				g.drawImage(image, x, y, w, h);
			}
		} else {
			if (g instanceof Graphics2D) {
				GraphicsUtils.setExcellentRenderingHints((Graphics2D) g);
			}
			g.drawImage(image, x, y);
			int nx = input.getMouseX(), ny = input.getMouseY();
			if (nx > x && nx < getScreenX() + w && ny > y
					&& ny < getScreenY() + h) {
				Color oldColor = g.getColor();
				g.setAlpha(0.5F);
				g.setColor(Color.RED);
				g.drawOval(input.getMouseX() - 20 + 4,
						input.getMouseY() - 20 + 4, 20, 20);
				g.drawOval(input.getMouseX() - 20 + 3,
						input.getMouseY() - 20 + 3, 22, 22);
				g.drawOval(input.getMouseX() - 20 + 2,
						input.getMouseY() - 20 + 2, 24, 24);
				g.drawOval(input.getMouseX() - 20 + 1,
						input.getMouseY() - 20 + 1, 26, 26);
				g.setFont(GraphicsUtils.getFont("Helvetica", 1, 20));
				g.drawString("+", input.getMouseX() - 20 + 8,
						input.getMouseY() - 20 + 21);
				g.setColor(oldColor);
				g.setAlpha(1.0F);
			}
			if (g instanceof Graphics2D) {
				GraphicsUtils.setGeneralRenderingHints((Graphics2D) g);
			}
		}
	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
		}
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void run() {
		try {
			do {
				while (running) {
					if (graphics == null) {
						this.image = GraphicsUtils.createIntdexedImage(
								getWidth(), getHeight());
						this.graphics = this.image.createGraphics();
						narrow();
					}
					count++;
					if (!drawImage(graphics)) {
						index++;
					} else {
						index = 0;
						on = false;
					}
					if (index == 5) {
						on = true;
					}
					Thread.sleep(30L);
				}
			} while (isLoop);
		} catch (Exception ex) {
		}
	}

	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public boolean isMatching() {
		return matching;
	}

	public void setMatching(boolean matching) {
		this.matching = matching;
	}

	public void setLegible(boolean legible) {
		if (legible) {
			quality = 0;
		} else {
			quality = 1;
		}
	}

	/**
	 * 当前屏幕图像
	 * 
	 * @return
	 */
	public BufferedImage getImage() {
		return image;
	}

	public String getUIName() {
		return "Zoom";
	}

}
