package org.loon.framework.game.simple.core.graphics;

import java.awt.Graphics;

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
public class MatrixImage {

	private int width, height;

	private int[][] matrix;

	public MatrixImage(int w, int h) {
		width = w;
		height = h;
		matrix = new int[w][h];
	}

	public MatrixImage crop(int x, int y, int w, int h) {
		MatrixImage matriximage = new MatrixImage(w, h);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++)
				matriximage.matrix[i][j] = matrix[x + i][y + j];

		}

		return matriximage;
	}

	public void setColor(int x, int y, int pixel) {
		matrix[x][y] = pixel;
	}

	public void draw(Graphics g, int x, int y) {
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++)
				if (matrix[w][h] != -1) {
					g.setColor(LColor.getColor(matrix[w][h]));
					g.drawRect(x + w, y + h, 1, 1);
				}
		}
	}

	public void draw(Graphics g) {
		draw(g, 0, 0);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
