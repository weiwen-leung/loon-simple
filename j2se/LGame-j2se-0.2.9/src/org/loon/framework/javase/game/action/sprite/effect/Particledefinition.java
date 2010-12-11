package org.loon.framework.javase.game.action.sprite.effect;

import java.awt.Color;

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
public class Particledefinition {

	public float dx,dy;

	public int maxLife,minLife;

	public Color colour;

	public float hue;

	public float saturation;

	public float brightness;

	public boolean over;

	public Particledefinition() {
		dx = 0.0F;
		dy = 0.0F;
		maxLife = 0;
		minLife = 0;
		over = false;
	}

}
