/**
 *
 */
package me.d2o.utils.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: GameLevelTitles
 *
 * @author bo.hanssen
 * @since Jan 31, 2017 4:45:59 PM
 *
 */
public class GameLevelTitles {

	private Map<Integer, String> titles;
	private List<Integer> keys;
	private boolean sorted = false;

	public GameLevelTitles() {
		titles = new HashMap<>();
		keys = new ArrayList<>();
	}

	public void addLevel(int key, String title) {
		titles.put(key, title);
		keys.add(key);
	}

	public String getLevel(int worth) {
		if (!sorted) {
			sorted = true;
			Collections.sort(keys);
		}
		String label = "title1";
		for (int t : keys) {
			if (worth >= t) {
				label = titles.get(t);
			} else {
				break;
			}
		}
		return label;
	}
}
