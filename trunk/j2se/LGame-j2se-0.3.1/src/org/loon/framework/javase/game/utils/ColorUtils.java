package org.loon.framework.javase.game.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;

/**
 * 
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
public class ColorUtils {

	public int R, G, B;

	public static final int ALPHA = 24;

	public static final int RED = 16;

	public static final int GREEN = 8;

	public static final int BLUE = 0;

	public static final int NONE = 0;

	public static final int FILL = 1 << 0;

	public static final int SHARPEN = 1 << 1;

	public static final int ALL = (FILL | SHARPEN);

	private ColorUtils() {
	}

	/**
	 * 转化数据为ARGB
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 * @return
	 */
	public static int getARGB(int r, int g, int b, int alpha) {
		return (alpha << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * 锐化指定像素集合
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param f
	 */
	public static void sharpen(int[] pixels, int w, int h, double f) {
		int[] tmp = new int[pixels.length];
		System.arraycopy(pixels, 0, tmp, 0, tmp.length);
		for (int y = 0; y < h; y = y + 2) {
			for (int x = 0; x < w; x = x + 2) {
				for (int i = 0; i < 3; ++i) {
					int color = 0;
					switch (i) {
					case 0:
						color = RED;
						break;
					case 1:
						color = GREEN;
						break;
					case 2:
						color = BLUE;
						break;
					}
					int val = ((int) (getPixel(color, pixels, x - 1, y, w, h)
							* -f + getPixel(color, pixels, x, y - 1, w, h) * -f
							+ getPixel(color, pixels, x, y, w, h) * (1 + 4 * f)
							+ getPixel(color, pixels, x, y + 1, w, h) * -f + getPixel(
							color, pixels, x - 1, y, w, h)
							* -f));
					putPixel(val, color, tmp, x, y, w, h);
				}
			}
		}
		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				pixels[w * y + x] = tmp[w * y + x];
			}
		}
		tmp = null;
	}

	public static void putPixel(int val, int color, int pixels[], int x, int y,
			int w, int h) {
		int nval;

		if (x < 0) {
			x = 0;
		}
		if (x >= w) {
			x = w - 1;
		}
		if (y < 0) {
			y = 0;
		}
		if (y >= h) {
			y = h - 1;
		}
		if (val < 0) {
			val = 0;
		}
		if (val > 255) {
			val = 255;
		}
		switch (color) {
		case ALPHA:
			nval = (pixels[w * y + x] & (~(255 << ALPHA))) | (val << ALPHA);
			break;
		case RED:
			nval = (pixels[w * y + x] & (~(255 << RED))) | (val << RED);
			break;
		case GREEN:
			nval = (pixels[w * y + x] & (~(255 << GREEN))) | (val << GREEN);
			break;
		case BLUE:
			nval = (pixels[w * y + x] & (~(255 << BLUE))) | (val << BLUE);
			break;
		default:
			nval = pixels[w * y + x];
			break;
		}
		pixels[w * y + x] = nval;
	}

	public static int getPixel(int color, int pixels[], int x, int y, int w,
			int h) {
		if (x < 0) {
			x = 0;
		}
		if (x >= w) {
			x = w - 1;
		}
		if (y < 0) {
			y = 0;
		}
		if (y >= h) {
			y = h - 1;
		}
		int val = pixels[w * y + x];
		switch (color) {
		case ALPHA:
			val = val >> ALPHA;
			break;
		case RED:
			val = val >> RED;
			break;
		case GREEN:
			val = val >> GREEN;
			break;
		case BLUE:
			val = val >> BLUE;
			break;
		}
		return (val & 255);
	}

	/**
	 * 返回RGB的字符串形式
	 * 
	 * @param color
	 * @return
	 */
	public static String getRGBtoString(int color) {
		int a = color >>> 24;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = color & 0xff;

		if (a == 0xff) {
			return "rgb(" + r + ", " + g + ", " + b + ")";
		} else {
			return "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
		}
	}

	/**
	 * 判定两个lcolor是否相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(final ColorUtils a, final ColorUtils b) {
		return (a.R == b.R) && (a.G == b.G) && (a.B == b.B);
	}

	/**
	 * 判定两个lcolor是否相等
	 * 
	 * @param color
	 * @return
	 */
	public boolean equals(final ColorUtils color) {
		return ColorUtils.equals(this, color);
	}

	/**
	 * 返回透明色
	 * 
	 * @param foregroundPixel
	 * @param backgroundPixel
	 * @param alpha
	 * @return
	 */
	public static int getColorTransparence(int foregroundPixel,
			int backgroundPixel, double alpha) {
		int[] fores = ColorUtils.getRGBs(foregroundPixel);
		int[] backs = ColorUtils.getRGBs(backgroundPixel);
		int nr = 0;
		int ng = 0;
		int nb = 0;
		if (alpha == 0.5) {
			nr = (int) (fores[0] / 2 + backs[0] / 2);
			ng = (int) (fores[1] / 2 + backs[1] / 2);
			nb = (int) (fores[2] / 2 + backs[2] / 2);
		} else {
			nr = (int) (alpha * (fores[0] & 0xFF) + (1 - alpha)
					* (backs[0] & 0xFF) + 0.5);
			ng = (int) (alpha * (fores[1] & 0xFF) + (1 - alpha)
					* (backs[1] & 0xFF) + 0.5);
			nb = (int) (alpha * (fores[2] & 0xFF) + (1 - alpha)
					* (backs[2] & 0xFF) + 0.5);
		}
		return ColorUtils.getPixel(nr, ng, nb);
	}

	public static int[][] getPixels2D(int[] pixels, int w, int h, int size) {
		int[][] npixels = new int[w][h];
		for (int i = 0; i < npixels.length; i++) {
			for (int j = 0; j < npixels[i].length; j++) {
				npixels[i][j] = pixels[i + j * size];
			}
		}
		int pixel[][] = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixel[i][j] = npixels[j][i];
			}
		}
		return pixel;
	}

	/**
	 * 转换当前贴图为灰白位图
	 * 
	 */
	public static void convertToGray(final int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = colorToGray(pixels[i]);
		}
	}

	/**
	 * 转换当前贴图为异色(反色)位图
	 * 
	 */
	public static void convertToXor(final int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = colorToXor(pixels[i]);
		}
	}

	/**
	 * 转换指定像素为灰白
	 * 
	 * @param pixel
	 * @return
	 */
	private static int colorToGray(final int pixel) {
		int[] rgbs = ColorUtils.getRGBs(pixel);
		int value = (int) (0.299 * rgbs[0] + 0.587 * rgbs[1] + 0.114 * rgbs[2]);
		int npixel = (255 << 24) + (value << 16) + (value << 8) + value;
		return npixel;
	}

	/**
	 * 异或指定像素
	 * 
	 * @param pixel
	 * @return
	 */
	private static int colorToXor(final int pixel) {
		int[] rgbs = ColorUtils.getRGBs(pixel);
		int r = rgbs[0] ^ 0xff;
		int g = rgbs[1] ^ 0xff;
		int b = rgbs[2] ^ 0xff;
		int npixel = (255 << 24) + (r << 16) + (g << 8) + b;
		return npixel;
	}

	/**
	 * 像素前乘
	 * 
	 * @param argbColor
	 * @return
	 */
	public static int premultiply(int argbColor) {
		int a = argbColor >>> 24;
		if (a == 0) {
			return 0;
		} else if (a == 255) {
			return argbColor;
		} else {
			int r = (argbColor >> 16) & 0xff;
			int g = (argbColor >> 8) & 0xff;
			int b = argbColor & 0xff;
			r = (a * r + 127) / 255;
			g = (a * g + 127) / 255;
			b = (a * b + 127) / 255;
			return (a << 24) | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * 像素前乘
	 * 
	 * @param rgbColor
	 * @param alpha
	 * @return
	 */
	public static int premultiply(int rgbColor, int alpha) {
		if (alpha <= 0) {
			return 0;
		} else if (alpha >= 255) {
			return 0xff000000 | rgbColor;
		} else {
			int r = (rgbColor >> 16) & 0xff;
			int g = (rgbColor >> 8) & 0xff;
			int b = rgbColor & 0xff;

			r = (alpha * r + 127) / 255;
			g = (alpha * g + 127) / 255;
			b = (alpha * b + 127) / 255;
			return (alpha << 24) | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * 消除前乘像素
	 * 
	 * @param preARGBColor
	 * @return
	 */
	public static int unpremultiply(int preARGBColor) {
		int a = preARGBColor >>> 24;
		if (a == 0) {
			return 0;
		} else if (a == 255) {
			return preARGBColor;
		} else {
			int r = (preARGBColor >> 16) & 0xff;
			int g = (preARGBColor >> 8) & 0xff;
			int b = preARGBColor & 0xff;

			r = 255 * r / a;
			g = 255 * g / a;
			b = 255 * b / a;
			return (a << 24) | (r << 16) | (g << 8) | b;
		}
	}

	public static int[] getScaledPixels(Image image, int width, int height) {
		Image scaled = image.getScaledInstance(width, height,
				Image.SCALE_SMOOTH);
		return ColorUtils.getPixels(scaled, width, height);
	}

	/**
	 * 转换指定图像为像素
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static int[] getPixels(final Image image, int width, int height) {
		BufferedImage bufferImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) bufferImage.getRaster().getDataBuffer())
				.getData();
		PixelGrabber pgr = new PixelGrabber(image, 0, 0, width, height, pixels,
				0, width);
		try {
			pgr.grabPixels();
		} catch (InterruptedException ex) {
		}
		return pixels;
	}

	/**
	 * 将颜色Pixel数值返回为LColor
	 * 
	 * @param c
	 * @return
	 */
	public static ColorUtils getLColor(int pixel) {
		ColorUtils color = new ColorUtils();
		int[] colors = ColorUtils.getRGBs(pixel);
		color.R = colors[0];
		color.G = colors[1];
		color.B = colors[2];
		return color;
	}

	public static Color getColor(ColorUtils color) {
		return new Color(color.R, color.G, color.B);
	}

	/**
	 * 将color返回为像素
	 * 
	 * @param color
	 * @return
	 */
	public int getPixel(final ColorUtils color) {
		return getPixel(color.R, color.G, color.B);
	}
	
	/**
	 * 将color返回为像素
	 * 
	 * @return
	 */
	public int getPixel() {
		return getPixel(R, G, B);
	}
	
	public static int getPixel(int r, int g, int b) {
		return (255 << 24) + (r << 16) + (g << 8) + b;
	}

	/**
	 * 将指定像素转为Color
	 * 
	 * @param pixel
	 * @return
	 */
	public static Color getColor(final int pixel) {
		return new Color(pixel >> 16 & 0xff, pixel >> 8 & 0xff, pixel & 0xff);
	}
	

	/**
	 * 获得r,g,b
	 * 
	 * @param pixel
	 * @return
	 */
	public static int[] getRGBs(final int pixel) {
		int[] rgbs = new int[3];
		rgbs[0] = (pixel >> 16) & 0xff;
		rgbs[1] = (pixel >> 8) & 0xff;
		rgbs[2] = (pixel) & 0xff;
		return rgbs;
	}

	public Color getColor() {
		return new Color(R, G, B);
	}

	/**
	 * 注入r,g,b数值
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public ColorUtils(final int r, final int g, final int b) {
		this.R = r;
		this.G = g;
		this.B = b;
	}

	/**
	 * 设定透明色
	 * 
	 * @param color
	 * @param alpha
	 * @return
	 */
	public static Color getAlphaColor(int r, int g, int b, int alpha) {
		return new Color(r, g, b, alpha);
	}

	/**
	 * 设定透明色
	 * 
	 * @param color
	 * @param alpha
	 * @return
	 */
	public static Color getAlphaColor(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(),
				alpha);
	}

	/**
	 * 转换一个整数字符串为Color
	 * 
	 * @param str
	 * @return
	 */
	public static Color decode(String str) {
		if (StringUtils.isEmpty(str))
			return null;
		try {
			return Color.decode(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 转化r,g,b为指定LColor
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static ColorUtils fromArgb(final int r, final int g, final int b) {
		return new ColorUtils(r, g, b);
	}

}
