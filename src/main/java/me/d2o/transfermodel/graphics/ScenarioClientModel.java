/**
 *
 */
package me.d2o.transfermodel.graphics;

import java.util.ArrayList;
import java.util.List;

import me.d2o.persistence.model.scenario.Scenario;

/**
 * Class: ScenarioClientModel
 *
 * @author bo.hanssen
 * @since Jan 25, 2017 8:33:57 PM
 *
 */
public class ScenarioClientModel {

	private List<Integer> regionIds;
	private String title;
	private int width;
	private int height;
	private int originx;
	private int originy;

	public ScenarioClientModel(Scenario scenario) {
		this.title = scenario.getTitleKey();
		this.width = scenario.getWidth();
		this.height = scenario.getHeight();
		this.originx = scenario.getOriginx();
		this.originy = scenario.getOriginy();
		regionIds = new ArrayList<>();
		scenario.getRegions().values().forEach(region -> regionIds.add(region.getId()));
	}

	public List<Integer> getRegionIds() {
		return regionIds;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getOriginx() {
		return originx;
	}

	public int getOriginy() {
		return originy;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
