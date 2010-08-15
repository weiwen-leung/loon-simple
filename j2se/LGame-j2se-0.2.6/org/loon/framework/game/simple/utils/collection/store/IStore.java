package org.loon.framework.game.simple.utils.collection.store;

import java.util.Map;

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

public interface IStore {


	/**
	 * 添加键值对
	 * 
	 * @param key
	 * @param data
	 */
	public void put(final Object key, final Object data);

	/**
	 * 检查指定键值对象是否包含在store中
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(final Object key);

	/**
	 * 获得指定列名得缓存条目
	 * 
	 * @param key
	 * @return
	 */
	public StoreEntry getEntry(final Object key);

	/**
	 * 得到指定键值缓存所在位置
	 * 
	 * @param key
	 * @return
	 */
	public long getLocation(final Object key);

	/**
	 * 得到上次访问的数据
	 * 
	 * @param key
	 * @return
	 */
	public long getAccessed(final Object key);

	/**
	 * 得到缓存大小
	 * 
	 * @return
	 */
	public int size();

	/**
	 * 得到缓存的map形式
	 * 
	 * @return
	 */
	public Map getMap();

	/**
	 * 删除指定键值缓存
	 * 
	 * @param key
	 */
	public void remove(final Object key);

	/**
	 * 清空缓存数据
	 * 
	 */
	public void clear();

}
