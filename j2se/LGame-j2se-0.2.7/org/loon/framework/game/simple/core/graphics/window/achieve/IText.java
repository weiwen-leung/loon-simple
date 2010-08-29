package org.loon.framework.game.simple.core.graphics.window.achieve;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.graphics.window.LText;
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

public class IText extends UIFactory {
	
	public IText() {
		this.put("Background Color", Color.BLACK);
		this.put("Background Disabled Color", Color.GRAY);
		this.put("Background Uneditable Color",Color.BLACK);
		
		this.put("Background Border Color", Color.WHITE);
		this.put("Background Border Disabled Color", Color.GRAY);
		this.put("Background Border Uneditable Color", Color.BLACK);
	}
	
	public String getUIName() {
		return "Text";
	}
	
	public String[] getUIDescription() {
		return new String[] {
		        "TextField", "TextField Disabled", "TextField Not Editable"
		};
	}
	
	public BufferedImage[] createUI(LComponent component, int w, int h) {
		BufferedImage[] ui = GraphicsUtils.createImage(3, w, h,
		        Transparency.OPAQUE);
		
		String[] color = new String[] {
		        "Background Color", "Background Disabled Color",
		        "Background Uneditable Color"
		};
		String[] border = new String[] {
		        "Background Border Color", "Background Border Disabled Color",
		        "Background Border Uneditable Color"
		};
		
		for (int i = 0; i < ui.length; i++) {
			Graphics2D g = ui[i].createGraphics();
			
			g.setColor((Color) this.get(color[i], component));
			g.fillRect(0, 0, w, h);
			
			g.setColor((Color) this.get(border[i], component));
			g.drawRect(0, 0, w - 1, h - 1);
			
			g.dispose();
		}
		
		return ui;
	}
	
	public void processUI(LComponent component, BufferedImage[] ui) {
	}
	
	public void createUI(LGraphics g, int x, int y, LComponent component, BufferedImage[] ui) {
		LText textField = (LText) component;
		
		if (!textField.isEnabled()) {
			g.drawImage(ui[1], x, y);
			
		}
		else if (!textField.isEditable()) {
			g.drawImage(ui[2], x, y);
			
		}
		else {
			g.drawImage(ui[0], x, y);
		}
	}
	
}
