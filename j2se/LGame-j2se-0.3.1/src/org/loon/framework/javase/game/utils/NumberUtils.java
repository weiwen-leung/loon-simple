package org.loon.framework.javase.game.utils;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Copyright 2008 - 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng  
 * @email：ceponline@yahoo.com.cn 
 * @version 0.1
 */
public class NumberUtils {

	//PS:因为手机容量问题，比较PC版有所简化
	private NumberUtils(){
		
	}
	static public final float PI = 3.1415927f;

	static private final int SIN_BITS = 13; // Adjust for accuracy.
	static private final int SIN_MASK = ~(-1 << SIN_BITS);
	static private final int SIN_COUNT = SIN_MASK + 1;

	static private final float radFull = PI * 2;
	static private final float degFull = 360;
	static private final float radToIndex = SIN_COUNT / radFull;
	static private final float degToIndex = SIN_COUNT / degFull;

	static public final float radiansToDegrees = 180f / PI;
	static public final float degreesToRadians = PI / 180;

	static public final float[] sin = new float[SIN_COUNT];
	static public final float[] cos = new float[SIN_COUNT];

	static {
		for (int i = 0; i < SIN_COUNT; i++) {
			float a = (i + 0.5f) / SIN_COUNT * radFull;
			sin[i] = (float)Math.sin(a);
			cos[i] = (float)Math.cos(a);
		}
		for (int i = 0; i < 360; i += 90) {
			sin[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
			cos[(int)(i * degToIndex) & SIN_MASK] = (float)Math.cos(i * degreesToRadians);
		}
	}

	static public final float sin (float rad) {
		return sin[(int)(rad * radToIndex) & SIN_MASK];
	}

	static public final float cos (float rad) {
		return cos[(int)(rad * radToIndex) & SIN_MASK];
	}

	static public final float sinDeg (float deg) {
		return sin[(int)(deg * degToIndex) & SIN_MASK];
	}

	static public final float cosDeg (float deg) {
		return cos[(int)(deg * degToIndex) & SIN_MASK];
	}

	// ---

	static private final int ATAN2_BITS = 7; // Adjust for accuracy.
	static private final int ATAN2_BITS2 = ATAN2_BITS << 1;
	static private final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);
	static private final int ATAN2_COUNT = ATAN2_MASK + 1;
	static private final int ATAN2_DIM = (int)Math.sqrt(ATAN2_COUNT);
	static private final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);
	static private final float[] atan2 = new float[ATAN2_COUNT];
	static {
		for (int i = 0; i < ATAN2_DIM; i++) {
			for (int j = 0; j < ATAN2_DIM; j++) {
				float x0 = (float)i / ATAN2_DIM;
				float y0 = (float)j / ATAN2_DIM;
				atan2[j * ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
			}
		}
	}

	static public final float atan2 (float y, float x) {
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
		int xi = (int)(x * invDiv);
		int yi = (int)(y * invDiv);
		return (atan2[yi * ATAN2_DIM + xi] + add) * mul;
	}

	// ---

	static public Random random = new Random();

	/**
	 * Returns a random number between 0 (inclusive) and the specified value (inclusive).
	 */
	static public final int random (int range) {
		return random.nextInt(range + 1);
	}

	static public final int random (int start, int end) {
		return start + random.nextInt(end - start + 1);
	}

	static public final boolean randomBoolean () {
		return random.nextBoolean();
	}

	static public final float random () {
		return random.nextFloat();
	}

	static public final float random (float range) {
		return random.nextFloat() * range;
	}

	static public final float random (float start, float end) {
		return start + random.nextFloat() * (end - start);
	}

	// ---

	static public int nextPowerOfTwo (int value) {
		if (value == 0) return 1;
		if ((value & value - 1) == 0) return value;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}

	static public boolean isPowerOfTwo (int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	// ---

	static private final int BIG_ENOUGH_INT = 16 * 1024;
	static private final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	static private final double CEIL = 0.9999999;
	static private final double BIG_ENOUGH_CEIL = Double.longBitsToDouble(Double.doubleToLongBits(BIG_ENOUGH_INT + 1) - 1);
	static private final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;

	/**
	 * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats from
	 * -(2^14) to (Float.MAX_VALUE - 2^14).
	 */
	static public int floor (float x) {
		return (int)(x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats that are
	 * positive. Note this method simply casts the float to int.
	 */
	static public int floorPositive (float x) {
		return (int)x;
	}

	/**
	 * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats from
	 * -(2^14) to (Float.MAX_VALUE - 2^14).
	 */
	static public int ceil (float x) {
		return (int)(x + BIG_ENOUGH_CEIL) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats that
	 * are positive.
	 */
	static public int ceilPositive (float x) {
		return (int)(x + CEIL);
	}

	/**
	 * Returns the closest integer to the specified float. This method will only properly round floats from -(2^14) to
	 * (Float.MAX_VALUE - 2^14).
	 */
	static public int round (float x) {
		return (int)(x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the closest integer to the specified float. This method will only properly round floats that are positive.
	 */
	static public int roundPositive (float x) {
		return (int)(x + 0.5f);
	}

	/**
	 * 获得一个随机的unsigned int
	 * 
	 * @param maxInt
	 * @param doNotInclude1
	 * @param doNotInclude2
	 * @return
	 */
	public static int getRandomUnsignedInt(int maxInt, int doNotInclude1,
			int doNotInclude2) {
		int n = 2;
		if (doNotInclude1 == doNotInclude2) {
			doNotInclude2 = maxInt + 1;
		}
		if (doNotInclude1 > doNotInclude2) {
			n = doNotInclude2;
			doNotInclude2 = doNotInclude1;
			doNotInclude1 = n;
			n = 2;
		}
		if (doNotInclude1 < 0) {
			doNotInclude1 = maxInt + 1;
		}
		if (doNotInclude2 < 0) {
			doNotInclude2 = maxInt + 1;
		}
		if (doNotInclude1 > maxInt) {
			n--;
		}
		if (doNotInclude2 > maxInt) {
			n--;
		}
		int val = (int) Math.floor(Math.random()
				* ((double) maxInt - (double) n));
		if (val >= doNotInclude1) {
			val++;
		}
		if (val >= doNotInclude2) {
			val++;
		}
		return val;
	}

	/**
	 * 获得一个随机的unsigned int
	 * 
	 * @param maxInt
	 * @param doNotInclude
	 * @return
	 */
	public static int getRandomUnsignedInt(int maxInt, int doNotInclude) {
		int val = 0;
		if (doNotInclude > -1 && doNotInclude <= maxInt) {
			val = (int) Math.floor(Math.random() * ((double) maxInt - 1.0D));
			if (val >= doNotInclude) {
				val++;
			}
		} else {
			val = (int) Math.floor(Math.random() * (double) maxInt);
		}
		return val;
	}

	/**
	 * 获得一个随机的unsigned int
	 * 
	 * @param maxInt
	 * @return
	 */
	public static int getRandomUnsignedInt(int maxInt) {
		return getRandomUnsignedInt(maxInt, -1);
	}

	/**
	 * 返回一组随机数
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static int getRandomInt(int num1, int num2) {
		int result = 0;
		if (num2 > -1 && num2 <= num1) {
			result = (int) Math.floor(Math.random() * ((double) num1 - 1.0D));
			if (result >= num2) {
				result++;
			}
		} else {
			result = (int) Math.floor(Math.random() * (double) num1);
		}
		return result;
	}

	/**
	 * 取中值
	 * 
	 * @param i
	 * @param min
	 * @param max
	 * @return
	 */
	public static int mid(int i, int min, int max) {
		return Math.max(i, Math.min(min, max));
	}

	final static private String[] zeros = { "", "0", "00", "000", "0000",
			"00000", "000000", "0000000", "00000000", "000000000", "0000000000" };

	/**
	 * 为指定数值补足位数
	 * 
	 * @param number
	 * @param numDigits
	 * @return
	 */
	public static String addZeros(long number, int numDigits) {
		return addZeros(String.valueOf(number), numDigits);
	}

	/**
	 * 为指定数值补足位数
	 * 
	 * @param number
	 * @param numDigits
	 * @return
	 */
	public static String addZeros(String number, int numDigits) {
		int length = numDigits - number.length();
		if (length != 0) {
			number = zeros[length] + number;
		}
		return number;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNan(String param) {
		boolean result = false;
		if (param == null || "".equals(param)) {
			return result;
		}
		param = param.replace('d', '_').replace('f', '_');
		try {
			Double test = new Double(param);
			test.intValue();
			result = true;
		} catch (NumberFormatException ex) {
			return result;
		}
		return result;
	}

	/**
	 * 检查一个数字是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(int val) {
		return (val == Integer.MIN_VALUE) ? true : 0 == val;
	}

	/**
	 * 检查一个字符串数字是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		return (val == null | "".equals(val) | val.equals(Integer
				.toString(Integer.MAX_VALUE)));
	}

	/**
	 * 单纯计算两个数值的百分比
	 * 
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	public static double toPercent(long divisor, long dividend) {
		if (divisor == 0 || dividend == 0) {
			return 0d;
		}
		double cd = divisor * 1d;
		double pd = dividend * 1d;

		return (Math.round(cd / pd * 10000) * 1d) / 100;
	}

	/**
	 * 获得一组指定长度的随机数
	 * 
	 * @param size
	 * @return
	 */
	public static int toRandom(int size) {
		Random rad = new Random();
		rad.setSeed(System.currentTimeMillis());
		return Math.abs(rad.nextInt()) % size;
	}


	/**
	 * 获得100%进制剩余数值百分比。
	 * 
	 * @param maxValue
	 * @param minusValue
	 * @return
	 */
	public static float minusPercent(float maxValue, float minusValue) {
		return 100 - ((minusValue / maxValue) * 100);
	}

	/**
	 * 获得100%进制数值百分比。
	 * 
	 * @param maxValue
	 * @param minusValue
	 * @return
	 */
	public static float percent(float maxValue, float minValue) {
		return (minValue / maxValue) * 100;
	}

	/**
	 * 将value转化成中文数字的大小写
	 * 
	 * @param value
	 * @param type
	 *            1:大写中文 2：小写中文
	 * 
	 * @return
	 */
	public static String toConvertCnNumber(long value, int type) {
		String[] chNumber = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String[] digit = { "", "拾", "佰", "仟", "万", "十", "百", "仟" };
		switch (type) {
		case 1:
			String[] capsCNumber = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒",
					"捌", "玖" };
			chNumber = capsCNumber;
		case 2:
			String[] minCNumber = { "零", "一", "二", "三", "四", "五", "六", "七",
					"八", "九" };
			chNumber = minCNumber;
		}
		String retStr = "";

		String inputStr = Long.toString(value);
		for (int i = inputStr.length(); i > 0; i--) {
			char ch = inputStr.charAt(i - 1);
			if (ch != '0') {

				retStr = chNumber[ch - '0'] + digit[inputStr.length() - i]
						+ retStr;
			} else {
				if (inputStr.length() - i == 4)
					retStr = "零万" + retStr;
				else
					retStr = "零" + retStr;
			}
		}

		int pos = retStr.indexOf("零零");
		while (pos >= 0) {
			retStr = retStr.replaceAll("零零", "零");
			pos = retStr.indexOf("零零");
		}

		retStr = retStr.replaceAll("零万", "万");

		return retStr;
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */

	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */

	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}


	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */

	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}

