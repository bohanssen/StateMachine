/**
 *
 */
package me.d2o.transfermodel.gameplay.battle;

/**
 * Class: BattleResult
 *
 * @author bo.hanssen
 * @since Feb 5, 2017 12:38:39 PM
 *
 */
public class BattleResult {

	private String victor;
	private String loser;
	private String vresult;
	private String lresult;
	private int vpercent;
	private int lpercent;

	/**
	 * @param victor
	 * @param loser
	 * @param vresult
	 * @param lresult
	 * @param vpercent
	 * @param lpercent
	 */
	public BattleResult(String victor, String loser, String vresult, String lresult, int vpercent, int lpercent) {
		super();
		this.victor = victor;
		this.loser = loser;
		this.vresult = vresult;
		this.lresult = lresult;
		this.vpercent = vpercent;
		this.lpercent = lpercent;
	}

	public String getVictor() {
		return victor;
	}

	public void setVictor(String victor) {
		this.victor = victor;
	}

	public String getLoser() {
		return loser;
	}

	public void setLoser(String loser) {
		this.loser = loser;
	}

	public String getVresult() {
		return vresult;
	}

	public void setVresult(String vresult) {
		this.vresult = vresult;
	}

	public String getLresult() {
		return lresult;
	}

	public void setLresult(String lresult) {
		this.lresult = lresult;
	}

	public int getVpercent() {
		return vpercent;
	}

	public void setVpercent(int vpercent) {
		this.vpercent = vpercent;
	}

	public int getLpercent() {
		return lpercent;
	}

	public void setLpercent(int lpercent) {
		this.lpercent = lpercent;
	}

}
