package org.loon.framework.android.game.core.graphics.window.actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.LInput;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LContainer;

/**
 * 
 * Copyright 2008 - 2011
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
 * @version 0.1.1
 */

@SuppressWarnings("unchecked")
public abstract class ActorLayer extends LContainer {

	private final static int min_size = 48;

	private CollisionChecker collisionChecker;

	private boolean isBounded;

	protected int cellSize;

	ActorTreeSet objects;

	long elapsedTime;

	public ActorLayer(int x, int y, int layerWidth, int layerHeight,
			int cellSize) {
		this(x, y, layerWidth, layerHeight, cellSize, true);
	}

	public ActorLayer(int x, int y, int layerWidth, int layerHeight,
			int cellSize, boolean bounded) {
		super(x, y, layerWidth, layerHeight);
		this.collisionChecker = new CollisionManager();
		this.objects = new ActorTreeSet();
		this.cellSize = cellSize;
		this.initialize(layerWidth, layerHeight, cellSize);
		this.isBounded = bounded;
	}

	private void initialize(int width, int height, int cellSize) {
		this.cellSize = cellSize;
		this.collisionChecker.initialize(cellSize);
	}

	public LInput screenInput() {
		return input;
	}

	public int getCellSize() {
		return this.cellSize;
	}

	/**
	 * 添加角色到Layer(在Layer中添加的角色将自动赋予碰撞检查)
	 * 
	 * @param object
	 * @param x
	 * @param y
	 */
	public void addObject(Actor object, int x, int y) {
		synchronized (collisionChecker) {
			if (this.objects.add(object)) {
				object.addLayer(x, y, this);
				this.collisionChecker.addObject(object);
				object.addLayer(this);
			}
		}
	}

	/**
	 * 添加角色到Layer
	 * 
	 * @param object
	 */
	public void addObject(Actor object) {
		addObject(object, object.x, object.y);
	}

	/**
	 * 将指定角色于Layer前置
	 * 
	 * @param actor
	 */
	void sendToFront(Actor actor) {
		if (objects != null) {
			synchronized (objects) {
				if (objects != null) {
					objects.sendToFront(actor);
				}
			}
		}
	}

	/**
	 * 将指定角色于Layer后置
	 * 
	 * @param actor
	 */
	void sendToBack(Actor actor) {
		if (objects != null) {
			synchronized (objects) {
				if (objects != null) {
					objects.sendToBack(actor);
				}
			}
		}
	}

	/**
	 * 参考指定大小根据Layer范围生成一组不重复的随机坐标
	 * 
	 * @param act
	 * @param count
	 * @return
	 */
	public RectBox[] getRandomLayerLocation(int nx, int ny, int nw, int nh,
			int count) {
		if (count <= 0) {
			throw new RuntimeException("count <= 0 !");
		}
		int layerWidth = getWidth();
		int layerHeight = getHeight();
		int actorWidth = nw > min_size ? nw : min_size;
		int actorHeight = nh > min_size ? nh : min_size;
		int x = nx / actorWidth;
		int y = ny / actorHeight;
		int row = layerWidth / actorWidth;
		int col = layerHeight / actorHeight;
		RectBox[] randoms = new RectBox[count];
		int oldRx = 0, oldRy = 0;
		int index = 0;
		for (int i = 0; i < count * 100; i++) {
			if (index >= count) {
				return randoms;
			}
			int rx = LSystem.random.nextInt(row);
			int ry = LSystem.random.nextInt(col);
			if (oldRx != rx && oldRy != ry && rx != x && ry != y
					&& rx * actorWidth != nx && ry * actorHeight != ny) {
				boolean stop = false;
				for (int j = 0; j < index; j++) {
					if (randoms[j].x == rx && randoms[j].y == ry && oldRx != x
							&& oldRy != y && rx * actorWidth != nx
							&& ry * actorHeight != ny) {
						stop = true;
						break;
					}
				}
				if (stop) {
					continue;
				}
				randoms[index] = new RectBox(rx * actorWidth, ry * actorHeight,
						actorWidth, actorHeight);
				oldRx = rx;
				oldRy = ry;
				index++;
			}
		}
		return null;
	}

	/**
	 * 参考指定大小根据Layer范围生成一组不重复的随机坐标
	 * 
	 * @param actorWidth
	 * @param actorHeight
	 * @param count
	 * @return
	 */
	public RectBox[] getRandomLayerLocation(int actorWidth, int actorHeight,
			int count) {
		return getRandomLayerLocation(0, 0, actorWidth, actorHeight, count);
	}

	/**
	 * 参考指定角色根据Layer范围生成一组不重复的随机坐标
	 * 
	 * @param actor
	 * @param count
	 * @return
	 */
	public RectBox[] getRandomLayerLocation(Actor actor, int count) {
		RectBox rect = actor.getRectBox();
		return getRandomLayerLocation(rect.x, rect.y, rect.width, rect.height,
				count);
	}

	/**
	 * 参考指定Actor大小根据Layer生成一个不重复的随机坐标
	 * 
	 * @param actor
	 * @return
	 */
	public RectBox getRandomLayerLocation(Actor actor) {
		RectBox[] rects = getRandomLayerLocation(actor, 1);
		if (rects != null) {
			return rects[0];
		}
		return null;
	}

	/**
	 * 删除指定的角色
	 * 
	 * @param object
	 */
	public void removeObject(Actor object) {
		synchronized (collisionChecker) {
			if (this.objects.remove(object)) {
				this.collisionChecker.removeObject(object);
			}
			object.setLayer((ActorLayer) null);
		}
	}

	/**
	 * 删除所有指定的游戏类
	 * 
	 * @param clazz
	 */
	public void removeObject(Class clazz) {
		synchronized (collisionChecker) {
			Iterator it = objects.iterator();
			while (it.hasNext()) {
				Actor actor = (Actor) it.next();
				Class cls = actor.getClass();
				if (clazz == null || clazz == cls || clazz.isInstance(actor)
						|| clazz.equals(cls)) {
					if (this.objects.remove(actor)) {
						this.collisionChecker.removeObject(actor);
					}
					actor.setLayer((ActorLayer) null);
				}
			}
		}
	}

	/**
	 * 删除指定集合中的所有角色
	 * 
	 * @param objects
	 */
	public void removeObjects(Collection objects) {
		Iterator iter = objects.iterator();
		while (iter.hasNext()) {
			Actor actor = (Actor) iter.next();
			this.removeObject(actor);
		}
	}

	/**
	 * 获得含有指定角色碰撞的List集合
	 * 
	 * @param actor
	 * @return
	 */
	public List getCollisionObjects(Actor actor) {
		return getCollisionObjects(actor.getClass());
	}

	/**
	 * 刷新缓存数据，重置世界
	 * 
	 */
	public void reset() {
		if (objects != null) {
			synchronized (objects) {
				if (objects != null) {
					objects.clear();
					objects = null;
				}
				if (collisionChecker != null) {
					collisionChecker.clear();
					collisionChecker = null;
				}
				this.collisionChecker = new CollisionManager();
				this.objects = new ActorTreeSet();
			}
		}
	}

	/**
	 * 获得指定类所产生角色碰撞的List集合
	 * 
	 * @param cls
	 * @return
	 */
	public List getCollisionObjects(Class cls) {
		ArrayList result = new ArrayList();
		Iterator it = this.objects.iterator();
		while (it.hasNext()) {
			Actor actor = (Actor) it.next();
			if (cls == null || cls.isInstance(actor)) {
				result.add(actor);
			}
		}
		return result;
	}

	/**
	 * 返回指定角色类在指定位置的List集合
	 * 
	 * @param x
	 * @param y
	 * @param cls
	 * @return
	 */
	public List getCollisionObjectsAt(int x, int y, Class cls) {
		return this.collisionChecker.getObjectsAt(x, y, cls);
	}

	/**
	 * 角色对象总数
	 * 
	 * @return
	 */
	public int size() {
		return this.objects.size();
	}

	public abstract void action(long elapsedTime);

	public boolean isBounded() {
		return this.isBounded;
	}

	Actor getSynchronizedObject(int x, int y) {
		if (objects == null) {
			return null;
		}
		synchronized (objects) {
			Collection collection = getCollisionObjects(x, y);
			if (collection == null) {
				return null;
			}
			if (collection.isEmpty()) {
				return null;
			}
			Iterator iter = collection.iterator();
			Actor tmp = (Actor) iter.next();
			int seq = tmp.getLastPaintSeqNum();
			while (iter.hasNext()) {
				Actor actor = (Actor) iter.next();
				int actorSeq = actor.getLastPaintSeqNum();
				if (actorSeq > seq) {
					tmp = actor;
					seq = actorSeq;
				}
			}
			return tmp;
		}
	}

	List getIntersectingObjects(Actor actor, Class cls) {
		return this.collisionChecker.getIntersectingObjects(actor, cls);
	}

	Actor getOnlyIntersectingObject(Actor object, Class cls) {
		return this.collisionChecker.getOnlyIntersectingObject(object, cls);
	}

	List getObjectsInRange(int x, int y, int r, Class cls) {
		return this.collisionChecker.getObjectsInRange(x, y, r, cls);
	}

	List getNeighbours(Actor actor, int distance, boolean d, Class cls) {
		if (distance < 0) {
			throw new RuntimeException("distance < 0");
		} else {
			return this.collisionChecker.getNeighbours(actor, distance, d, cls);
		}
	}

	int getHeightInPixels() {
		return this.getHeight() * this.cellSize;
	}

	int getWidthInPixels() {
		return this.getWidth() * this.cellSize;
	}

	int toCellCeil(int pixel) {
		return (int) Math.ceil((double) pixel / (double) this.cellSize);
	}

	int toCellFloor(int pixel) {
		return (int) Math.floor((double) pixel / (double) this.cellSize);
	}

	double getCellCenter(int c) {
		double cellCenter = (double) (c * this.cellSize)
				+ (double) this.cellSize / 2.0D;
		return cellCenter;
	}

	Collection getCollisionObjects(int x, int y) {
		ArrayList result = new ArrayList(20);
		Iterator it = objects.iterator();
		while (it.hasNext()) {
			Actor actor = (Actor) it.next();
			RectBox bounds = actor.getRectBox();
			if (bounds.contains(x, y)) {
				result.add(actor);
			}
		}
		return result;
	}

	void updateObjectLocation(Actor object, int oldX, int oldY) {
		this.collisionChecker.updateObjectLocation(object, oldX, oldY);
	}

	void updateObjectSize(Actor object) {
		this.collisionChecker.updateObjectSize(object);
	}

	Actor getOnlyObjectAt(Actor object, int dx, int dy, Class cls) {
		return this.collisionChecker.getOnlyObjectAt(object, dx, dy, cls);
	}

	ActorTreeSet getObjectsListInPaintO() {
		return this.objects;
	}

}
