package org.loon.framework.javase.game.core;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.loon.framework.javase.game.GameScene;
import org.loon.framework.javase.game.core.graphics.Deploy;
import org.loon.framework.javase.game.core.graphics.IScreen;

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
public interface IHandler extends MouseListener, MouseMotionListener,
		KeyListener, FocusListener {
	
	public abstract void destroy();
	
	public abstract Point getTextOrigin();
	
	public abstract void setID(int id);

	public abstract int getID();

	public abstract void setScene(GameScene frame);

	public abstract GameScene getScene();

	public abstract Window getWindow();
	
	public abstract void setDeploy(Deploy deploy);

	public abstract Deploy getDeploy();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract IScreen getScreen();

	public abstract void setScreen(final IScreen control);
	
	public abstract void changeText(String text);
}
