package org.loon.framework.javase.game.core;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import org.loon.framework.javase.game.GameScene;
import org.loon.framework.javase.game.Java2DView;
import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.graphics.Deploy;
import org.loon.framework.javase.game.core.graphics.Screen;

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
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Handler implements IHandler {

	private GameScene scene;

	private Deploy deploy;

	private final LinkedList screens;

	private Screen currentControl;

	private boolean isInstance;

	private int id, width, height;

	private Point textOrigin;

	private final RectBox rect;

	public Handler(GameScene scene, int width, int height) {
		this.width = width;
		this.height = height;
		this.scene = scene;
		this.textOrigin = new Point(0, 0);
		this.rect = new RectBox(0, 0, width, height);
		this.screens = new LinkedList();
	}

	public Handler(int width, int height) {
		this.width = width;
		this.height = height;
		this.textOrigin = new Point(0, 0);
		this.rect = new RectBox(0, 0, width, height);
		this.screens = new LinkedList();
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public RectBox getScreenBox() {
		return rect;
	}

	public synchronized Screen getScreen() {
		return currentControl;
	}

	public void runFirstScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getFirst();
			if (o != currentControl) {
				setScreen((Screen) o, false);
			}
		}
	}

	public void runLastScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getLast();
			if (o != currentControl) {
				setScreen((Screen) o, false);
			}
		}
	}

	public void runPreviousScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i - 1 > -1) {
						setScreen((Screen) screens.get(i - 1), false);
						return;
					}
				}
			}
		}
	}

	public void runNextScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i + 1 < size) {
						setScreen((Screen) screens.get(i + 1), false);
						return;
					}
				}
			}
		}
	}

	public void runIndexScreen(int index) {
		int size = screens.size();
		if (size > 0 && index > -1 && index < size) {
			Object o = screens.get(index);
			if (currentControl != o) {
				setScreen((Screen) screens.get(index), false);
			}
		}
	}

	public void addScreen(final Screen screen) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [IScreen] instance !");
		}
		screens.add(screen);
	}

	public List getScreens() {
		return (List) screens;
	}

	public int getScreenCount() {
		return screens.size();
	}

	public void setScreen(final Screen screen) {
		setScreen(screen, true);
	}

	private void setScreen(final Screen screen, boolean put) {
		synchronized (this) {
			if (screen == null) {
				this.isInstance = false;
				throw new RuntimeException(
						"Cannot create a [Screen] instance !");
			}
			screen.setOnLoadState(false);
			if (currentControl == null) {
				currentControl = screen;
			} else {
				synchronized (currentControl) {
					currentControl.destroy();
					currentControl = screen;
				}
			}
			this.currentControl.resize();
			this.isInstance = true;
			if (screen instanceof EmulatorListener) {
				if (deploy.getView() instanceof Java2DView) {
					Java2DView l2d = (Java2DView) deploy.getView();
					l2d.update();
					l2d.setEmulatorListener((EmulatorListener) screen);
				}
			} else {
				if (deploy.getView() instanceof Java2DView) {
					((Java2DView) deploy.getView()).setEmulatorListener(null);
				}
			}
			Thread load = null;
			try {
				load = new Thread() {
					public void run() {
						for (; LSystem.isLogo;) {
							try {
								Thread.sleep(60);
							} catch (InterruptedException e) {
							}
						}
						screen.setClose(false);
						screen.onLoad();
						screen.setOnLoadState(true);
						screen.onLoaded();
					}
				};
				load.setPriority(Thread.NORM_PRIORITY - 1);
				load.start();
			} catch (Exception ex) {
				throw new RuntimeException(currentControl.getName()
						+ " onLoad:" + ex.getMessage());
			} finally {
				load = null;
			}
			if (put) {
				screens.add(screen);
			}
			Thread.yield();
		}
	}

	public void keyPressed(KeyEvent e) {
		if (isInstance) {
			currentControl.keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (isInstance) {
			currentControl.keyReleased(e);
		}
	}

	public void keyTyped(KeyEvent e) {
		if (isInstance) {
			currentControl.keyTyped(e);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (isInstance) {
			currentControl.mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (isInstance) {
			currentControl.mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (isInstance) {
			currentControl.mouseExited(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isInstance) {
			if (deploy.getEmulatorButtons() != null) {
				deploy.getEmulatorButtons().hit(e.getX(), e.getY());
			}
			currentControl.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isInstance) {
			if (deploy.getEmulatorButtons() != null) {
				deploy.getEmulatorButtons().unhit();
			}
			currentControl.mouseReleased(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (isInstance) {
			currentControl.mouseDragged(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (isInstance) {
			currentControl.mouseMoved(e);
		}
	}

	public void changeText(String text) {
		if (isInstance) {
			currentControl.changeText(text);
		}
	}

	public void focusGained(FocusEvent e) {
		if (isInstance) {
			currentControl.focusGained(e);
		}
	}

	public void focusLost(FocusEvent e) {
		if (isInstance) {
			currentControl.focusLost(e);
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

	public void destroy() {
		if (currentControl != null) {
			currentControl.destroy();
		}
	}

}
