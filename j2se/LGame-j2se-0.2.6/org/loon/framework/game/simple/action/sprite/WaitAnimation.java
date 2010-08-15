package org.loon.framework.game.simple.action.sprite;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.loon.framework.game.simple.core.graphics.device.LGraphics;

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
public class WaitAnimation {

	private Color color;

	private static final Color defaultBlackColor = new Color(0.5f, 0.5f, 0.5f);

	private static final Color defaultWhiteColor = new Color(240, 240, 240);

	private double r;

	private final List list;

	private boolean isRunning = false;

	private static final double sx = 1.0D, sy = 1.0D;

	public WaitAnimation(int width, int height) {
		int r1 = width / 8, r2 = height / 8;
		this.r = (r1 < r2 ? r1 : r2) / 2;
		this.list = new ArrayList(Arrays.asList(new Object[] {
				new Ellipse2D.Double(sx + 3 * r, sy + 0 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 5 * r, sy + 1 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 6 * r, sy + 3 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 5 * r, sy + 5 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 3 * r, sy + 6 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 1 * r, sy + 5 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 0 * r, sy + 3 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 1 * r, sy + 1 * r, 2 * r, 2 * r) }));
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void black() {
		this.color = defaultBlackColor;
	}

	public void white() {
		this.color = defaultWhiteColor;
	}

	public void next() {
		if (isRunning) {
			list.add(list.remove(0));
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void draw(LGraphics g, int x, int y, int w, int h) {
		g.setAntialiasAll( true);
		g.setColor(color);
		float alpha = 0.0f;
		int nx = x + w / 2 - (int) r * 4, ny = y + h / 2 - (int) r * 4;
		g.translate(nx, ny);
		for (Iterator it = list.iterator(); it.hasNext();) {
			Shape s = (Shape) it.next();
			alpha = isRunning ? alpha + 0.1f : 0.5f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					alpha));
			g.fill(s);
		}
		g.setAntialiasAll( false);
		g.setAlpha(1.0F);
		g.translate(-nx, -ny);
	}

}
