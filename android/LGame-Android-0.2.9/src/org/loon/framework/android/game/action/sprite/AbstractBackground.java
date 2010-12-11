package org.loon.framework.android.game.action.sprite;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;

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
 * @version 0.1.1
 */
public abstract class AbstractBackground extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Camera camera;

	private RectBox bounds;

	protected float alpha;

	protected boolean visible;

	public AbstractBackground(int w, int h) {
		this.camera = new Camera(0, 0, w, h);
		this.visible = true;
	}

	public AbstractBackground() {
		this(LSystem.getSystemHandler().getWidth(), LSystem
				.getSystemHandler().getHeight());
	}

	public void createUI(LGraphics g) {
		createUI(g, x(), y(), camera.getXover(), camera.getYover(), camera
				.getWidth(), camera.getHeight());
	}

	public abstract void createUI(LGraphics g, int nx, int ny, int x, int y,
			int w, int h);

	public int getWidth() {
		return camera.getWidth() - camera.getXover();
	}

	public int getHeight() {
		return camera.getHeight() - camera.getYover();
	}

	public void setSize(int w, int h) {
		camera.setSize(w, h);
	}

	public void setToCenter(int x, int y, int w, int h) {
		camera.setLocation(x + (w / 2) - (camera.getWidth() / 2), y + (h / 2)
				- (camera.getHeight() / 2));
	}

	public void setToCenter(Sprite centered) {
		this.setToCenter(centered.x(), centered.y(), centered.getWidth(),
				centered.getHeight());
	}

	public void setClip(int x, int y) {
		camera.setClip(x, y);
	}

	public void setClip(int x, int y, int width, int height) {
		camera.setClip(x, y, width, height);
	}

	public void setClip(Rectangle r) {
		camera.setClip(r);
	}

	public void setClip(RectBox r) {
		camera.setClip(r);
	}

	public RectBox getClip() {
		return camera.getClip();
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	public RectBox getCollisionBox() {
		if (bounds == null) {
			bounds = new RectBox(x(), y(), camera.getWidth(), camera
					.getHeight());
		} else {
			bounds.setBounds(x(), y(), camera.getWidth(), camera.getHeight());
		}
		return bounds;
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

}

