package org.loon.framework.game.simple.core.graphics.window.achieve;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.core.graphics.window.UIFactory;
import org.loon.framework.game.simple.utils.GraphicsUtils;
/**
 * 
 * Copyright 2008 - 2009
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
public class IPanel extends UIFactory {
	
	public IPanel() {
	}
	
	public String getUIName() {
		return "Panel";
	}
	
	public String[] getUIDescription() {
		return new String[] {
		        "Panel"
		};
	}
	
	public BufferedImage[] createUI(LComponent component, int w, int h) {
		BufferedImage[] ui = GraphicsUtils.createImage(2, w, h,
		        Transparency.OPAQUE);
		return ui;
	}
	
	public void processUI(LComponent component, BufferedImage[] ui) {
	}
	
	public void createUI(Graphics2D g, int x, int y, LComponent component, BufferedImage[] ui) {
		if (!component.isEnabled()) {
			g.drawImage(ui[1], x, y, null);
		}
		else {
			g.drawImage(ui[0], x, y, null);
		}
	}
	
}
