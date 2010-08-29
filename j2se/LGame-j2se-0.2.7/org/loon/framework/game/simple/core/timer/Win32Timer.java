package org.loon.framework.game.simple.core.timer;

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
public class Win32Timer extends SystemTimer implements Runnable {

	private static final int NUM_SAMPLES_BITS = 6;

	private static final int NUM_SAMPLES = 1 << NUM_SAMPLES_BITS;

	private static final int NUM_SAMPLES_MASK = NUM_SAMPLES - 1;

	private static final int SLEEP_TIME = 5;

	private long[] samples;

	private int numSamples;

	private int firstIndex;

	private long estTime;

	private long lastTime;

	private Thread timerThread;

	public void start() {
		super.start();
		timerThread = null;
		numSamples = 0;
		firstIndex = 0;
		samples = new long[NUM_SAMPLES];
		estTime = System.currentTimeMillis() * 1000;
		lastTime = estTime;

		timerThread = new Thread(this);
		timerThread.setDaemon(true);
		timerThread.setPriority(Thread.MAX_PRIORITY);
		timerThread.start();
	}

	public void stop() {
		super.stop();
		timerThread = null;
	}

	public long getTimeMillis() {
		return getTimeMicros() / 1000;
	}

	public long getTimeMicros() {
		return Math.max(estTime, System.currentTimeMillis() * 1000);
	}

	public void run() {

		Thread thisThread = Thread.currentThread();

		while (timerThread == thisThread) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException ex) {
			}

			update();
		}
	}

	private void update() {
		synchronized (this) {
			long time = System.currentTimeMillis();
			if (time > lastTime) {
				estTime = Math.max(estTime, time * 1000L);
			} else if (numSamples > 0) {
				estTime += 1000L * (time - getFirstSample()) / numSamples;
			} else {
				estTime = time * 1000L;
			}
			addSample(time);
			lastTime = time;
			notifyAll();
		}
	}

	private void addSample(long sample) {
		samples[(firstIndex + numSamples) & NUM_SAMPLES_MASK] = sample;
		if (numSamples == NUM_SAMPLES) {
			firstIndex = (firstIndex + 1) & NUM_SAMPLES_MASK;
		} else {
			numSamples++;
		}
	}

	private long getFirstSample() {
		return samples[firstIndex];
	}
}
