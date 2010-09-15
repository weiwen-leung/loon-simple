package org.loon.framework.javase.game.action.map.shapes;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 * Copyright 2008 - 2010
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
public abstract class Matrix implements Serializable {

	public Matrix() {
		super();
	}

	public abstract Matrix setIdentity();

	public abstract Matrix invert();

	public abstract Matrix load(FloatBuffer buf);

	public abstract Matrix loadTranspose(FloatBuffer buf);

	public abstract Matrix negate();

	public abstract Matrix store(FloatBuffer buf);

	public abstract Matrix storeTranspose(FloatBuffer buf);

	public abstract Matrix transpose();

	public abstract Matrix setZero();

	public abstract float determinant();

}
