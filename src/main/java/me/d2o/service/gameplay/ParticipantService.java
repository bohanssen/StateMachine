/**
 *
 */
package me.d2o.service.gameplay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Participant;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.repository.ParticipantRepository;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.GameEvent;
import me.d2o.transfermodel.graphics.PlayerInfoModel;
import me.d2o.utils.game.GameLevelTitles;
import me.d2o.utils.i18n.Translate;

/**
 * Class: ParticipantService
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 11:13:30 AM
 *
 */
@Service
public class ParticipantService {

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private GameLevelTitles levelTitles;

	@Autowired
	private BattleService battleService;

	@Autowired
	private Translate translate;

	public List<Participant> getParticipants(UserEntity user) {
		return participantRepository.getPlayerByUser(user);
	}

	public Participant getParticipant(GameEvent event) {
		return participantRepository.getPlayerByUserAndGame(userService.getUser(event), gameService.getGame(event));
	}

	public PlayerInfoModel getInfo(long gameId, Participant player) {
		Game game = gameService.getGame(gameId);
		Battle battle = battleService.getBattle(gameId);
		String info = "";
		if (battle != null && (battle.getDefence() == null || battle.getDefence().isEmpty())) {
			info = battle.getDefendor().getUser().getNickname() + " is under attack!";
		}
		String name = game.getTurn().containsKey(0) ? userService.getUser(game.getTurn().get(0)).getNickname() : "";
		return new PlayerInfoModel(player.getCash(), this.getWorth(player), player.getLevel(),
				player.getRole().getColor(), name, info);
	}

	public PlayerInfoModel getInfo(long gameId) {
		return this.getInfo(gameId,
				participantRepository.getPlayerByUserIdAndGameId(userService.getUser().getId(), gameId));
	}

	public int getWorth(Participant participant) {
		int worth = 0;
		for (Territory territory : territoryService.getTerritories(participant)) {
			worth += 20;
			worth += territory.getRegion().getBonus();
		}
		worth = (worth > 1000) ? 1000 : worth;
		participant.setLevel(translate.getMessage(levelTitles.getLevel(worth), participant.getUser()));
		return worth;
	}
}
