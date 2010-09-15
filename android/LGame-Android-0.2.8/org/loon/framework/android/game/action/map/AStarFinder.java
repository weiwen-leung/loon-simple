package org.loon.framework.android.game.action.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class AStarFinder {

	private Field2D map;

	private Vector2D goal;

	private List<ScoredPath> pathes;

	private Set<Vector2D> visitedCache;

	public static List<Vector2D> find(Field2D maps, Vector2D start, Vector2D goal) {
		AStarFinder astar = new AStarFinder();
		List<Vector2D> path = astar.calc(maps, start, goal);
		return path;
	}

	public static List<?> find(int[][] maps, Vector2D start, Vector2D goal) {
		return AStarFinder.find(new Field2D(maps), start, goal);
	}

	public List<Vector2D> calc(Field2D map, Vector2D start, Vector2D goal) {
		return new AStarFinder().solve(map, start, goal);
	}

	private List<Vector2D> solve(Field2D map, Vector2D start, Vector2D goal) {
		this.map = map;
		this.goal = goal;
		visitedCache = new HashSet<Vector2D>();
		pathes = new LinkedList<ScoredPath>();
		visitedCache.add(start);
		LinkedList<Vector2D> path = new LinkedList<Vector2D>();
		path.add(start);
		ScoredPath spath = new ScoredPath(0, path);
		pathes.add(spath);
		return astar();
	}

	private List<Vector2D> astar() {
		while (pathes.size() > 0) {
			ScoredPath spath = (ScoredPath) pathes.remove(0);
			Vector2D current =  spath.path.get(spath.path.size() - 1);
			if (current.equals(goal)) {
				return spath.path;
			}
			for (Iterator<Vector2D> it = map.neighbors(current).iterator(); it.hasNext();) {
				Vector2D next =  it.next();
				if (visitedCache.contains(next)) {
					continue;
				}
				visitedCache.add(next);
				if (!map.isHit(next)) {
					continue;
				}
				ArrayList<Vector2D> path = new ArrayList<Vector2D>(spath.path);
				path.add(next);
				int score = spath.score + map.score(goal, next);
				insert(score, path);
			}
		}
		return null;
	}

	private void insert(int score, ArrayList<Vector2D> path) {
		for (int i = 0; i < pathes.size(); i += 1) {
			ScoredPath spath = (ScoredPath) pathes.get(i);
			if (spath.score >= score) {
				pathes.add(i, new ScoredPath(score, path));
				return;
			}
		}
		pathes.add(new ScoredPath(score, path));
	}

	private class ScoredPath {

		private int score;

		private List<Vector2D> path;

		ScoredPath(int score, List<Vector2D> path) {
			this.score = score;
			this.path = path;
		}
	}
}
