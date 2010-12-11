package org.loon.framework.android.game.action.map.shapes;

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
public class Point2D {

	public float x;
	public float y;
	public int type;

	public static final int POINT_CONVEX = 1;
	public static final int POINT_CONCAVE = 2;

	public Point2D(float x, float y) {

		this.x = x;
		this.y = y;
	}

	public boolean equals(Object obj) {

		Point2D p = (Point2D) obj;
		if (p.x == this.x && p.y == this.y && p.type == this.type) {

			return true;
		} else {
			return false;
		}

	}

}
