package org.loon.framework.javase.game.core.graphics.window.actor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;

import org.loon.framework.javase.game.action.map.Field2D;
import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.filter.ImageFilterFactory;
import org.loon.framework.javase.game.core.graphics.filter.ImageFilterType;
import org.loon.framework.javase.game.core.timer.LTimer;
import org.loon.framework.javase.game.utils.GraphicsUtils;

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

	private static final AffineTransform atform = new AffineTransform();

	protected boolean visible, actorDrag, pressed;

	private Actor dragActor;

	private LTimer timer = new LTimer(0);

	private boolean isMouseClick;

	private ImageFilterFactory factory;

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
		this.actorDrag = true;
		this.visible = true;
		this.customRendering = true;
		this.isLimitMove = true;
		this.isMouseClick = true;
		this.factory = ImageFilterFactory.getInstance();
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
			boolean isListener = false;
			Iterator iter = objects.iterator();
			Actor thing = null;
			while (iter.hasNext()) {
				thing = (Actor) iter.next();
				if (!thing.isVisible()) {
					continue;
				}
				isListener = (thing.actorListener != null);
				thing.update(elapsedTime);
				if (isListener) {
					thing.actorListener.update(elapsedTime);
				}
				RectBox rect = thing.getRectBox();
				int actorX = minX + thing.x;
				int actorY = minY + thing.y;
				int actorWidth = rect.getWidth();
				int actorHeight = rect.getHeight();
				if (actorX + actorWidth < minX || actorX > maxX
						|| actorY + actorHeight < minY || actorY > maxY) {
					continue;
				}
				LImage actorImage = thing.getImage();
				if (actorImage != null) {
					thing.setLastPaintSeqNum(paintSeq++);
					boolean isBitmapFilter = ImageFilterType.NoneFilter != thing.filterType;
					Image bitmap = actorImage.getBufferedImage();
					if (isBitmapFilter) {
						bitmap = factory.doFilter(bitmap, thing.filterType);
					}
					int rotation = thing.getRotation();
					if (thing.alpha < 1.0) {
						g.setAlpha(thing.alpha);
					}
					if (rotation != 0) {
						double halfWidth = actorImage.getWidth() / 2;
						double halfHeight = actorImage.getHeight() / 2;
						double newWidth = actorX + halfWidth;
						double newHeight = actorY + halfHeight;
						atform.setToIdentity();
						atform.translate(newWidth, newHeight);
						atform.rotate(Math.toRadians(rotation));
						atform.translate(-newWidth, -newHeight);
						atform.translate(actorX, actorY);
						g.drawImage(bitmap, atform);
					} else {
						g.drawImage(bitmap, actorX, actorY);
					}
					if (isBitmapFilter) {
						bitmap.flush();
						bitmap = null;
					}
					if (thing.alpha < 1.0) {
						g.setAlpha(1.0F);
					}
				}
				if (actorX == 0 && actorY == 0) {
					thing.draw(g);
					if (isListener) {
						thing.actorListener.draw(g);
					}
				} else {
					g.translate(actorX, actorY);
					thing.draw(g);
					if (isListener) {
						thing.actorListener.draw(g);
					}
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

	public void setField2DBackground(Field2D field, HashMap pathMap) {
		setField2DBackground(field, pathMap, null);
	}

	public void setField2DBackground(Field2D field, HashMap pathMap,
			String fileName) {
		setField2D(field);
		Image background = null;
		if (fileName != null) {
			setTileBackground(fileName);
			background = getBackground();
		} else {
			background = GraphicsUtils.createImage(getWidth(), getHeight(),
					false);
		}
		Graphics g = background.getGraphics();
		for (int i = 0; i < field.getWidth(); i++) {
			for (int j = 0; j < field.getHeight(); j++) {
				int index = field.getType(j, i);
				Object o = pathMap.get(new Integer(index));
				if (o != null) {
					if (o instanceof LImage) {
						g.drawImage(((LImage) o).getBufferedImage(), field
								.tilesToWidthPixels(i), field
								.tilesToHeightPixels(j), null);
					} else if (o instanceof Actor) {
						addObject(((Actor) o), field.tilesToWidthPixels(i),
								field.tilesToHeightPixels(j));
					}
				}
			}
		}
		g.dispose();
		setBackground(background);
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

		setBackground(background.getBufferedImage());
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

	public Actor getClickActor() {
		return dragActor;
	}

	protected void processMousePressed() {
		if (!isMouseClick) {
			return;
		}
		if (this.input.getMousePressed() == MouseEvent.BUTTON1) {
			int dx = this.input.getMouseX() - this.getScreenX();
			int dy = this.input.getMouseY() - this.getScreenY();
			dragActor = getSynchronizedObject(dx, dy);
			if (dragActor != null) {
				if (dragActor.isClick()) {
					dragActor.downClick(dx, dy);
					if (dragActor.actorListener != null) {
						dragActor.actorListener.downClick(dx, dy);
					}
				}
			}
			this.downClick(dx, dy);
		}
	}

	protected void processMouseReleased() {
		if (!isMouseClick) {
			return;
		}
		if (this.input.getMouseReleased() == MouseEvent.BUTTON1) {
			int dx = this.input.getMouseX() - this.getScreenX();
			int dy = this.input.getMouseY() - this.getScreenY();
			dragActor = getSynchronizedObject(dx, dy);
			if (dragActor != null) {
				if (dragActor.isClick()) {
					dragActor.upClick(dx, dy);
					if (dragActor.actorListener != null) {
						dragActor.actorListener.upClick(dx, dy);
					}
				}
			}
			this.upClick(dx, dy);
			this.dragActor = null;
		}
	}

	public void go(int x, int y) {
		if (isNotMoveInScreen(x, y)) {
			setLocation(x - getWidth() / 2, y - getHeight() / 2);
		}
	}

	protected void processMouseEntered() {
		this.pressed = true;
	}

	protected void processMouseExited() {
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

	protected void processMouseDragged() {
		int dropX = 0;
		int dropY = 0;
		if (!locked) {
			boolean moveActor = false;
			if (actorDrag) {
				synchronized (objects) {
					dropX = this.input.getMouseX() - this.getScreenX();
					dropY = this.input.getMouseY() - this.getScreenY();
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
								if (dragActor.actorListener != null) {
									dragActor.actorListener.drag(dropX, dropY);
								}
							}
							moveActor = true;
						}
					}
				}
			}
			if (!moveActor) {
				if (isNotMoveInScreen(this.input.getMouseDX() + this.x(),
						this.input.getMouseDY() + this.y())) {
					return;
				}
				if (getContainer() != null) {
					getContainer().sendToFront(this);
				}
				dropX = this.input.getMouseDX();
				dropY = this.input.getMouseDY();
				this.move(dropX, dropY);
				this.drag(dropX, dropY);
			}
		} else {
			if (!actorDrag) {
				return;
			}
			synchronized (objects) {
				dropX = this.input.getMouseX() - this.getScreenX();
				dropY = this.input.getMouseY() - this.getScreenY();
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
							if (dragActor.actorListener != null) {
								dragActor.actorListener.drag(dropX, dropY);
							}
						}
					}
				}
			}
		}
	}

	public void moveCamera(Actor actor) {
		moveCamera(actor.getX(), actor.getY());
	}

	public boolean isMousePressed() {
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

	public boolean isMouseClick() {
		return isMouseClick;
	}

	public void setMouseClick(boolean isMouseClick) {
		this.isMouseClick = isMouseClick;
	}

	public int getLayerMouseX() {
		return this.input.getMouseX() - this.getScreenX();
	}

	public int getLayerMouseY() {
		return this.input.getMouseY() - this.getScreenY();
	}

	protected void validateSize() {
		super.validateSize();
	}

	public String getUIName() {
		return "Layer";
	}

}
