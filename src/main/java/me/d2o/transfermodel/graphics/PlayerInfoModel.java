/**
 *
 */
package me.d2o.transfermodel.graphics;

/**
 * Class: PlayerInfoModel
 *
 * @author bo.hanssen
 * @since Jan 30, 2017 9:18:42 PM
 *
 */
public class PlayerInfoModel {

	private int cash;
	private int bonus;
	private String level;
	private String color;
	private String nickname;
	private String info;

	/**
	 * @param cash
	 * @param bonus
	 * @param level
	 * @param color
	 */
	public PlayerInfoModel(int cash, int bonus, String level, String color, String nickname, String info) {
		super();
		this.cash = cash;
		this.bonus = bonus;
		this.level = level;
		this.color = color;
		this.nickname = nickname;
		this.info = info;
	}

	/**
	 *
	 */
	public PlayerInfoModel() {
		super();
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
