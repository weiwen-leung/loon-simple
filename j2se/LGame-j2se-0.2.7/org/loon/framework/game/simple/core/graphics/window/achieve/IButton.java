package org.loon.framework.game.simple.core.graphics.window.achieve;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.core.graphics.LComponent;
import org.loon.framework.game.simple.core.graphics.GameFont;
import org.loon.framework.game.simple.core.graphics.LSystemFont;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.graphics.window.LButton;
import org.loon.framework.game.simple.core.graphics.window.UIFactory;
import org.loon.framework.game.simple.core.graphics.window.UIStatic;
import org.loon.framework.game.simple.utils.GraphicsUtils;
import org.loon.framework.game.simple.utils.StringUtils;

/**
 * 
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

public class IButton extends UIFactory {

	public IButton() {
		this.put("Background Color", Color.BLACK);
		this.put("Background Over Color", Color.GRAY);
		this.put("Background Pressed Color", Color.GRAY);
		this.put("Background Border Color", Color.BLACK);
		this.put("Background Disabled Color", new Color(139, 139, 139));

		this.put("Text Color", Color.WHITE);
		this.put("Text Over Color", Color.WHITE);
		this.put("Text Pressed Color", Color.WHITE);
		this.put("Text Disabled Color", new Color(181, 181, 181));

		GameFont font = new LSystemFont(new Font("华文新魏", Font.BOLD, 18));
		this.put("Text Font", font);
		this.put("Text Over Font", font);
		this.put("Text Pressed Font", font);
		this.put("Text Disabled Font", font);

		this.put("Text Horizontal Alignment Integer", UIStatic.CENTER);
		this.put("Text Vertical Alignment Integer", UIStatic.CENTER);
		this.put("Text Insets", new Insets(5, 5, 5, 5));
		this.put("Text Vertical Space Integer", new Integer(1));
	}

	public String getUIName() {
		return "Button";
	}

	public String[] getUIDescription() {
		return new String[] { "Button", "Button Over", "Button Pressed",
				"Button Disabled" };
	}

	public BufferedImage[] createUI(LComponent component, int w, int h) {
		BufferedImage[] ui = GraphicsUtils.createImage(4, w, h,
				Transparency.OPAQUE);

		String[] color = new String[] { "Background Color",
				"Background Over Color", "Background Pressed Color",
				"Background Disabled Color" };

		Color borderColor = (Color) this.get("Background Border Color",
				component);
		for (int i = 0; i < 4; i++) {
			Graphics2D g = ui[i].createGraphics();
			g.setColor((Color) this.get(color[i], component));
			switch (i) {
			case 0:
				g.fill3DRect(0, 0, w - 1, h - 1, true);
				break;
			case 1:
				g.fillRect(0, 0, w - 1, h - 1);
				break;
			case 2:
				g.fill3DRect(0, 0, w - 1, h - 1, false);
				break;
			case 3:
				g.fill3DRect(0, 0, w - 1, h - 1, true);
				break;
			}
			if (borderColor != null) {
				g.setColor(borderColor);
				g.drawRect(0, 0, w - 1, h - 1);
			}
			g.dispose();
		}

		return ui;
	}

	public void processUI(LComponent component, BufferedImage[] ui) {
		LButton button = (LButton) component;
		String[] color = new String[] { "Text Color", "Text Over Color",
				"Text Pressed Color", "Text Disabled Color" };
		String[] font = new String[] { "Text Font", "Text Over Font",
				"Text Pressed Font", "Text Disabled Font" };
		if (button.getText() != null && button.getText().length() > 0) {
			String[] document = StringUtils.parseString(button.getText());
			for (int i = 0; i < 4; i++) {
				Graphics2D g = ui[i].createGraphics();
				GraphicsUtils.setAntialias(g,true);
				GraphicsUtils.drawString(g, document, button.getWidth(), button
						.getHeight(), (GameFont) this.get(font[i], component),
						(Color) this.get(color[i], component), (Integer) this
								.get("Text Horizontal Alignment Integer",
										component), (Integer) this.get(
								"Text Vertical Alignment Integer", component),
						(Insets) this.get("Text Insets", component),
						(Integer) this.get("Text Vertical Space Integer",
								component));
				g.dispose();
			}
		}
	}

	public void createUI(LGraphics g, int x, int y, LComponent component,
			BufferedImage[] buttonImage) {
		LButton button = (LButton) component;
		if (!button.isEnabled()) {
			g.drawImage(buttonImage[3], x, y);
		} else if (button.isMousePressed()) {
			g.drawImage(buttonImage[2], x, y);
		} else if (button.isMouseOver()) {
			g.drawImage(buttonImage[1], x, y);
		} else {
			g.drawImage(buttonImage[0], x, y);
		}
	}

}
