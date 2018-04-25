/**
 *
 */
package me.d2o.service.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.general.Invite;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.repository.GameRepository;
import me.d2o.service.general.InviteService;
import me.d2o.service.general.UserService;
import me.d2o.service.scenario.RoleService;
import me.d2o.service.scenario.SceneService;
import me.d2o.statemachine.MachineEvent;
import me.d2o.statemachine.MachineStateEmbeddableEntity;
import me.d2o.transfermodel.menu.GameCreateRequest;
import me.d2o.transfermodel.menu.GameSummary;
import me.d2o.utils.i18n.Translate;

/**
 * Class: GameService
 *
 * @author bo.hanssen
 * @since Jan 19, 2017 5:10:27 PM
 *
 */
@Service
public class GameService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GameRepository gameRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private InviteService inviteService;

	@Autowired
	private SceneService sceneService;

	@Autowired
	private SimpMessagingTemplate mq;

	@Autowired
	private Translate translate;

	public Game getGame(long id) {
		return gameRepo.findOne(id);
	}

	public void continueFlowAsUser(MachineEvent event, UserEntity user) {
		logger.info("Continue flow as user [{}]", user.getNickname());
		event.setUserId(user.getId());
		SecurityContextHolder.clearContext();
		userService.authenticate(user);
	}

	public UserEntity continueFlowAsTurnUser(MachineEvent event) {
		UserEntity user = userService.getUser(this.getGame(event).getTurn().get(0));
		continueFlowAsUser(event, user);
		return user;
	}

	public Game getGame(MachineEvent event) {
		return getGame(event.getGameId());
	}

	public boolean checkTurn(Game game, long userId) {
		boolean check = game.getTurn().get(0).longValue() == userId;
		logger.info("Checking turn passed: {}", check);
		return check;
	}

	private void createSummary(Game game, List<Long> exclude, List<GameSummary> games) {
		if (exclude.contains(game.getId())) {
			return;
		}
		GameSummary summary = new GameSummary();
		summary.setId(game.getId());

		List<String> participants = new ArrayList<>();
		game.getParticipants().values().forEach(par -> participants.add(par.getUser().getNickname()));
		while (participants.size() != game.getScenario().getRoles().size()) {
			participants.add(translate.getMessage("empty-slot", userService.getUser()));
		}
		summary.setParticipants(participants);

		summary.setTitle(translate.getMessage(game.getScenario().getTitleKey(), userService.getUser()));
		summary.setTurn(false);
		games.add(summary);
	}

	public List<Role> getAvailableRoles(long gameId) {
		List<Role> roles = new ArrayList<>();
		Set<Integer> taken = getGame(gameId).getParticipants().keySet();
		getGame(gameId).getScenario().getRoles().values().forEach(r -> {
			if (!taken.contains(r.getId())) {
				roles.add(r);
			}
		});
		return roles;
	}

	public List<GameSummary> getMyGames(List<Long> exclude) {
		List<GameSummary> games = new ArrayList<>();
		participantService.getParticipants(userService.getUser())
				.forEach(participant -> createSummary(participant.getGame(), exclude, games));
		return games;
	}

	public List<GameSummary> getMyInvites(List<Long> exclude) {
		List<GameSummary> games = new ArrayList<>();
		inviteService.getInvites(userService.getUser())
				.forEach(invite -> createSummary(invite.getTarget(), exclude, games));
		return games;
	}

	public List<GameSummary> getOpenGames(List<Long> exclude) {
		List<GameSummary> games = new ArrayList<>();
		participantService.getParticipants(userService.getUser())
				.forEach(participant -> exclude.add(participant.getGame().getId()));
		gameRepo.findByStateAndOpen(new MachineStateEmbeddableEntity(), true)
				.forEach(game -> createSummary(game, exclude, games));
		return games;
	}

	public boolean registerPlayer(Game game, int roleId) {
		if (game.getParticipants().containsKey(roleId)) {
			return false;
		}
		Participant player = new Participant();
		player.setRole(roleService.getRole(roleId));
		player.setUser(userService.getUser());
		game.addParticipant(player);
		return true;
	}

	public void triggerUpdateMenu(Long id) {
		mq.convertAndSend("/topic/updatemenu", id);
	}

	public void removeInvite(Game game) {
		Invite invite = inviteService.getInvite(userService.getUser(), game);
		if (invite != null) {
			inviteService.remove(invite);
		}
	}

	public void createGame(GameCreateRequest request) {
		try {
			Game game = new Game();
			game.setScenario(sceneService.getScenario(request.getKey()));
			game.setOpen(request.getInvitees().length == 0);

			registerPlayer(game, request.getRole());
			gameRepo.save(game);
			int ai = 0;

			if (request.getInvitees().length != 0) {
				for (long userId : request.getInvitees()) {
					UserEntity target = userService.getUser(userId);
					ai = inviteService.create(game, target, ai);
				}
			}
			triggerUpdateMenu(0L);
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}
}
