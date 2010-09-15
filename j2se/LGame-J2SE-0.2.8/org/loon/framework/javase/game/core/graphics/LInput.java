package org.loon.framework.javase.game.core.graphics;

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
public interface LInput {

	public static final int NO_BUTTON = -1;

	public static final int NO_KEY = -1;
	
	public abstract boolean isClick();
	
	public abstract boolean leftClick();

	public abstract boolean middleClick();

	public abstract boolean rightClick();
	
	public abstract int getShakeNumber();

	public abstract void setShakeNumber(int shake);
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
	public abstract void update(long l);

	public abstract void refresh();

	public abstract void mouseMove(int i, int j);

	public abstract int getMouseX();

	public abstract int getMouseY();

	public abstract int getMouseDX();

	public abstract int getMouseDY();

	public abstract int getMouseReleased();

	public abstract boolean isMouseReleased(int i);

	public abstract int getMousePressed();
	
	public abstract boolean isMousePressed(int i);

	public abstract boolean isMouseDown(int i);

	public abstract int getKeyReleased();

	public abstract boolean isKeyReleased(int i);

	public abstract int getKeyPressed();

	public abstract boolean isKeyPressed(int i);

	public abstract boolean isKeyDown(int i);

}
