package org.loon.framework.android.game.action.map;

import java.util.ArrayList;
import java.util.List;

import org.loon.framework.android.game.core.graphics.LImage;
/**
 * 
 * Copyright 2008 - 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng  
 * @email：ceponline@yahoo.com.cn 
 * @version 0.1
 */
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