package org.loon.framework.android.game.action.map.shapes;


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
public class Camera {

	private float minX, minY;

	private float maxX, maxY;

	private float width, height;

	private RectBox bounds;

	public Camera(float width, float height) {
		this(0.0F, 0.0F, width, height);
	}

	public Camera(float x, float y, float width, float height) {
		this.minX = x;
		this.maxX = x + width;
		this.minY = y;
		this.maxY = y + height;
		this.width = width;
		this.height = height;
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		this.maxX = minX + width;
		this.maxY = minY + height;
	}

	public void setX(float x) {
		this.minX = x;
		this.maxX = x + width;
	}

	public void setY(float y) {
		this.minY = y;
		this.maxY = y + height;
	}

	public float getMinX() {
		return this.minX;
	}

	public float getMaxX() {
		return this.maxX;
	}

	public float getMinY() {
		return this.minY;
	}

	public float getMaxY() {
		return this.maxY;
	}

	public float getOldWidth() {
		return width;
	}

	public float getOldHeight() {
		return height;
	}

	public float getWidth() {
		return this.maxX - this.minX;
	}

	public float getHeight() {
		return this.maxY - this.minY;
	}

	public float getX() {
		return minX;
	}

	public float getY() {
		return minY;
	}

	public float getCenterX() {
		return minX + (this.maxX - minX) / 2;
	}

	public float getCenterY() {
		return minY + (this.maxY - minY) / 2;
	}

	public void setCenter(float cx, float cy) {
		final float dX = cx - this.getCenterX();
		final float dY = cy - this.getCenterY();
		this.minX += dX;
		this.maxX += dX;
		this.minY += dY;
		this.maxY += dY;
	}

	public void offsetCenter(float offsetX, float offsetY) {
		this.setCenter(this.getCenterX() + offsetX, this.getCenterY()
						+ offsetY);
	}

	public RectBox getRectBox() {
		if (bounds == null) {
			bounds = new RectBox((int) minX, (int) minY, (int) getWidth(),
					(int) getHeight());
		} else {
			bounds.setBounds((int) minX, (int) minY, (int) getWidth(),
					(int) getHeight());
		}
		return bounds;
	}

}
