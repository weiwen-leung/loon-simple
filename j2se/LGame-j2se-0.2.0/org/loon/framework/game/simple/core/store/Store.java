package org.loon.framework.game.simple.core.store;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.utils.CollectionUtils;

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
class Store implements IStore, Serializable, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982090185173057054L;

	// 默认数据保存2小时，每隔30分钟检查一次
	public final static long TIMEOUT_DEFAULT = 2 * LSystem.HOUR,
			SLEEP_DEFAULT = 30 * LSystem.MINUTE;

	private long timeout = TIMEOUT_DEFAULT, sleep = SLEEP_DEFAULT;

	private Map lazy = CollectionUtils.synchronizedLRUMap(100);

	private Thread thread = null;

	private boolean isInterrupt = false;

	public Store() {
		this.start();
	}

	public Store(final long timeout, final long sleep) {
		this.timeout = timeout;
		this.sleep = sleep;
		this.start();
	}

	/**
	 * 添加一组键值对
	 * 
	 * @param key
	 * @param data
	 */
	synchronized public void put(final Object key, final Object data) {
		StoreEntry entry = new StoreEntry(data);
		lazy.put(key, entry);
	}

	/**
	 * 检查指定键值对象是否包含在store中
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(final Object key) {
		return lazy.containsKey(key);
	}

	/**
	 * 获得指定列名得缓存条目
	 * 
	 * @param key
	 * @return
	 */
	public StoreEntry getEntry(final Object key) {
		StoreEntry entry = (StoreEntry) lazy.get(key);
		return entry;
	}

	/**
	 * 得到指定键值缓存所在位置
	 * 
	 * @param key
	 * @return
	 */
	public long getLocation(final Object key) {
		StoreEntry entry = (StoreEntry) lazy.get(key);
		if (entry == null)
			return 0;
		return entry.entered;
	}

	/**
	 * 得到上次访问的数据
	 * 
	 * @param key
	 * @return
	 */
	public long getAccessed(final Object key) {
		StoreEntry entry = (StoreEntry) lazy.get(key);
		if (entry == null)
			return 0;
		return entry.accessed;
	}

	/**
	 * 得到缓存大小
	 * 
	 * @return
	 */
	public int size() {
		return lazy.size();
	}

	/**
	 * 得到缓存的map
	 * 
	 * @return
	 */
	public Map getMap() {
		return Collections.unmodifiableMap((Map) lazy);
	}

	/**
	 * 删除指定键值缓存
	 * 
	 * @param key
	 */
	public void remove(Object key) {
		lazy.remove(key);
	}

	/**
	 * 启动线程
	 * 
	 */
	void start() {
		if (timeout < 0) {
			return;
		}
		stop();
		thread = new Thread(this);
		thread.setDaemon(true);
		isInterrupt = false;
		thread.start();
	}

	/**
	 * 停止线程
	 * 
	 */
	void stop() {
		while (thread != null) {
			isInterrupt = true;
			thread.interrupt();
			if (!thread.isAlive())
				thread = null;
			else {
				try {
					// 延迟10毫秒
					Thread.sleep(10);
				} catch (InterruptedException ie) {
				}
			}
		}
	}

	/**
	 * 循环运算
	 */
	public void run() {
		// 循环中
		while (!isInterrupt) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException ie) {
			}
			if (isInterrupt) {
				break;
			}
			synchronized (this) {
				// 获得当前时间
				long now = System.currentTimeMillis();
				Set set = lazy.entrySet();
				for (Iterator it = set.iterator(); it.hasNext();) {
					Object key = ((Entry) it.next()).getKey();
					StoreEntry entry = (StoreEntry) lazy.get(key);
					// 自省
					if (expire(key, entry, now)) {
						lazy.remove(key);
					}
				}
			}
		}
	}

	/**
	 * 条目自省
	 * 
	 * @param key
	 * @param entry
	 * @param now
	 * @return
	 */
	protected boolean expire(final Object key, final StoreEntry entry,
			final long now) {
		if ((now - entry.accessed) > timeout) {
			return true;
		}
		return false;
	}

	/**
	 * 清空缓存数据
	 * 
	 */
	public void clear() {
		lazy.clear();
	}

	/*public static void main(String[] args) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				IStore store = StoreFactory.getInstance(LSystem.MINUTE,
						LSystem.SECOND);
				store.put("test", "testing");
				while (true) {
					if (store.getEntry("test") == null) {
						System.out.println("clear");
					}
				}

			}

		});
		thread.start();
	}*/

}
