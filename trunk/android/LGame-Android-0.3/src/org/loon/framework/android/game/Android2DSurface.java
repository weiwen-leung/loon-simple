package org.loon.framework.android.game;

import org.loon.framework.android.game.core.EmulatorButtons;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.graphics.LImage;

import android.graphics.Bitmap;

/**
 * 
 * Copyright 2008 - 2010
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
 * @version 0.1.0
 */
public interface Android2DSurface {

	public void setFPS(long frames);

	public long getMaxFPS();

	public long getCurrentFPS();

	public void update();

	public void update(LImage img);

	public void update(Bitmap bit);

	public void updateLocation(Bitmap bit, int x, int y);

	public void updateLocation(LImage img, int x, int y);

	public void updateResize(LImage img, int w, int h);

	public void updateResize(Bitmap bit, int w, int h);

	public void update(LImage img, int w, int h);

	public void update(Bitmap bit, int w, int h);

	public void updateFull(LImage img, int w, int h);

	public void updateFull(Bitmap bit, int w, int h);

	public void setEmulatorListener(EmulatorListener emulator);

	public EmulatorListener getEmulatorListener();

	public EmulatorButtons getEmulatorButtons();

}
