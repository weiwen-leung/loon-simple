package org.loon.framework.javase.game.core.geom;

/**
 * Copyright 2008 - 2011
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
public class Matrix3f {

	private final static float DEGREE_TO_RAD = (float) Math.PI / 180;

	public float[] vals = new float[9];

	public Matrix3f() {
		idt();
	}

	public Matrix3f idt() {
		this.vals[0] = 1;
		this.vals[1] = 0;
		this.vals[2] = 0;

		this.vals[3] = 0;
		this.vals[4] = 1;
		this.vals[5] = 0;

		this.vals[6] = 0;
		this.vals[7] = 0;
		this.vals[8] = 1;

		return this;
	}

	public Matrix3f mul(Matrix3f m) {
		float v00 = vals[0] * m.vals[0] + vals[3] * m.vals[1] + vals[6]
				* m.vals[2];
		float v01 = vals[0] * m.vals[3] + vals[3] * m.vals[4] + vals[6]
				* m.vals[5];
		float v02 = vals[0] * m.vals[6] + vals[3] * m.vals[7] + vals[6]
				* m.vals[8];

		float v10 = vals[1] * m.vals[0] + vals[4] * m.vals[1] + vals[7]
				* m.vals[2];
		float v11 = vals[1] * m.vals[3] + vals[4] * m.vals[4] + vals[7]
				* m.vals[5];
		float v12 = vals[1] * m.vals[6] + vals[4] * m.vals[7] + vals[7]
				* m.vals[8];

		float v20 = vals[2] * m.vals[0] + vals[5] * m.vals[1] + vals[8]
				* m.vals[2];
		float v21 = vals[2] * m.vals[3] + vals[5] * m.vals[4] + vals[8]
				* m.vals[5];
		float v22 = vals[2] * m.vals[6] + vals[5] * m.vals[7] + vals[8]
				* m.vals[8];

		vals[0] = v00;
		vals[1] = v10;
		vals[2] = v20;
		vals[3] = v01;
		vals[4] = v11;
		vals[5] = v21;
		vals[6] = v02;
		vals[7] = v12;
		vals[8] = v22;

		return this;
	}

	public Matrix3f setToRotation(float angle) {
		angle = DEGREE_TO_RAD * angle;
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

		this.vals[0] = cos;
		this.vals[1] = sin;
		this.vals[2] = 0;

		this.vals[3] = -sin;
		this.vals[4] = cos;
		this.vals[5] = 0;

		this.vals[6] = 0;
		this.vals[7] = 0;
		this.vals[8] = 1;

		return this;
	}

	public Matrix3f setToTranslation(float x, float y) {
		this.vals[0] = 1;
		this.vals[1] = 0;
		this.vals[2] = 0;

		this.vals[3] = 0;
		this.vals[4] = 1;
		this.vals[5] = 0;

		this.vals[6] = x;
		this.vals[7] = y;
		this.vals[8] = 1;

		return this;
	}

	public Matrix3f setToScaling(float sx, float sy) {
		this.vals[0] = sx;
		this.vals[1] = 0;
		this.vals[2] = 0;

		this.vals[3] = 0;
		this.vals[4] = sy;
		this.vals[5] = 0;

		this.vals[6] = 0;
		this.vals[7] = 0;
		this.vals[8] = 1;

		return this;
	}

	public float det() {
		return vals[0] * vals[4] * vals[8] + vals[3] * vals[7] * vals[2]
				+ vals[6] * vals[1] * vals[5] - vals[0] * vals[7] * vals[5]
				- vals[3] * vals[1] * vals[8] - vals[6] * vals[4] * vals[2];
	}

	public Matrix3f set(Matrix3f mat) {
		vals[0] = mat.vals[0];
		vals[1] = mat.vals[1];
		vals[2] = mat.vals[2];
		vals[3] = mat.vals[3];
		vals[4] = mat.vals[4];
		vals[5] = mat.vals[5];
		vals[6] = mat.vals[6];
		vals[7] = mat.vals[7];
		vals[8] = mat.vals[8];
		return this;
	}

	public float[] getValues() {
		return vals;
	}
}
