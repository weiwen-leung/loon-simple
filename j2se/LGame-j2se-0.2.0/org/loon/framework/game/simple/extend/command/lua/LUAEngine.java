package org.loon.framework.game.simple.extend.command.lua;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.resource.Resources;
import org.loon.framework.game.simple.utils.FileUtils;

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
public class LUAEngine {

	private static LuaState lua;

	private static boolean init;

	/**
	 * 启动LUA引擎
	 * 
	 */
	public static void begine() {
		if (lua == null) {
			if (LSystem.isWindows() && !init && !LSystem.isApplet) {
				LUA_LIB.loadLibrary("lua5.1.dll");
				LUA_LIB.loadLibrary("luajava-1.1.dll");
			} else {
				init = false;
				throw new RuntimeException(
						"Sorry,The current OS does not support this feature !");
			}
			init = true;
			lua = LuaStateFactory.newLuaState();
			lua.openLibs();
			LUAPrint jprintln = new LUAPrint(lua);
			try {
				jprintln.register("println");
			} catch (LuaException e) {
				throw new RuntimeException(
						"Failed to register println() in Lua !"
								+ e.getMessage());
			}
		}
	}

	/**
	 * 关闭运行中的lua实例
	 * 
	 */
	public static void close() {
		if (!lua.isClosed()) {
			lua.close();
		}
	}

	/**
	 * 返回当前的lua引擎对象
	 * 
	 * @return
	 */
	public static LuaState getLua() {
		return lua;
	}

	/**
	 * 清空lua脚本实例
	 * 
	 */
	public static void reset() {
		LUAEngine.close();
		lua = null;
	}

	/**
	 * 运行指定的lua脚本
	 * 
	 * @param file
	 * @return
	 */
	public static int runLuaFile(String fileName) {
		return lua.LdoString(FileUtils.readToString(Resources
				.getResourceToInputStream(fileName)));
	}

	/**
	 * 运行lua命令，并返回成功与否
	 * 
	 * @param command
	 * @return
	 */
	public static boolean runLuaString(String command) {
		return lua.LdoString(command) == 1 ? true : false;
	}

	/**
	 * 运行一个指定的lua函数
	 * 
	 * @param command
	 */
	public static void executeLuaFunction(String command) {
		lua.getGlobal(command);
		lua.call(0, 0);
	}

	/**
	 * 传递字符串参数给lua脚本
	 * 
	 * @param command
	 * @param args
	 */
	public static void executeLuaFunction(String command, String[] args) {
		lua.getGlobal(command);
		for (int i = 0; i < args.length; i++) {
			lua.pushString(args[i]);
		}
		lua.call(args.length, 0);
	}

	/**
	 * 传递对象参数给lua脚本
	 * 
	 * @param command
	 * @param args
	 */
	public static void executeLuaFunction(String command, Object[] args) {
		lua.getGlobal(command);
		for (int i = 0; i < args.length; i++) {
			lua.pushJavaObject(args[i]);
		}
		lua.call(args.length, 0);
	}

	/**
	 * 运行指定lua脚本函数,并返回一个String
	 * 
	 * @param command
	 * @return
	 */
	public static String executeLuaFunctionWithStringReturn(String command) {
		lua.getGlobal(command);
		lua.call(0, 1);
		return lua.toString(-1);
	}

	/**
	 * 调用指定脚本参数的字符串列表并返回String
	 * 
	 * @param command
	 * @param args
	 * @return
	 */
	public static String executeLuaFunctionWithStringReturn(String command,
			String[] args) {
		lua.getGlobal(command);
		for (int i = 0; i < args.length; i++) {
			lua.pushString(args[i]);
		}
		lua.call(args.length, 1);
		return lua.toString(-1);
	}

	/**
	 * 调用指定脚本参数的对象列表并返回String
	 * 
	 * @param command
	 * @param args
	 * @return
	 */
	public static String executeLuaFunctionWithStringReturn(String command,
			Object[] args) {
		lua.getGlobal(command);
		for (int i = 0; i < args.length; i++) {
			lua.pushJavaObject(args[i]);
		}
		lua.call(args.length, 1);
		return lua.toString(-1);
	}

	/**
	 * 查找指定名称的lua脚本变量
	 * 
	 * @param name
	 * @return
	 */
	public static String getGlobalString(String name) {
		lua.getGlobal(name);
		String var = lua.toString(-1);
		lua.pop(1);
		return var;
	}

}
