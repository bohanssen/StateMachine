/**
 *
 */
package me.d2o.service.general;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.config.statemachine.Events;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.general.Invite;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.repository.InviteRepository;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.notification.NotificationFactory;
import me.d2o.statemachine.StateMachineService;
import me.d2o.transfermodel.menu.JoinRequest;

/**
 * Class: InviteService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 11:18:33 AM
 *
 */
@Service
public class InviteService {

	@Autowired
	private InviteRepository inviteRepository;

	@Autowired
	private NotificationFactory notify;

	@Autowired
	private UserService userService;

	@Autowired
	private StateMachineService fsm;

	@Autowired
	private GameService gameService;

	public List<Invite> getInvites(UserEntity user) {
		return inviteRepository.findByUser(user);
	}

	public Invite getInvite(UserEntity user, Game game) {
		return inviteRepository.findByUserAndGame(user, game);
	}

	public int create(Game game, UserEntity user, int ai) {
		if ("AI".equals(user.getRole())) {
			JoinRequest request = new JoinRequest();
			request.setGameId(game.getId());
			request.setRole(gameService.getAvailableRoles(game.getId()).get(ai).getId());
			fsm.triggerTransition(user.getId(), game.getId(), Events.REGISTER, request);
			ai += 1;
		} else {
			inviteRepository.save(new Invite(game, user));
			notify.sendPushNotification("Invite", userService.getUser().getNickname() + " invited you to a game.",
					user);
		}
		return ai;
	}

	public void remove(Invite invite) {
		inviteRepository.delete(invite);
	}
}
