package org.loon.framework.game.simple.extend;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.game.simple.utils.GraphicsUtils;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
final public class MessageFormDialog {

	final static private int rmxp_space_width = 10, rmxp_space_height = 10,
			rmxp_space_size = 27;

	final static private Map lazyImages = new HashMap(10);

	public final static Image getRMXPDialog(String fileName, int width,
			int height) {
		return MessageFormDialog.getRMXPDialog(GraphicsUtils
				.loadImage(fileName), width, height);
	}

	public final static Image getRMXPloadBuoyage(String fileName, int width,
			int height) {
		return getRMXPloadBuoyage(GraphicsUtils.loadImage(fileName), width,
				height);
	}

	public final static Image getRMXPloadBuoyage(Image rmxpImage, int width,
			int height) {
		String keyName = ("buoyage" + width + "|" + height).intern();
		Image lazyImage = (Image) lazyImages.get(keyName);
		if (lazyImage == null) {
			Image image, left, right, center, up, down = null;
			int objWidth = 32;
			int objHeight = 32;
			int x1 = 128;
			int x2 = 160;
			int y1 = 64;
			int y2 = 96;
			int k = 1;
			try {
				image = GraphicsUtils.drawClipImage(rmxpImage, objWidth,
						objHeight, x1, y1, x2, y2);
				lazyImage = GraphicsUtils.createImage(width, height, false);
				Graphics g = lazyImage.getGraphics();
				left = GraphicsUtils.drawClipImage(image, k, height, 0, 0, k,
						objHeight);
				right = GraphicsUtils.drawClipImage(image, k, height, objWidth
						- k, 0, objWidth, objHeight);
				center = GraphicsUtils.drawClipImage(image, width, height, k,
						k, objWidth - k, objHeight - k);
				up = GraphicsUtils.drawClipImage(image, width, k, 0, 0,
						objWidth, k);
				down = GraphicsUtils.drawClipImage(image, width, k, 0,
						objHeight - k, objWidth, objHeight);
				g.drawImage(center, 0, 0, null);
				g.drawImage(left, 0, 0, null);
				g.drawImage(right, width - k, 0, null);
				g.drawImage(up, 0, 0, null);
				g.drawImage(down, 0, height - k, null);
				g.dispose();
				lazyImages.put(keyName, lazyImage);
			} catch (Exception e) {
				return null;
			} finally {
				left = null;
				right = null;
				center = null;
				up = null;
				down = null;
				image = null;
			}
		}
		return lazyImage;

	}

	public final static Image getRMXPDialog(Image rmxpImage, int width,
			int height) {
		String keyName = ("dialog" + width + "|" + height).intern();
		Image lazyImage = (Image) lazyImages.get(keyName);
		if (lazyImage == null) {
			int objWidth = 64;
			int objHeight = 64;
			int x1 = 128;
			int x2 = 192;
			int y1 = 0;
			int y2 = 64;
			Image image = null;
			Image messageImage = null;
			try {
				image = GraphicsUtils.drawClipImage(rmxpImage, objWidth,
						objHeight, x1, y1, x2, y2);
				messageImage = GraphicsUtils.drawClipImage(rmxpImage, 128, 128,
						0, 0, 128, 128);
			} catch (Exception e) {
				e.printStackTrace();
			}

			MessageDialogSplit mds = new MessageDialogSplit(image,
					rmxp_space_width, rmxp_space_height, rmxp_space_size);
			mds.split();
			int doubleSpace = rmxp_space_size * 2;
			if (width < doubleSpace) {
				width = doubleSpace + 5;
			}
			if (height < doubleSpace) {
				height = doubleSpace + 5;
			}
			lazyImage = GraphicsUtils.createImage(width - 10, height, true);
			Graphics graphics = lazyImage.getGraphics();
			GraphicsUtils.setAlpha(graphics, 0.5d);
			messageImage = GraphicsUtils.getResize(messageImage, width
					- rmxp_space_width * 2, height - rmxp_space_height);
			graphics.drawImage(messageImage, 5, 5, null);
			GraphicsUtils.setAlpha(graphics, 1.0d);
			graphics.drawImage(mds.getLeftUpImage(), 0, 0, null);
			graphics.drawImage(mds.getRightUpImage(),
					(width - rmxp_space_size - rmxp_space_width), 0, null);
			graphics.drawImage(mds.getLeftDownImage(), 0,
					(height - rmxp_space_size), null);
			graphics.drawImage(mds.getRightDownImage(), (width
					- rmxp_space_size - rmxp_space_width),
					(height - rmxp_space_size), null);
			int nWidth = width - doubleSpace;
			int nHeight = height - doubleSpace;
			graphics.drawImage(GraphicsUtils.getResize(mds.getUpImage(),
					nWidth, rmxp_space_size), rmxp_space_size, 0, null);
			graphics.drawImage(GraphicsUtils.getResize(mds.getDownImage(),
					nWidth, rmxp_space_size), rmxp_space_size,
					(height - rmxp_space_size), null);
			graphics.drawImage(GraphicsUtils.getResize(mds.getLeftImage(),
					rmxp_space_size, nHeight), 0, rmxp_space_size, null);
			graphics.drawImage(GraphicsUtils.getResize(mds.getRightImage(),
					rmxp_space_size, nHeight),
					(width - rmxp_space_size - rmxp_space_width),
					rmxp_space_size, null);
			graphics.dispose();
			lazyImages.put(keyName, lazyImage);
		}
		return lazyImage;
	}

	public static void clear() {
		lazyImages.clear();
	}
}
