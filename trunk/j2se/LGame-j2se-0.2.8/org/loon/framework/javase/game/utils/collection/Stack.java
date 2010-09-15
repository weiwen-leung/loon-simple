package org.loon.framework.javase.game.utils.collection;

import org.loon.framework.javase.game.utils.CollectionUtils;

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
public class Stack {

	private Object[] stack;

	private int size;

	public Stack() {
		this(CollectionUtils.INITIAL_CAPACITY);
	}

	public Stack(int initialSize) {
		stack = new Object[initialSize];
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() <= 0;
	}

	public void push(Object bool) {
		ensureSize();
		stack[size++] = bool;
	}

	public Object pop() {
		if (size <= 0) {
			throw new ArrayIndexOutOfBoundsException(
					"Cannot pop, the stack is empty !");
		} else {
			Object temp = stack[--size];
			stack[size] = null;
			return temp;
		}
	}

	private void ensureSize() {
		if (stack.length == size) {
			stack = CollectionUtils.copyOf(stack, (size * 4) / 3);
		}
	}

	public void clear() {
		size = 0;
	}

	public Object peek() {
		return peek(size);
	}

	public Object peek(int index) {
		if (index < 0 || index >= size) {
			throw new ArrayIndexOutOfBoundsException("Invalid index given !");
		} else {
			return stack[index];
		}
	}

	public void compact() {
		stack = CollectionUtils.copyOf(stack, size);
	}

}
