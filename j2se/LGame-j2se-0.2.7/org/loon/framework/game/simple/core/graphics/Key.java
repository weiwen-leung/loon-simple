package org.loon.framework.game.simple.core.graphics;

import java.awt.event.KeyEvent;
/**
 * 
 * Copyright 2008 - 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng  
 * @email：ceponline@yahoo.com.cn 
 * @version 0.1
 */
public class Key {

	public boolean key_right;

	public boolean key_left;

	public boolean key_up;

	public boolean key_down;

	public boolean key_a;

	public boolean key_b;

	public boolean key_c;

	public boolean key_d;

	public boolean key_space;

	public boolean KeyNum0;

	public boolean KeyNum1;

	public boolean KeyNum2;

	public boolean KeyNum3;

	public boolean KeyNum4;

	public boolean KeyNum5;

	public boolean KeyNum6;

	public boolean KeyNum7;

	public boolean KeyNum8;

	public boolean KeyNum9;

	public Key() {
		key_right = false;
		key_left = false;
		key_up = false;
		key_down = false;
		key_a = false;
		key_b = false;
		key_c = false;
		key_d = false;
		key_space = false;
		KeyNum0 = false;
		KeyNum1 = false;
		KeyNum2 = false;
		KeyNum3 = false;
		KeyNum4 = false;
		KeyNum5 = false;
		KeyNum6 = false;
		KeyNum7 = false;
		KeyNum8 = false;
		KeyNum9 = false;
	}

	public void SetKeyCode(KeyEvent keyevent, boolean click) {

		if (click) {

			switch (keyevent.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				key_left = true;
				break;

			case KeyEvent.VK_RIGHT:
				key_right = true;
				break;

			case KeyEvent.VK_UP:
				key_up = true;
				break;

			case KeyEvent.VK_DOWN:
				key_down = true;
				break;

			case KeyEvent.VK_A:
				key_a = true;
				break;

			case KeyEvent.VK_B:
				key_b = true;
				break;

			case KeyEvent.VK_C:
				key_c = true;
				break;

			case KeyEvent.VK_D:
				key_d = true;
				break;

			case KeyEvent.VK_SPACE:
				key_space = true;
				break;

			case KeyEvent.VK_0:
				KeyNum0 = true;
				break;

			case KeyEvent.VK_1:
				KeyNum1 = true;
				key_a = true;
				break;

			case KeyEvent.VK_2:
				KeyNum2 = true;
				break;

			case KeyEvent.VK_3:
				KeyNum3 = true;
				key_b = true;
				break;

			case KeyEvent.VK_4:
				KeyNum4 = true;
				break;

			case KeyEvent.VK_5:
				KeyNum5 = true;
				break;

			case KeyEvent.VK_6:
				KeyNum6 = true;
				break;

			case KeyEvent.VK_7:
				KeyNum7 = true;
				key_c = true;
				break;

			case KeyEvent.VK_8:
				KeyNum8 = true;
				break;

			case KeyEvent.VK_9:
				KeyNum9 = true;
				key_d = true;
				break;
			}

		} else {
			switch (keyevent.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				key_left = false;
				break;

			case KeyEvent.VK_RIGHT:
				key_right = false;
				break;

			case KeyEvent.VK_UP:
				key_up = false;
				break;

			case KeyEvent.VK_DOWN:
				key_down = false;
				break;

			case KeyEvent.VK_A:
				key_a = false;
				break;

			case KeyEvent.VK_B:
				key_b = false;
				break;

			case KeyEvent.VK_C:
				key_c = false;
				break;

			case KeyEvent.VK_D:
				key_d = false;
				break;

			case KeyEvent.VK_SPACE:
				key_space = false;
				break;

			case KeyEvent.VK_0:
				KeyNum0 = false;
				break;

			case KeyEvent.VK_1:
				KeyNum1 = false;
				key_a = false;
				break;

			case KeyEvent.VK_2:
				KeyNum2 = false;
				break;

			case KeyEvent.VK_3:
				KeyNum3 = false;
				key_b = false;
				break;

			case KeyEvent.VK_4:
				KeyNum4 = false;
				break;

			case KeyEvent.VK_5:
				KeyNum5 = false;
				break;

			case KeyEvent.VK_6:
				KeyNum6 = false;
				break;

			case KeyEvent.VK_7:
				KeyNum7 = false;
				key_c = false;
				break;

			case KeyEvent.VK_8:
				KeyNum8 = false;
				break;

			case KeyEvent.VK_9:
				KeyNum9 = false;
				key_d = false;
				break;
			}
		}
	}

}
