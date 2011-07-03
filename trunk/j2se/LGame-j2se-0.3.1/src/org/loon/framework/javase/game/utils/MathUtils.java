package org.loon.framework.javase.game.utils;

import org.loon.framework.javase.game.core.LSystem;

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
public class MathUtils {

	public static final float PI_OVER2 = 1.5708f;

	public static final float PI_OVER4 = 0.785398f;

	static private final int BIG_ENOUGH_INT = 16 * 1024;

	static private final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;

	static private final double CEIL = 0.9999999;

	static private final double BIG_ENOUGH_CEIL = Double
			.longBitsToDouble(Double.doubleToLongBits(BIG_ENOUGH_INT + 1) - 1);

	static private final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;

	static private final int ATAN2_BITS = 7;

	static private final int ATAN2_BITS2 = ATAN2_BITS << 1;

	static private final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);

	static private final int ATAN2_COUNT = ATAN2_MASK + 1;

	static private final int ATAN2_DIM = (int) Math.sqrt(ATAN2_COUNT);

	static private final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);

	static private final float[] atan2 = new float[ATAN2_COUNT];

	public static final float PI = 3.1415927f;

	public static final float TWO_PI = 6.28319f;

	static private final int SIN_BITS = 13;

	static private final int SIN_MASK = ~(-1 << SIN_BITS);

	static private final int SIN_COUNT = SIN_MASK + 1;

	static private final float radFull = PI * 2;

	static private final float degFull = 360;

	static private final float radToIndex = SIN_COUNT / radFull;

	static private final float degToIndex = SIN_COUNT / degFull;

	public static final float RAD_TO_DEG = 180.0f / PI;

	public static final float DEG_TO_RAD = PI / 180.0f;

	public static final float[] sin = new float[SIN_COUNT];

	public static final float[] cos = new float[SIN_COUNT];

	static {
		for (int i = 0; i < SIN_COUNT; i++) {
			float a = (i + 0.5f) / SIN_COUNT * radFull;
			sin[i] = (float) Math.sin(a);
			cos[i] = (float) Math.cos(a);
		}
		for (int i = 0; i < 360; i += 90) {
			sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i
					* DEG_TO_RAD);
			cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i
					* DEG_TO_RAD);
		}
	}

	static public final float tan(float angle) {
		return (float) Math.tan(angle);
	}

	static public final float asin(float value) {
		return (float) Math.asin(value);
	}

	static public final float acos(float value) {
		return (float) Math.acos(value);
	}

	static public final float atan(float value) {
		return (float) Math.atan(value);
	}

	static public final float mag(float a, float b) {
		return (float) Math.sqrt(a * a + b * b);
	}

	static public final float mag(float a, float b, float c) {
		return (float) Math.sqrt(a * a + b * b + c * c);
	}

	static public final float dist(float x1, float y1, float x2, float y2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1));
	}

	static public final float dist(float x1, float y1, float z1, float x2,
			float y2, float z2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1));
	}

	static public final float abs(float n) {
		return (n < 0) ? -n : n;
	}

	static public final int abs(int n) {
		return (n < 0) ? -n : n;
	}

	static public final float sq(float a) {
		return a * a;
	}

	static public final float sqrt(float a) {
		return (float) Math.sqrt(a);
	}

	static public final float log(float a) {
		return (float) Math.log(a);
	}

	static public final float exp(float a) {
		return (float) Math.exp(a);
	}

	static public final float pow(float a, float b) {
		return (float) Math.pow(a, b);
	}

	static public final int max(int a, int b) {
		return (a > b) ? a : b;
	}

	static public final int max(int a, int b, int c) {
		return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
	}

	static public final float max(float a, float b, float c) {
		return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
	}

	static public final float norm(float value, float start, float stop) {
		return (value - start) / (stop - start);
	}

	static public final float map(float value, float istart, float istop,
			float ostart, float ostop) {
		return ostart + (ostop - ostart)
				* ((value - istart) / (istop - istart));
	}

	static public final float degrees(float radians) {
		return radians * MathUtils.RAD_TO_DEG;
	}

	static public final float radians(float degrees) {
		return degrees * MathUtils.DEG_TO_RAD;
	}

	public static final float sin(float rad) {
		return sin[(int) (rad * radToIndex) & SIN_MASK];
	}

	public static final float cos(float rad) {
		return cos[(int) (rad * radToIndex) & SIN_MASK];
	}

	public static final float sinDeg(float deg) {
		return sin[(int) (deg * degToIndex) & SIN_MASK];
	}

	public static final float cosDeg(float deg) {
		return cos[(int) (deg * degToIndex) & SIN_MASK];
	}

	static {
		for (int i = 0; i < ATAN2_DIM; i++) {
			for (int j = 0; j < ATAN2_DIM; j++) {
				float x0 = (float) i / ATAN2_DIM;
				float y0 = (float) j / ATAN2_DIM;
				atan2[j * ATAN2_DIM + i] = (float) Math.atan2(y0, x0);
			}
		}
	}

	public static final float atan2(float y, float x) {
		float add, mul;
		if (x < 0) {
			if (y < 0) {
				y = -y;
				mul = 1;
			} else
				mul = -1;
			x = -x;
			add = -3.141592653f;
		} else {
			if (y < 0) {
				y = -y;
				mul = -1;
			} else
				mul = 1;
			add = 0;
		}
		float invDiv = 1 / ((x < y ? y : x) * INV_ATAN2_DIM_MINUS_1);
		int xi = (int) (x * invDiv);
		int yi = (int) (y * invDiv);
		return (atan2[yi * ATAN2_DIM + xi] + add) * mul;
	}

	public static final int random(int range) {
		return LSystem.random.nextInt(range + 1);
	}

	public static final int random(int start, int end) {
		return start + LSystem.random.nextInt(end - start + 1);
	}

	public static final boolean randomBoolean() {
		return LSystem.random.nextBoolean();
	}

	public static final float random() {
		return LSystem.random.nextFloat();
	}

	public static final float random(float range) {
		return LSystem.random.nextFloat() * range;
	}

	public static final float random(float start, float end) {
		return start + LSystem.random.nextFloat() * (end - start);
	}

	public static int nextPowerOfTwo(int value) {
		if (value == 0)
			return 1;
		if ((value & value - 1) == 0)
			return value;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}

	public static int floor(float x) {
		return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	}

	public static int floorPositive(float x) {
		return (int) x;
	}

	public static int ceil(float x) {
		return (int) (x + BIG_ENOUGH_CEIL) - BIG_ENOUGH_INT;
	}

	public static int ceilPositive(float x) {
		return (int) (x + CEIL);
	}

	public static int round(float x) {
		return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
	}

	public static int roundPositive(float x) {
		return (int) (x + 0.5f);
	}

	public static float Barycentric(float value1, float value2, float value3,
			float amount1, float amount2) {
		return value1 + (value2 - value1) * amount1 + (value3 - value1)
				* amount2;
	}

	public static float catmullRom(float value1, float value2, float value3,
			float value4, float amount) {
		double amountSquared = amount * amount;
		double amountCubed = amountSquared * amount;
		return (float) (0.5 * (2.0 * value2 + (value3 - value1) * amount
				+ (2.0 * value1 - 5.0 * value2 + 4.0 * value3 - value4)
				* amountSquared + (3.0 * value2 - value1 - 3.0 * value3 + value4)
				* amountCubed));
	}

	public static float clamp(float value, float min, float max) {
		value = (value > max) ? max : value;
		value = (value < min) ? min : value;
		return value;
	}

	public static float distance(float value1, float value2) {
		return Math.abs(value1 - value2);
	}

	public static float hermite(float value1, float tangent1, float value2,
			float tangent2, float amount) {
		double v1 = value1, v2 = value2, t1 = tangent1, t2 = tangent2, s = amount, result;
		double sCubed = s * s * s;
		double sSquared = s * s;

		if (amount == 0f) {
			result = value1;
		} else if (amount == 1f) {
			result = value2;
		} else {
			result = (2 * v1 - 2 * v2 + t2 + t1) * sCubed
					+ (3 * v2 - 3 * v1 - 2 * t1 - t2) * sSquared + t1 * s + v1;
		}
		return (float) result;
	}

	public static float lerp(float value1, float value2, float amount) {
		return value1 + (value2 - value1) * amount;
	}

	public static float max(float value1, float value2) {
		return Math.max(value1, value2);
	}

	public static float min(float value1, float value2) {
		return Math.min(value1, value2);
	}

	public static float smoothStep(float value1, float value2, float amount) {
		float result = clamp(amount, 0f, 1f);
		result = hermite(value1, 0f, value2, 0f, result);
		return result;
	}

	public static float toDegrees(float radians) {
		return (float) (radians * 57.295779513082320876798154814105);
	}

	public static float toRadians(float degrees) {
		return (float) (degrees * 0.017453292519943295769236907684886);
	}

	public static float wrapAngle(float angle) {
		angle = (float) Math.IEEEremainder((double) angle, 6.2831854820251465d);
		if (angle <= -3.141593f) {
			angle += 6.283185f;
			return angle;
		}
		if (angle > 3.141593f) {
			angle -= 6.283185f;
		}
		return angle;
	}

}
