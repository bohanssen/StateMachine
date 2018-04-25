/**
 *
 */
package me.d2o.utils.i18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: Dictionary
 * 
 * @author bo.hanssen
 * @since Jan 29, 2017 3:12:46 PM
 *
 */
public class Dictionary {

	private List<String> keys;

	public Dictionary() {
		keys = new ArrayList<>();
	}

	public void add(String key) {
		keys.add(key);
	}

	public List<String> getKeys() {
		return keys;
	}
}
