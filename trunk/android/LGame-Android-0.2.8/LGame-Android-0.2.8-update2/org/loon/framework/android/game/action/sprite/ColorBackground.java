package org.loon.framework.android.game.action.sprite;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.device.LGraphics;

/**
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1.1
 */
public class ColorBackground extends AbstractBackground {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LColor color;

	public ColorBackground(LColor color, int w, int h) {
		super(w, h);
		this.color = color;
	}

	public ColorBackground(LColor color) {
		this.color = color;
	}

	public LColor getColor() {
		return color;
	}

	public void setColor(LColor color) {
		this.color = color;
	}

	public void createUI(LGraphics g, int nx, int ny, int x, int y, int w, int h) {
		if (!visible) {
			return;
		}
		LColor oldColor = g.getColor();
		g.translate(nx, ny);
		if (alpha > 0.1 && alpha < 1.0) {
			g.setAlpha(alpha);
			g.setColor(color);
			g.fillRect(x, y, w, h);
			g.setAlpha(1.0F);
		} else {
			g.setColor(color);
			g.fillRect(x, y, w, h);
		}
		g.translate(-nx, -ny);
		g.setColor(oldColor);
	}

}
