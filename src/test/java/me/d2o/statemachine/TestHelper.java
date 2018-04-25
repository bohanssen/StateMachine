package me.d2o.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.persistence.repository.GameRepository;

@Service
public class TestHelper {

	@Autowired
	private GameRepository games;

	@Transactional(readOnly = true)
	public String getState(long gameId) {
		return games.getOne(gameId).getState().getState();
	}

	@Transactional
	public void clear(long gameId) {
		games.delete(games.findOne(gameId));
	}
}
