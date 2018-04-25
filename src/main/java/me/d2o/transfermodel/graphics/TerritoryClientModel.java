/**
 *
 */
package me.d2o.transfermodel.graphics;

import java.util.List;

import me.d2o.persistence.model.game.Territory;
import me.d2o.transfermodel.gameplay.DivisionClientModel;

/**
 * Class: TerritoryClientModel
 *
 * @author bo.hanssen
 * @since Jan 25, 2017 9:10:23 PM
 *
 */
public class TerritoryClientModel {

	private int id;
	private boolean hasTown;
	private String color;
	private int total;
	private List<DivisionClientModel> divisions;
	private List<DivisionClientModel> transfers;
	private long userId;

	public TerritoryClientModel(Territory t, List<DivisionClientModel> divisions, int total,
			List<DivisionClientModel> transfers) {
		id = t.getRegion().getId();
		hasTown = t.isTown();
		color = t.getRole().getColor();
		this.total = total;
		this.divisions = divisions;
		this.transfers = transfers;
		userId = t.getUser() != null ? t.getUser().getId() : 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isHasTown() {
		return hasTown;
	}

	public void setHasTown(boolean hasTown) {
		this.hasTown = hasTown;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<DivisionClientModel> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<DivisionClientModel> divisions) {
		this.divisions = divisions;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<DivisionClientModel> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<DivisionClientModel> transfers) {
		this.transfers = transfers;
	}

}
