package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.GameManager;
import org.loon.framework.javase.game.action.map.Camera;
import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

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
 * @version 0.1
 */
public abstract class AbstractBackground implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Camera camera;

	private int layer;

	protected float alpha;

	protected boolean visible;

	public AbstractBackground(int w, int h) {
		this.camera = new Camera(0, 0, w, h);
		this.visible = true;
	}

	public AbstractBackground() {
		this(GameManager.getSystemHandler().getWidth(), GameManager
				.getSystemHandler().getHeight());
	}

	public void createUI(LGraphics g) {
		createUI(g, (int) camera.getMinX(), (int) camera.getMinY(),
				(int) camera.getWidth(), (int) camera.getHeight());
	}

	public void setSize(float w, float h) {
		camera.setSize(w, h);
	}

	public void setCenter(float x, float y) {
		camera.setCenter(x, y);
	}

	public abstract void createUI(LGraphics g, int x, int y, int w, int h);

	public void setX(float x) {
		camera.setX(x);
	}

	public void setY(float y) {
		camera.setY(y);
	}

	public int getWidth() {
		return (int) camera.getWidth();
	}

	public int getHeight() {
		return (int) camera.getHeight();
	}

	public void setToCenter(int cx, int cy) {
		camera.setCenter(cx, cy);
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	public RectBox getCollisionBox() {
		return camera.getRectBox();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void update(long timer) {

	}

	public Camera getCamera() {
		return camera;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public double getX() {
		return camera.getX();
	}

	public double getY() {
		return camera.getY();
	}

	public int x() {
		return (int) camera.getX();
	}

	public int y() {
		return (int) camera.getY();
	}
}
