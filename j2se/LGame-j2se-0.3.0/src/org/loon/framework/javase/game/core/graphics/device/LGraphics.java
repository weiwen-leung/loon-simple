package org.loon.framework.javase.game.core.graphics.device;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import org.loon.framework.javase.game.action.map.shapes.Triangle;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.LPixmap;
import org.loon.framework.javase.game.core.graphics.Trans;

/**
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

	public void restore();

	public void drawClear(Color color);

	public void drawClear();

	public void draw(Shape s);

	public void drawSixStart(Color color, int x, int y, int r);

	public void rectFill(int x, int y, int width, int height, Color color);

	public void rectDraw(int x, int y, int width, int height, Color color);

	public void rectOval(int x, int y, int width, int height, Color color);

	public void drawCenterString(String s, int x, int y);

	public void drawShadeString(String s, int x, int y, Color color,
			Color color1, int k);

	public void drawCenterShadeString(String s, int x, int y, Color color,
			Color color1, int k);

	public void drawCenterShadeString(String s, int x, int y, Color color,
			Color color1);

	public void drawCenterRoundedString(String s, int x, int y, Color color,
			Color color1);

	public void drawStyleString(String message, int x, int y, Color color,
			Color color1);

	public double getAlpha();

	public void setAntialiasAll(boolean flag);

	public void setAntiAlias(boolean flag);

	public void setAlpha(double alpha);

	public void setAlphaValue(double alpha);

	public int getStrokeStyle();

	public void setStrokeStyle(int style);

	public void draw3DString(String s, int x, int y, Color c);

	public void drawRGB(int[] rgbData, int offset, int scanlength, int x,
			int y, int width, int height, boolean processAlpha);

	public void drawRect(int x, int y, int width, int height);

	public void fillTriangle(Triangle[] ts);

	public void fillTriangle(Triangle[] ts, int x, int y);

	public void fillTriangle(Triangle t);

	public void fillTriangle(Triangle t, int x, int y);

	public void drawTriangle(Triangle[] ts);

	public void drawTriangle(Triangle[] ts, int x, int y);

	public void drawTriangle(Triangle t);

	public void drawTriangle(Triangle t, int x, int y);

	public boolean drawImage(Image img, AffineTransform xform);

	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y);

	public void drawRenderedImage(RenderedImage img, AffineTransform xform);

	public void drawRenderableImage(RenderableImage img, AffineTransform xform);

	public void drawChars(char[] chars, int ofs, int len, int x, int y,
			int align);
	
	public void drawChars(char[] message, int offset, int length, int x, int y);
	
	public void drawSubString(String str, int offset, int len, int x, int y,
			int anchor);

	public void drawString(String str, int x, int y);

	public void drawString(String str, int x, int y, int anchor);

	public void drawString(String str, float x, float y);

	public void drawString(AttributedCharacterIterator iterator, int x, int y);

	public void drawString(AttributedCharacterIterator iterator, float x,
			float y);

	public void drawRegion(LImage src, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor);

	public void drawRegion(Image img, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor);

	public void drawRegion(LImage src, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst);

	public void drawRegion(Image img, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst);

	public void transform(int transform, int width, int height);

	public void fill(Shape s);

	public boolean hit(Rectangle rect, Shape s, boolean onStroke);

	public GraphicsConfiguration getDeviceConfiguration();

	public void setComposite(Composite comp);

	public void setPaint(Paint paint);

	public void setGradientPaint(int x1, int y1, Color c1, int x2, int y2,
			Color c2);

	public void setStroke(Stroke s);

	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue);

	public Object getRenderingHint(RenderingHints.Key hintKey);

	public void setRenderingHints(Map hints);

	public void addRenderingHints(Map hints);

	public RenderingHints getRenderingHints();

	public int getClipX();

	public int getClipY();

	public int getClipWidth();

	public int getClipHeight();

	public void translate(int x, int y);

	public void translate(double tx, double ty);

	public void rotate(double theta);

	public void rotate(double theta, double x, double y);

	public void scale(double sx, double sy);

	public void shear(double shx, double shy);

	public void transform(AffineTransform Tx);

	public void setTransform(AffineTransform Tx);

	public AffineTransform getTransform();

	public Paint getPaint();

	public Composite getComposite();

	public void setBackground(Color color);

	public Color getBackground();

	public Stroke getStroke();

	public void clip(Shape s);

	public FontRenderContext getFontRenderContext();

	public FontMetrics getFontMetrics();

	public Graphics create();

	public Graphics create(int x, int y, int w, int h);

	public void setGrayScale(int grey);

	public int getGrayScale();

	public int getGreenComponent();

	public int getRedComponent();

	public int getBlueComponent();

	public Color getColor();

	public void setColor(Color c);

	public void setColor(int r, int g, int b);

	public void setColor(int pixel);

	public void setPaintMode();

	public void setXORMode(Color c1);

	public Font getFont();

	public LFont getLFont();

	public void setFont(Font font);

	public void setFont(LFont font);

	public void setFont(int size);

	public FontMetrics getFontMetrics(Font f);

	public Rectangle getClipBounds();

	public void clipRect(int x, int y, int width, int height);

	public void setClip(int x, int y, int width, int height);

	public Shape getClip();

	public void setClip(Shape clip);

	public void copyArea(int x, int y, int width, int height, int dx, int dy);

	public void copyArea(int x_src, int y_src, int width, int height,
			int x_dest, int y_dest, int anchor);

	public void drawLine(int x1, int y1, int x2, int y2);

	public void fillRect(int x, int y, int width, int height);

	public void clearRect(int x, int y, int width, int height);

	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight);

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight);

	public void fill3DRect(int x, int y, int width, int height, boolean raised);

	public void drawOval(int x, int y, int width, int height);

	public void fillOval(int x, int y, int width, int height);

	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle);

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle);

	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);

	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints);

	public void drawPolygon(Polygon p);

	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints);

	public void fillPolygon(Polygon p);

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3);

	public void drawPixmap(LPixmap img);

	public void drawPixmap(LPixmap img, int x, int y);

	public boolean drawImage(Image img, int x, int y);

	public boolean drawImage(Image img, int x, int y, int width, int height);

	public void drawImage(Image image, int x, int y, int anchor);

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2);

	public void drawMirrorImage(Image img, int x, int y);

	public boolean drawImage(LImage img, int x, int y);

	public boolean drawImage(LImage img, int x, int y, int width, int height);

	public void drawImage(LImage image, int x, int y, int anchor);

	public boolean drawImage(LImage img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2);

	public void drawMirrorImage(LImage img, int x, int y);

	public Graphics getGraphics();

	public void fill();

	public void dispose();

	public boolean isClose();

}
