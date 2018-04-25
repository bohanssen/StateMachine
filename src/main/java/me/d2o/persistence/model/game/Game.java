package me.d2o.persistence.model.game;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.Version;

import me.d2o.persistence.model.scenario.Scenario;
import me.d2o.statemachine.MachineStateEmbeddableEntity;

/**
 * The Class Game.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:31 PM
 */
@Entity
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Embedded
	private MachineStateEmbeddableEntity state;

	private boolean open = true;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
	@MapKeyColumn(name = "role_id")
	private Map<Integer, Participant> players = new HashMap<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.ALL)
	@MapKeyColumn(name = "key")
	private Map<String, Territory> territories = new HashMap<>();

	@ElementCollection
	private Map<Integer, Long> turn;

	@ManyToOne
	private Scenario scenario;

	@Version
	private int version;
	
	@PrePersist
	public void init() {
		this.state = new MachineStateEmbeddableEntity();
		this.turn = new HashMap<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public Map<Integer, Participant> getParticipants() {
		return players;
	}

	public void setParticipants(Map<Integer, Participant> players) {
		this.players = players;
	}

	public void addParticipant(Participant player) {
		player.setGame(this);
		this.players.put(player.getRole().getId(), player);
	}

	public Map<String, Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(Map<String, Territory> territories) {
		this.territories = territories;
	}

	public void addTerritory(Territory territory) {
		this.territories.put(territory.getKey(), territory);
		territory.setGame(this);
	}

	public Map<Integer, Long> getTurn() {
		return turn;
	}

	public void setTurn(Map<Integer, Long> turn) {
		this.turn = turn;
	}

	public MachineStateEmbeddableEntity getState() {
		return state;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public int getVersion() {
		return version;
	}

}
