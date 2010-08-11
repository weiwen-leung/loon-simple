package org.loon.framework.game.simple.core.graphics.window;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;

import org.loon.framework.game.simple.action.sprite.Animation;
import org.loon.framework.game.simple.action.sprite.SpriteImage;
import org.loon.framework.game.simple.core.graphics.LContainer;
import org.loon.framework.game.simple.utils.GraphicsUtils;

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
public class LPaper extends LContainer {

	private Animation animation = new Animation();

	public LPaper(Image background, int x, int y) {
		super(x, y, background.getWidth(null), background.getHeight(null));
		this.customRendering = true;
		this.background = background;
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public LPaper(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName), x, y);
	}

	public LPaper(int x, int y, int w, int h) {
		this(GraphicsUtils.createImage(w < 1 ? w = 1 : w, h < 1 ? h = 1 : h,
				Transparency.TRANSLUCENT), x, y);
	}

	public Animation getAnimation() {
		return this.animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void addAnimationFrame(SpriteImage image, long timer) {
		animation.addFrame(image, timer);
	}

	public void addAnimationFrame(String fileName, long timer) {
		animation.addFrame(fileName, timer);
	}

	public void addAnimationFrame(Image image, long timer) {
		animation.addFrame(image, timer);
	}

	public void doClick() {
	}

	protected void processMouseClicked() {
		this.doClick();
	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.VK_ENTER) {
			this.doClick();
		}
	}

	protected void createCustomUI(Graphics2D g, int x, int y, int w, int h) {
		if (visible) {
			if (animation.getSpriteImage() != null) {
				g.drawImage(animation.getSpriteImage().serializablelImage
						.getImage(), x, y, null);
			}
		}
	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			animation.update(elapsedTime);
		}
	}

	protected void processMouseDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getMouseDX(), this.input.getMouseDY());
		}
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	protected void validateSize() {
		super.validateSize();
	}

	public String getUIName() {
		return "Paper";
	}

}
