package org.loon.framework.android.game.action.map.shapes;

import org.loon.framework.android.game.core.graphics.geom.Ellipse2D;

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
public class Circle {

	private float x, y;

	private float radius;

	public Circle(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.radius = r;
	}

	public Circle() {

	}

	public boolean equals(Object other) {
		if (other instanceof Circle) {
			Circle oCircle = (Circle) other;
			return oCircle.x == x && oCircle.y == y && oCircle.radius == radius;
		}
		return false;
	}

	public double getCubage() {
		return Math.PI * radius * radius;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public boolean intersects(Circle other) {
		float r1 = getRadius();
		float r2 = other.getRadius();

		float dx = getX() - other.getX();
		float dy = getY() - other.getY();
		float dxSq = dx * dx;
		float dySq = dy * dy;

		float circleDistSq = (dxSq + dySq);

		if ((r1 + r2) * (r1 + r2) >= circleDistSq) {
			return true;
		} else {
			return false;
		}
	}

	public void merge(Circle one, Circle two) {
		float dx = one.getX() - two.getX();
		float dy = one.getY() - two.getY();
		float dxSq = dx * dx;
		float dySq = dy * dy;
		float circleDistSq = (dxSq + dySq);
		float r2 = (one.getRadius() - two.getRadius());
		if (r2 * r2 >= circleDistSq) {
			if (one.getRadius() < two.getRadius()) {
				setRadius(two.getRadius());
				setX(two.getX());
				setY(two.getY());
			} else {
				setRadius(one.getRadius());
				setX(one.getX());
				setY(one.getY());
			}
		} else {
			double circleDist = Math.sqrt(circleDistSq);
			double r = (circleDist + one.getRadius() + two.getRadius()) / 2.;
			setRadius((float) Math.ceil(r));
			if (circleDist > 0) {
				double f = ((r - one.getRadius()) / circleDist);
				setX(one.getX() - ((float) Math.ceil(f * dx)));
				setY(one.getY() - ((float) Math.ceil(f * dy)));
			} else {
				setX(one.getX());
				setY(one.getY());
			}
		}
	}

	public Ellipse2D.Float getShape() {
		float diameter = 2 * radius;
		return new Ellipse2D.Float(0, 0, diameter, diameter);
	}

}
