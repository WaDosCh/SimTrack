/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.test;

import ch.awae.simtrack.controller.BasicBorderConnectionSpawner;
import ch.awae.simtrack.controller.IBorderConnectionSpawner;
import ch.awae.simtrack.model.Map;

public class Test {

	public static void main(String[] args) {

		IBorderConnectionSpawner spawner = new BasicBorderConnectionSpawner(100);

		Map m = new Map(100, 40, spawner);

		System.out.println(m.getBorderTracks().size() + " connections placed");

		m.getBorderTracks().forEach(
				(p, t) -> System.out.println(p + " >> " + t));

	}
}
