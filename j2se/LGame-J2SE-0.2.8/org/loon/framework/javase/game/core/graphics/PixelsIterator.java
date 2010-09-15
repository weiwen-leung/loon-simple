package org.loon.framework.javase.game.core.graphics;

import java.util.Iterator;

public class PixelsIterator implements Iterator {

	final private int[][] items;

	private int index = 0;
	
	private int size = 0;

	public PixelsIterator(final int[][] items) {
			this.items = items;
			this.size = items.length;
	}

	public boolean hasNext() {
		return index < size;
	}

	public Object next() {
			return items[index++];
	}

	public void remove() {
	}

}
