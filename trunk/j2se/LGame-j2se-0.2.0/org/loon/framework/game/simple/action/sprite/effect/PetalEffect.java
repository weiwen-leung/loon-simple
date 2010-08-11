package org.loon.framework.game.simple.action.sprite.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.GameManager;
import org.loon.framework.game.simple.action.map.RectBox;
import org.loon.framework.game.simple.action.sprite.ISprite;
import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.timer.LTimer;
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
public class PetalEffect implements ISprite {

	/**
	 * 樱花灿烂特效
	 */
	private static final long serialVersionUID = 1L;

	private int max = 30, flag = 13, matching = -1, layer;

	final static private Image[] images = {
			GraphicsUtils.loadImage("system/image/sakura2.png"),
			GraphicsUtils.loadImage("system/image/sakura1.png") };

	private boolean visible = true;

	private BufferedImage tmp_img;

	private Graphics2D graphics2D;

	private int sakura_width = images[0].getWidth(null);

	private int sakura_height = images[0].getHeight(null);

	private int[] xs = new int[max], ys = new int[max], maxs = new int[max],
			d = new int[max], s = new int[max];

	private boolean[] bools = new boolean[max];

	private int x, y, tmp_x, tmp_y, width, height, randx, randy;

	private LTimer timer = new LTimer(100);

	public PetalEffect() {
		this(0, 0);
	}

	public PetalEffect(int x, int y) {
		this(x, y, GameManager.getSystemHandler().getWidth(), GameManager
				.getSystemHandler().getHeight());
	}

	public PetalEffect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.randx = LSystem.random.nextInt(w);
		this.randy = LSystem.random.nextInt(h);
		this.tmp_img = GraphicsUtils.createImage(width, height, true);
		for (int i = 0; i < max; i++) {
			this.xs[i] = (int) (LSystem.random.nextFloat() * (w - 20));
			this.ys[i] = (int) (-LSystem.random.nextFloat() * h - 30);
			this.maxs[i] = (int) (LSystem.random.nextFloat() * 2 * flag - flag);
			this.d[i] = (i < max / 2) ? 1 : 0;
			this.bools[i] = (int) (LSystem.random.nextFloat()) == 0;
			this.s[i] = (int) (LSystem.random.nextFloat() * 3 + 5);
		}
	}

	public void createUI(Graphics2D g) {
		if (visible) {
			g.drawImage(tmp_img, x, y, null);
			if (graphics2D != null) {
				graphics2D.dispose();
				graphics2D = null;
			}
		}
	}

	private final int abs(int i) {
		return i > 0 ? i : -i;
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public void update(long elapsedTime) {
		if (visible && timer.action(elapsedTime)) {
			this.tmp_img = GraphicsUtils.createImage(width, height, true);
			this.graphics2D = tmp_img.createGraphics();
			for (int i = 0; i < max; i++) {
				if (maxs[i] == flag || maxs[i] == -flag) {
					bools[i] = !bools[i];
				}
				maxs[i] += bools[i] ? 1 : -1;
				if (d[i] == 0
						&& (matching == i || matching < 0
								&& abs(randx - xs[i] - sakura_width / 2) < sakura_width / 2
								&& abs(randy - ys[i] - sakura_height / 2) < sakura_height / 2)) {
					xs[i] = randx - sakura_width / 2;
					ys[i] = randy - sakura_height / 2;
					maxs[i] = 0;
					matching = i;
				} else {
					xs[i] += maxs[i];
					if (ys[i] < height) {
						ys[i] += s[i];
					} else {
						xs[i] = (int) (LSystem.random.nextFloat() * (width - 1));
						ys[i] = -(int) (LSystem.random.nextFloat() * 1)
								- images[d[i]].getHeight(null);
					}
				}
				this.tmp_x = xs[i];
				this.tmp_y = ys[i];
				graphics2D.drawImage(images[d[i]], tmp_x, tmp_y, null);
			}
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getMatching() {
		return matching;
	}

	public void finalize() {
		if (graphics2D != null) {
			graphics2D.dispose();
			graphics2D = null;
		}
	}

	public void setMatching(int matching) {
		this.matching = matching;
	}

	public void setLocation(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public RectBox getCollisionBox() {
		return new RectBox(x,y,width,height);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public float getAlpha() {
		return 0;
	}
}
