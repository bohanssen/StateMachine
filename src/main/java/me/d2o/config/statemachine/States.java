/**
 *
 */
package me.d2o.config.statemachine;

/**
 * Class: States
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 2:37:18 PM
 *
 */
public class States {

	public static final String NONE = "";
	public static final String INITIAL = "initial";
	public static final String SETUP = "setup";
	public static final String SET_TOWN = "set_town";
	public static final String START = "start";

	// Game play
	public static final String COLLECT = "Collect";
	public static final String BUY = "Buy";
	public static final String NEW_CAPITOL = "add capitol";
	public static final String BATTLE_START = "battle_start";
	public static final String BATTLE_DEFENCE = "battle_defence";
	public static final String BATTLE_CHECK = "battle_check";
	public static final String BATTLE_OWNER = "battle_owner";
	public static final String MOBILISATION = "mobilisation";
	public static final String TERMINATE_TURN = "terminate_turn";
	public static final String START_NEXT_PLAYER = "start_next_player";

	public static final String FINISHED = "finished";
	public static final String MARKED_FOR_DELETION = "delete";

	private States() {
	}
}
