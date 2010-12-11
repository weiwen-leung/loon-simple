package org.loon.framework.javase.game.action.sprite.effect;

import java.awt.Color;

import org.loon.framework.javase.game.core.graphics.device.LGraphics;

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
public class Particles {

	public float x;

	public float y;

	public float dx;

	public float dy;

	public int life;

	public int maxLife;

	public Particledefinition definition;

	private static int maxParticles;

	private static Particles particles[];

	public Particles() {
		if (particles == null) {
			maxParticles = 256;
			particles = new Particles[maxParticles + 1];
		}
	}

	public static void spawn(int x, int y, int size,
			Particledefinition p) {
		for (int l = 0; l < size; l++) {
			Particles particles1 = new Particles();
			particles1.x = x;
			particles1.y = y;
			particles1.dx = p.dx
					* (float) (Math.random() * 2D - 1.0D);
			particles1.dy = p.dy
					* (float) (Math.random() * 2D - 1.0D);
			particles1.life = p.minLife
					+ (int) ((float) (p.maxLife - p.minLife) * (float) Math
							.random());
			particles1.maxLife = particles1.life;
			particles1.definition = p;
			for (int i1 = 1; i1 <= maxParticles; i1++) {
				if (particles[i1] != null) {
					continue;
				}
				particles[i1] = particles1;
				break;
			}
		}

	}

	public boolean tick() {
		x += dx;
		y += dy;
		life--;
		return life <= 0;
	}

	public static void update() {
		for (int i = 1; i <= maxParticles; i++) {
			if (particles[i] != null && particles[i].tick()) {
				particles[i] = null;
			}
		}
	}

	public static void draw(LGraphics g) {
		g.setColor(Color.WHITE);
		for (int i = 1; i <= maxParticles; i++)
			if (particles[i] != null) {
				g
						.setColor(Color
								.getHSBColor(
										particles[i].definition.hue,
										particles[i].definition.saturation,
										particles[i].definition.brightness
												* ((float) particles[i].life / (float) particles[i].maxLife)));
				if (particles[i] != null) {
					g
							.drawRect((int) particles[i].x,
									(int) particles[i].y, 1, 1);
				}
			}

	}
}
