package org.loon.framework.javase.game;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import org.loon.framework.javase.game.core.EmulatorButtons;
import org.loon.framework.javase.game.core.EmulatorListener;
import org.loon.framework.javase.game.core.IHandler;

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
public interface IView {
	
	public abstract BufferedImage getAwtImage();
	
	public abstract void setEmulatorListener(EmulatorListener emulator);
	
	public abstract EmulatorListener getEmulatorListener();
	
	public abstract EmulatorButtons getEmulatorButtons() ;
	
	public abstract FontMetrics getFontMetrics(Font font);
	
	public abstract boolean isShowLogo();

	public abstract void setShowLogo(boolean showLogo);

	public abstract void requestFocus();

	public abstract void setShowFPS(final boolean isFPS);

	public abstract void setFPS(long frames);

	public abstract IHandler getHandler();

	public boolean isRunning();

	public void setRunning(boolean running);

	public abstract void destroy();

	public abstract long getMaxFPS();
	
	public abstract long getCurrentFPS();

	public abstract void update();
	
	public abstract void update(BufferedImage img);
	
	public abstract void updateLocation(BufferedImage img, int x, int y);
	
	public abstract void update(BufferedImage img,int w,int h);

	public abstract void updateFull(BufferedImage img,int w,int h);
	
	public abstract void mainLoop();

	public abstract void mainStop();

	public abstract void createScreen();

	public abstract void startPaint();

	public abstract void endPaint();

}
