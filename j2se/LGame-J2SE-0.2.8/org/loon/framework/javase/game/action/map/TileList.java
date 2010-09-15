package org.loon.framework.javase.game.action.map;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class TileList {

	private List images;

	public TileList() {
		images = new ArrayList();
	}

	public void addImage(Image image) {
		images.add(image);
	}

	public Image elementAt(int mIndex) {
		if (mIndex > -1 && mIndex < images.size()) {
			return (Image) images.get(mIndex);
		}
		return null;
	}

	public int size() {
		return images.size();
	}

}