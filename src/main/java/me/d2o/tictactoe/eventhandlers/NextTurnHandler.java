/**
 *
 */
package me.d2o.tictactoe.eventhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
import me.d2o.config.statemachine.States;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.GameRepository;
import me.d2o.statemachine.MachineEvent;
import me.d2o.statemachine.MachineEventHandler;

/**
 * Class: CapitolInitializeHandler
 *
 * @author bo.hanssen
 * @since Jan 28, 2017 10:20:54 AM
 *
 */
@Service
@Transactional
public class NextTurnHandler extends MachineEventHandler {

	@Autowired
	private GameRepository gameRepository;
	
	@Override
	public void handleEvent(MachineEvent event) {
		Game game = gameRepository.findOne(event.getMachineId());
		char m = game.getState().getState().equals(States.CHECK_PLAYER1) ? 'x' : 'o';
		if (game.checkVictory(m)){
			System.out.println(game.getState().getState() + " has won the game!");
			game.printBoard();
			event.setPropagate(Events.FINALIZE);
		} else {
			event.setPropagate(Events.PLAY);
		}
	}

	@Override
	public String eventType() {
		return Events.NEXT_TURN;
	}

}
