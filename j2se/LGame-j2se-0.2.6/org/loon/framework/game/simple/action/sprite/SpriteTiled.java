package org.loon.framework.game.simple.action.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.loon.framework.game.simple.action.map.RectBox;
import org.loon.framework.game.simple.core.LObject;
import org.loon.framework.game.simple.core.graphics.LImage;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.graphics.device.LGraphicsJava2D;
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
public class SpriteTiled extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int cellWidth;

	private int cellHeight;

	private boolean isVisible;

	private byte[][] grid;

	private Image[] tiles;

	private Image image;

	private int[] tileXPositions;

	private int[] tileYPositions;

	private byte[][] bufferGrid;

	private int bufferFirstColumn;

	private int bufferFirstRow;

	private int bufferLastColumn;

	private int bufferLastRow;

	private BufferedImage bufferImage;

	private LGraphics bufferGraphics;

	private byte[] animatedTiles;

	private int animatedTilesLength;

	private int numberOfTiles;

	private int numberOfColumns;

	private int gridColumns, gridRows;

	private int width, height;

	private float alpha;

	public SpriteTiled(Image image, int columns, int rows, int tileWidth,
			int tileHeight) {
		this.grid = new byte[columns][rows];
		this.gridColumns = columns;
		this.gridRows = rows;
		this.width = columns * tileWidth;
		this.height = rows * tileHeight;
		this.isVisible = true;
		setStaticTileSet(image, tileWidth, tileHeight);
	}

	public SpriteTiled(int columns, int rows, LImage image, int tileWidth,
			int tileHeight) {
		this(image.getBufferedImage(), columns, rows, tileWidth, tileHeight);
	}

	public SpriteTiled(String fileName, int columns, int rows, int tileWidth,
			int tileHeight) {
		this.grid = new byte[columns][rows];
		this.gridColumns = columns;
		this.gridRows = rows;
		this.width = columns * tileWidth;
		this.height = rows * tileHeight;
		this.isVisible = true;
		setStaticTileSet(GraphicsUtils.loadImage(fileName), tileWidth,
				tileHeight);
	}

	public void setStaticTileSet(Image image, int tileWidth, int tileHeight) {
		this.cellWidth = tileWidth;
		this.cellHeight = tileHeight;
		int columns = image.getWidth(null) / tileWidth;
		int rows = image.getHeight(null) / tileHeight;
		this.image = image;
		this.tileXPositions = new int[columns];
		int pos = 0;
		for (int i = 0; i < columns; i++) {
			this.tileXPositions[i] = pos;
			pos += tileWidth;
		}
		this.tileYPositions = new int[rows];
		pos = 0;
		for (int i = 0; i < rows; i++) {
			this.tileYPositions[i] = pos;
			pos += tileHeight;
		}
		Image[] tileImages = new Image[columns * rows];
		int index = 0;
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Image tile = GraphicsUtils.createImage(tileWidth, tileHeight,
						true);
				Graphics g = tile.getGraphics();
				g.drawImage(image, -1 * column * tileWidth, -1 * row
						* tileHeight, null);
				g.dispose();
				tileImages[index] = tile;
				index++;
			}
		}
		this.tiles = tileImages;
		if (columns * rows < this.numberOfTiles) {
			for (int i = 0; i < this.grid.length; i++) {
				for (int j = 0; j < this.grid[i].length; j++) {
					this.grid[i][j] = 0;
				}
			}
		}
		this.numberOfTiles = columns * rows;
		this.numberOfColumns = columns;
	}

	public int createAnimatedTile(int staticTileIndex) {
		if (staticTileIndex >= this.numberOfTiles) {
			throw new IllegalArgumentException("invalid static tile index: "
					+ staticTileIndex + " (there are only ["
					+ this.numberOfTiles + "] tiles available.");
		}
		if (this.animatedTiles == null) {
			this.animatedTiles = new byte[4];
		} else if (this.animatedTilesLength == this.animatedTiles.length) {
			byte[] newAnimatedTiles = new byte[this.animatedTilesLength * 2];
			System.arraycopy(this.animatedTiles, 0, newAnimatedTiles, 0,
					this.animatedTilesLength);
			this.animatedTiles = newAnimatedTiles;
		}

		this.animatedTiles[this.animatedTilesLength] = (byte) staticTileIndex;
		this.animatedTilesLength++;
		return -1 * this.animatedTilesLength;
	}

	public void setAnimatedTile(int animatedTileIndex, int staticTileIndex) {
		if (staticTileIndex > this.numberOfTiles) {
			throw new IllegalArgumentException("invalid static tile index: "
					+ staticTileIndex + " (there are only ["
					+ this.numberOfTiles + "] tiles available.");
		}
		int animatedIndex = (-1 * animatedTileIndex) - 1;
		this.animatedTiles[animatedIndex] = (byte) staticTileIndex;
	}

	public int getAnimatedTile(int animatedTileIndex) {
		int animatedIndex = (-1 * animatedTileIndex) - 1;
		return this.animatedTiles[animatedIndex];
	}

	public void setCell(int col, int row, int tileIndex) {
		if (tileIndex > this.numberOfTiles) {
			throw new IllegalArgumentException("invalid static tile index: "
					+ tileIndex + " (there are only [" + this.numberOfTiles
					+ "] tiles available.");

		}
		this.grid[col][row] = (byte) tileIndex;

	}

	public int getCell(int col, int row) {
		return this.grid[col][row];
	}

	public void fillCells(int col, int row, int numCols, int numRows,
			int tileIndex) {
		if (tileIndex > this.numberOfTiles) {
			throw new IllegalArgumentException("invalid static tile index: "
					+ tileIndex + " (there are only [" + this.numberOfTiles
					+ "] tiles available.");
		}
		int endCols = col + numCols;
		int endRows = row + numRows;
		for (int i = col; i < endCols; i++) {
			for (int j = row; j < endRows; j++) {
				this.grid[i][j] = (byte) tileIndex;
			}
		}
	}

	public final int getCellWidth() {
		return this.cellWidth;
	}

	public final int getCellHeight() {
		return this.cellHeight;
	}

	public final int getColumns() {
		return this.gridColumns;
	}

	public final int getRows() {
		return this.gridRows;
	}

	public void createUI(LGraphics g) {
		if (!this.isVisible) {
			return;
		}

		if (alpha > 0 && alpha < 1.0) {
			g.setAlpha(alpha);
		}
		Rectangle rect = g.getClipBounds();
		int clipX = rect.x;
		int clipY = rect.y;
		int clipWidth = rect.width;
		int clipHeight = rect.height;

		int firstColumn = 0;
		int lastColumn;
		if (this.x() < clipX) {
			firstColumn = (clipX - this.x()) / this.cellWidth;
			lastColumn = ((clipX - this.x() + clipWidth) / this.cellWidth) + 1;

		} else {
			lastColumn = (clipWidth / this.cellWidth) + 1;
		}
		if (lastColumn > this.gridColumns) {
			lastColumn = this.gridColumns;
		}

		int firstRow = 0;
		int lastRow;
		if (this.y() < clipY) {
			firstRow = (clipY - this.y()) / this.cellHeight;
			lastRow = ((clipY - this.y() + clipHeight) / this.cellHeight) + 1;
		} else {
			lastRow = (clipHeight / this.cellHeight) + 1;
		}
		if (lastRow > this.gridRows) {
			lastRow = this.gridRows;
		}

		int xStart = this.x() + (firstColumn * this.cellWidth);
		int yStart = this.y() + (firstRow * this.cellHeight);

		int y = yStart;
		int x = xStart;

		byte[][] gridTable = this.grid;

		x = 0;
		y = 0;

		boolean clearBuffer = false;
		if (this.bufferGrid == null) {
			clearBuffer = true;
			this.bufferGrid = new byte[this.gridColumns][this.gridRows];
			this.bufferImage = GraphicsUtils.createImage(this.cellWidth
					* ((clipWidth / this.cellWidth) + 2), this.cellHeight
					* ((clipHeight / this.cellHeight) + 2));
			this.bufferGraphics = new LGraphicsJava2D(this.bufferImage);
			this.bufferFirstColumn = firstColumn;
			this.bufferFirstRow = firstRow;
		} else if (this.bufferFirstColumn != firstColumn
				|| this.bufferFirstRow != firstRow) {

			clearBuffer = true;
			this.bufferFirstColumn = firstColumn;
			this.bufferFirstRow = firstRow;
		}
		LGraphics originalGraphics = g;
		g = this.bufferGraphics;
		g.setColor(Color.black);
		if (clearBuffer) {
			g.fillRect(0, 0, this.bufferImage.getWidth(), this.bufferImage
					.getHeight());
		}
		for (int column = firstColumn; column < lastColumn; column++) {
			byte[] gridColumn = gridTable[column];
			for (int row = firstRow; row < lastRow; row++) {
				int cellIndex = gridColumn[row];
				if (cellIndex != 0) {

					int tileIndex;
					if (cellIndex < 0) {
						tileIndex = this.animatedTiles[(-1 * cellIndex) - 1] - 1;
					} else {
						tileIndex = cellIndex - 1;
					}

					if (!clearBuffer
							&& tileIndex == this.bufferGrid[column][row]) {

						if (!(row >= this.bufferLastRow || column >= this.bufferLastColumn)) {
							y += this.cellHeight;
							continue;
						}
					}

					g.setClip(x, y, this.cellWidth, this.cellHeight);

					g.clipRect(clipX, clipY, clipWidth, clipHeight);

					int tileColumn = tileIndex % this.numberOfColumns;
					int tileRow = tileIndex / this.numberOfColumns;
					int tileX = x - this.tileXPositions[tileColumn];
					int tileY = y - this.tileYPositions[tileRow];
					g.drawImage(this.image, tileX, tileY);

					Image tile = this.tiles[tileIndex];

					g.drawImage(tile, x, y);

					this.bufferGrid[column][row] = (byte) tileIndex;

				} else if (this.bufferGrid[column][row] != 0) {

					g.fillRect(x, y, this.cellWidth, this.cellHeight);
					this.bufferGrid[column][row] = 0;
				}

				y += this.cellHeight;
			}
			y = 0;

			y = yStart;

			x += this.cellWidth;
		}

		originalGraphics.drawImage(this.bufferImage, xStart, yStart);

		this.bufferLastColumn = lastColumn;
		this.bufferLastRow = lastRow;

		g.setClip(clipX, clipY, clipWidth, clipHeight);
		if (alpha > 0 && alpha < 1.0) {
			g.setAlpha(0);
		}
	}

	public int getTileAt(int x, int y) {
		int column = (x - this.x()) / this.cellWidth;
		int row = (y - this.y()) / this.cellHeight;
		if (column < 0 || column >= this.gridColumns || row < 0
				|| row >= this.gridRows) {
			return 0;
		} else {
			return this.grid[column][row];
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void update(long timer) {

	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public RectBox getCollisionBox() {
		return new RectBox(Math.round(getLocation().x()), Math
				.round(getLocation().y()), width, height);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}

}
