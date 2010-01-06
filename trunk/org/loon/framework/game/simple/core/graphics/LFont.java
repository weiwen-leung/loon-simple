package org.loon.framework.game.simple.core.graphics;

import java.awt.Graphics2D;

/**
 * Copyright 2008 - 2009
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
public interface LFont {

	    public abstract int drawString(Graphics2D g, String s, int i, int j);

	    public abstract int drawString(Graphics2D g, String s, int i, int j, int k, int l);

	    public abstract int drawText(Graphics2D g, String s, int i, int j, int k, int l, int i1, 
	            int j1);

	    public abstract int getWidth(String s);

	    public abstract int getWidth(char c);

	    public abstract int getHeight();

	    public abstract boolean isAvailable(char c);

	    public static final int LEFT = 1;
	    public static final int RIGHT = 2;
	    public static final int CENTER = 3;
	    public static final int JUSTIFY = 4;
	
}

