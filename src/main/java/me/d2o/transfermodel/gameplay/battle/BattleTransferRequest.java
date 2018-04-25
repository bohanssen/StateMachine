package me.d2o.transfermodel.gameplay.battle;

import java.util.Map;

public class BattleTransferRequest {

	private long gameId;
	private int suplier;
	private int consumer;
	private Map<String,Integer> transfers;
	
	public long getGameId() {
		return gameId;
	}
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	public int getSuplier() {
		return suplier;
	}
	public void setSuplier(int suplier) {
		this.suplier = suplier;
	}
	public int getConsumer() {
		return consumer;
	}
	public void setConsumer(int consumer) {
		this.consumer = consumer;
	}
	public Map<String, Integer> getTransfers() {
		return transfers;
	}
	public void setTransfers(Map<String, Integer> transfers) {
		this.transfers = transfers;
	}
	
	@Override
	public String toString() {
		return "BattleTransferRequest [gameId=" + gameId + ", suplier=" + suplier + ", consumer=" + consumer
				+ ", transfers=" + transfers + "]";
	}
	
}
