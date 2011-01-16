package org.loon.framework.android.game.core.graphics.window.actor;

import java.util.Iterator;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.timer.LTimer;

import android.graphics.Matrix;

/**
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Layer extends ActorLayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Matrix matrix = new Matrix();

	protected boolean visible, actorDrag, pressed;

	private Actor dragActor;

	private LTimer timer = new LTimer(0);

	private RectBox screenRect;

	private boolean isLimitMove, isTouchClick;

	public Layer(int w, int h) {
		this(0, 0, w, h);
	}

	public Layer(int w, int h, boolean bounded) {
		this(0, 0, w, h, bounded);
	}

	public Layer(int x, int y, int w, int h) {
		this(x, y, w, h, true);
	}

	public Layer(int x, int y, int w, int h, boolean bounded) {
		this(x, y, w, h, bounded, 1);
	}

	public Layer(int x, int y, int w, int h, boolean bounded, int size) {
		super(x, y, w, h, size, bounded);
		this.setLocation(x, y);
		this.screenRect = LSystem.getSystemHandler().getScreenBox();
		this.actorDrag = true;
		this.visible = true;
		this.customRendering = true;
		this.isLimitMove = true;
		this.isTouchClick = true;
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public void downClick(int x, int y) {
	}

	public void upClick(int x, int y) {
	}

	public void downKey() {
	}

	public void upKey() {
	}

	public void drag(int x, int y) {
	}

	/**
	 * 设定动作触发延迟时间
	 * 
	 * @param delay
	 */
	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	/**
	 * 返回动作触发延迟时间
	 * 
	 * @return
	 */
	public long getDelay() {
		return timer.getDelay();
	}

	/**
	 * 动作处理
	 * 
	 * @param elapsedTime
	 */
	public void action(long elapsedTime) {

	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			if (timer.action(this.elapsedTime = elapsedTime)) {
				action(elapsedTime);
			}
		}
	}

	public void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (!visible) {
			return;
		}
		paintObjects(g, x, y, x + w, y + h);
		if (x == 0 && y == 0) {
			paint(g);
		} else {
			g.translate(x, y);
			paint(g);
			g.translate(-x, -y);
		}
	}

	public void paint(LGraphics g) {

	}

	public void paintObjects(LGraphics g, int minX, int minY, int maxX, int maxY) {
		if (objects == null) {
			return;
		}
		synchronized (objects) {
			int paintSeq = 0;
			Iterator<?> iter = objects.iterator();
			Actor thing = null;
			while (iter.hasNext()) {
				thing = (Actor) iter.next();
				if (!thing.isVisible()) {
					continue;
				}
				thing.update(elapsedTime);
				RectBox rect = thing.getRectBox();
				int actorX = minX + thing.x;
				int actorY = minY + thing.y;
				int actorWidth = rect.getWidth();
				int actorHeight = rect.getHeight();
				if (actorX + actorWidth < minX || actorX > maxX
						|| actorY + actorHeight < minY || actorY > maxY) {
					continue;
				}
				LImage image = thing.getImage();
				if (image != null) {
					thing.setLastPaintSeqNum(paintSeq++);
					int rotation = thing.getRotation();
					if (thing.alpha > 0.1 && thing.alpha < 0.9) {
						g.setAlpha(thing.alpha);
						if (rotation != 0) {
							float halfWidth = image.getWidth() / 2;
							float halfHeight = image.getHeight() / 2;
							float newWidth = actorX + halfWidth;
							float newHeight = actorY + halfHeight;
							matrix.reset();
							matrix.preTranslate(newWidth, newHeight);
							matrix.preRotate(rotation);
							matrix.preTranslate(-newWidth, -newHeight);
							matrix.preTranslate(actorX, actorY);
							g.drawImage(image, matrix);
						} else {
							g.drawImage(image, actorX, actorY);
						}
						g.setAlpha(1.0F);
					} else {
						if (rotation != 0) {
							float halfWidth = image.getWidth() / 2;
							float halfHeight = image.getHeight() / 2;
							float newWidth = actorX + halfWidth;
							float newHeight = actorY + halfHeight;
							matrix.reset();
							matrix.preTranslate(newWidth, newHeight);
							matrix.preRotate(rotation);
							matrix.preTranslate(-newWidth, -newHeight);
							matrix.preTranslate(actorX, actorY);
							g.drawImage(image, matrix);
						} else {
							g.drawImage(image, actorX, actorY);
						}
					}
				}
				if (actorX == 0 && actorY == 0) {
					thing.draw(g);
				} else {
					g.translate(actorX, actorY);
					thing.draw(g);
					g.translate(-actorX, -actorY);
				}
			}
		}
	}

	public void centerOn(final Actor object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				/ 2 - object.getHeight() / 2);
	}

	public void topOn(final Actor object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, 0);
	}

	public void leftOn(final Actor object) {
		object.setLocation(0, getHeight() / 2 - object.getHeight() / 2);
	}

	public void rightOn(final Actor object) {
		object.setLocation(getWidth() - object.getWidth(), getHeight() / 2
				- object.getHeight() / 2);
	}

	public void bottomOn(final Actor object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				- object.getHeight());
	}

	public void centerOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				/ 2 - object.getHeight() / 2);
	}

	public void topOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, 0);
	}

	public void leftOn(final LObject object) {
		object.setLocation(0, getHeight() / 2 - object.getHeight() / 2);
	}

	public void rightOn(final LObject object) {
		object.setLocation(getWidth() - object.getWidth(), getHeight() / 2
				- object.getHeight() / 2);
	}

	public void bottomOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				- object.getHeight());
	}

	public void setTileBackground(String fileName) {
		setTileBackground(LImage.createImage(fileName));
	}

	public void setTileBackground(LImage image) {
		if (image == null) {
			return;
		}
		int layerWidth = getWidth();
		int layerHeight = getHeight();
		int tileWidth = image.getWidth();
		int tileHeight = image.getHeight();

		LImage background = LImage.createImage(layerWidth, layerHeight, false);
		LGraphics g = background.getLGraphics();
		for (int x = 0; x < layerWidth; x += tileWidth) {
			for (int y = 0; y < layerHeight; y += tileHeight) {
				g.drawImage(image, x, y);
			}
		}
		g.dispose();

		setBackground(background);
	}

	public int getScroll(RectBox visibleRect, int orientation, int direction) {
		int cellSize = this.getCellSize();
		double scrollPos = 0.0D;
		if (orientation == 0) {
			if (direction < 0) {
				scrollPos = visibleRect.getMinX();
			} else if (direction > 0) {
				scrollPos = visibleRect.getMaxX();
			}
		} else if (direction < 0) {
			scrollPos = visibleRect.getMinY();
		} else if (direction > 0) {
			scrollPos = visibleRect.getMaxY();
		}
		int increment = Math.abs((int) Math.IEEEremainder(scrollPos, cellSize));
		if (increment == 0) {
			increment = cellSize;
		}
		return increment;
	}

	protected void processTouchPressed() {
		if (!isTouchClick) {
			return;
		}
		if (!input.isMoving()) {
			int dx = this.input.getTouchX() - this.getScreenX();
			int dy = this.input.getTouchY() - this.getScreenY();
			dragActor = getSynchronizedObject(dx, dy);
			if (dragActor != null) {
				if (dragActor.isClick()) {
					dragActor.downClick(dx, dy);
				}
			}
			this.downClick(dx, dy);
		}
	}

	protected void processTouchReleased() {
		if (!isTouchClick) {
			return;
		}
		if (!input.isMoving()) {
			int dx = this.input.getTouchX() - this.getScreenX();
			int dy = this.input.getTouchY() - this.getScreenY();
			dragActor = getSynchronizedObject(dx, dy);
			if (dragActor != null) {
				if (dragActor.isClick()) {
					dragActor.upClick(dx, dy);
				}
			}
			this.upClick(dx, dy);
			this.dragActor = null;
		}
	}

	protected void processTouchEntered() {
		this.pressed = true;
	}

	protected void processTouchExited() {
		this.pressed = false;
	}

	protected void processKeyPressed() {
		if (this.isSelected()) {
			this.downKey();
		}
	}

	protected void processKeyReleased() {
		if (this.isSelected()) {
			this.upKey();
		}
	}

	protected void processTouchDragged() {
		int dropX = 0;
		int dropY = 0;
		if (!locked) {
			if (isNotMoveInScreen()) {
				return;
			}
			synchronized (Layer.class) {
				if (getContainer() != null) {
					getContainer().sendToFront(this);
				}
				dropX = this.input.getTouchDX();
				dropY = this.input.getTouchDY();
				this.move(dropX, dropY);
				this.drag(dropX, dropY);
			}
		} else {
			if (!actorDrag) {
				return;
			}
			dropX = this.input.getTouchX() - this.getScreenX();
			dropY = this.input.getTouchY() - this.getScreenY();
			if (dragActor == null) {
				dragActor = getSynchronizedObject(dropX, dropY);
			}
			if (dragActor != null && dragActor.isDrag()) {
				synchronized (dragActor) {
					objects.sendToFront(dragActor);
					RectBox rect = dragActor.getBoundingRect();
					int dx = dropX - (rect.width / 2);
					int dy = dropY - (rect.height / 2);
					if (dragActor.getLayer() != null) {
						dragActor.setLocation(dx, dy);
						dragActor.drag(dropX, dropY);
					}
				}
			}
		}
	}

	private boolean isNotMoveInScreen() {
		if (!this.isLimitMove) {
			return false;
		}
		int width = getWidth() - screenRect.width;
		int height = getHeight() - screenRect.height;
		int x = this.input.getTouchDX() + this.x();
		int y = this.input.getTouchDY() + this.y();
		int limitX = x + width;
		int limitY = y + height;
		if (getWidth() > screenRect.width) {
			if (limitX > width) {
				return true;
			} else if (limitX < 0) {
				return true;
			}
		} else {
			if (!screenRect.contains(x, y, getWidth(), getHeight())) {
				return true;
			}
		}
		if (getHeight() > screenRect.height) {
			if (limitY > height) {
				return true;
			} else if (limitY < 0) {
				return true;
			}
		} else {
			if (!screenRect.contains(x, y, getWidth(), getHeight())) {
				return true;
			}
		}
		return false;
	}

	public boolean isTouchPressed() {
		return this.pressed;
	}

	public boolean isActorDrag() {
		return actorDrag;
	}

	public void setActorDrag(boolean actorDrag) {
		this.actorDrag = actorDrag;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLimitMove() {
		return isLimitMove;
	}

	public void setLimitMove(boolean isLimitMove) {
		this.isLimitMove = isLimitMove;
	}

	public boolean isTouchClick() {
		return isTouchClick;
	}

	public void setTouchClick(boolean isTouchClick) {
		this.isTouchClick = isTouchClick;
	}

	protected void validateSize() {
		super.validateSize();
	}

	public String getUIName() {
		return "Layer";
	}

}
