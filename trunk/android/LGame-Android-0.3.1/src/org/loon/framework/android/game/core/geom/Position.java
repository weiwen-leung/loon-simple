package org.loon.framework.android.game.core.geom;

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
public class Position {

	public int x = 0;

	public int y = 0;

	public Position() {
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position position) {
		x = position.x;
		y = position.y;
	}

	public  void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	public  void setPosition(Position position) {
		setPosition(position.x, position.y);
	}

	public  void setX(int x) {
		this.x = x;
	}

	public  void setY(int y) {
		this.y = y;
	}

	public  int getX() {
		return x;
	}

	public  int getY() {
		return y;
	}

	public  void modX(int mod) {
		x += mod;
	}

	public  void modY(int mod) {
		y += mod;
	}

	public  void modPosition(int xMod, int yMod) {
		x += xMod;
		y += yMod;
	}
}
