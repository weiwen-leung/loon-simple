package org.loon.framework.javase.game.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
final public class ConsoleUtils {

	private static BufferedReader reader;

	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private static String readData() {
		try {
			return reader.readLine();
		} catch (IOException ex) {
			return null;
		}
	}

	public static String readString() {
		return readData();
	}

	public static int readInteger() {
		return Integer.parseInt(readData());
	}

	public static float readFloat() {
		return Float.parseFloat(readData());
	}

	public static double readDouble() {
		return Double.parseDouble(readData());
	}

	public static long readLong() {
		return Long.parseLong(readData());
	}

	public static char[] readChars() {
		return readData().toCharArray();
	}

	public static String readSubstring(int start, int end) {
		return readData().substring(start, end);
	}

	public static int execute(String command) throws InterruptedException,
			IOException {
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		return process.exitValue();
	}

	public static Process exec(String command[], boolean stop)
			throws InterruptedException, IOException {
		Process p = Runtime.getRuntime().exec(command);
		if (stop)
			p.waitFor();
		return p;
	}

	public static Process exec(String exePath, List files, boolean stop)
			throws InterruptedException, IOException {
		List filePaths = new ArrayList();
		filePaths.add(exePath);
		for (int i = 0; i < files.size(); i++) {
			File file = (File) files.get(i);
			filePaths.add(file.getPath());
		}
		String command[] = (String[]) filePaths.toArray(new String[filePaths
				.size()]);
		return exec(command, stop);
	}

	public static Process exec(String command, boolean stop)
			throws InterruptedException, IOException {
		return exec(new String[] { command }, stop);
	}
}
