package org.loon.framework.javase.game;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.utils.GraphicsUtils;

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
public class GameCursor {
	
	public static Cursor getCursor(String fileName) {
		if (!LSystem.isWindows()) {
			return null;
		}
		Dimension dimension;
		Toolkit tk;
		if ((dimension = (tk = Toolkit.getDefaultToolkit()).getBestCursorSize(
				1, 1)).width == 0
				|| dimension.height == 0) {
			dimension.width = dimension.height = 1;
		}
		return tk.createCustomCursor(GraphicsUtils.loadImage(fileName),
				new Point(0, 0), "");
	}

}
