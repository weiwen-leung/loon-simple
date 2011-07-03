package org.loon.framework.android.game.action.sprite;

import org.loon.framework.android.game.core.geom.RectBox;

/**
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
public class SpriteRotateSheet {

	public static RectBox getNewBounds(int x, int y, int width, int height,
			double rotate) {
		double rotation = Math.toRadians(rotate);
		double angSin = Math.sin(rotation);
		double angCos = Math.cos(rotation);
		int newW = (int) Math.floor((width * Math.abs(angCos))
				+ (height * Math.abs(angSin)));
		int newH = (int) Math.floor((height * Math.abs(angCos))
				+ (width * Math.abs(angSin)));
		int centerX = (int) (x + (width / 2));
		int centerY = (int) (y + (height / 2));
		int newX = (int) (centerX - (newW / 2));
		int newY = (int) (centerY - (newH / 2));
		return new RectBox(newX, newY, newW, newH);
	}

}
