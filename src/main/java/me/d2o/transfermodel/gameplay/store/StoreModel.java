/**
 *
 */
package me.d2o.transfermodel.gameplay.store;

import java.util.List;
import java.util.Map;

/**
 * Class: StoreModel
 * 
 * @author bo.hanssen
 * @since Jan 30, 2017 11:07:45 PM
 *
 */
public class StoreModel {

	private Map<String, Integer> capitols;
	private List<UnitClientModel> units;

	public Map<String, Integer> getCapitols() {
		return capitols;
	}

	public void setCapitols(Map<String, Integer> capitols) {
		this.capitols = capitols;
	}

	public List<UnitClientModel> getUnits() {
		return units;
	}

	public void setUnits(List<UnitClientModel> units) {
		this.units = units;
	}

}
