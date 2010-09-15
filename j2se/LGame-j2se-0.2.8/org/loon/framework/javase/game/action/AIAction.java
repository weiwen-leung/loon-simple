package org.loon.framework.javase.game.action;

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
public class AIAction implements IAction {

	private Rule rule;

	private IAction action;

	public AIAction() {
	}

	public AIAction(IAction action) {
		setAction(action);
	}

	public void setAction(IAction action) {
		this.action = action;
	}

	public IAction getImpl() {
		return this.action;
	}

	public void doAction(long timer) {
		if (rule == null || rule.isRun()) {
			action.doAction(timer);
		}
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
}
