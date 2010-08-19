package org.loon.framework.android.game.core.graphics.device;

import java.util.TreeMap;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.geom.BasicStroke;

import org.loon.framework.android.game.core.graphics.geom.AffineTransform;
import org.loon.framework.android.game.core.graphics.geom.GeneralPath;
import org.loon.framework.android.game.core.graphics.geom.NoninvertibleTransformException;
import org.loon.framework.android.game.core.graphics.geom.PathIterator;
import org.loon.framework.android.game.core.graphics.geom.Polygon;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;
import org.loon.framework.android.game.core.graphics.geom.Shape;
import org.loon.framework.android.game.core.graphics.geom.Stroke;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelXorXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;

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
public final class LGraphicsAndroid2D implements LGraphics {

	private static final DashPathEffect dashPathEffect = new DashPathEffect(
			new float[] { 5, 5 }, 0);

	private static final Matrix tmp_matrix = new Matrix();

	private final static double RAD_360 = Math.PI / 180 * 360;

	private int strokeStyle;

	private float[] mirror = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };

	private Bitmap grapBitmap;

	private Canvas canvas;

	private Paint paint;

	private Rect clip;

	private LFont font;

	private TreeMap<Integer, Bitmap> mirrorImage;

	private boolean isClose;

	public LGraphicsAndroid2D(Bitmap bit) {
		this.grapBitmap = bit;
		this.initGraphics();
	}

	/**
	 * 初始化画布
	 */
	public void initGraphics() {
		if (paint == null) {
			paint = new Paint();
		}
		if (canvas == null) {
			canvas = new Canvas(grapBitmap);
			this.canvas.clipRect(0, 0, grapBitmap.getWidth(), grapBitmap
					.getHeight());
			this.clip = canvas.getClipBounds();
			this.setFont(LFont.getDefaultFont());
			this.canvas.save(Canvas.CLIP_SAVE_FLAG);
		}
		this.isClose = false;
	}

	/**
	 * 刷新画布
	 */
	public void reset() {
		if (canvas != null && !isClose) {
			if (paint == null) {
				paint = new Paint();
			}
			if (grapBitmap != null) {
				canvas.clipRect(0, 0, grapBitmap.getWidth(), grapBitmap
						.getHeight());
			}
			this.clip = canvas.getClipBounds();
			this.setFont(LFont.getDefaultFont());
			this.canvas.save(Canvas.CLIP_SAVE_FLAG);
		}
	}

	/**
	 * 转换Matrix格式，将内部数据反转
	 * 
	 */
	public float[] getInverseMatrix() {
		if (isClose) {
			return null;
		}
		AffineTransform af = new AffineTransform(createAWTMatrix(getMatrix()));
		try {
			af = af.createInverse();
		} catch (NoninvertibleTransformException e) {
		}
		return createMatrix(af);
	}

	/**
	 * 获得当前LGraphics的Matrix参数
	 */
	public float[] getMatrix() {
		if (isClose) {
			return null;
		}
		float[] f = new float[9];
		canvas.getMatrix().getValues(f);
		return f;
	}

	/**
	 * 创建一个新的 LGraphics对象，它是当前LGraphics对象的克隆
	 */
	public LGraphics create() {
		if (isClose) {
			return null;
		}
		return new LGraphicsAndroid2D(Bitmap.createBitmap(grapBitmap));
	}

	/**
	 * 基于此 LGraphics创建一个新的 LGraphics对象，但是使用新的转换和剪贴区域
	 * 
	 */
	public LGraphics create(int x, int y, int w, int h) {
		if (isClose) {
			return null;
		}
		return new LGraphicsAndroid2D(Bitmap.createBitmap(grapBitmap, x, y, w,
				h));
	}

	public boolean isAntiAlias() {
		if (isClose) {
			return false;
		}
		return paint.isAntiAlias();
	}

	public void setAntiAlias(boolean flag) {
		if (isClose) {
			return;
		}
		paint.setAntiAlias(flag);
	}

	public void setAlphaValue(int alpha) {
		if (isClose) {
			return;
		}
		paint.setAlpha(alpha);
	}

	public void setAlpha(float alpha) {
		setAlphaValue((int) (255 * alpha));
	}

	public float getAlpha() {
		if (isClose) {
			return 0F;
		}
		return paint.getAlpha() / 255;
	}

	public float getAlphaValue() {
		if (isClose) {
			return 0F;
		}
		return paint.getAlpha();
	}

	public void setColor(int r, int g, int b) {
		if (isClose) {
			return;
		}
		paint.setColor(LColor.getRGB(r, g, b));
	}

	public void setColorValue(int pixels) {
		if (isClose) {
			return;
		}
		paint.setColor(pixels);
	}

	public void setColor(int pixels) {
		if (isClose) {
			return;
		}
		paint.setColor(LColor.getRGB(pixels));
	}

	public void setColor(int r, int g, int b, int a) {
		if (isClose) {
			return;
		}
		paint.setColor(LColor.getARGB(r, g, b, a));
	}

	public void setColor(LColor c) {
		if (isClose) {
			return;
		}
		paint.setColor(c.getRGB());
	}

	public void setColorAll(LColor c) {
		if (isClose) {
			return;
		}
		paint.setColor(c.getRGB());
		canvas.drawColor(c.getRGB());
	}

	public LColor getColor() {
		if (isClose) {
			return LColor.black;
		}
		return new LColor(paint.getColor());
	}

	public final int getColorRGB() {
		if (isClose) {
			return 0;
		}
		return paint.getColor() & 0x00FFFFFF;
	}

	public final int getColorARGB() {
		if (isClose) {
			return 0;
		}
		return paint.getColor();
	}

	public final void setColorRGB24(int rgb) {
		if (isClose) {
			return;
		}
		paint.setColor(0xFF000000 | (rgb & 0x00FFFFFF));
	}

	public final void setColorARGB32(int argb) {
		if (isClose) {
			return;
		}
		paint.setColor(argb);
	}

	public void setGrayScale(int grey) {
		if (grey < 0 || grey > 255) {
			throw new IllegalArgumentException();
		}
		setColor(grey, grey, grey);
	}

	public int getGrayScale() {
		return (getRedComponent() + getGreenComponent() + getBlueComponent()) / 3;
	}

	public int getGreenComponent() {
		return (getColor().getGreen() >> 8) & 255;
	}

	public int getRedComponent() {
		return (getColor().getRed() >> 16) & 255;
	}

	public int getBlueComponent() {
		return (getColor().getBlue() >> 16) & 255;
	}

	public FontMetrics getFontMetrics() {
		if (font != null) {
			return font.getFontMetrics();
		}
		return null;
	}

	public LFont getFont() {
		if (isClose) {
			return null;
		}
		return font;
	}

	public LFont getLFont() {
		return getFont();
	}

	public void setFont(int size) {
		setFont(LFont.getFont(LSystem.FONT_NAME, 0, size));
	}

	public void setFont(String familyName, int style, int size) {
		setFont(LFont.getFont(familyName, style, size));
	}

	public void setFont(LFont font) {
		if (isClose) {
			return;
		}
		Paint typefacePaint = font.getTypefacePaint();
		if (this.paint != null) {
			this.paint.setTextSize(font.getSize());
			this.paint.setTypeface(typefacePaint.getTypeface());
			this.paint.setUnderlineText(typefacePaint.isUnderlineText());
		} else {
			this.paint = new Paint(typefacePaint);
		}
		this.font = font;
	}

	/**
	 * 翻转画布为指定角度
	 */
	public void setTransform(int transform, int width, int height) {
		switch (transform) {
		case TRANS_ROT90: {
			translate(height, 0);
			rotate(ANGLE_90);
			break;
		}
		case TRANS_ROT180: {
			translate(width, height);
			rotate(Math.PI);
			break;
		}
		case TRANS_ROT270: {
			translate(0, width);
			rotate(ANGLE_270);
			break;
		}
		case TRANS_MIRROR: {
			translate(width, 0);
			scale(-1, 1);
			break;
		}
		case TRANS_MIRROR_ROT90: {
			translate(height, 0);
			rotate(ANGLE_90);
			translate(width, 0);
			scale(-1, 1);
			break;
		}
		case TRANS_MIRROR_ROT180: {
			translate(width, 0);
			scale(-1, 1);
			translate(width, height);
			rotate(Math.PI);
			break;
		}
		case TRANS_MIRROR_ROT270: {
			rotate(ANGLE_270);
			scale(-1, 1);
			break;
		}
		}
	}

	/**
	 * 将AffineTransform转换为Matrix
	 * 
	 */
	private static float[] createMatrix(AffineTransform aff) {
		double[] at = new double[9];
		aff.getMatrix(at);
		float[] f = new float[at.length];
		f[0] = (float) at[0];
		f[1] = (float) at[2];
		f[2] = (float) at[4];
		f[3] = (float) at[1];
		f[4] = (float) at[3];
		f[5] = (float) at[5];
		f[6] = 0;
		f[7] = 0;
		f[8] = 1;
		return f;
	}

	/**
	 * 转换Matrix参数格式
	 * 
	 */
	private float[] createAWTMatrix(float[] matrix) {
		float[] at = new float[9];
		at[0] = matrix[0];
		at[1] = matrix[3];
		at[2] = matrix[1];
		at[3] = matrix[4];
		at[4] = matrix[2];
		at[5] = matrix[5];
		at[6] = 0;
		at[7] = 0;
		at[8] = 1;
		return at;
	}

	public boolean hitClip(int x, int y, int width, int height) {
		return getClipBounds().intersects(new Rectangle(x, y, width, height));
	}

	public Rectangle getClipBounds() {
		if (isClose) {
			return new Rectangle(0, 0, 1, 1);
		}
		Rect r = canvas.getClipBounds();
		return new Rectangle(r.left, r.top, r.width(), r.height());
	}

	/**
	 * 使用当前 LGraphics设置勾画 Shape轮廓
	 * 
	 */
	public void draw(Shape s) {
		if (isClose) {
			return;
		}
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(getPath(s), paint);
		paint.setStyle(tmp);
	}

	/**
	 * 使用 LGraphics的设置，填充 Shape的内部区域
	 */
	public void fill(Shape s) {
		if (isClose) {
			return;
		}
		canvas.drawPath(getPath(s), paint);
	}

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		if (isClose) {
			return;
		}
		canvas.drawRoundRect(new RectF(x, y, x + width, y + height), arcWidth,
				arcHeight, paint);
	}

	private Path getPath(Shape s) {
		if (isClose) {
			return null;
		}
		Path path = new Path();
		PathIterator pi = s.getPathIterator(null);
		while (pi.isDone() == false) {
			getSegment(pi, path);
			pi.next();
		}
		return path;
	}

	private void getSegment(PathIterator pi, Path path) {
		if (isClose) {
			return;
		}
		float[] coordinates = new float[6];
		int type = pi.currentSegment(coordinates);
		switch (type) {
		case PathIterator.SEG_MOVETO:
			path.moveTo(coordinates[0], coordinates[1]);
			break;
		case PathIterator.SEG_LINETO:
			path.lineTo(coordinates[0], coordinates[1]);
			break;
		case PathIterator.SEG_QUADTO:
			path.quadTo(coordinates[0], coordinates[1], coordinates[2],
					coordinates[3]);
			break;
		case PathIterator.SEG_CUBICTO:
			path.cubicTo(coordinates[0], coordinates[1], coordinates[2],
					coordinates[3], coordinates[4], coordinates[5]);
			break;
		case PathIterator.SEG_CLOSE:
			path.close();
			break;
		default:
			break;
		}
	}

	public void setXORMode(LColor color) {
		if (isClose) {
			return;
		}
		paint.setXfermode(new PixelXorXfermode(color.getRGB()));
	}

	public void setPaintMode() {
		if (isClose) {
			return;
		}
		paint.setXfermode(null);
	}

	public void setFill() {
		if (isClose) {
			return;
		}
		paint.setStyle(Style.FILL);
	}

	public Stroke getStroke() {
		if (isClose) {
			return null;
		}
		if (paint != null) {
			return new BasicStroke(paint.getStrokeWidth(), paint.getStrokeCap()
					.ordinal(), paint.getStrokeJoin().ordinal());
		}
		return null;
	}

	public void setStroke(Stroke s) {
		if (isClose) {
			return;
		}
		BasicStroke bs = (BasicStroke) s;
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(bs.getLineWidth());
		int cap = bs.getEndCap();
		if (cap == 0) {
			paint.setStrokeCap(Paint.Cap.BUTT);
		} else if (cap == 1) {
			paint.setStrokeCap(Paint.Cap.ROUND);
		} else if (cap == 2) {
			paint.setStrokeCap(Paint.Cap.SQUARE);
		}
		int join = bs.getLineJoin();
		if (join == 0) {
			paint.setStrokeJoin(Paint.Join.MITER);
		} else if (join == 1) {
			paint.setStrokeJoin(Paint.Join.ROUND);
		} else if (join == 2) {
			paint.setStrokeJoin(Paint.Join.BEVEL);
		}
	}

	public void rotate(double theta) {
		if (isClose) {
			return;
		}
		Matrix matrix = canvas.getMatrix();
		matrix.preRotate((float) LSystem.getDegree((float) (RAD_360 - theta)));
		canvas.concat(matrix);
	}

	public void rotate(double theta, double x, double y) {
		if (isClose) {
			return;
		}
		Matrix matrix = canvas.getMatrix();
		matrix.preRotate((float) LSystem.getDegree((float) theta), (float) x,
				(float) y);
		canvas.concat(matrix);
	}

	public void scale(double sx, double sy) {
		if (isClose) {
			return;
		}
		Matrix matrix = canvas.getMatrix();
		matrix.setScale((float) sx, (float) sy);
		canvas.concat(matrix);
	}

	public void rectFill(int x, int y, int width, int height, LColor color) {
		if (isClose) {
			return;
		}
		setColor(color);
		fillRect(x, y, width, height);
	}

	public void rectDraw(int x, int y, int width, int height, LColor color) {
		if (isClose) {
			return;
		}
		setColor(color);
		drawRect(x, y, width, height);
	}

	public void rectOval(int x, int y, int width, int height, LColor color) {
		if (isClose) {
			return;
		}
		setColor(color);
		drawOval(x, y, width, height);
		fillOval(x, y, width, height);
	}

	/**
	 * 绘制五角星
	 */
	public void drawSixStart(LColor color, int x, int y, int r) {
		if (isClose) {
			return;
		}
		setColor(color);
		drawTriangle(color, x, y, r);
		drawRTriangle(color, x, y, r);
	}

	/**
	 * 绘制正三角
	 */
	public void drawTriangle(LColor color, int x, int y, int r) {
		if (isClose) {
			return;
		}
		int x1 = x;
		int y1 = y - r;
		int x2 = x - (int) (r * Math.cos(Math.PI / 6));
		int y2 = y + (int) (r * Math.sin(Math.PI / 6));
		int x3 = x + (int) (r * Math.cos(Math.PI / 6));
		int y3 = y + (int) (r * Math.sin(Math.PI / 6));
		int[] xpos = new int[3];
		xpos[0] = x1;
		xpos[1] = x2;
		xpos[2] = x3;
		int[] ypos = new int[3];
		ypos[0] = y1;
		ypos[1] = y2;
		ypos[2] = y3;
		setColor(color);
		fillPolygon(xpos, ypos, 3);
	}

	/**
	 * 绘制倒三角
	 */
	public void drawRTriangle(LColor color, int x, int y, int r) {
		if (isClose) {
			return;
		}
		int x1 = x;
		int y1 = y + r;
		int x2 = x - (int) (r * Math.cos(Math.PI / 6.0));
		int y2 = y - (int) (r * Math.sin(Math.PI / 6.0));
		int x3 = x + (int) (r * Math.cos(Math.PI / 6.0));
		int y3 = y - (int) (r * Math.sin(Math.PI / 6.0));
		int[] xpos = new int[3];
		xpos[0] = x1;
		xpos[1] = x2;
		xpos[2] = x3;
		int[] ypos = new int[3];
		ypos[0] = y1;
		ypos[1] = y2;
		ypos[2] = y3;
		setColor(color);
		fillPolygon(xpos, ypos, 3);
	}

	public void drawBitmap(Bitmap bit, int x, int y) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas.drawBitmap(bit, x, y, paint);
	}

	public void drawImage(String fileName, int x, int y, int w, int h) {
		drawImage(GraphicsUtils.loadImage(fileName, true), x, y, w, h);
	}

	public void drawImage(String fileName, int x, int y) {
		drawImage(GraphicsUtils.loadImage(fileName, true), x, y);
	}

	public void drawImage(LImage img, int x, int y) {
		if (img != null) {
			drawBitmap(img.getBitmap(), x, y);
		}
	}

	public void drawBitmap(Bitmap bit, int x, int y, int anchor) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		int newx = x;
		int newy = y;
		if (anchor == 0) {
			anchor = LGraphicsAndroid2D.TOP | LGraphicsAndroid2D.LEFT;
		}
		if ((anchor & LGraphicsAndroid2D.RIGHT) != 0) {
			newx -= bit.getWidth();
		} else if ((anchor & LGraphicsAndroid2D.HCENTER) != 0) {
			newx -= bit.getWidth() / 2;
		}
		if ((anchor & LGraphicsAndroid2D.BOTTOM) != 0) {
			newy -= bit.getHeight();
		} else if ((anchor & LGraphicsAndroid2D.VCENTER) != 0) {
			newy -= bit.getHeight() / 2;
		}
		canvas.drawBitmap(bit, newx, newy, paint);
	}

	public void drawImage(LImage img, int x, int y, int anchor) {
		if (img != null) {
			drawBitmap(img.getBitmap(), x, y, anchor);
		}
	}

	public void drawRegion(Bitmap bit, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		if (x_src + width > bit.getWidth() || y_src + height > bit.getHeight()
				|| width < 0 || height < 0 || x_src < 0 || y_src < 0) {
			throw new IllegalArgumentException("Area out of Image");
		}
		int dW = width, dH = height;

		Bitmap newBitmap = null;

		switch (transform) {
		case TRANS_NONE: {
			newBitmap = bit;
			break;
		}
		case TRANS_ROT90: {
			tmp_matrix.reset();
			tmp_matrix.preRotate(90);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			dW = width;
			dH = height;
			break;
		}
		case TRANS_ROT180: {
			tmp_matrix.reset();
			tmp_matrix.preRotate(180);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			break;
		}
		case TRANS_ROT270: {
			tmp_matrix.reset();
			tmp_matrix.preRotate(270);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			dW = height;
			dH = width;
			break;
		}
		case TRANS_MIRROR: {
			tmp_matrix.reset();
			tmp_matrix.preScale(-1, 1);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			break;
		}
		case TRANS_MIRROR_ROT90: {
			tmp_matrix.reset();
			tmp_matrix.preScale(-1, 1);
			tmp_matrix.preRotate(-90);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			dW = height;
			dH = width;
			break;
		}
		case TRANS_MIRROR_ROT180: {
			tmp_matrix.reset();
			tmp_matrix.preScale(-1, 1);
			tmp_matrix.preRotate(-180);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			break;
		}
		case TRANS_MIRROR_ROT270: {
			tmp_matrix.reset();
			tmp_matrix.preScale(-1, 1);
			tmp_matrix.preRotate(-270);
			newBitmap = Bitmap.createBitmap(bit, x_src, y_src, width, height,
					tmp_matrix, true);
			dW = height;
			dH = width;
			break;
		}
		default:
			newBitmap = null;
			throw new IllegalArgumentException("Bad transform");
		}

		boolean badAnchor = false;

		if (anchor == 0) {
			anchor = TOP | LEFT;
		}

		if ((anchor & 0x7f) != anchor || (anchor & BASELINE) != 0) {
			badAnchor = true;
		}

		if ((anchor & TOP) != 0) {
			if ((anchor & (VCENTER | BOTTOM)) != 0) {
				badAnchor = true;
			}
		} else if ((anchor & BOTTOM) != 0) {
			if ((anchor & VCENTER) != 0) {
				badAnchor = true;
			} else {
				y_dst -= dH - 1;
			}
		} else if ((anchor & VCENTER) != 0) {
			y_dst -= (dH - 1) >>> 1;
		} else {
			badAnchor = true;
		}

		if ((anchor & LEFT) != 0) {
			if ((anchor & (HCENTER | RIGHT)) != 0) {
				badAnchor = true;
			}
		} else if ((anchor & RIGHT) != 0) {
			if ((anchor & HCENTER) != 0) {
				badAnchor = true;
			} else {
				x_dst -= dW - 1;
			}
		} else if ((anchor & HCENTER) != 0) {
			x_dst -= (dW - 1) >>> 1;
		} else {
			badAnchor = true;
		}

		if (badAnchor) {
			throw new IllegalArgumentException("Bad Anchor");
		}
		Rect srcRect = new Rect(x_src, y_src, x_src + dW, y_src + dH);
		Rect dstRect = new Rect(x_dst, y_dst, x_dst + dW, y_dst + dH);

		canvas.drawBitmap(newBitmap, srcRect, dstRect, paint);

		if (transform != TRANS_NONE) {
			newBitmap.recycle();
			newBitmap = null;
		}

	}

	public void drawRegion(LImage src, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
		if (src == null) {
			return;
		}
		drawRegion(src.getBitmap(), x_src, y_src, width, height, transform,
				x_dst, y_dst, anchor);
	}

	public void drawBitmap(Bitmap bit, int x, int y, int w, int h) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		int width = bit.getWidth();
		int height = bit.getHeight();
		if (width == w && height == h) {
			drawBitmap(bit, x, y);
			return;
		}
		if (LSystem.isOverrunOS21()) {
			float scaleWidth = ((float) w) / width;
			float scaleHeight = ((float) h) / height;
			tmp_matrix.reset();
			tmp_matrix.postScale(scaleWidth, scaleHeight);
			tmp_matrix.postTranslate(x, y);
			drawBitmap(bit, tmp_matrix, false);
		} else {
			Rect srcR = new Rect(0, 0, w, h);
			Rect dstR = new Rect(x, y, x + w, y + h);
			canvas.drawBitmap(bit, srcR, dstR, paint);
		}

	}

	public void drawImage(LImage img, Matrix marMatrix, boolean filter) {
		if (isClose) {
			return;
		}
		if (img == null) {
			return;
		}
		paint.setFilterBitmap(filter);
		canvas.drawBitmap(img.getBitmap(), marMatrix, paint);
		paint.setFilterBitmap(false);

	}

	public void drawImage(LImage img, Matrix marMatrix) {
		if (isClose) {
			return;
		}
		if (img == null) {
			return;
		}
		canvas.drawBitmap(img.getBitmap(), marMatrix, paint);

	}

	public void drawBitmap(Bitmap bit, Matrix marMatrix, boolean filter) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		paint.setFilterBitmap(filter);
		canvas.drawBitmap(bit, marMatrix, paint);
		paint.setFilterBitmap(false);
	}

	public void drawBitmap(Bitmap bit, Matrix marMatrix) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas.drawBitmap(bit, marMatrix, paint);
	}

	public void drawMirrorBitmap(Bitmap bit, int x, int y, boolean filter) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		if (mirrorImage == null) {
			mirrorImage = new TreeMap<Integer, Bitmap>();
		}
		int hash = GraphicsUtils.hashBitmap(bit);
		Bitmap dst = mirrorImage.get(hash);
		if (dst == null) {
			tmp_matrix.reset();
			tmp_matrix.setValues(mirror);
			int width = bit.getWidth();
			int height = bit.getHeight();
			dst = Bitmap.createBitmap(bit, 0, 0, width, height, tmp_matrix,
					filter);
			mirrorImage.put(hash, dst);
		}
		canvas.drawBitmap(dst, x, y, paint);
	}

	public void drawMirrorImage(LImage img, int x, int y, boolean filter) {
		if (img != null) {
			drawMirrorBitmap(img.getBitmap(), x, y, filter);
		}
	}

	public void drawFlipImage(Bitmap bit, int x, int y, boolean filter) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		tmp_matrix.reset();
		tmp_matrix.postRotate(180);
		Bitmap dst = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit
				.getHeight(), tmp_matrix, filter);
		canvas.drawBitmap(dst, x, y, paint);
		dst.recycle();
		dst = null;

	}

	public void drawFlipImage(LImage img, int x, int y, boolean filter) {
		drawFlipImage(img, x, y, filter);
	}

	public void drawReverseBitmap(Bitmap bit, int x, int y, boolean filter) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		tmp_matrix.reset();
		tmp_matrix.postRotate(270);
		Bitmap dst = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit
				.getHeight(), tmp_matrix, filter);
		canvas.drawBitmap(dst, x, y, paint);
		dst.recycle();
		dst = null;

	}

	public void drawReverseImage(LImage img, int x, int y, boolean filter) {
		drawReverseBitmap(img.getBitmap(), x, y, filter);
	}

	public void drawImage(LImage img, int x, int y, int w, int h) {
		if (isClose) {
			return;
		}
		if (img == null) {
			return;
		}
		int width = img.getWidth();
		int height = img.getHeight();
		if (width == w && height == h) {
			drawImage(img, x, y);
			return;
		}
		if (LSystem.isOverrunOS21()) {
			float scaleWidth = ((float) w) / width;
			float scaleHeight = ((float) h) / height;
			tmp_matrix.reset();
			tmp_matrix.postScale(scaleWidth, scaleHeight);
			tmp_matrix.postTranslate(x, y);
			drawImage(img, tmp_matrix, false);
		} else {
			Rect srcR = new Rect(0, 0, w, h);
			Rect dstR = new Rect(x, y, x + w, y + h);
			canvas.drawBitmap(img.getBitmap(), srcR, dstR, paint);
		}

	}

	public void drawBitmap(Bitmap bit, int x, int y, int w, int h, int x1,
			int y1, int w1, int h1) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas.drawBitmap(bit, new Rect(x1, y1, w1, h1), new Rect(x, y, w, h),
				paint);
	}

	public void drawBitmap(Bitmap bit, Rect r1, Rect r2) {
		if (isClose) {
			return;
		}
		if (bit == null) {
			return;
		}
		canvas.drawBitmap(bit, r1, r2, null);
	}

	public void drawImage(LImage img, int x, int y, int w, int h, int x1,
			int y1, int w1, int h1) {
		if (isClose) {
			return;
		}

		if (img != null) {
			canvas.drawBitmap(img.getBitmap(), new Rect(x1, y1, w1, h1),
					new Rect(x, y, w, h), paint);
		}
	}

	public void drawArc(int x, int y, int width, int height, int sa, int ea) {
		if (isClose) {
			return;
		}
		paint.setStrokeWidth(0);
		canvas.drawArc(new RectF(x, y, x + width, y + height), 360 - (ea + sa),
				ea, true, paint);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		if (isClose) {
			return;
		}
		if (x1 > x2) {
			x1++;
		} else {
			x2++;
		}
		if (y1 > y2) {
			y1++;
		} else {
			y2++;
		}
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	public void drawRect(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setStyle(tmp);
	}

	public void drawBytes(byte[] message, int offset, int length, int x, int y) {
		if (isClose) {
			return;
		}
		drawString(new String(message, offset, length), x, y);
	}

	public void drawChars(char[] message, int offset, int length, int x, int y) {
		if (isClose) {
			return;
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawText(message, offset, length, x, y, paint);
		paint.setFlags(flag);
	}

	public void drawString(String str, int x, int y, int anchor) {
		if (isClose) {
			return;
		}
		int newx = x;
		int newy = y;
		if (anchor == 0) {
			anchor = LGraphicsAndroid2D.TOP | LGraphicsAndroid2D.LEFT;
		}
		if ((anchor & LGraphicsAndroid2D.TOP) != 0) {
			newy -= font.getFontMetrics().ascent;
		} else if ((anchor & LGraphicsAndroid2D.BOTTOM) != 0) {
			newy -= font.getFontMetrics().descent;
		}
		if ((anchor & LGraphicsAndroid2D.HCENTER) != 0) {
			newx -= font.getTypefacePaint().measureText(str) / 2;
		} else if ((anchor & LGraphicsAndroid2D.RIGHT) != 0) {
			newx -= font.getTypefacePaint().measureText(str);
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawText(str, newx, newy, paint);
		paint.setFlags(flag);
	}

	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor) {
		if (isClose) {
			return;
		}
		int newx = x;
		int newy = y;
		if (anchor == 0) {
			anchor = LGraphicsAndroid2D.TOP | LGraphicsAndroid2D.LEFT;
		}
		if ((anchor & LGraphicsAndroid2D.TOP) != 0) {
			newy -= font.getFontMetrics().ascent;
		} else if ((anchor & LGraphicsAndroid2D.BOTTOM) != 0) {
			newy -= font.getFontMetrics().descent;
		}
		if ((anchor & LGraphicsAndroid2D.HCENTER) != 0) {
			newx -= font.getTypefacePaint().measureText(str) / 2;
		} else if ((anchor & LGraphicsAndroid2D.RIGHT) != 0) {
			newx -= font.getTypefacePaint().measureText(str);
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawText(str, offset, len + offset, newx, newy, paint);
		paint.setFlags(flag);
	}

	public void drawSubString(String message, int x, int y, int w, int h,
			int anchor) {
		if (isClose) {
			return;
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawText(message, x, x + y, w, h, paint);
		paint.setFlags(flag);
	}

	public void drawString(String message, float x, float y) {
		if (isClose) {
			return;
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawText(message, x, y, paint);
		paint.setFlags(flag);
	}

	public void drawString(String message, int x, int y) {
		if (isClose) {
			return;
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawText(message, x, y, paint);
		paint.setFlags(flag);
	}

	public void draw3DString(String s, int x, int y, LColor c) {
		setColor(LColor.black);
		for (int i = -2; i < 4; i++) {
			for (int j = -2; j < 4; j++) {
				drawString(s, x + i, y + j);
			}
		}
		setColor(c);
		drawString(s, x, y);
	}

	public void drawStyleString(String message, int x, int y, int color,
			int color1) {
		if (isClose) {
			return;
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		canvas.drawText(message, x + 1, y, paint);
		canvas.drawText(message, x - 1, y, paint);
		canvas.drawText(message, x, y + 1, paint);
		canvas.drawText(message, x, y - 1, paint);
		paint.setColor(color1);
		canvas.drawText(message, x, y, paint);
		paint.setFlags(flag);
	}

	public void drawStyleString(String message, int x, int y, LColor c1,
			LColor c2) {
		if (isClose) {
			return;
		}
		int flag = paint.getFlags();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(c1.getRGB());
		canvas.drawText(message, x + 1, y, paint);
		canvas.drawText(message, x - 1, y, paint);
		canvas.drawText(message, x, y + 1, paint);
		canvas.drawText(message, x, y - 1, paint);
		paint.setColor(c2.getRGB());
		canvas.drawText(message, x, y, paint);
		paint.setFlags(flag);
	}

	public void drawRGB(int[] colors, int offset, int stride, int x, int y,
			int width, int height, boolean hasAlpha) {
		if (isClose) {
			return;
		}
		canvas.drawBitmap(colors, offset, stride, x, y, width, height,
				hasAlpha, paint);
	}

	public void drawClear() {
		if (isClose) {
			return;
		}
		paint.setColor(Color.BLACK);
		canvas.drawColor(Color.BLACK);
	}

	/**
	 * 设置 LGraphics的背景色
	 */
	public void setBackground(LColor color) {
		if (isClose) {
			return;
		}
		paint.setColor(color.getRGB());
		canvas.drawColor(color.getRGB());
	}

	/**
	 * 获得LGraphics的背景色
	 */
	public LColor getBackground() {
		return getColor();
	}

	public void fillArc(int x, int y, int width, int height, int sa, int ea) {
		if (isClose) {
			return;
		}
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawArc(new RectF(x, y, x + width, y + height), 360 - (sa + ea),
				ea, true, paint);
		paint.setStyle(tmp);
	}

	public void fillOval(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
	}

	public void fillPolygon(int[] xpoints, int[] ypoints, int npoints) {
		if (isClose) {
			return;
		}
		canvas.save(Canvas.CLIP_SAVE_FLAG);
		GeneralPath filledPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				npoints);
		filledPolygon.moveTo(xpoints[0], ypoints[0]);
		for (int index = 1; index < xpoints.length; index++) {
			filledPolygon.lineTo(xpoints[index], ypoints[index]);
		}
		filledPolygon.closePath();

		Path path = getPath(filledPolygon);

		canvas.clipPath(path);
		canvas.drawPath(path, paint);
		canvas.restore();
	}

	public void fillPolygon(Polygon p) {
		fillPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public void fillRect(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	public void fillAlphaRect(int x, int y, int w, int h, LColor c) {
		fillAlphaRect(x, y, w, h, c.getRGB());
	}

	public void fillAlphaRect(int x, int y, int w, int h, int pixel) {
		if (isClose) {
			return;
		}
		int color = paint.getColor();
		paint.setColor(pixel);
		float f = x;
		float f1 = y;
		float f2 = x + w;
		float f3 = y + h;
		canvas.drawRect(f, f1, f2, f3, paint);
		paint.setColor(color);
	}

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		if (isClose) {
			return;
		}
		paint.setStyle(Paint.Style.FILL);
		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.lineTo(x1, y1);
		canvas.drawPath(path, paint);
	}

	public void fill3DRect(int x, int y, int width, int height, boolean raised) {
		if (isClose) {
			return;
		}
		LColor color = getColor();
		LColor colorUp, colorDown;
		if (raised) {
			colorUp = color.brighter();
			colorDown = color.darker();
			setColor(color);
		} else {
			colorUp = color.darker();
			colorDown = color.brighter();
			setColor(colorUp);
		}

		width--;
		height--;
		fillRect(x + 1, y + 1, width - 1, height - 1);

		setColor(colorUp);
		fillRect(x, y, width, 1);
		fillRect(x, y + 1, 1, height);

		setColor(colorDown);
		fillRect(x + width, y, 1, height);
		fillRect(x + 1, y + height, width, 1);
	}

	public void draw3DRect(Rectangle rect, LColor back, boolean down) {
		if (isClose) {
			return;
		}
		int x1 = rect.x;
		int y1 = rect.y;
		int x2 = rect.x + rect.width - 1;
		int y2 = rect.y + rect.height - 1;
		if (!down) {
			setColor(back);
			drawLine(x1, y1, x1, y2);
			drawLine(x1, y1, x2, y2);
			setColor(back.brighter());
			drawLine(x1 + 1, y1 + 1, x1 + 1, y2 - 1);
			drawLine(x1 + 1, y1 + 1, x2 - 1, y1 + 1);
			setColor(LColor.black);
			drawLine(x1, y2, x2, y2);
			drawLine(x2, y1, x2, y2);
			setColor(back.darker());
			drawLine(x1 + 1, y2 - 1, x2 - 1, y2 - 1);
			drawLine(x2 - 1, y1 + 2, x2 - 1, y2 - 1);
		} else {
			setColor(LColor.black);
			drawLine(x1, y1, x1, y2);
			drawLine(x1, y1, x2, y1);
			setColor(back.darker());
			drawLine(x1 + 1, y1 + 1, x1 + 1, y2 - 1);
			drawLine(x1 + 1, y1 + 1, x2 - 1, y1 + 1);
			setColor(back.brighter());
			drawLine(x1, y2, x2, y2);
			drawLine(x2, y1, x2, y2);
			setColor(back);
			drawLine(x1 + 1, y2 - 1, x2 - 1, y2 - 1);
			drawLine(x2 - 1, y1 + 2, x2 - 1, y2 - 1);
		}
	}

	/**
	 * 绘制指定矩形的 3D突出显示边框
	 */
	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		if (isClose) {
			return;
		}
		LColor color = getColor();
		LColor colorUp, colorDown;
		if (raised) {
			colorUp = color.brighter();
			colorDown = color.darker();
		} else {
			colorUp = color.darker();
			colorDown = color.brighter();
		}

		setColor(colorUp);
		fillRect(x, y, width, 1);
		fillRect(x, y + 1, 1, height);

		setColor(colorDown);
		fillRect(x + width, y, 1, height);
		fillRect(x + 1, y + height, width, 1);
	}

	public int getClipHeight() {
		return clip.bottom - clip.top;
	}

	public int getClipWidth() {
		return clip.right - clip.left;
	}

	public int getClipX() {
		return clip.left;
	}

	public int getClipY() {
		return clip.top;
	}

	public void clip(Shape s) {
		if (isClose) {
			return;
		}
		canvas.clipPath(getPath(s));
	}

	public void draw(Path s) {
		if (isClose) {
			return;
		}
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		s.transform(canvas.getMatrix());
		canvas.drawPath(s, paint);
		paint.setStyle(tmp);
	}

	public void setTransform(AffineTransform aff) {
		if (isClose) {
			return;
		}
		tmp_matrix.reset();
		tmp_matrix.setValues(createMatrix(aff));
		Matrix m = new Matrix();
		m.setValues(getInverseMatrix());
		canvas.concat(m);
		canvas.concat(tmp_matrix);
	}

	public AffineTransform getTransform() {
		if (isClose) {
			return null;
		}
		return new AffineTransform(createAWTMatrix(getMatrix()));
	}

	public void clearRect(int x, int y, int width, int height) {
		fillRect(x, y, width, height);
	}

	public void clearScreen(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		canvas.clipRect(x, y, x + width, y + height);
		LColor c = getColor();
		if (c != null) {
			canvas
					.drawARGB(c.getAlpha(), c.getBlue(), c.getGreen(), c
							.getRed());
		} else {
			canvas.drawARGB(0xff, 0xff, 0xff, 0xff);
		}
	}

	public void copyArea(int x_src, int y_src, int width, int height,
			int x_dest, int y_dest, int anchor) {
		if (width <= 0 || height <= 0) {
			return;
		}
		boolean badAnchor = false;
		if ((anchor & 0x7f) != anchor || (anchor & BASELINE) != 0) {
			badAnchor = true;
		}
		if ((anchor & TOP) != 0) {
			if ((anchor & (VCENTER | BOTTOM)) != 0)
				badAnchor = true;
		} else if ((anchor & BOTTOM) != 0) {
			if ((anchor & VCENTER) != 0)
				badAnchor = true;
			else {
				y_dest -= height - 1;
			}
		} else if ((anchor & VCENTER) != 0) {
			y_dest -= (height - 1) >>> 1;
		} else {
			badAnchor = true;
		}
		if ((anchor & LEFT) != 0) {
			if ((anchor & (HCENTER | RIGHT)) != 0)
				badAnchor = true;
		} else if ((anchor & RIGHT) != 0) {
			if ((anchor & HCENTER) != 0)
				badAnchor = true;
			else {
				x_dest -= width;
			}
		} else if ((anchor & HCENTER) != 0) {
			x_dest -= (width - 1) >>> 1;
		} else {
			badAnchor = true;
		}
		if (badAnchor) {
			throw new IllegalArgumentException("Bad Anchor");
		}
		copyArea(x_src, y_src, width, height, x_dest - x_src, y_dest - y_src);
	}

	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		if (isClose) {
			return;
		}
		if (x < 0) {
			width += x;
			x = 0;
		}
		if (y < 0) {
			height += y;
			y = 0;
		}
		if (x + width > grapBitmap.getWidth()) {
			width = grapBitmap.getWidth() - x;
		}
		if (y + height > grapBitmap.getHeight()) {
			height = grapBitmap.getHeight() - y;
		}
		Bitmap copy = Bitmap.createBitmap(grapBitmap, x, y, width, height);
		canvas.drawBitmap(copy, x + dx, y + dy, null);
		copy.recycle();
		copy = null;
	}

	public void clipRect(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		canvas.clipRect(x, y, x + width, y + height);
		clip = canvas.getClipBounds();
	}

	public void shear(double shx, double shy) {
		if (isClose) {
			return;
		}
		Matrix martix = canvas.getMatrix();
		martix.setSkew((float) shx, (float) shy);
		canvas.concat(martix);
	}

	public void setStrokeStyle(int style) {
		if (isClose) {
			return;
		}
		if (style != SOLID && style != DOTTED) {
			throw new IllegalArgumentException();
		}
		this.strokeStyle = style;
		if (style == SOLID) {
			paint.setPathEffect(null);
		} else {
			paint.setPathEffect(dashPathEffect);
		}
	}

	public int getStrokeStyle() {
		return this.strokeStyle;
	}

	public void transform(AffineTransform aff) {
		Matrix m = new Matrix();
		m.setValues(createMatrix(aff));
		canvas.concat(m);
	}

	public void translate(float x, float y) {
		if (isClose) {
			return;
		}
		canvas.translate(x, y);
		clip.left -= x;
		clip.right -= x;
		clip.top -= y;
		clip.bottom -= y;
	}

	public void translate(int x, int y) {
		if (isClose) {
			return;
		}
		canvas.translate(x, y);
		clip.left -= x;
		clip.right -= x;
		clip.top -= y;
		clip.bottom -= y;
	}

	public void setClip(Rect rect) {
		setClip(rect.left, rect.top, rect.width(), rect.height());
	}

	public void setClip(Shape shape) {
		Rectangle r = shape.getBounds();
		setClip(r.x, r.y, r.width, r.height);
	}

	public void setClip(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		if (x == clip.left && x + width == clip.right && y == clip.top
				&& y + height == clip.bottom) {
			return;
		}
		if (x < clip.left || x + width > clip.right || y < clip.top
				|| y + height > clip.bottom) {
			canvas.restore();
			canvas.save(Canvas.CLIP_SAVE_FLAG);
		}
		clip.left = x;
		clip.top = y;
		clip.right = x + width;
		clip.bottom = y + height;
		canvas.clipRect(clip);
	}

	public void drawOval(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
		paint.setStyle(tmp);
	}

	public void drawPolygon(int[] xpoints, int[] ypoints, int npoints) {
		if (isClose) {
			return;
		}
		canvas.drawLine(xpoints[npoints - 1], ypoints[npoints - 1], xpoints[0],
				ypoints[0], paint);
		for (int i = 0; i < npoints - 1; i++) {
			canvas.drawLine(xpoints[i], ypoints[i], xpoints[i + 1],
					ypoints[i + 1], paint);
		}
	}

	public void drawPolygon(Polygon p) {
		drawPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public void drawPolyline(int[] xpoints, int[] ypoints, int npoints) {
		if (isClose) {
			return;
		}
		for (int i = 0; i < npoints - 1; i++) {
			drawLine(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
		}
	}

	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		if (isClose) {
			return;
		}
		canvas.drawRoundRect(new RectF(x, y, width, height), arcWidth,
				arcHeight, paint);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Paint getPaint() {
		return paint;
	}

	public Rect getClip() {
		return clip;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setBitmap(Bitmap bit) {
		this.canvas.setBitmap(bit);
	}

	public Bitmap getBitmap() {
		return grapBitmap;
	}

	public boolean isClose() {
		return isClose;
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public void dispose() {
		isClose = true;
		font = null;
		paint = null;
		canvas = null;
		if (mirrorImage != null) {
			mirrorImage.clear();
			mirrorImage = null;
		}
	}

}
