package org.loon.framework.game.simple.core.graphics.window.achieve;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.core.graphics.LFont;
import org.loon.framework.game.simple.core.graphics.LSystemFont;
import org.loon.framework.game.simple.core.graphics.window.LToolTip;
import org.loon.framework.game.simple.core.graphics.window.UIFactory;
import org.loon.framework.game.simple.core.graphics.window.UIStatic;
import org.loon.framework.game.simple.utils.GraphicsUtils;
import org.loon.framework.game.simple.utils.StringUtils;
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
public class IToolTip extends UIFactory {

	public IToolTip() {
		this.put("Background Color", new Color(255, 255, 231));
		this.put("Background Border Color", Color.BLACK);
		this.put("Text Color", Color.BLACK);
		this.put("Text Font", new LSystemFont(new Font(LSystem.FONT, Font.PLAIN,
						12)));

		this.put("Text Insets", new Insets(6, 6, 6, 6));
		this.put("Text Vertical Space Integer", new Integer(1));
	}

	public String getUIName() {
		return "ToolTip";
	}

	public String[] getUIDescription() {
		return new String[] { "ToolTip" };
	}

	public BufferedImage[] createUI(LComponent component, int w, int h) {
		LComponent tooltip = ((LToolTip) component).getToolTipComponent();
		if (tooltip == null) {
			return null;
		}

		String tipText = tooltip.getToolTipText();
		String[] document =  StringUtils.parseString(tipText);

		LFont font = (LFont) this.get("Text Font", component);
		Insets inset = (Insets) this.get("Text Insets", component);
		int space = ((Integer) this.get("Text Vertical Space Integer",
				component)).intValue();
		int width = 0;
		for (int i = 0; i < document.length; i++) {
			w = font.getWidth(document[i]) + inset.left + inset.right;
			if (w > width) {
				width = w;
			}
		}
		int height = (document.length * (font.getHeight() + space)) - space
				+ inset.top + inset.bottom;

		BufferedImage[] ui = GraphicsUtils.createImage(1, width, height,
				Transparency.OPAQUE);
		Graphics2D g = ui[0].createGraphics();

		g.setColor((Color) this.get("Background Color", component));
		g.fillRect(1, 1, width - 2, height - 2);
		Color borderColor = (Color) this.get("Background Border Color",
				component);
		if (borderColor != null) {
			g.setColor(borderColor);
			g.drawRect(0, 0, width - 1, height - 1);
		}

		g.dispose();

		return ui;
	}

	public void processUI(LComponent component, BufferedImage[] ui) {
		LComponent tooltip = ((LToolTip) component).getToolTipComponent();
		if (tooltip == null) {
			return;
		}

		String tipText = tooltip.getToolTipText();

		Graphics2D g = ui[0].createGraphics();
		GraphicsUtils.drawString(g,  StringUtils.parseString(tipText), ui[0]
				.getWidth(), ui[0].getHeight(), (LFont) this.get(
				"Text Font", component), (Color) this.get("Text Color",
				component), UIStatic.LEFT, UIStatic.TOP, (Insets) this.get(
				"Text Insets", component), (Integer) this.get(
				"Text Vertical Space Integer", component));
		g.dispose();
	}

	public void createUI(Graphics2D g, int x, int y, LComponent component,
			BufferedImage[] ui) {
		g.drawImage(ui[0], x, y, null);
	}

}
