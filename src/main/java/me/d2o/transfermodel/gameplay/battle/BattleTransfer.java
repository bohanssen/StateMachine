package me.d2o.transfermodel.gameplay.battle;

import java.util.List;

import me.d2o.transfermodel.gameplay.DivisionClientModel;

public class BattleTransfer {

	private String consumer;
	private String suplier;
	private int suplierId;
	private int consumerId;
	
	private List<DivisionClientModel> available;
	
	public String getConsumer() {
		return consumer;
	}
	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}
	public String getSuplier() {
		return suplier;
	}
	public void setSuplier(String suplier) {
		this.suplier = suplier;
	}
	public List<DivisionClientModel> getAvailable() {
		return available;
	}
	public void setAvailable(List<DivisionClientModel> available) {
		this.available = available;
	}
	public int getSuplierId() {
		return suplierId;
	}
	public void setSuplierId(int suplierId) {
		this.suplierId = suplierId;
	}
	public int getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(int consumerId) {
		this.consumerId = consumerId;
	}
	
}
