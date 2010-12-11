package org.loon.framework.javase.game.action.map;

import java.awt.Point;
import java.awt.Rectangle;

import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.action.map.shapes.Vector2D;
import org.loon.framework.javase.game.action.sprite.AbstractBackground;
import org.loon.framework.javase.game.action.sprite.ISprite;

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
public class Camera {

	private Vector2D location;

	private int width, height;

	private RectBox bounds;

	public Camera(int width, int height) {
		this.location = new Vector2D(0, 0);
		this.width = width;
		this.height = height;
	}

	public Camera(int x, int y, int width, int height) {
		this.location = new Vector2D(0, 0);
		this.width = width;
		this.height = height;
	}

	public Camera(AbstractBackground background) {
		this(background.x(), background.y(), background.getWidth(), background
				.getHeight());
	}

	public void setLocation(int x, int y) {
		location.setLocation(x, y);
	}

	public int getXover() {
		return location.x();
	}

	public int getYover() {
		return location.y();
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

	public void setWidth(int setter) {
		width = setter;
	}

	public void setHeight(int setter) {
		height = setter;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void move_45D_up() {
		move_45D_up(1);
	}

	public void move_45D_up(int multiples) {
		location.move_multiples(Field2D.UP, multiples);
	}

	public void move_45D_left() {
		move_45D_left(1);
	}

	public void move_45D_left(int multiples) {
		location.move_multiples(Field2D.LEFT, multiples);
	}

	public void move_45D_right() {
		move_45D_right(1);
	}

	public void move_45D_right(int multiples) {
		location.move_multiples(Field2D.RIGHT, multiples);
	}

	public void move_45D_down() {
		move_45D_down(1);
	}

	public void move_45D_down(int multiples) {
		location.move_multiples(Field2D.DOWN, multiples);
	}

	public void move_up() {
		move_up(1);
	}

	public void move_up(int multiples) {
		location.move_multiples(Field2D.TUP, multiples);
	}

	public void move_left() {
		move_left(1);
	}

	public void move_left(int multiples) {
		location.move_multiples(Field2D.TLEFT, multiples);
	}

	public void move_right() {
		move_right(1);
	}

	public void move_right(int multiples) {
		location.move_multiples(Field2D.TRIGHT, multiples);
	}

	public void move_down() {
		move_down(1);
	}

	public void move_down(int multiples) {
		location.move_multiples(Field2D.TDOWN, multiples);
	}

	public void move(Vector2D vector2D) {
		location.move(vector2D);
	}

	public void move(double x, double y) {
		location.move(x, y);
	}

	public void setLocation(double x, double y) {
		location.setLocation(x, y);
	}

	public int x() {
		return (int) location.getX();
	}

	public int y() {
		return (int) location.getY();
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public void setX(Integer x) {
		location.setX(x.intValue());
	}

	public void setX(double x) {
		location.setX(x);
	}

	public void setY(Integer y) {
		location.setY(y.intValue());
	}

	public void setY(double y) {
		location.setY(y);
	}

	public Vector2D getLocation() {
		return location;
	}

	public void setLocation(Vector2D location) {
		this.location = location;
	}

	public boolean isOnScreen(RectBox r) {
		if (bounds == null) {
			bounds = new RectBox(location.x(), location.y(), width, height);
		} else {
			bounds.setBounds(location.x(), location.y(), width, height);
		}
		if (bounds.intersects(r) || bounds.contains(r)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOnScreen(ISprite r) {
		return isOnScreen(r.getCollisionBox());
	}

	public void setClip(int x, int y) {
		this.setLocation(x, y);
	}

	public void setClip(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.width = height;
	}

	public void setClip(Rectangle r) {
		this.setLocation(r.x, r.y);
		this.width = r.width;
		this.width = r.height;
	}

	public void setClip(RectBox r) {
		this.setLocation(r.x, r.y);
		this.width = r.width;
		this.width = r.height;
	}

	public RectBox getClip() {
		if (bounds == null) {
			bounds = new RectBox(location.x(), location.y(), width, height);
		} else {
			bounds.setBounds(location.x(), location.y(), width, height);
		}
		return bounds;
	}

	public void translate(int x, int y) {
		location.move(x, y);
	}

	public void centerOn(Point p) {
		location.setLocation(p.x - (width / 2), p.y - (height / 2));
	}

	public void centerOn(int x, int y) {
		location.setLocation(x - (width / 2), y - (height / 2));
	}


}
