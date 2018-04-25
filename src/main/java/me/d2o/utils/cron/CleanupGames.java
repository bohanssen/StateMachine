/*
 *
 * @Author: bo.hanssen
 * Created: Dec 21, 2016 11:23:09 AM
 */
package me.d2o.utils.cron;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import me.d2o.config.statemachine.Events;
import me.d2o.config.statemachine.States;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.repository.GameRepository;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.StateMachineService;

/**
 * The Class CleanupGames.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:41 PM
 */
@Service
public class CleanupGames {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private long serviceID;
	
	@Autowired
	private GameRepository games;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private UserService userService;
	
	@PostConstruct
	private void init(){
		serviceID = userService.getUser("AI_1").getId();
	}
	
	@Autowired
	@Scheduled(cron = "${schedule.cleanup.game}")
	private void cleanup() {
		MDC.put("logFileName", "cleanup/" + this.getClass().getSimpleName());
		logger.info("Cleanup old games:");
		for (Game game : games.findAll()) {
			if (game.getState().getState().equals(States.MARKED_FOR_DELETION)) {
				logger.info("Game [{}] is marked for deletion",game);
				games.delete(game);
				logger.info("Deleted Game [{}]",game);
			} else if (game.getState().getState().equals(States.FINISHED)) {
				fsm.triggerTransition(serviceID, game.getId(), Events.REMOVE, false);
				logger.info("Marked Game [{}] for deletion",game);
			}
		}

	}
}
