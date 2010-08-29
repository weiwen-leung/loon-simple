package org.loon.framework.game.simple.core;

import java.util.Vector;

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
public class Runner extends Thread {

	private Vector queues;

	private boolean stop;

	private Runnable queueProcessedListener;

	private static Runner instance;

	private boolean paused;

	public Runner() {
		this.queues = new Vector();
		start();
	}

	public Runner(boolean paused) {
		this.paused = paused;
		start();
	}

	synchronized public void pause() {
		paused = true;
	}

	synchronized public void unpause() {
		paused = false;
		notify();
	}

	public static Runner getInstance() {
		if (instance == null) {
			instance = new Runner();
		}
		return instance;
	}

	public void setQueueListener(Runnable r) {
		queueProcessedListener = r;
	}

	public void run() {
		boolean events;
		while (!stop) {
			synchronized (this) {
				while (paused) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			}
			events = false;
			while (!queues.isEmpty()) {
				events = true;
				Object running = queues.firstElement();
				queues.removeElementAt(0);
				try {
					if (running instanceof Thread) {
						Thread thread = (Thread) running;
						if (!thread.isAlive()) {
							thread.start();
						}
					} else {
						((Runnable) running).run();
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
				if (paused) {
					break;
				}
			}
			if (events && queueProcessedListener != null) {
				queueProcessedListener.run();
			}
			synchronized (this) {
				if (!queues.isEmpty()) {
					continue;
				}
				if (stop) {
					return;
				}
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public synchronized void wait(Runnable runnable) {
		queues.addElement(runnable);
		try {
			runnable.wait();
		} catch (InterruptedException ex) {
		}
	}

	public synchronized void call(Runnable runnable) {
		queues.addElement(runnable);
		notify();
	}

	public static void callTask(Runnable runnable) {
		getInstance().call(runnable);
	}

	public synchronized void kill() {
		stop = true;
		notify();
	}

}
