/**
 *
 */
package me.d2o.config.statemachine;

import org.springframework.stereotype.Component;

/**
 * Class: Events
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 2:41:26 PM
 *
 */
@Component
public class Events {

	public static final String INITIALIZE = "INITIALIZE";
	public static final String PLAY = "PLAY";
	public static final String NEXT_TURN = "NEXT_TURN";
	public static final String FINALIZE = "FINALIZE";

	private Events() {
	}
}
