package org.loon.framework.android.game.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import org.loon.framework.android.game.Android2DHandler;
import org.loon.framework.android.game.IHandler;
import org.loon.framework.android.game.LGameAndroid2DActivity;
import org.loon.framework.android.game.LGameAndroid2DView;
import org.loon.framework.android.game.Android2DSurface;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.core.timer.NanoTimer;
import org.loon.framework.android.game.core.timer.SystemTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.StringUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Display;

/**
 * 
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class LSystem {

	// 框架名
	final static public String FRAMEWORK = "LGame-Android";

	// 框架版本信息
	final static public Version VERSION = new Version(0, 2, 9);

	// 允许的最大窗体宽（横屏）
	public static int MAX_SCREEN_WIDTH = 480;

	// 允许的最大窗体高（横屏）
	public static int MAX_SCREEN_HEIGHT = 320;

	// 秒
	final static public long SECOND = 1000;

	// 分
	final static public long MINUTE = SECOND * 60;

	// 小时
	final static public long HOUR = MINUTE * 60;

	// 天
	final static public long DAY = HOUR * 24;

	// 周
	final static public long WEEK = DAY * 7;

	// 理论上一年
	final static public long YEAR = DAY * 365;

	// 行分隔符
	final static public String LS = System.getProperty("line.separator", "\n");

	// 文件分割符
	final static public String FS = System.getProperty("file.separator", "\\");

	// 随机数
	final static public Random random = new Random();

	// 竖屏
	public static final int ANDROID_ORIENTATION_PORTRAIT_ID = 0;

	// 横屏
	public static final int ANDROID_ORIENTATION_LANDSCAPE_ID = 1;

	// 屏幕是否横屏
	public static boolean SCREEN_LANDSCAPE;

	// 最大缓存数量
	final static public int DEFAULT_MAX_CACHE_SIZE = 20;

	// 是否启动旋转图片缓存
	final static public boolean DEFAULT_ROTATE_CACHE = true;

	final static public String encoding = "UTF-8";

	final static public int FONT_TYPE = 15;

	final static public int FONT_SIZE = 1;

	final static public String FONT_NAME = "Monospaced";

	final static public int DEFAULT_MAX_FPS = 60;

	private static IHandler handler;

	private static Android2DSurface surface2D;

	public static boolean isPaused;

	public static boolean AUTO_REPAINT;

	private static String temp_file;

	private static int TMP_MAJOR = 0;

	final private static String BULID_BRAND;

	final private static String BULID_MODEL;

	final private static String BULIDM_PRODUCT;

	final private static String BULIDM_RELEASE;

	final private static String BULIDM_SDK;

	final private static int OS_11 = 0;

	final private static int OS_15 = 1;

	final private static int OS_16 = 2;

	final private static int OS_20 = 3;

	final private static int OS_21 = 4;

	final private static int OS_22 = 5;

	final private static int OS_30 = 5;

	// 初始化手机版本环境相关数据
	static {
		BULID_BRAND = Build.BRAND.toLowerCase();
		BULID_MODEL = Build.MODEL.toLowerCase();
		BULIDM_PRODUCT = Build.PRODUCT.toLowerCase();
		BULIDM_RELEASE = Build.VERSION.RELEASE;
		BULIDM_SDK = Build.VERSION.SDK;
		if (BULIDM_RELEASE.indexOf("1.1") != -1) {
			TMP_MAJOR = OS_11;
		} else if (BULIDM_RELEASE.indexOf("1.5") != -1) {
			TMP_MAJOR = OS_15;
		} else if (BULIDM_RELEASE.indexOf("1.6") != -1) {
			TMP_MAJOR = OS_16;
		} else if (BULIDM_RELEASE.indexOf("2.0") != -1) {
			TMP_MAJOR = OS_20;
		} else if (BULIDM_RELEASE.indexOf("2.1") != -1) {
			TMP_MAJOR = OS_21;
		} else if (BULIDM_RELEASE.indexOf("2.2") != -1) {
			TMP_MAJOR = OS_22;
		} else if (BULIDM_RELEASE.indexOf("3.0") != -1) {
			TMP_MAJOR = OS_30;
		} else {
			TMP_MAJOR = OS_15;
		}

	}

	public static boolean isOverrunOS11() {
		return TMP_MAJOR > OS_11;
	}

	public static boolean isOverrunOS16() {
		return TMP_MAJOR > OS_16;
	}

	public static boolean isOverrunOS21() {
		return TMP_MAJOR > OS_21;
	}

	public static String getModel() {
		return BULID_MODEL;
	}

	public static String getProductName() {
		return BULIDM_PRODUCT;
	}

	public static int getMajorOSVersion() {
		return TMP_MAJOR;
	}

	public static String getOSVersion() {
		return BULIDM_RELEASE;
	}

	public static String getSDKVersion() {
		return BULIDM_SDK;
	}

	public static boolean isPaused() {
		return isPaused;
	}

	public static boolean isEmulator() {
		final boolean isGeneric = BULID_BRAND.indexOf("generic") != -1;
		final boolean isSdk = BULID_MODEL.indexOf("sdk") != -1;
		return isGeneric && isSdk;
	}

	public static boolean isSamsungGalaxy() {
		final boolean isSamsung = BULID_BRAND.indexOf("samsung") != -1;
		final boolean isGalaxy = BULID_MODEL.indexOf("galaxy") != -1;
		return isSamsung && isGalaxy;
	}

	public static boolean isDroidOrMilestone() {
		final boolean isMotorola = BULID_BRAND.indexOf("moto") != -1;
		final boolean isDroid = BULID_MODEL.indexOf("droid") != -1;
		final boolean isMilestone = BULID_MODEL.indexOf("milestone") != -1;
		return isMotorola && (isDroid || isMilestone);
	}

	/**
	 * 清空框架临时资源
	 */
	public static void destroy() {
		GraphicsUtils.destroy();
		// ZipResource.destroy();
		Resources.destroy();
		LSystem.gc();
	}

	public static Android2DSurface getSurface2D() {
		return surface2D;
	}

	/**
	 * 退出当前应用
	 * 
	 */
	public static void exit() {
		synchronized (handler) {
			try {
				if (handler != null) {
					if (handler instanceof Android2DHandler) {
						((LGameAndroid2DView) handler.getView())
								.setRunning(false);
						((Android2DHandler) handler).getLGameActivity()
								.finish();
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 刷新绘图
	 */
	public static void repaint() {
		if (surface2D != null) {
			surface2D.update();
		}
	}

	/**
	 * 刷新绘图
	 * 
	 * @param bit
	 */
	public static void repaint(Bitmap bit) {
		if (surface2D != null) {
			surface2D.update(bit);
		}
	}

	/**
	 * 刷新绘图
	 * 
	 * @param img
	 */
	public static void repaint(LImage img) {
		if (surface2D != null) {
			surface2D.update(img);
		}
	}

	/**
	 * 刷新绘图并按照指定长宽比例居中显示
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public static void repaint(LImage img, int w, int h) {
		if (surface2D != null) {
			surface2D.update(img, w, h);
		}
	}

	/**
	 * 刷新绘图并按照指定长宽比例居中显示
	 * 
	 * @param bit
	 * @param w
	 * @param h
	 */
	public static void repaint(Bitmap bit, int w, int h) {
		if (surface2D != null) {
			surface2D.update(bit, w, h);
		}
	}

	/**
	 * 刷新绘图为指定大小并居中显示
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public static void repaintFull(LImage img, int w, int h) {
		if (surface2D != null) {
			surface2D.updateFull(img, w, h);
		}
	}

	/**
	 * 刷新绘图为指定大小并居中显示
	 * 
	 * @param bit
	 * @param w
	 * @param h
	 */
	public static void repaintFull(Bitmap bit, int w, int h) {
		if (surface2D != null) {
			surface2D.updateFull(bit, w, h);
		}
	}

	/**
	 * 变更画布为指定大小
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public static void repaintResize(LImage img, int w, int h) {
		if (surface2D != null) {
			surface2D.updateResize(img, w, h);
		}
	}

	/**
	 * 变更画布为指定大小
	 * 
	 * @param bit
	 * @param w
	 * @param h
	 */
	public static void repaintResize(Bitmap bit, int w, int h) {
		if (surface2D != null) {
			surface2D.updateResize(bit, w, h);
		}
	}

	/**
	 * 刷新图像到指定位置
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public static void repaintLocation(LImage img, int x, int y) {
		if (surface2D != null) {
			surface2D.updateLocation(img, x, y);
		}
	}

	/**
	 * 刷新图像到指定位置
	 * 
	 * @param bit
	 * @param w
	 * @param h
	 */
	public static void repaintLocation(Bitmap bit, int x, int y) {
		if (surface2D != null) {
			surface2D.updateLocation(bit, x, y);
		}
	}

	/**
	 * 设定刷新率
	 * 
	 * @param fps
	 */
	public static void setFPS(long fps) {
		if (surface2D != null) {
			surface2D.setFPS(fps);
		}
	}

	/**
	 * 返回当前刷新率
	 */
	public static long getFPS() {
		if (surface2D != null) {
			return surface2D.getCurrentFPS();
		}
		return 0;
	}

	/**
	 * 返回最大刷新率
	 */
	public static long getMaxFPS() {
		if (surface2D != null) {
			return surface2D.getMaxFPS();
		}
		return 1;
	}

	/**
	 * 返回当前的Activity
	 * 
	 * @return
	 */
	public static Activity getActivity() {
		if (handler != null) {
			if (handler instanceof Android2DHandler) {
				return (Activity) ((Android2DHandler) handler)
						.getLGameActivity();
			}
		}
		return null;
	}

	/**
	 * 返回当前屏幕状态
	 * 
	 * @return
	 */
	public static String getScreenOrientation() {
		return LSystem.getScreenOrientation(getActivity());
	}

	/**
	 * 检测指定Activity的屏幕状态
	 * 
	 * @param aActivity
	 * @return
	 */
	public static String getScreenOrientation(final Activity aActivity) {
		final Display display = aActivity.getWindowManager()
				.getDefaultDisplay();

		final int orientation = display.getOrientation();
		if (orientation == ANDROID_ORIENTATION_PORTRAIT_ID
				&& looksLikePortrait(aActivity)) {
			return "PORTRAIT";
		}
		if (orientation == ANDROID_ORIENTATION_LANDSCAPE_ID
				&& looksLikeLandscape(aActivity)) {
			return "LANDSCAPE";
		}

		if (looksLikePortrait(aActivity))
			return "PORTRAIT";
		if (looksLikeLandscape(aActivity))
			return "LANDSCAPE";
		if (looksLikeSquare(aActivity))
			return "SQUARE";

		return "PORTRAIT";
	}

	public static boolean looksLikePortrait(final Activity aActivity) {
		final Display display = aActivity.getWindowManager()
				.getDefaultDisplay();
		return display.getWidth() < display.getHeight();
	}

	public static boolean looksLikeLandscape(final Activity aActivity) {
		final Display display = aActivity.getWindowManager()
				.getDefaultDisplay();
		return display.getWidth() > display.getHeight();
	}

	public static boolean looksLikeSquare(final Activity aActivity) {
		final Display display = aActivity.getWindowManager()
				.getDefaultDisplay();
		return display.getWidth() == display.getHeight();
	}

	public static int getRandom(int i, int j) {
		return i + random.nextInt((j - i) + 1);
	}

	public static SystemTimer getSystemTimer() {
		return new NanoTimer();
	}

	public static void setupHandler(LGameAndroid2DActivity activity,
			LGameAndroid2DView view, boolean landscape) {
		surface2D = view;
		handler = new Android2DHandler(activity, view, landscape);
	}

	public static String getVersionName() {
		if (handler != null) {
			if (handler instanceof Android2DHandler) {
				return ((Android2DHandler) handler).getLGameActivity()
						.getVersionName();
			}
		}
		return null;
	}

	public static File getCacheFile() {
		if (handler != null) {
			if (handler instanceof Android2DHandler) {
				return ((Android2DHandler) handler).getLGameActivity()
						.getCacheDir();
			}
		}
		return null;
	}

	public static File getCacheFile(String fileName) {
		final String file = LSystem.getCacheFileName();
		fileName = StringUtils.replaceIgnoreCase(fileName, "\\", "/");
		if (file != null) {
			if (fileName.startsWith("/") || fileName.startsWith("\\")) {
				fileName = fileName.substring(1, fileName.length());
			}
			if (file.endsWith("/") || file.endsWith("\\")) {
				return new File(LSystem.getCacheFileName() + fileName);
			} else {
				return new File(LSystem.getCacheFileName() + LSystem.FS
						+ fileName);
			}
		} else {
			return new File(fileName);
		}
	}

	public static String getCacheFileName() {
		if (temp_file == null) {
			Activity activity = null;
			if (handler != null) {
				if (handler instanceof Android2DHandler) {
					activity = ((Android2DHandler) handler).getLGameActivity();
				}
			}
			if (activity != null) {
				temp_file = activity.getCacheDir().getAbsolutePath();
			}
		}
		return temp_file;
	}

	public static IHandler getSystemHandler() {
		return handler;
	}

	/**
	 * 申请回收系统资源
	 * 
	 */
	final public static void gc() {
		System.gc();
	}

	/**
	 * 以指定范围内的指定概率执行gc
	 * 
	 * @param size
	 * @param rand
	 */
	final public static void gc(final int size, final long rand) {
		if (rand > size) {
			throw new RuntimeException(
					("GC random probability " + rand + " > " + size).intern());
		}
		if (LSystem.random.nextInt(size) <= rand) {
			LSystem.gc();
		}
	}

	/**
	 * 以指定概率使用gc回收系统资源
	 * 
	 * @param rand
	 */
	final public static void gc(final long rand) {
		gc(100, rand);
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param buffer
	 * @return
	 */
	public static final InputStream read(byte[] buffer) {
		return new BufferedInputStream(new ByteArrayInputStream(buffer));
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param file
	 * @return
	 */
	public static final InputStream read(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param fileName
	 * @return
	 */
	public static final InputStream read(String fileName) {
		return read(new File(fileName));
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param file
	 */
	final public static void loadPropertiesFileToSystem(final File file) {
		Properties properties = System.getProperties();
		InputStream in = LSystem.read(file);
		loadProperties(properties, in, file.getName());
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param file
	 * @return
	 */
	final public static Properties loadPropertiesFromFile(final File file) {
		if (file == null) {
			return null;
		}
		Properties properties = new Properties();
		try {
			InputStream in = LSystem.read(file);
			loadProperties(properties, in, file.getName());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param properties
	 * @param inputStream
	 * @param fileName
	 */
	private static void loadProperties(Properties properties,
			InputStream inputStream, String fileName) {
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(
						("error closing input stream from file " + fileName
								+ ", ignoring , " + e.getMessage()).intern());
			}
		}
	}

	/**
	 * 写入整型数据到OutputStream
	 * 
	 * @param out
	 * @param number
	 */
	public final static void writeInt(final OutputStream out, final int number) {
		byte[] bytes = new byte[4];
		try {
			for (int i = 0; i < 4; i++) {
				bytes[i] = (byte) ((number >> (i * 8)) & 0xff);
			}
			out.write(bytes);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 从InputStream中获得整型数据
	 * 
	 * @param in
	 * @return
	 */
	final static public int readInt(final InputStream in) {
		int data = -1;
		try {
			data = (in.read() & 0xff);
			data |= ((in.read() & 0xff) << 8);
			data |= ((in.read() & 0xff) << 16);
			data |= ((in.read() & 0xff) << 24);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return data;
	}

}
