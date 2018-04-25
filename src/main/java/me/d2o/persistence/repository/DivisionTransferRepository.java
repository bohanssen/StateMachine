package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.DivisionTransfer;
import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Territory;

/**
 * The Interface DivisionTransferRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:41 PM
 */
public interface DivisionTransferRepository extends JpaRepository<DivisionTransfer, Integer> {

	
	public DivisionTransfer findDivisionTransferBySuplierAndConsumerAndUnitKey(Territory suplier, Territory consumer,
			String unitKey);

	
	public List<DivisionTransfer> findDivisionTransferByGame(Game game);
}
