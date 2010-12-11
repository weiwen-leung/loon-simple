package org.loon.framework.javase.game.action;

import java.util.HashMap;

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
public abstract class Rule {

	private HashMap map;

	/**
	 * 用于限制Action权限
	 * 
	 */
	public Rule() {
		map = new HashMap(20);
	}

	public void updateParameter(String parameter, Object value) {
		map.put(parameter, value);
	}

	public Object getParameter(String parameter) {
		return map.get(parameter);
	}

	/**
	 * 判断执行与否
	 * 
	 * @return
	 */
	public abstract boolean isRun();

}
