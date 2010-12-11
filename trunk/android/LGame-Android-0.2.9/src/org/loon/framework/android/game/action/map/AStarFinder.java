package org.loon.framework.android.game.action.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.loon.framework.android.game.action.map.shapes.Vector2D;


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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class AStarFinder {
	
	private Vector2D goal;

	private LinkedList<ScoredPath> pathes;

	private ArrayList<Vector2D> path;

	private Set<Vector2D> visitedCache;

	private ScoredPath spath;

	private static Vector2D start, over;

	private static Field2D fieldMap;

	private static AStarFinder astar;

	public static ArrayList<Vector2D> find(int[][] maps, int x1, int y1, int x2, int y2,
			boolean flag) {
		if (start == null) {
			start = new Vector2D(x1, y1);
		} else {
			start.set(x1, y1);
		}
		if (over == null) {
			over = new Vector2D(x2, y2);
		} else {
			over.set(x2, y2);
		}
		return find(maps, start, over, flag);
	}

	public static ArrayList<Vector2D> find(Field2D maps, int x1, int y1, int x2, int y2,
			boolean flag) {
		if (astar == null) {
			astar = new AStarFinder();
		}
		if (start == null) {
			start = new Vector2D(x1, y1);
		} else {
			start.set(x1, y1);
		}
		if (over == null) {
			over = new Vector2D(x2, y2);
		} else {
			over.set(x2, y2);
		}
		return astar.calc(maps, start, over, flag);
	}

	public static ArrayList<Vector2D> find(Field2D maps, Vector2D start, Vector2D goal,
			boolean flag) {
		if (astar == null) {
			astar = new AStarFinder();
		}
		return astar.calc(maps, start, goal, flag);
	}

	public static ArrayList<Vector2D> find(int[][] maps, Vector2D start, Vector2D goal,
			boolean flag) {
		if (astar == null) {
			astar = new AStarFinder();
		}
		if (fieldMap == null) {
			fieldMap = new Field2D(maps);
		} else {
			fieldMap.setMap(maps);
		}
		return astar.calc(fieldMap, start, goal, flag);
	}

	private ArrayList<Vector2D> calc(Field2D field, Vector2D start, Vector2D goal, boolean flag) {
		if (start.equals(goal)) {
			return null;
		}
		this.goal = goal;
		if (visitedCache == null) {
			visitedCache = new HashSet<Vector2D>();
		} else {
			visitedCache.clear();
		}
		if (pathes == null) {
			pathes = new LinkedList<ScoredPath>();
		} else {
			pathes.clear();
		}
		visitedCache.add(start);
		if (path == null) {
			path = new ArrayList<Vector2D>();
		} else {
			path.clear();
		}
		path.add(start);
		if (spath == null) {
			spath = new ScoredPath(0, path);
		} else {
			spath.score = 0;
			spath.path = path;
		}
		pathes.add(spath);
		return astar(field, flag);
	}

	private ArrayList<Vector2D> astar(Field2D field, boolean flag) {
		for (;pathes.size() > 0;) {
			ScoredPath spath =  pathes.remove(0);
			Vector2D current =  spath.path.get(spath.path.size() - 1);
			if (current.equals(goal)) {
				return spath.path;
			}
			ArrayList<Vector2D> list = field.neighbors(current, flag);
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Vector2D next = list.get(i);
				if (visitedCache.contains(next)) {
					continue;
				}
				visitedCache.add(next);
				if (!field.isHit(next)) {
					continue;
				}
				ArrayList<Vector2D> path = new ArrayList<Vector2D>(spath.path);
				path.add(next);
				int score = spath.score + field.score(goal, next);
				insert(score, path);
			}
		}
		return null;
	}

	private void insert(int score, ArrayList<Vector2D> path) {
		int size = pathes.size();
		for (int i = 0; i < size; i += 1) {
			ScoredPath spath = pathes.get(i);
			if (spath.score >= score) {
				pathes.add(i, new ScoredPath(score, path));
				return;
			}
		}
		pathes.add(new ScoredPath(score, path));
	}

	private class ScoredPath {

		private int score;

		private ArrayList<Vector2D> path;

		ScoredPath(int score, ArrayList<Vector2D> path) {
			this.score = score;
			this.path = path;
		}

	}
}
