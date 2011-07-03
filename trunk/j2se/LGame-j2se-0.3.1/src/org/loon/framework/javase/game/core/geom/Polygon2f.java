package org.loon.framework.javase.game.core.geom;

import org.loon.framework.javase.game.utils.CollectionUtils;

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
public class Polygon2f {

	public int npoints;

	public float[] xpoints;

	public float[] ypoints;

	private static final int MIN_LENGTH = 4;

	public Polygon2f() {
		xpoints = new float[MIN_LENGTH];
		ypoints = new float[MIN_LENGTH];
	}

	public Polygon2f(float xpoints[], float ypoints[], int npoints) {
		if (npoints > xpoints.length || npoints > ypoints.length) {
			throw new IndexOutOfBoundsException("npoints > xpoints.length || "
					+ "npoints > ypoints.length");
		}
		if (npoints < 0) {
			throw new NegativeArraySizeException("npoints < 0");
		}
		this.npoints = npoints;
		this.xpoints = CollectionUtils.copyOf(xpoints, npoints);
		this.ypoints = CollectionUtils.copyOf(ypoints, npoints);
	}

	public void addPoint(float x, float y) {
		if (npoints >= xpoints.length || npoints >= ypoints.length) {
			int newLength = (npoints * 2);
			if (newLength < MIN_LENGTH) {
				newLength = MIN_LENGTH;
			} else if ((newLength & (newLength - 1)) != 0) {
				newLength = Integer.highestOneBit(newLength);
			}
			xpoints = CollectionUtils.copyOf(xpoints, newLength);
			ypoints = CollectionUtils.copyOf(ypoints, newLength);
		}
		xpoints[npoints] = x;
		ypoints[npoints] = y;
		npoints++;
	}

	public float[] getVertices() {
		int vertice_size = xpoints.length * 2;
		float[] verts = new float[vertice_size];
		for (int i = 0, j = 0; i < vertice_size; i += 2, j++) {
			verts[i] = xpoints[j];
			verts[i + 1] = ypoints[j];
		}
		return verts;
	}

	public void reset() {
		npoints = 0;
		xpoints = new float[MIN_LENGTH];
		ypoints = new float[MIN_LENGTH];
	}
}
