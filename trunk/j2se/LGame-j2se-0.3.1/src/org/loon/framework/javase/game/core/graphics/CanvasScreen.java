package org.loon.framework.javase.game.core.graphics;

import java.awt.event.KeyEvent;

import org.loon.framework.javase.game.action.sprite.j2me.J2MEKey;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.input.LKey;
import org.loon.framework.javase.game.core.input.LTouch;

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
public abstract class CanvasScreen extends Screen implements J2MEKey {

	public static final int UP_PRESSED = 0x0002;

	public static final int DOWN_PRESSED = 0x0040;

	public static final int LEFT_PRESSED = 0x0004;

	public static final int RIGHT_PRESSED = 0x0020;

	public static final int FIRE_PRESSED = 0x0100;

	public static final int GAME_A_PRESSED = 0x0200;

	public static final int GAME_B_PRESSED = 0x0400;

	public static final int GAME_C_PRESSED = 0x0800;

	public static final int GAME_D_PRESSED = 0x1000;

	private int keyStates;

	private int releasedKeys;

	public CanvasScreen() {
		LSystem.AUTO_REPAINT = false;
		this.setFPS(getMaxFPS());
		this.setRepaintMode(SCREEN_NOT_REPAINT);
	}

	public CanvasScreen(int nw, int nh, int w, int h) {
		LSystem.AUTO_REPAINT = false;
		this.setFPS(getMaxFPS());
		this.setRepaintMode(SCREEN_NOT_REPAINT);
	}

	public CanvasScreen(int w, int h) {
		LSystem.AUTO_REPAINT = false;
		this.setFPS(getMaxFPS());
		this.setRepaintMode(SCREEN_NOT_REPAINT);
	}

	public int getKeyStates() {
		int states = this.keyStates;
		this.keyStates &= ~this.releasedKeys;
		this.releasedKeys = 0;
		return states;
	}

	public int getGameAction(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			return J2MEKey.UP;
		case KeyEvent.VK_DOWN:
			return J2MEKey.DOWN;
		case KeyEvent.VK_LEFT:
			return J2MEKey.LEFT;
		case KeyEvent.VK_RIGHT:
			return J2MEKey.RIGHT;
		case KeyEvent.VK_ENTER:
			return J2MEKey.FIRE;
		case KeyEvent.VK_A:
			return J2MEKey.GAME_A;
		case KeyEvent.VK_B:
			return J2MEKey.GAME_B;
		case KeyEvent.VK_C:
			return J2MEKey.GAME_C;
		case KeyEvent.VK_D:
			return J2MEKey.GAME_D;

		case KeyEvent.VK_0:
		case KeyEvent.VK_1:
		case KeyEvent.VK_2:
		case KeyEvent.VK_3:
		case KeyEvent.VK_4:
		case KeyEvent.VK_5:
		case KeyEvent.VK_6:
		case KeyEvent.VK_7:
		case KeyEvent.VK_8:
		case KeyEvent.VK_9:
			int rval = J2MEKey.KEY_NUM0 + (keyCode - KeyEvent.VK_0);
			return rval;
		case KeyEvent.VK_MULTIPLY:
			return J2MEKey.KEY_STAR;
		case KeyEvent.VK_NUMBER_SIGN:
			return J2MEKey.KEY_POUND;
		default:
			return 0;
		}
	}

	public int getKeyCode(int gameAction) {
		switch (gameAction) {
		case J2MEKey.UP:
			return KeyEvent.VK_UP;
		case J2MEKey.DOWN:
			return KeyEvent.VK_DOWN;
		case J2MEKey.LEFT:
			return KeyEvent.VK_LEFT;
		case J2MEKey.RIGHT:
			return KeyEvent.VK_RIGHT;
		case J2MEKey.FIRE:
			return KeyEvent.VK_ENTER;
		case J2MEKey.GAME_A:
			return KeyEvent.VK_A;
		case J2MEKey.GAME_B:
			return KeyEvent.VK_B;
		case J2MEKey.GAME_C:
			return KeyEvent.VK_C;
		case J2MEKey.GAME_D:
			return KeyEvent.VK_D;
		case J2MEKey.KEY_NUM0:
		case J2MEKey.KEY_NUM1:
		case J2MEKey.KEY_NUM2:
		case J2MEKey.KEY_NUM3:
		case J2MEKey.KEY_NUM4:
		case J2MEKey.KEY_NUM5:
		case J2MEKey.KEY_NUM6:
		case J2MEKey.KEY_NUM7:
		case J2MEKey.KEY_NUM8:
		case J2MEKey.KEY_NUM9:
			int rval = KeyEvent.VK_0 + (gameAction - J2MEKey.KEY_NUM0);
			return rval;
		case J2MEKey.KEY_POUND:
			return KeyEvent.VK_NUMBER_SIGN;
		case J2MEKey.KEY_STAR:
			return KeyEvent.VK_MULTIPLY;
		default:
			return 0;
		}
	}

	public void flushGraphics(int x, int y, int width, int height) {
		this.repaint();
	}

	public void flushGraphics() {
		this.repaint();
	}

	public synchronized void repaint() {
		LSystem.AUTO_REPAINT = true;
	}

	public void repaint(int x, int y, int width, int height) {
		repaint();
	}

	public abstract void keyPressed(int keyCode);

	public void onKeyDown(LKey e) {
		int keyCode = e.getKeyCode();
		int gameAction = getGameAction(keyCode);
		if (gameAction != 0) {
			int bit = 1 << gameAction;
			this.keyStates |= bit;
			this.releasedKeys &= ~bit;
		}
		keyPressed(keyCode);
	}

	public void onKeyUp(LKey e) {
		keyReleased(e.getKeyCode());
	}

	public abstract void keyReleased(int keyCode);

	public void exitGame() {
		System.exit(0);
	}

	public abstract void paint(GLEx g);

	final public void draw(GLEx g) {
		paint(g);
		LSystem.AUTO_REPAINT = false;
	}

	public void dispose() {
		super.dispose();
	}

	public abstract void pointerPressed(double x, double y);

	public abstract void pointerReleased(double x, double y);

	public abstract void pointerMove(double x, double y);

	public abstract void pointerDrag(double x, double y);

	public void touchDown(LTouch e) {
		pointerPressed(e.getX(), e.getY());
	}

	public void touchUp(LTouch e) {
		pointerReleased(e.getX(), e.getY());
	}

	public void touchDrag(LTouch e) {
		pointerDrag(e.getX(), e.getY());
	}

	public void touchMove(LTouch e) {
		pointerMove(e.getX(), e.getY());
	}

}
