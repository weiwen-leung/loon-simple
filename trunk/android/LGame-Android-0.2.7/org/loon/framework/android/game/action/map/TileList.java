package org.loon.framework.android.game.action.map;

import java.util.ArrayList;
import java.util.List;

import org.loon.framework.android.game.core.graphics.LImage;

public class TileList {

	private List<LImage> images;

	public TileList() {
		images = new ArrayList<LImage>();
	}

	public void addImage(LImage image) {
		images.add(image);
	}

	public LImage elementAt(int mIndex) {
		if (mIndex > -1 && mIndex < images.size()) {
			return images.get(mIndex);
		}
		return null;
	}

	public int size() {
		return images.size();
	}

}