package org.loon.framework.javase.game.action.sprite;

import java.awt.Image;

import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.LSystem;
/**
 * Copyright 2008 - 2011
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
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;

/**
 * Copyright 2008 - 2011
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
 * @version 0.1.0
 */
public class WaitSprite extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float alpha;

	private LTimer delay;

	private boolean visible;

	private WaitAnimation wait;

	private RectBox rect;

	public WaitSprite(int s) {
		this(s, LSystem.screenRect.width, LSystem.screenRect.height);
	}

	public WaitSprite(int s, int w, int h) {
		this.wait = new WaitAnimation(s, w, h);
		this.wait.setRunning(true);
		this.delay = new LTimer(120);
		this.alpha = 1.0F;
		this.visible = true;
	}

	public void createUI(LGraphics g) {
		if (!visible) {
			return;
		}
		if (alpha > 0 && alpha < 1.0) {
			g.setAlpha(alpha);
			wait.draw(g, x(), y());
			g.setAlpha(1.0F);
		} else {
			wait.draw(g, x(), y());
		}
	}

	public int getHeight() {
		return wait.getHeight();
	}

	public int getWidth() {
		return wait.getWidth();
	}

	public void update(long elapsedTime) {
		if (!visible) {
			return;
		}
		if (delay.action(elapsedTime)) {
			wait.next();
		}
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	public RectBox getCollisionBox() {
		if (rect == null) {
			rect = new RectBox(x(), y(), getWidth(), getHeight());
		} else {
			rect.setBounds(x(), y(), getWidth(), getHeight());
		}
		return rect;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Image getBitmap() {
		return null;
	}

	public void dispose() {
		
	}

}
