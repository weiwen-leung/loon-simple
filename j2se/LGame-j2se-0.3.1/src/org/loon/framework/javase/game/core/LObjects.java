package org.loon.framework.javase.game.core;

import java.util.ArrayList;

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
public class LObjects {

	protected LObject[] lazyObjects;

	protected ArrayList<LObject> objects;

	protected ArrayList<LObject> pendingAdd;

	protected ArrayList<LObject> pendingRemove;

	public LObjects() {
		objects = new ArrayList<LObject>();
		pendingAdd = new ArrayList<LObject>();
		pendingRemove = new ArrayList<LObject>();
		lazyObjects = new LObject[] {};
	}

	public void update(long elapsedTime) {
		commits();
		for (LObject o : lazyObjects) {
			o.update(elapsedTime);
		}
	}

	public void commits() {
		boolean changes = false;
		final int additionCount = pendingAdd.size();
		if (additionCount > 0) {
			final Object[] additionsArray = pendingAdd.toArray();
			for (int i = 0; i < additionCount; i++) {
				LObject object = (LObject) additionsArray[i];
				objects.add(object);
			}
			pendingAdd.clear();
			changes = true;
		}
		final int removalCount = pendingRemove.size();
		if (removalCount > 0) {
			final Object[] removalsArray = pendingRemove.toArray();
			for (int i = 0; i < removalCount; i++) {
				LObject object = (LObject) removalsArray[i];
				objects.remove(object);
			}
			pendingRemove.clear();
			changes = true;
		}
		if (changes) {
			lazyObjects = objects.toArray(new LObject[] {});
		}
	}

	public LObject[] getObjects() {
		return lazyObjects;
	}

	public int getCount() {
		return lazyObjects.length;
	}

	public int getConcreteCount() {
		return lazyObjects.length + pendingAdd.size() - pendingRemove.size();
	}

	public LObject get(int index) {
		return lazyObjects[index];
	}

	/**
	 * 添加指定对象
	 * 
	 * @param object
	 */
	public void add(LObject object) {
		pendingAdd.add(object);
	}

	/**
	 * 删除指定对象
	 * 
	 * @param object
	 */
	public void remove(LObject object) {
		pendingRemove.add(object);
	}

	/**
	 * 删除所有对象
	 */
	public void removeAll() {
		final int count = objects.size();
		final Object[] objectArray = objects.toArray();
		for (int i = 0; i < count; i++) {
			pendingRemove.add((LObject) objectArray[i]);
		}
		pendingAdd.clear();
	}

	/**
	 * 查找一个制定名称的LObject对象
	 * 
	 * @param name
	 * @return
	 */
	public LObject getObjectByName(String name) {
		commits();
		for (LObject object : lazyObjects) {
			if (object != null) {
				if (object.name != null) {
					if (object.name.equals(name)) {
						return object;
					}
				}
			}
		}
		return null;
	}

}
