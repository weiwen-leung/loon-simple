package org.loon.framework.android.game.core.graphics.device;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Trans;
import org.loon.framework.android.game.core.graphics.geom.Polygon;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;
import org.loon.framework.android.game.core.graphics.geom.Shape;
import org.loon.framework.android.game.core.graphics.geom.Stroke;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

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
 * @version 0.1.1
 */
public interface LGraphics extends Trans{

	final static public double ANGLE_90 = Math.PI / 2;

	final static public double ANGLE_270 = Math.PI * 3 / 2;

	public static final int HCENTER = 1;

	public static final int VCENTER = 2;

	public static final int LEFT = 4;

	public static final int RIGHT = 8;

	public static final int TOP = 16;

	public static final int BOTTOM = 32;

	public static final int BASELINE = 64;

	public static final int SOLID = 0;

	public static final int DOTTED = 1;
	
	public void initGraphics();
	
	public void reset();

	public float[] getMatrix();

	public LGraphicsAndroid2D create();

	public boolean isAntiAlias();

	public void setAntiAlias(boolean flag);

	public void setAlphaValue(int alpha);

	public void setAlpha(float alpha);

	public float getAlpha();

	public float getAlphaValue();

	public void setColor(int r, int g, int b);

	public void setColorValue(int pixels);

	public void setColor(int pixels);

	public void setColor(int r, int g, int b, int a);

	public void setColor(LColor c);

	public void setColorAll(LColor c);

	public LColor getColor();

	public int getColorRGB();

	public int getColorARGB();

	public void setColorRGB24(int rgb);

	public void setColorARGB32(int argb);
	
	public void setGrayScale(int grey);

	public int getGrayScale();

	public int getGreenComponent();

	public int getRedComponent();

	public int getBlueComponent();

	public LFont getFont();
	
	public LFont getLFont();
	
	public void setFont(int size);

	public void setFont(String familyName, int style, int size);

	public void setFont(LFont font);

	public boolean hitClip(int x, int y, int width, int height);
	
	public void transform(int transform, int width, int height);

	public Rectangle getClipBounds();

	public void draw(Shape s);

	public void fill(Shape s);

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight);

	public void setXORMode(LColor color);

	public void setPaintMode();

	public void setFill();

	public Stroke getStroke();

	public void setStroke(Stroke s);

	public void rotate(double theta);

	public void rotate(double theta, double x, double y);

	public void scale(double sx, double sy);

	public void rectFill(int x, int y, int width, int height, LColor color);

	public void rectDraw(int x, int y, int width, int height, LColor color);

	public void rectOval(int x, int y, int width, int height, LColor color);

	public void drawSixStart(LColor color, int x, int y, int r);

	public void drawTriangle(LColor color, int x, int y, int r);

	public void drawRTriangle(LColor color, int x, int y, int r);

	public void drawBitmap(Bitmap bit, int x, int y);

	public void drawImage(String fileName, int x, int y, int w, int h);

	public void drawImage(String fileName, int x, int y);

	public void drawImage(LImage img, int x, int y);

	public void drawBitmap(Bitmap bit, int x, int y, int anchor);

	public void drawImage(LImage img, int x, int y, int anchor);

	public void drawRegion(LImage src, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor);

	public void drawBitmap(Bitmap bit, int x, int y, int w, int h);

	public void drawImage(LImage img, Matrix marMatrix, boolean filter);

	public void drawImage(LImage img, Matrix marMatrix);

	public void drawBitmap(Bitmap bit, Matrix marMatrix, boolean filter);

	public void drawBitmap(Bitmap bit, Matrix marMatrix);

	public void drawMirrorBitmap(Bitmap bit, int x, int y, boolean filter);

	public void drawMirrorImage(LImage img, int x, int y, boolean filter);

	public void drawFlipImage(Bitmap bit, int x, int y, boolean filter);

	public void drawFlipImage(LImage img, int x, int y, boolean filter);

	public void drawReverseBitmap(Bitmap bit, int x, int y, boolean filter);

	public void drawReverseImage(LImage img, int x, int y, boolean filter);

	public void drawImage(LImage img, int x, int y, int w, int h);

	public void drawBitmap(Bitmap bit, int x, int y, int w, int h, int x1,
			int y1, int w1, int h1);

	public void drawBitmap(Bitmap bit, Rect r1, Rect r2);

	public void drawImage(LImage img, int x, int y, int w, int h, int x1,
			int y1, int w1, int h1);

	public void drawArc(int x, int y, int width, int height, int sa, int ea);

	public void drawLine(int x1, int y1, int x2, int y2);

	public void drawRect(int x, int y, int width, int height);

	public void drawBytes(byte[] message, int offset, int length, int x, int y);

	public void drawChars(char[] message, int offset, int length, int x, int y);

	public void drawString(String message, int x, int y);

	public void drawString(String str, int x, int y, int anchor);

	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor);

	public void drawSubString(String message, int x, int y, int w, int h, int anchor);

	public void drawString(String message, float x, float y);

	public void draw3DString(String s, int x, int y, LColor c);

	public void drawStyleString(String message, int x, int y, int color,
			int color1);

	public void drawStyleString(String message, int x, int y, LColor c1,
			LColor c2);
	
	public void drawRGB(int[] colors, int offset, int stride, int x, int y,
			int width, int height, boolean hasAlpha);
	
	public void drawClear();

	public void backgroundColor(LColor color);

	public void fillArc(int x, int y, int width, int height, int sa, int ea);

	public void fillOval(int x, int y, int width, int height);

	public void fillPolygon(int[] xpoints, int[] ypoints, int npoints);

	public void fillPolygon(Polygon p);

	public void fillRect(int x, int y, int width, int height);

	public void fillAlphaRect(int x, int y, int w, int h, LColor c);

	public void fillAlphaRect(int x, int y, int w, int h, int pixel);

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3);

	public void fill3DRect(int x, int y, int width, int height, boolean raised);

	public void draw3DRect(Rectangle rect, LColor back, boolean down);

	public void draw3DRect(int x, int y, int width, int height, boolean raised);

	public int getClipHeight();

	public int getClipWidth();

	public int getClipX();

	public int getClipY();

	public void clearRect(int x, int y, int width, int height);

	public void clearScreen(int x, int y, int width, int height);
	
	public void copyArea(int x, int y, int width, int height,
			int x_dest, int y_dest, int anchor);
	
	public void copyArea(int x, int y, int width, int height, int dx, int dy);

	public void clipRect(int x, int y, int width, int height);

	public void shear(double shx, double shy);

	public void setStrokeStyle(int style);

	public int getStrokeStyle();

	public void translate(int x, int y);

	public void setClip(Rect rect);

	public void setClip(int x, int y, int width, int height);

	public void drawOval(int x, int y, int width, int height);

	public void drawPolygon(int[] xpoints, int[] ypoints, int npoints);

	public void drawPolygon(Polygon p);

	public void drawPolyline(int[] xpoints, int[] ypoints, int npoints);

	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight);

	public Canvas getCanvas();

	public Paint getPaint();

	public Rect getClip();

	public void setPaint(Paint paint);

	public void setBitmap(Bitmap bit);

	public Bitmap getBitmap();

	public boolean isClose();

	public void dispose();

}
