package me.d2o.persistence.model.scenario;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Scenario.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:41 PM
 */
@Entity
@XmlRootElement
public class Scenario {

	@Id
	private String titleKey;
	private double neutral;
	private int neutralQuantity;

	private int width;
	private int height;
	private int originx;
	private int originy;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scenario", cascade = CascadeType.ALL)
	@MapKey(name = "key")
	private Map<String, Region> regions = new HashMap<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "scenario", cascade = CascadeType.ALL)
	@MapKey(name = "key")
	private Map<String, Role> roles = new HashMap<>();

	
	public void addRegion(Region region) {
		regions.put(region.getKey(), region);
		region.setScenario(this);
	}

	
	public void addRole(Role role) {
		roles.put(role.getKey(), role);
		role.setScenario(this);
	}

	
	public String getTitleKey() {
		return titleKey;
	}

	
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	
	public Map<String, Region> getRegions() {
		return regions;
	}

	
	public void setRegions(Map<String, Region> regions) {
		this.regions = regions;
	}

	
	public Map<String, Role> getRoles() {
		return roles;
	}

	
	public void setRoles(Map<String, Role> roles) {
		this.roles = roles;
	}

	
	public Region findRegion(String key) {
		return regions.get(key);
	}

	
	public Role findRole(String key) {
		return roles.get(key);
	}

	
	public double getNeutral() {
		return neutral;
	}

	
	public void setNeutral(double neutral) {
		this.neutral = neutral;
	}

	
	public int getNeutralQuantity() {
		return neutralQuantity;
	}

	
	public void setNeutralQuantity(int neutralQuantity) {
		this.neutralQuantity = neutralQuantity;
	}

	
	public int getWidth() {
		return width;
	}

	
	public void setWidth(int width) {
		this.width = width;
	}

	
	public int getHeight() {
		return height;
	}

	
	public void setHeight(int height) {
		this.height = height;
	}

	
	public int getOriginx() {
		return originx;
	}

	
	public void setOriginx(int originx) {
		this.originx = originx;
	}

	
	public int getOriginy() {
		return originy;
	}

	
	public void setOriginy(int originy) {
		this.originy = originy;
	}

}
