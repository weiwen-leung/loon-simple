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
public class Matrix2D {

	public Vector2D col1 = new Vector2D();

	public Vector2D col2 = new Vector2D();

	public Matrix2D() {
	}

	public Matrix2D(double angle) {
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		col1.x = c;
		col2.x = -s;
		col1.y = s;
		col2.y = c;
	}

	public Matrix2D(Vector2D col1, Vector2D col2) {
		this.col1.set(col1);
		this.col2.set(col2);
	}

	public Vector2D multiply(Vector2D vec) {
		double x = col1.x * vec.x + col2.x * vec.y;
		double y = col1.y * vec.x + col2.y * vec.y;
		return new Vector2D(x, y);
	}

	public Matrix2D transpose() {
		return new Matrix2D(new Vector2D(col1.x, col2.x), new Vector2D(col1.y,
				col2.y));
	}

	public Matrix2D invert() {
		double a = col1.x, b = col2.x, c = col1.y, d = col2.y;
		Matrix2D m2d = new Matrix2D();
		double det = a * d - b * c;
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
