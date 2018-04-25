/**
 *
 */
package me.d2o.tictactoe.eventhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.statemachine.Events;
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
public class InitializeGameEventHandler extends MachineEventHandler {

	@Autowired
	private GameRepository gameRepository;
	
	@Override
	public void handleEvent(MachineEvent event) {
		System.out.println("Welcome by a lame game called tic tac toe!");
		Game game = gameRepository.findOne(event.getMachineId());
		game.printBoard();
		event.setPropagate(Events.PLAY);
	}

	@Override
	public String eventType() {
		return Events.INITIALIZE;
	}

}
