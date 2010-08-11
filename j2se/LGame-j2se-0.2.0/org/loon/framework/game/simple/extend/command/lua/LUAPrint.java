package org.loon.framework.game.simple.extend.command.lua;

import java.io.PrintStream;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaState;

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
public class LUAPrint extends JavaFunction {

	private PrintStream out;

	LuaState ls;

	public LUAPrint(LuaState ls) {
		this(System.out, ls);
	}

	public LUAPrint(PrintStream out, LuaState ls) {
		super(ls);
		this.out = out;
		this.ls = ls;
	}

	public int execute() {
		int line = ls.getTop();
		String message = ls.toString(line);
		out.println(message);
		return 0;
	}

}
