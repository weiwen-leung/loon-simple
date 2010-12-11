package org.loon.framework.javase.game.utils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

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
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1
 */
final public class MouseUtils {

	public static boolean isRightMouse(MouseEvent event) {
		return (getMouseType(event) == 2);
	}
	
	public static boolean isMiddleMouse(MouseEvent event) {
		return (getMouseType(event) == 1);
	}
	
	public static boolean isLeftMouse(MouseEvent event) {
		return (getMouseType(event) == 0);
	}

	public static int getMouseType(MouseEvent event) {
		if (event.getModifiers() == InputEvent.BUTTON1_MASK) {
			return 0;
		}
		if (event.getModifiers() == InputEvent.BUTTON3_MASK) {
			return 2;
		}
		if (event.getModifiers() == InputEvent.BUTTON2_MASK) {
			return 1;
		}
		return -1;
	}

}
