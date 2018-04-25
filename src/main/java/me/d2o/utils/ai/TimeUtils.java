/**
 *
 */
package me.d2o.utils.ai;

/**
 * Class: TimeUtils
 * 
 * @author bo.hanssen
 * @since Jul 17, 2017 3:44:01 PM
 *
 */
public class TimeUtils {

	public static void sleep(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
