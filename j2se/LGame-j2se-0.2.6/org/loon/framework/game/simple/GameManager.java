package org.loon.framework.game.simple;

import java.util.ArrayList;
import java.util.List;

import org.loon.framework.game.simple.core.IHandler;
import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.timer.NanoTimer;
import org.loon.framework.game.simple.core.timer.SystemTimer;
import org.loon.framework.game.simple.core.timer.Win32Timer;

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
public class GameManager {

	final static private GameManager INSTANCE = new GameManager();

	private SystemTimer timer;

	private GameContext mainContext, initContext;

	private List allContexts;

	private Thread initContextThread;

	private GameManager() {
		if (LSystem.isOverrunJdk14()) {
			try {
				Class c = Class.forName("java.lang.System");
				c.getMethod("nanoTime", new Class[0]);
				timer = new NanoTimer();
				return;
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		if (timer == null) {
			if (LSystem.isWindows()) {
				timer = new Win32Timer();
				return;
			}
		}

		if (timer == null) {
			timer = new SystemTimer();
			return;
		}

	}

	public GameContext getContext() {

		if (allContexts == null) {
			return mainContext;
		}

		synchronized (this) {
			if (allContexts == null) {
				return mainContext;
			}

			ThreadGroup currentThreadGroup = Thread.currentThread()
					.getThreadGroup();
			for (int i = 0; i < allContexts.size(); i++) {
				GameContext context = (GameContext) allContexts.get(i);
				ThreadGroup contextThreadGroup = context.getThreadGroup();
				if (contextThreadGroup == currentThreadGroup
						|| contextThreadGroup.parentOf(currentThreadGroup)) {
					return context;
				}
			}

			if (initContext != null
					&& Thread.currentThread() == initContextThread) {
				return initContext;
			}

			return (GameContext) (allContexts
					.get(GameContext.nextContextID - 1));

		}
	}

	private synchronized GameContext getAppContext(IView app) {
		if (mainContext != null && mainContext.getView() == app) {
			return mainContext;
		}

		if (allContexts != null) {
			for (int i = 0; i < allContexts.size(); i++) {
				GameContext context = (GameContext) allContexts.get(i);
				if (context.getView() == app) {
					return context;
				}
			}
		}

		return null;
	}

	public synchronized boolean isRegistered(IView app) {
		return (getAppContext(app) != null);
	}

	private synchronized int getNumRegisteredApps() {
		if (allContexts == null) {
			if (mainContext == null) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return allContexts.size();
		}
	}

	public synchronized GameContext registerApp(IView app) {

		if (app == null) {
			return null;
		}

		GameContext context = getAppContext(app);
		if (context != null) {
			return context;
		}

		boolean wasEmpty = (getNumRegisteredApps() == 0);

		GameContext newContext = new GameContext(app, timer);
		if (mainContext == null) {
			mainContext = newContext;
		} else {
			if (allContexts == null) {
				allContexts = new ArrayList();
				allContexts.add(mainContext);
			}
			allContexts.add(newContext);
		}
		initContext = newContext;
		initContextThread = Thread.currentThread();
		initContext = null;
		initContextThread = null;
		if (wasEmpty) {
			timer.start();
		}

		return newContext;
	}

	public synchronized void unregisterApp(IView app) {
		if (app == null || !isRegistered(app)) {
			return;
		}

		if (mainContext != null && mainContext.getView() == app) {
			mainContext = null;
		}

		if (allContexts != null) {
			for (int i = 0; i < allContexts.size(); i++) {
				GameContext context = (GameContext) allContexts.get(i);
				if (context.getView() == app) {
					allContexts.remove(i);
					break;
				}
			}

			if (mainContext == null) {
				mainContext = (GameContext) allContexts.get(0);
			}

			if (allContexts.size() == 1) {
				allContexts = null;
			}
		}

		if (getNumRegisteredApps() == 0) {
			timer.stop();
		}
	}

	public long getTimeMillis() {
		return timer.getTimeMillis();
	}

	public long getTimeMicros() {
		return timer.getTimeMicros();
	}

	public static GameManager getInstance() {
		return INSTANCE;
	}

	public IHandler getSystemHandler(int id) {
		if (allContexts != null) {
			if (id > 0 && id < allContexts.size()) {
				return ((GameContext) allContexts.get(id)).getView()
						.getHandler();
			}
		}
		return null;
	}

	public static IHandler getSystemHandler() {
		GameContext context = INSTANCE.getContext();
		if (context != null) {
			return context.getView().getHandler();
		} else {
			return null;
		}
	}

}
