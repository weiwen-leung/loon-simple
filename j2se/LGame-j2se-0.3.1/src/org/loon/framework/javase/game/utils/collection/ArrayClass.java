package org.loon.framework.javase.game.utils.collection;

import java.lang.reflect.Array;

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

@SuppressWarnings("unchecked")
public class ArrayClass<T> {

	private static int CAPACITY_SIZE = 40;

	private static int CAPACITY_GROWTH_RATE = 2;

	public T[] items;

	Class<?> type;

	public int size;

	public ArrayClass(Class<?> type) {
		this(type, CAPACITY_SIZE);
	}

	public ArrayClass(Class<?> type, int initialSize) {
		this.type = type;
		this.items = newArray(initialSize);
	}

	public void add(T object) {
		if (contains(object)) {
			return;
		}
		if (size == items.length) {
			update();
		}
		items[size] = object;
		size++;
	}

	public boolean contains(T object) {
		for (int i = 0; i < size; i++) {
			if (items[i] == object) {
				return true;
			}
		}
		return false;
	}

	public boolean remove(T object) {
		for (int i = 0; i < size; i++) {
			if (items[i] == object) {
				for (int j = i; j < size - 1; j++) {
					items[j] = items[j + 1];
				}
				size--;
				return true;
			}
		}
		return false;
	}

	public void clear() {
		size = 0;
	}

	public void reset() {
		items = newArray(CAPACITY_SIZE);
		size = 0;
	}

	private T[] newArray(int size) {
		return (T[]) Array.newInstance(type, size);
	}

	private void update() {
		int newSize = items.length * CAPACITY_GROWTH_RATE;
		T[] newArray = newArray(newSize);
		for (int i = 0; i < items.length; i++) {
			newArray[i] = items[i];
		}
		this.items = newArray;
	}

	public static <T> ArrayClass<T> create(Class<T> clazz) {
		return new ArrayClass<T>(clazz);
	}

	public static <T> ArrayClass<T> create(Class<T> clazz, int size) {
		return new ArrayClass<T>(clazz, size);
	}
}
