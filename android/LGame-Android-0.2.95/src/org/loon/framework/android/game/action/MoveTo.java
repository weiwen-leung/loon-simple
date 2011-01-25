package org.loon.framework.android.game.action;

import java.util.LinkedList;

import org.loon.framework.android.game.action.map.AStarFinder;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.action.map.shapes.Vector2D;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;

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
 * @version 0.1
 */
public class MoveTo extends ActionEvent {

	private Vector2D startLocation, endLocation;

	private Field2D layerMap;

	private boolean flag;

	private LinkedList<Vector2D> path;

	private int startX, startY, endX, endY, moveX, moveY;

	private int direction, speed;

	public MoveTo(final Field2D map, int x, int y, boolean flag) {
		this.startLocation = new Vector2D();
		this.endLocation = new Vector2D(x, y);
		this.layerMap = map;
		this.flag = flag;
		this.speed = 4;
	}

	public MoveTo(final Field2D map, Vector2D pos, boolean flag) {
		this(map, pos.x(), pos.y(), flag);
	}

	public void onLoad() {
		if (!original.getRectBox().contains(endLocation.x(), endLocation.y())) {
			path = AStarFinder.find(layerMap, startLocation.x()
					/ layerMap.getTileWidth(), startLocation.y()
					/ layerMap.getTileHeight(), endLocation.x()
					/ layerMap.getTileWidth(), endLocation.y()
					/ layerMap.getTileHeight(), flag);
		}
	}

	public void start(Actor target) {
		super.start(target);
		startLocation.set(target.getX(), target.getY());
	}

	public LinkedList<Vector2D> getPath() {
		return path;
	}

	public int getDirection() {
		return direction;
	}

	public Field2D getLayerMap() {
		return layerMap;
	}

	public void update(long elapsedTime) {
		if (endX == startX && endY == startY) {
			if (path.size() > 1) {
				Vector2D moveStart =  path.get(0);
				Vector2D moveEnd =  path.get(1);
				startX = moveStart.x() * layerMap.getTileWidth();
				startY = moveStart.y() * layerMap.getTileHeight();
				endX = moveEnd.x() * layerMap.getTileWidth();
				endY = moveEnd.y() * layerMap.getTileHeight();
				moveX = moveEnd.x() - moveStart.x();
				moveY = moveEnd.y() - moveStart.y();
				direction = Field2D.getDirection(moveX, moveY);
			}
			path.remove(0);
		}
		switch (direction) {
		case Field2D.TUP:
			startY -= speed;
			if (startY < endY) {
				startY = endY;
			}
			break;
		case Field2D.TDOWN:
			startY += speed;
			if (startY > endY) {
				startY = endY;
			}
			break;
		case Field2D.TLEFT:
			startX -= speed;
			if (startX < endX) {
				startX = endX;
			}
			break;
		case Field2D.TRIGHT:
			startX += speed;
			if (startX > endX) {
				startX = endX;
			}
			break;
		case Field2D.UP:
			startX += speed;
			startY -= speed;
			if (startX > endX) {
				startX = endX;
			}
			if (startY < endY) {
				startY = endY;
			}
			break;
		case Field2D.DOWN:
			startX -= speed;
			startY += speed;
			if (startX < endX) {
				startX = endX;
			}
			if (startY > endY) {
				startY = endY;
			}
			break;
		case Field2D.LEFT:
			startX -= speed;
			startY -= speed;
			if (startX < endX) {
				startX = endX;
			}
			if (startY < endY) {
				startY = endY;
			}
			break;
		case Field2D.RIGHT:
			startX += speed;
			startY += speed;
			if (startX > endX) {
				startX = endX;
			}
			if (startY > endY) {
				startY = endY;
			}
			break;
		}
		original.setLocation(startX, startY);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isComplete() {
		return path == null || path.size() == 0 || isComplete;
	}

}
