package me.d2o.tictactoe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.GameRepository;
import me.d2o.statemachine.StateMachineService;

@Component
public class StartGame {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private StateMachineService fsm;
	
	@EventListener
	protected void startupGame(SpringApplicationEvent event) {
		Game game = new Game();
		gameRepository.save(game);
		fsm.triggerTransition(game.getId(), Events.INITIALIZE);
	}
}
