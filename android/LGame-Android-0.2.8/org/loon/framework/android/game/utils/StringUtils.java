package org.loon.framework.android.game.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 
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
final public class StringUtils {
	// PS:因为手机容量问题，比较PC版有所简化
	private StringUtils() {
	}

	/**
	 * 过滤指定字符串
	 * 
	 * @param string
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replace(String string, String oldString,
			String newString) {
		if (string == null)
			return null;
		if (newString == null)
			return string;
		int i = 0;
		if ((i = string.indexOf(oldString, i)) >= 0) {
			char string2[] = string.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(string2.length);
			buf.append(string2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = string.indexOf(oldString, i)) > 0; j = i) {
				buf.append(string2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(string2, j, string2.length - j);
			return buf.toString();
		} else {
			return string;
		}
	}

	/**
	 * 不匹配大小写的过滤指定字符串
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 不匹配大小写的过滤指定字符串
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @param count
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 以指定条件过滤字符串
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @param count
	 * @return
	 */
	public static final String replace(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 以指定字符分割字符串
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static String[] split(String str, char c) {
		str += c;
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				n++;
			}
		}
		String out[] = new String[n];
		for (int i = 0; i < n; i++) {
			int index = str.indexOf(c);
			out[i] = str.substring(0, index);
			str = str.substring(index + 1, str.length());
		}
		return out;
	}

	/**
	 * 检查一组字符串是否完全由中文组成
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinaLanguage(String str) {
		char[] chars = str.toCharArray();
		int[] ints = new int[2];
		boolean isChinese = false;
		int length = chars.length;
		byte[] bytes = null;
		for (int i = 0; i < length; i++) {
			bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isChinese = true;
				}
			} else {
				return false;
			}
		}
		return isChinese;
	}


	/**
	 * 判断是否为null
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String param) {
		return param == null || param.length() == 0 || param.trim().equals("");
	}

	/**
	 * 判断是否可能为true
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isCovertBoolean(String param) {
		if (isEmpty(param))
			return false;
		switch (param.charAt(0)) {
		case 49: // '1'
		case 84: // 'T'
		case 89: // 'Y'
		case 116: // 't'
		case 121: // 'y'
			return true;
		}
		return false;
	}

	/**
	 * 显示指定编码下的字符长度
	 * 
	 * @param encoding
	 * @param str
	 * @return
	 */
	public static int getBytesLengthOfEncoding(String encoding, String str) {
		if (str == null || str.length() == 0)
			return 0;
		try {
			byte bytes[] = str.getBytes(encoding);
			int length = bytes.length;
			return length;
		} catch (UnsupportedEncodingException exception) {
			System.err.println(exception.getMessage());
		}
		return 0;
	}

	/**
	 * 转化指定字符串为指定编码格式
	 * 
	 * @param context
	 * @param encoding
	 * @return
	 */
	public static String getSpecialString(String context, String encoding) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(context
					.getBytes());
			InputStreamReader isr = new InputStreamReader(in, encoding);
			BufferedReader reader = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			String result;
			while ((result = reader.readLine()) != null) {
				buffer.append(result);
			}
			return buffer.toString();
		} catch (Exception ex) {
			return context;
		}
	}

	/**
	 * 批量转换字符串数组编码
	 * 
	 * @param s
	 * @return
	 */
	public String[] getString(String[] strs, String sourceEncoding,
			String objectEncoding) {
		String[] ss = new String[strs.length];
		try {
			for (int i = 0; i < strs.length; i++) {
				byte[] aa = strs[i].getBytes(sourceEncoding);
				ss[i] = new String(aa, objectEncoding);
			}
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return ss;
	}

	/**
	 * 检查指定字符串中是否存在中文字符。
	 * 
	 * @param checkStr
	 *            指定需要检查的字符串。
	 * @return 逻辑值（True Or False）。
	 */
	public static final boolean hasChinese(String checkStr) {
		boolean checkedStatus = false;
		boolean isError = false;
		String spStr = " _-";
		int checkStrLength = checkStr.length() - 1;
		for (int i = 0; i <= checkStrLength; i++) {
			char ch = checkStr.charAt(i);
			if (ch < '\176') {
				ch = Character.toUpperCase(ch);
				if (((ch < 'A') || (ch > 'Z')) && ((ch < '0') || (ch > '9'))
						&& (spStr.indexOf(ch) < 0)) {
					isError = true;
				}
			}
		}
		checkedStatus = !isError;
		return checkedStatus;
	}

	/**
	 * 以正则表达式部分截取
	 * 
	 * @param value
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String subStitute(String value, String pattern,
			String replacement) {
		if (value == null || value.length() == 0)
			return value;
		if (pattern == null || pattern.length() == 0)
			return value;
		StringBuffer sb = new StringBuffer();
		do {
			int patternIndex = value.indexOf(pattern);
			if (patternIndex == -1) {
				sb.append(value);
				break;
			}
			sb.append(value.substring(0, patternIndex) + replacement);
			value = value.substring(patternIndex + pattern.length(), value
					.length());
		} while (true);
		return sb.toString();
	}

	/**
	 * 检查是否为纯字母
	 * 
	 * @param value
	 * @return
	 */
	public final static boolean isAlphabet(String value) {
		if (value == null || value.length() == 0)
			return false;
		for (int i = 0; i < value.length(); i++) {
			char c = Character.toUpperCase(value.charAt(i));
			if ('A' <= c && c <= 'Z')
				return true;
		}
		return false;
	}

	/**
	 * 检查是否为字母与数字混合
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isAlphabetNumeric(String value) {
		if (value == null || value.trim().length() == 0)
			return true;
		for (int i = 0; i < value.length(); i++) {
			char letter = value.charAt(i);
			if (('a' > letter || letter > 'z')
					&& ('A' > letter || letter > 'Z')
					&& ('0' > letter || letter > '9'))
				return false;
		}
		return true;
	}

	/**
	 * 过滤首字符
	 * 
	 * @param str
	 * @param pattern
	 * @param replace
	 * @return
	 */
	public static final String replaceFirst(String str, String pattern,
			String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		if ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	/**
	 * 以" "充满指定字符串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillSpace(String str, int length) {
		int strLength = str.length();
		if (strLength >= length) {
			return str;
		}
		StringBuffer spaceBuffer = new StringBuffer();
		for (int i = 0; i < (length - strLength); i++) {
			spaceBuffer.append(" ");
		}
		return str + spaceBuffer.toString();
	}

	/**
	 * 得到定字节长的字符串，位数不足右补空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillSpaceByByte(String str, int length) {
		byte[] strbyte = str.getBytes();
		int strLength = strbyte.length;
		if (strLength >= length) {
			return str;
		}
		StringBuffer spaceBuffer = new StringBuffer();
		for (int i = 0; i < (length - strLength); i++) {
			spaceBuffer.append(" ");
		}
		return str.concat(spaceBuffer.toString());
	}

	/**
	 * 返回指定字符串长度
	 * 
	 * @param s
	 * @return
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		else
			return s.getBytes().length;
	}

	/**
	 * 将字符串的数字取出到一个字符串中
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}

	/**
	 * 获得特定字符总数
	 * 
	 * @param str
	 * @param chr
	 * @return
	 */
	public static int charCount(String str, char chr) {
		int count = 0;
		if (str != null) {
			int length = str.length();
			for (int i = 0; i < length; i++) {
				if (str.charAt(i) == chr) {
					count++;
				}
			}
			return count;
		}
		return count;
	}

	/**
	 * 返回指定字符位置前数据
	 * 
	 * @param str
	 * @param chr
	 * @param max
	 * @return
	 */
	public static String charSubstring(String str, char chr, int max) {
		int count = 0;
		StringBuffer sbr = new StringBuffer();
		if (str != null) {
			int length = str.length();
			for (int i = 0; i < length; i++) {
				char result = str.charAt(i);
				sbr.append(result);
				if (result == chr) {
					count++;
				}
				if (count == max) {
					return sbr.toString();
				}
			}
		}
		return sbr.toString();
	}

	/**
	 * 清除字符串数组中空格
	 * 
	 * @param strings
	 * @return
	 */
	public static String[] trim(String[] s) {
		if (s == null) {
			return null;
		}
		for (int i = 0, len = s.length; i < len; i++) {
			s[i] = s[i].trim();
		}
		return s;
	}

	/**
	 * 整理字符串中指定字符，清空指定符号
	 * 
	 * @param s
	 * @param delimit
	 * @return
	 */
	public static String trim(String s, char delimit[]) {
		if (s == null) {
			return null;
		}
		int length = s.length();
		int beginIndex = 0;
		int endIndex = length;
		for (; beginIndex < length; beginIndex++) {
			char c = s.charAt(beginIndex);
			boolean found = false;
			for (int i = 0; i < delimit.length; i++) {
				if (delimit[i] != c) {
					continue;
				}
				found = true;
				break;
			}
			if (!found)
				break;
		}

		for (; endIndex > beginIndex; endIndex--) {
			char c = s.charAt(endIndex - 1);
			boolean found = false;
			for (int i = 0; i < delimit.length; i++) {
				if (delimit[i] != c)
					continue;
				found = true;
				break;
			}

			if (!found)
				break;
		}

		if (beginIndex == endIndex)
			return "";
		if (beginIndex > 0 || endIndex < length)
			return s.substring(beginIndex, endIndex);
		else
			return s;
	}

}
