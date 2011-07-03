package org.loon.framework.android.game.core.geom;


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
public class Matrix2f {

	public Vector2f col1 = new Vector2f();

	public Vector2f col2 = new Vector2f();

	public Matrix2f() {
	}

	public Matrix2f(float angle) {
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		col1.x = c;
		col2.x = -s;
		col1.y = s;
		col2.y = c;
	}

	public Matrix2f(Vector2f col1, Vector2f col2) {
		this.col1.set(col1);
		this.col2.set(col2);
	}

	public Vector2f multiply(Vector2f vec) {
		float x = col1.x * vec.x + col2.x * vec.y;
		float y = col1.y * vec.x + col2.y * vec.y;
		return new Vector2f(x, y);
	}

	public Matrix2f transpose() {
		return new Matrix2f(new Vector2f(col1.x, col2.x), new Vector2f(col1.y,
				col2.y));
	}

	public Matrix2f invert() {
		float a = col1.x, b = col2.x, c = col1.y, d = col2.y;
		Matrix2f m2d = new Matrix2f();
		float det = a * d - b * c;
		if (det == 0.0f) {
			throw new RuntimeException("det == 0.0");
		}

		det = 1.0f / det;
		m2d.col1.x = det * d;
		m2d.col2.x = -det * b;
		m2d.col1.y = -det * c;
		m2d.col2.y = det * a;
		return m2d;
	}

}
