package org.loon.framework.android.game;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

/**
 * 
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class ViewAnimator extends Handler {

	public static final int NEXT = 0;

	private boolean running;
	private View view;
	private long nextTime;
	private int diff;

	public ViewAnimator(View view) {
		this(view, -1);
	}

	public ViewAnimator(View view, int fps) {
		running = false;
		this.view = view;
		this.diff = 1000 / fps;
	}

	public void start() {
		if (!running) {
			running = true;
			Message msg = obtainMessage(NEXT);
			sendMessageAtTime(msg, SystemClock.uptimeMillis());
		}
	}

	public void stop() {
		running = false;
	}

	public void handleMessage(Message msg) {
		if (running && msg.what == NEXT) {
			view.invalidate();
			msg = obtainMessage(NEXT);
			long current = SystemClock.uptimeMillis();
			if (nextTime < current) {
				nextTime = current + diff;
			}
			sendMessageAtTime(msg, nextTime);
			nextTime += diff;
		}
	}
}
