package org.loon.framework.javase.game.core.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;

import org.loon.framework.javase.game.utils.GraphicsUtils;

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
public class PixelsImage {

	private int[] pixels;

	private int width, height;

	private ImageLazy lazy;

	private Toolkit toolkit;

	public PixelsImage(Image image) {
		width = -1;
		height = -1;
		int i = image.getWidth(null);
		int j = image.getHeight(null);
		pixels = analyze(image);
		if (pixels != null) {
			width = i;
			height = j;
		}
		load();
	}

	public void destroy() {
		pixels = null;
		lazy = null;
	}

	public static void match(PixelsImage b1, PixelsImage b2) {
		int i1 = b1.getWidth();
		int j1 = b1.getHeight();
		int pixels1[] = b1.getPixels();
		int pixels2[] = b2.getPixels();
		for (int k1 = 1; k1 < j1 - 1; k1++) {
			int h1 = k1 * i1 + 1;
			for (int i3 = 1; i3 < i1 - 1; i3++) {
				int k4 = pixels1[h1];
				int l5 = pixels1[h1 - 1];
				int i7 = pixels1[h1 + 1];
				int j8 = pixels1[h1 - i1];
				int k9 = pixels1[h1 - 1 - i1];
				int k10 = pixels1[(h1 + 1) - i1];
				int k11 = pixels1[h1 + i1];
				int l11 = pixels1[(h1 - 1) + i1];
				int i12 = pixels1[h1 + 1 + i1];
				pixels2[h1++] = 0xff000000
						| ((k4 & 0xff0000) + (l5 & 0xff0000) + (i7 & 0xff0000)
								+ (j8 & 0xff0000) + (k9 & 0xff0000)
								+ (k10 & 0xff0000) + (k11 & 0xff0000)
								+ (l11 & 0xff0000) + (i12 & 0xff0000))
						/ 9
						& 0xff0000
						| ((k4 & 0xff00) + (l5 & 0xff00) + (i7 & 0xff00)
								+ (j8 & 0xff00) + (k9 & 0xff00)
								+ (k10 & 0xff00) + (k11 & 0xff00)
								+ (l11 & 0xff00) + (i12 & 0xff00))
						/ 9
						& 0xff00
						| ((k4 & 0xff) + (l5 & 0xff) + (i7 & 0xff)
								+ (j8 & 0xff) + (k9 & 0xff) + (k10 & 0xff)
								+ (k11 & 0xff) + (l11 & 0xff) + (i12 & 0xff))
						/ 9;
			}

		}

		for (int l1 = 1; l1 < i1 - 1; l1++) {
			int w2 = pixels1[l1];
			int j3 = pixels1[l1 - 1];
			int l4 = pixels1[l1 + 1];
			int i6 = pixels1[l1 + i1];
			int j7 = pixels1[(l1 - 1) + i1];
			int k8 = pixels1[l1 + 1 + i1];
			pixels2[l1] = 0xff000000
					| ((w2 & 0xff0000) + (j3 & 0xff0000) + (l4 & 0xff0000))
					/ 6
					& 0xff0000
					| ((w2 & 0xff00) + (j3 & 0xff00) + (l4 & 0xff00)
							+ (i6 & 0xff00) + (j7 & 0xff00) + (k8 & 0xff00))
					/ 6
					& 0xff00
					| ((w2 & 0xff) + (j3 & 0xff) + (l4 & 0xff) + (i6 & 0xff)
							+ (j7 & 0xff) + (k8 & 0xff)) / 6;
		}

		int w1 = (pixels1.length - i1) + 1;
		int h2 = pixels1.length - 1;
		for (int k3 = w1; k3 < h2; k3++) {
			int i5 = pixels1[k3];
			int j6 = pixels1[k3 - 1];
			int k7 = pixels1[k3 + 1];
			int l8 = pixels1[k3 - i1];
			int l9 = pixels1[k3 - 1 - i1];
			int l10 = pixels1[(k3 + 1) - i1];
			pixels2[k3] = 0xff000000
					| ((i5 & 0xff0000) + (j6 & 0xff0000) + (k7 & 0xff0000))
					/ 6
					& 0xff0000
					| ((i5 & 0xff00) + (j6 & 0xff00) + (k7 & 0xff00)
							+ (l8 & 0xff00) + (l9 & 0xff00) + (l10 & 0xff00))
					/ 6
					& 0xff00
					| ((i5 & 0xff) + (j6 & 0xff) + (k7 & 0xff) + (l8 & 0xff)
							+ (l9 & 0xff) + (l10 & 0xff)) / 6;
		}

		for (int l3 = pixels1.length - i1 * 2; l3 >= i1; l3 -= i1) {
			int j5 = pixels1[l3 - i1];
			int k6 = pixels1[(l3 + 1) - i1];
			int l7 = pixels1[l3];
			int i9 = pixels1[l3 + 1];
			int i10 = pixels1[l3 + i1];
			int i11 = pixels1[l3 + 1 + i1];
			pixels2[l3] = 0xff000000
					| ((j5 & 0xff0000) + (k6 & 0xff0000) + (l7 & 0xff0000))
					/ 6
					& 0xff0000
					| ((j5 & 0xff00) + (k6 & 0xff00) + (l7 & 0xff00)
							+ (i9 & 0xff00) + (i10 & 0xff00) + (i11 & 0xff00))
					/ 6
					& 0xff00
					| ((j5 & 0xff) + (k6 & 0xff) + (l7 & 0xff) + (i9 & 0xff)
							+ (i10 & 0xff) + (i11 & 0xff)) / 6;
		}

		for (int i4 = ((pixels1.length - i1 * 2) + i1) - 1; i4 >= i1; i4 -= i1) {
			int k5 = pixels1[i4 - i1];
			int l6 = pixels1[i4 - 1 - i1];
			int i8 = pixels1[i4];
			int j9 = pixels1[i4 - 1];
			int j10 = pixels1[i4 + i1];
			int j11 = pixels1[(i4 - 1) + i1];
			pixels2[i4] = ((k5 & 0xff0000) + (l6 & 0xff0000) + (i8 & 0xff0000))
					/ 6
					& 0xff0000
					| ((k5 & 0xff00) + (l6 & 0xff00) + (i8 & 0xff00)
							+ (j9 & 0xff00) + (j10 & 0xff00) + (j11 & 0xff00))
					/ 6
					& 0xff00
					| ((k5 & 0xff) + (l6 & 0xff) + (i8 & 0xff) + (j9 & 0xff)
							+ (j10 & 0xff) + (j11 & 0xff)) / 6;
		}

		pixels2[0] = ((pixels1[0] & 0xfcfcfc) >> 2)
				+ ((pixels1[1] & 0xfcfcfc) >> 2)
				+ ((pixels1[i1] & 0xfcfcfc) >> 2)
				+ ((pixels1[i1 + 1] & 0xfcfcfc) >> 2);
		int j4 = i1 - 1;
		pixels2[j4] = ((pixels1[j4] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 - 1] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 + i1] & 0xfcfcfc) >> 2)
				+ ((pixels1[(j4 + i1) - 1] & 0xfcfcfc) >> 2);
		j4 = i1 * (j1 - 1);
		pixels2[j4] = ((pixels1[j4] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 + 1] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 - i1] & 0xfcfcfc) >> 2)
				+ ((pixels1[(j4 - i1) + 1] & 0xfcfcfc) >> 2);
		j4 = (i1 * (j1 - 1) + i1) - 1;
		pixels2[j4] = ((pixels1[j4] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 - 1] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 - i1] & 0xfcfcfc) >> 2)
				+ ((pixels1[j4 - i1 - 1] & 0xfcfcfc) >> 2);
	}

	public Image get() {
		Image image = toolkit.createImage(lazy);
		toolkit.prepareImage(image, -1, -1, null);
		return image;
	}

	private int[] analyze(Image image) {
		Image image1 = image;
		int size = Toolkit.getDefaultToolkit().getColorModel().getPixelSize();
		if (size == 16) {
			Image image2 = GraphicsUtils.createImage(image1.getWidth(null),
					image1.getHeight(null));
			Graphics g = image2.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			image1 = image2;
		}

		int w = image1.getWidth(null);
		int h = image1.getHeight(null);
		if (w < 0 || h < 0 || image1 == null) {
			return null;
		}
		int pixels[] = new int[w * h];
		PixelGrabber pixelgrabber = new PixelGrabber(image1.getSource(), 0, 0,
				w, h, pixels, 0, w);
		try {
			if (!pixelgrabber.grabPixels(0L)) {
				pixelgrabber.grabPixels(0L);
			}
			if ((pixelgrabber.status() & 0x80) != 0) {
				return null;
			}
		} catch (Exception exception) {
			return null;
		}
		if (image1 != image) {
			image1.flush();
		}
		image=null;
		return pixels;
	}

	public PixelsImage(int w, int h) {
		width = -1;
		height = -1;
		pixels = new int[w * h];
		width = w;
		height = h;
		load();
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void load() {
		lazy = new ImageLazy(width, height, new DirectColorModel(32, 0xff0000,
				65280, 255), pixels, 0, width);
		toolkit = Toolkit.getDefaultToolkit();
	}

}
