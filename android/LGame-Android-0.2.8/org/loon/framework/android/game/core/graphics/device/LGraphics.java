package org.loon.framework.android.game.core.graphics.device;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Trans;
import org.loon.framework.android.game.core.graphics.geom.AffineTransform;
import org.loon.framework.android.game.core.graphics.geom.Polygon;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;
import org.loon.framework.android.game.core.graphics.geom.Shape;
import org.loon.framework.android.game.core.graphics.geom.Stroke;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;

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
public interface LGraphics extends Trans {

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

	public void clip(Shape s);

	public LGraphics create();

	public LGraphics create(int x, int y, int w, int h);

	public boolean isAntiAlias();

	public void setAntiAlias(boolean flag);

	public void setAlphaValue(int alpha);

	public void setAlpha(float alpha);

	public float getAlpha();

	public float getAlphaValue();

	public float[] getInverseMatrix();

	public void setColor(int r, int g, int b);

	public void setColorValue(int pixels);

	public void setColor(int pixels);

	public void setColor(int r, int g, int b, int a);

	public void setColor(LColor c);
	
	public void setColorARGB(LColor c);
	
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

	public AffineTransform getTransform();

	public FontMetrics getFontMetrics();

	public LFont getFont();

	public LFont getLFont();

	public void setFont(int size);

	public void setFont(String familyName, int style, int size);

	public void setFont(LFont font);

	public boolean hitClip(int x, int y, int width, int height);

	public void setTransform(int transform, int width, int height);

	public void setTransform(AffineTransform aff);

	public void transform(AffineTransform aff);

	public Rectangle getClipBounds();

	public void draw(Path s);

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

	public void drawRegion(Bitmap img, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor);

	public void drawRegion(LImage src, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor);

	public void drawBitmap(Bitmap bit, int x, int y, int w, int h);

	public void drawImage(LImage img, Matrix marMatrix, boolean filter);

	public void drawImage(LImage img, Matrix marMatrix);

	public void drawBitmap(Bitmap bit, Matrix marMatrix, boolean filter);

	public void drawBitmap(Bitmap bit, Matrix marMatrix);

	public void drawNotCacheMirrorBitmap(Bitmap bit, int x, int y,
			boolean filter);

	public void drawNotCacheMirrorImage(LImage img, int x, int y,
			boolean filter);

	public void drawMirrorBitmap(Bitmap bit, int x, int y, boolean filter);

	public void drawMirrorImage(LImage img, int x, int y, boolean filter);

	public void drawFlipBitmap(Bitmap bit, int x, int y, boolean filter);

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

	public void drawChar(char message, int x, int y);

	public void drawString(String message, int x, int y);

	public void drawString(String messagetr, int x, int y, int anchor);

	public void drawSubstring(String messagetr, int offset, int len, int x,
			int y, int anchor);

	public void drawSubString(String message, int x, int y, int w, int h,
			int anchor);

	public void drawString(String message, float x, float y);

	public void draw3DString(String message, int x, int y, LColor c);

	public void drawCenterString(String message, int x, int y);

	public void drawShadeString(String message, int x, int y, LColor c,
			LColor c1, int k);

	public void drawCenterShadeString(String message, int x, int y, LColor c,
			LColor c1, int k);

	public void drawCenterShadeString(String message, int x, int y, LColor c,
			LColor c1);

	public void drawCenterRoundedString(String message, int x, int y, LColor c,
			LColor c1);

	public void drawStyleString(String message, int x, int y, int c, int c1);

	public void drawStyleString(String message, int x, int y, LColor c1,
			LColor c2);

	public void drawRGB(int[] colors, int offset, int stride, int x, int y,
			int width, int height, boolean hasAlpha);

	public void drawClear();

	public void setBackground(LColor c);

	public LColor getBackground();

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

	public void copyArea(int x, int y, int width, int height, int x_dest,
			int y_dest, int anchor);

	public void copyArea(int x, int y, int width, int height, int dx, int dy);

	public void clipRect(int x, int y, int width, int height);

	public void shear(double shx, double shy);

	public void setStrokeStyle(int style);

	public int getStrokeStyle();

	public void translate(float x, float y);

	public void translate(int x, int y);

	public void setClip(Rect rect);

	public void setClip(Shape shape);

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
