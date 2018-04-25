package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.game.Game;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.model.scenario.Region;
import me.d2o.persistence.model.scenario.Role;

/**
 * The Interface TerritoryRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:36 PM
 */
public interface TerritoryRepository extends JpaRepository<Territory, Integer> {

	public List<Territory> findTerritoryByGame(Game game);

	public List<Territory> findTerritoryByGameAndRole(Game game, Role role);

	public List<Territory> findTerritoryByGameIdAndUserId(long gameId, long userId);

	public List<Territory> findTerritoryByGameIdAndUserIdAndTown(long gameId, long userId, boolean town);

	public Territory findTerritoryByGameIdAndRegionId(long gameId, int regionId);

	public Territory findTerritoryByGameAndUserAndRegion(Game game, UserEntity user, Region region);

	public Territory findByGameAndKey(Game game, String key);
}
