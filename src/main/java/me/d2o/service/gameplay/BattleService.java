/**
 *
 */
package me.d2o.service.gameplay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.persistence.model.game.Battle;
import me.d2o.persistence.repository.BattleRepository;
import me.d2o.statemachine.GameEvent;

/**
 * Class: BattleService
 *
 * @author bo.hanssen
 * @since Feb 3, 2017 8:40:12 AM
 *
 */
@Service
@Transactional
public class BattleService {

	@Autowired
	private BattleRepository battleRepository;

	public Battle initializeBattle(GameEvent event) {
		Battle battle;
		if (battleRepository.exists(event.getGameId())) {
			battle = battleRepository.getOne(event.getGameId());
			battle.reset();
		} else {
			battle = new Battle();
			battle.setId(event.getGameId());
			battleRepository.save(battle);
		}
		return battleRepository.getOne(event.getGameId());
	}

	public Battle getBattle(long gameId) {
		if (battleRepository.exists(gameId)) {
			return battleRepository.getOne(gameId);
		} else {
			return null;
		}
	}

	public Battle getBattle(GameEvent event) {
		return this.getBattle(event.getGameId());
	}

	public void remove(GameEvent event) {
		Battle battle = this.getBattle(event);
		if (battle != null) {
			battleRepository.delete(battle);
		}
	}
}
