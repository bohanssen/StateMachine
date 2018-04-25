/**
 *
 */
package me.d2o.utils.ai;

/**
 * Class: LoopController
 * 
 * @author bo.hanssen
 * @since Feb 22, 2017 10:06:52 AM
 *
 */
public class LoopController {

	private boolean active;

	public boolean isActive() {
		return active;
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

}
