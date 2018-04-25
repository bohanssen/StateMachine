package me.d2o.statemachine;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import me.d2o.Application;
import me.d2o.config.statemachine.Events;
import me.d2o.config.statemachine.States;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.repository.GameRepository;
import me.d2o.persistence.repository.ParticipantRepository;
import me.d2o.persistence.repository.RoleRepository;
import me.d2o.persistence.repository.UserRepository;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.gameplay.TerritoryService;
import me.d2o.service.general.UserService;
import me.d2o.transfermodel.gameplay.DeployRequest;
import me.d2o.transfermodel.menu.GameCreateRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class IntegrationTestAI {

	@Autowired
	private GameService gameService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private TestHelper helper;

	@Autowired
	private GameRepository games;

	@Test
	public void fullGameTest() throws InterruptedException, TimeoutException {
		userService.authenticate(userRepository.findByUsername("AI_1"));

		GameCreateRequest request = new GameCreateRequest();
		request.setKey("france");
		request.setRole(roleRepository.findRoleByKeyAndScenarioTitleKey("player1", "france").getId());
		request.setInvitees(new long[] { userRepository.findByUsername("AI_2").getId() });
		gameService.createGame(request);

		Thread.sleep(1000);
		long gameId = gameService.getMyGames(new ArrayList<Long>()).get(0).getId();
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.USER_CONNECTED, false);
		Thread.sleep(1000);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.USER_CONNECTED, false);
		Thread.sleep(1000);
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.USER_CONNECTED, false);
		Thread.sleep(1000);

		DeployRequest deploy = new DeployRequest();
		deploy.setGameId(gameId);
		Participant player = participantRepository.getPlayerByUserIdAndGameId(userService.getUser().getId(), gameId);
		List<Territory> territories = territoryService.getTerritories(player);
		deploy.setRegion(territories.get(0).getRegion().getId());
		fsm.triggerTransition(userService.getUser().getId(), gameId, Events.PLACE_TOWN_REQUEST, deploy);

		int time = 0;
		while (!helper.getState(gameId).equals(States.FINISHED)) {
			Thread.sleep(1000);
			time += 1;
			if (time == 600) {
				throw new TimeoutException();
			}
		}
		System.out.println("Test Finished in: " + time / 60.0 + " minutes.");
		assertEquals(States.FINISHED, helper.getState(gameId));
		helper.clear(gameId);
	}
}
