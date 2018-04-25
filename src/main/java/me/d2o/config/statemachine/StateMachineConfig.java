/**
 *
 */
package me.d2o.config.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.d2o.statemachine.StateMachineConfigurable;

/**
 * Class: StateMachineConfig
 *
 * @author bo.hanssen
 * @since Jan 20, 2017 5:15:53 PM
 *
 */
@Configuration
public class StateMachineConfig {

	@Bean
	public StateMachineConfigurable stateMachineConfigurable() {
		StateMachineConfigurable fsm = new StateMachineConfigurable();
		fsm.addTransition(Events.REGISTER, States.INITIAL, States.NONE, Events.ENROLL);
		fsm.addTransition(Events.CHECK_SLOTS_FILLED, States.INITIAL, States.NONE, Events.EXECUTE_CHECK_SLOTS_FILLED);

		// Setup game
		fsm.addTransition(Events.SETUP, States.INITIAL, States.SETUP, Events.INITIALIZE_GAME);
		fsm.addTransition(Events.INITIALIZE_AI_PLAYER, States.SETUP, States.NONE, Events.HANDLE_AI_PLAYER_SETUP);
		fsm.addTransition(Events.DEPLOY_UNIT_REQUEST, States.SETUP, States.NONE, Events.DEPLOY_UNIT);
		fsm.addTransition(Events.UNDEPLOY_UNIT_REQUEST, States.SETUP, States.NONE, Events.DEPLOY_UNIT);
		fsm.addTransition(Events.UNITS_READY, States.SETUP, States.SET_TOWN, Events.NOTIFY_PLACE_TOWN);

		fsm.addTransition(Events.PLACE_TOWN_REQUEST, States.SET_TOWN, States.NONE, Events.EXECUTE_PLACE_TOWN);
		fsm.addTransition(Events.AI_INIT_TOWN, States.SET_TOWN, States.NONE, Events.AI_PLACE_TOWN);
		fsm.addTransition(Events.PLACE_TOWN_READY, States.SET_TOWN, States.START, Events.INITIALIZE_TURN);

		// Game Play
		fsm.addTransition(Events.SETUP_READY, States.START, States.BUY, Events.COLLECT_RESOURCES, true);
		fsm.addTransition(Events.START_STORE, States.BUY, States.NONE, Events.OPEN_STORE, Events.AI_OPEN_STORE, true);
		fsm.addTransition(Events.PURCHASE_REQUEST, States.BUY, States.NONE, Events.PURCHASE_UNIT, true);

		fsm.addTransition(Events.PURCHASE_TOWN, States.BUY, States.NEW_CAPITOL, Events.NOTIFY_PLACE_TOWN, true);
		fsm.addTransition(Events.PLACE_TOWN_REQUEST, States.NEW_CAPITOL, States.NONE, Events.EXECUTE_PLACE_TOWN, true);
		fsm.addTransition(Events.PLACE_TOWN_READY, States.NEW_CAPITOL, States.BUY, Events.OPEN_STORE, true);

		fsm.addTransition(Events.CLOSE_STORE, States.BUY, States.BATTLE_START, Events.START_BATTLEMODE,
				Events.AI_START_BATTLE, true);
		fsm.addTransition(Events.START_ATTACK, States.BATTLE_START, States.BATTLE_DEFENCE, Events.EXECUTE_ATTACK, true);
		fsm.addTransition(Events.BATTLE_CHECK_OWNER, States.BATTLE_START, States.BATTLE_OWNER,
				Events.EXECUTE_BATTLE_CHECK_OWNER);
		fsm.addTransition(Events.BATTLE_CHECK_OWNER, States.BATTLE_START, States.BATTLE_OWNER,
				Events.EXECUTE_BATTLE_CHECK_OWNER, Events.AI_BATTLE_MOBILISATION, false);
		fsm.addTransition(Events.BATTLE_TRANSFER_REQUEST, States.BATTLE_OWNER, States.BATTLE_START, Events.EXECUTE_BATTLE_TRANSFER);
		fsm.addTransition(Events.START_DEFENCE, States.BATTLE_DEFENCE, States.NONE, Events.NOTIFY_DEFENCE);
		fsm.addTransition(Events.AI_START_DEFENCE, States.BATTLE_DEFENCE, States.BATTLE_CHECK,
				Events.AI_EXECUTE_DEFENCE);
		fsm.addTransition(Events.REQUEST_DEFENCE, States.BATTLE_DEFENCE, States.BATTLE_CHECK, Events.EXECUTE_DEFENCE);
		fsm.addTransition(Events.CHECK_BATTLE, States.BATTLE_CHECK, States.BATTLE_START, Events.EXECUTE_BATTLE_CHECK);
		fsm.addTransition(Events.ATTACK_NEUTRAL, States.BATTLE_DEFENCE, States.BATTLE_CHECK, Events.NEUTRAL_DEFENCE,
				true);
		fsm.addTransition(Events.NO_RESISTANCE, States.BATTLE_DEFENCE, States.BATTLE_START, Events.CHANGE_OWNER, true);

		fsm.addTransition(Events.STOP_BATTLE_MODE, States.BATTLE_START, States.MOBILISATION,
				Events.START_MOBILISATIONMODE, Events.AI_MOBILISATION, true);
		fsm.addTransition(Events.MOBILISATION_REQUEST, States.MOBILISATION, States.NONE, Events.EXECUTE_MOBILISATION,
				true);
		fsm.addTransition(Events.MOBILISATION_RESET, States.MOBILISATION, States.NONE,
				Events.EXECUTE_MOBILISATION_RESET, true);
		fsm.addTransition(Events.MOBILISATION_SUBMT, States.MOBILISATION, States.TERMINATE_TURN,
				Events.EXECUTE_MOBILISATION_SUBMIT, true);

		fsm.addTransition(Events.REMOVE, States.FINISHED, States.MARKED_FOR_DELETION, Events.NONE);
		fsm.addTransition(Events.FINISHED_GAME, States.START_NEXT_PLAYER, States.FINISHED, Events.NONE);

		// Fall back Events (triggered when stuck on state and user connects
		fsm.addTransition(Events.USER_CONNECTED, States.INITIAL, States.NONE, Events.EXECUTE_CHECK_SLOTS_FILLED);
		fsm.addTransition(Events.USER_CONNECTED, States.SETUP, States.NONE, Events.INITIAL_UNITS);
		fsm.addTransition(Events.USER_CONNECTED, States.BATTLE_OWNER, States.NONE, Events.EXECUTE_BATTLE_CHECK_OWNER);
		fsm.addTransition(Events.USER_CONNECTED, States.SET_TOWN, States.NONE, Events.NOTIFY_PLACE_TOWN);
		fsm.addTransition(Events.USER_CONNECTED, States.NEW_CAPITOL, States.NONE, Events.NOTIFY_PLACE_TOWN);
		fsm.addTransition(Events.USER_CONNECTED, States.START, States.NONE, Events.INITIALIZE_TURN);
		fsm.addTransition(Events.USER_CONNECTED, States.BUY, States.NONE, Events.OPEN_STORE, true);
		fsm.addTransition(Events.USER_CONNECTED, States.BATTLE_START, States.NONE, Events.START_BATTLEMODE,
				Events.AI_START_BATTLE, true);
		fsm.addTransition(Events.USER_CONNECTED, States.BATTLE_DEFENCE, States.NONE, Events.NOTIFY_DEFENCE);
		fsm.addTransition(Events.USER_CONNECTED, States.BATTLE_CHECK, States.BATTLE_START, Events.EXECUTE_BATTLE_CHECK);
		fsm.addTransition(Events.USER_CONNECTED, States.MOBILISATION, States.NONE, Events.START_MOBILISATIONMODE, true);
		fsm.addTransition(Events.USER_CONNECTED, States.TERMINATE_TURN, States.START_NEXT_PLAYER,
				Events.EXECUTE_TERMINATE_TURN);
		fsm.addTransition(Events.USER_CONNECTED, States.START_NEXT_PLAYER, States.BUY, Events.COLLECT_RESOURCES);

		return fsm;
	}

}
