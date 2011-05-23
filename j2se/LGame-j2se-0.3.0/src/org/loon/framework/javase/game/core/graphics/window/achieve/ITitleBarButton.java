package org.loon.framework.javase.game.core.graphics.window.achieve;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.loon.framework.javase.game.core.graphics.LComponent;
import org.loon.framework.javase.game.core.graphics.window.LForm;
import org.loon.framework.javase.game.utils.GraphicsUtils;

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
public class ITitleBarButton extends ITool {

	private boolean iconColsed = false;

	final private static Image[] close = GraphicsUtils.getSplitImages(
			"assets/closed.png", 20, 20, false);

	final private static Image[] hide = GraphicsUtils.getSplitImages(
			"assets/hide.png", 20, 20, false);

	final private static int closeWidth = close[0].getWidth(null),
			closeHeight = hide[0].getHeight(null), hideWidth = hide[0]
					.getWidth(null), hideHeight = hide[0].getHeight(null);

	public ITitleBarButton() {
		this.put("Background Color", Color.BLACK);
		this.put("Background Border Color", Color.BLACK);
	}

	public String getUIName() {
		return "TitleBarButton";
	}

	public void processUI(LComponent component, BufferedImage[] ui) {
		LForm.TTitleBar.TTitleBarButton button = (LForm.TTitleBar.TTitleBarButton) component;
		for (int i = 0; i < ui.length; i++) {
			Graphics2D g = ui[i].createGraphics();
			int width = ui[i].getWidth(), height = ui[i].getHeight();
			Image img;
			switch (button.getAction()) {
			case LForm.TTitleBar.TTitleBarButton.CLOSE_BUTTON:
				g.drawImage((iconColsed = !iconColsed) ? close[0] : close[1],
						((width - closeWidth) / 2) + 1,
						((height - closeHeight) / 2) + 1, null);
				break;
			case LForm.TTitleBar.TTitleBarButton.ICONIFIED_BUTTON:
				LForm pane = (LForm) button.getContainer().getContainer();
				img = (pane.isForm() == true) ? hide[0] : hide[1];
				g.drawImage(img, ((width - hideWidth) / 2) + 1,
						((height - hideHeight) / 2) + 1, null);
				break;
			}
		}
	}

}
