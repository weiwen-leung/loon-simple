package org.loon.framework.game.simple.core;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.loon.framework.game.simple.GameScene;
import org.loon.framework.game.simple.core.graphics.Deploy;
import org.loon.framework.game.simple.core.graphics.IScreen;

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
public class Handler implements IHandler {

	private GameScene scene;

	private Deploy deploy;

	private IScreen control;

	private boolean isInstance;

	private int id, width, height;

	private Point textOrigin;

	public Handler(GameScene scene, int width, int height) {
		this.width = width;
		this.height = height;
		this.scene = scene;
		this.textOrigin = new Point(0, 0);
	}

	public Handler(int width, int height) {
		this.width = width;
		this.height = height;
		this.textOrigin = new Point(0, 0);
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public synchronized IScreen getScreen() {
		return control;
	}

	public synchronized void setScreen(final IScreen screen) {
		if (screen == null) {
			this.isInstance = false;
			throw new RuntimeException("Cannot create a [IScreen] instance !");
		}
		this.control = screen;
		this.control.resize();
		this.isInstance = true;
		Thread.yield();
	}

	public void keyPressed(KeyEvent e) {
		if (isInstance) {
			control.keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (isInstance) {
			control.keyReleased(e);
		}
	}

	public void keyTyped(KeyEvent e) {
		if (isInstance) {
			control.keyTyped(e);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (isInstance) {
			control.mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (isInstance) {
			control.mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (isInstance) {
			control.mouseExited(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isInstance) {
			control.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isInstance) {
			control.mouseReleased(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (isInstance) {
			control.mouseDragged(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (isInstance) {
			control.mouseMoved(e);
		}
	}

	public void changeText(String text) {
		if (isInstance) {
			control.changeText(text);
		}
	}

	public void focusGained(FocusEvent e) {
		if (isInstance) {
			control.focusGained(e);
		}
	}

	public void focusLost(FocusEvent e) {
		if (isInstance) {
			control.focusLost(e);
		}
	}

	public GameScene getScene() {
		return scene;
	}

	public void setScene(GameScene scene) {
		this.scene = scene;
	}

	public Deploy getDeploy() {
		return deploy;
	}

	public void setDeploy(Deploy deploy) {
		this.deploy = deploy;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Window getWindow() {
		return scene.getWindow();
	}

	public Point getTextOrigin() {
		return textOrigin;
	}

	public void dispose() {
		if (control != null) {
			control.dispose();
		}
	}

}
