package org.loon.framework.javase.game.core.graphics.window.achieve;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.loon.framework.javase.game.core.graphics.GameFont;
import org.loon.framework.javase.game.core.graphics.LComponent;
import org.loon.framework.javase.game.core.graphics.LSystemFont;
import org.loon.framework.javase.game.core.graphics.window.LForm;
import org.loon.framework.javase.game.core.graphics.window.UIStatic;
import org.loon.framework.javase.game.utils.GraphicsUtils;
import org.loon.framework.javase.game.utils.StringUtils;
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

public class ITitleBar extends IPanel {
	
	public ITitleBar() {
		this.put("Background Color", Color.BLACK);
		
		this.put("Text Color", Color.BLACK);
		this.put("Text Disabled Color", Color.GRAY);

		GameFont font = new LSystemFont(new Font("华文新魏", Font.BOLD, 15));
		this.put("Text Font", font);
		this.put("Text Disabled Font", font);
		
		this.put("Text Vertical Alignment Integer", UIStatic.CENTER);
		this.put("Text Insets", new Insets(2, 6, 2, 0));
	}
	
	public String getUIName() {
		return "TitleBar";
	}
	
	public String[] UIDescription() {
		return new String[] {
		        "TitleBar", "TitleBar Disabled"
		};
	}
	
	public void processUI(LComponent component, BufferedImage[] ui) {
		LForm.TTitleBar titleBar = (LForm.TTitleBar) component;
		
		String[] color = new String[] {
		        "Text Color", "Text Disabled Color"
		};
		String[] font = new String[] {
		        "Text Font", "Text Disabled Font"
		};
		
		String[] document =  StringUtils.parseString(titleBar.getTitle());
		for (int i = 0; i < ui.length; i++) {
			Graphics2D g = ui[i].createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);
			GraphicsUtils.drawString(g, document, titleBar.getWidth(), titleBar
			        .getHeight(), (GameFont) this.get(font[i], component),
			        (Color) this.get(color[i], component), UIStatic.LEFT,
			        (Integer) this.get("Text Vertical Alignment Integer",
			                component), (Insets) this.get("Text Insets",
			                component), null);
			g.dispose();
		}
	}
	
}
