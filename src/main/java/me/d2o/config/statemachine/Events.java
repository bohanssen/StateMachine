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

	public static final String NONE = "";

	public static final String REGISTER = "register";
	public static final String ENROLL = "enroll";

	public static final String CHECK_SLOTS_FILLED = "check_slots_filled";
	public static final String EXECUTE_CHECK_SLOTS_FILLED = "execute_check_slots_filled";

	public static final String SETUP = "setup";
	public static final String INITIALIZE_GAME = "initialize_game";

	public static final String USER_CONNECTED = "user_connected";
	public static final String INITIAL_UNITS = "initial_units";
	public static final String DEPLOY_UNIT_REQUEST = "deploy_request";
	public static final String UNDEPLOY_UNIT_REQUEST = "undeploy_request";
	public static final String DEPLOY_UNIT = "deploy";
	public static final String UNITS_READY = "units_ready";

	public static final String NOTIFY_PLACE_TOWN = "notify_place_town";
	public static final String PLACE_TOWN_REQUEST = "place_town_request";
	public static final String EXECUTE_PLACE_TOWN = "execute_place_town";
	public static final String PLACE_TOWN_READY = "place_town_ready";
	public static final String INITIALIZE_TURN = "init_turn";
	public static final String SETUP_READY = "setup_ready";

	// Game play events
	public static final String COLLECT_RESOURCES = "collect";

	public static final String START_STORE = "start_store";
	public static final String OPEN_STORE = "open_store";
	public static final String PURCHASE_REQUEST = "purchase_request";
	public static final String PURCHASE_UNIT = "purchase_unit";
	public static final String PURCHASE_TOWN = "purchase town";
	public static final String CLOSE_STORE = "close_store";

	public static final String START_BATTLEMODE = "start_battlemode";
	public static final String START_ATTACK = "attack";
	public static final String EXECUTE_ATTACK = "execute_attack";
	public static final String START_DEFENCE = "start_defence";
	public static final String NOTIFY_DEFENCE = "notify_defence";
	public static final String REQUEST_DEFENCE = "defence";
	public static final String EXECUTE_DEFENCE = "execute_defence";
	public static final String CHECK_BATTLE = "check_battle";
	public static final String BATTLE_CHECK_OWNER = "check owner";
	public static final String EXECUTE_BATTLE_CHECK_OWNER = "execute check owner";
	public static final String BATTLE_TRANSFER_REQUEST = "battle transfer request";
	public static final String EXECUTE_BATTLE_TRANSFER = "execute battle transfer";
	public static final String EXECUTE_BATTLE_CHECK = "execute_battle_check";
	public static final String ATTACK_NEUTRAL = "attack_neutral";
	public static final String NEUTRAL_DEFENCE = "neutral_defence";
	public static final String NO_RESISTANCE = "no_resistance";
	public static final String CHANGE_OWNER = "change_owner";
	public static final String STOP_BATTLE_MODE = "stop_battle";

	public static final String START_MOBILISATIONMODE = "start_mobilasationmode";
	public static final String MOBILISATION_REQUEST = "mobilisation_request";
	public static final String EXECUTE_MOBILISATION = "execute_mobilisation";
	public static final String MOBILISATION_RESET = "mobilisation_reset";
	public static final String EXECUTE_MOBILISATION_RESET = "execute_mobilastion_reset";
	public static final String MOBILISATION_SUBMT = "mobilisation_submit";
	public static final String EXECUTE_MOBILISATION_SUBMIT = "execute_mobilastion_submit";

	public static final String EXECUTE_TERMINATE_TURN = "terminate_turn";
	public static final String FINISHED_GAME = "finished_game";

	// AI events
	public static final String INITIALIZE_AI_PLAYER = "init ai player";
	public static final String HANDLE_AI_PLAYER_SETUP = "ai player setup";
	public static final String AI_INIT_TOWN = "ai init town";
	public static final String AI_PLACE_TOWN = "ai place town";
	public static final String AI_OPEN_STORE = "ai_store";
	public static final String AI_START_BATTLE = "ai_battle";
	public static final String AI_MOBILISATION = "ai_mobilisation";
	public static final String AI_START_DEFENCE = "ai_start_defence";
	public static final String AI_EXECUTE_DEFENCE = "ai_execute_defence";
	public static final String AI_BATTLE_MOBILISATION = "ai_battle_mobilisation";

	public static final String REMOVE = "remove";

	private Events() {
	}
}
