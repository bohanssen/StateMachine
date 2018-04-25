/**
 *
 */
package me.d2o.service.scenario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.model.scenario.Scenario;
import me.d2o.persistence.repository.ScenarioRepository;
import me.d2o.service.gameplay.GameService;
import me.d2o.service.general.UserService;
import me.d2o.transfermodel.menu.PlayerModel;
import me.d2o.transfermodel.menu.ScenarioModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: SceneService
 *
 * @author bo.hanssen
 * @since Jan 19, 2017 9:09:20 AM
 *
 */
@Service
public class SceneService {

	@Autowired
	private ScenarioRepository sceneRepo;

	@Autowired
	private Translate translate;

	@Autowired
	private UserService uService;

	@Autowired
	private GameService gameService;

	private ScenarioModel initializeTemplate(Scenario scene, UserEntity user) {
		return new ScenarioModel(translate.getMessage(scene.getTitleKey(), user),
				translate.getMessage(scene.getTitleKey() + ".summary", user), scene.getTitleKey());
	}

	private void addRole(ScenarioModel template, UserEntity user, Role r) {
		String title = translate.getMessage(template.getKey() + "." + r.getKey(), user);
		String summary = translate.getMessage(template.getKey() + "." + r.getKey() + ".summary", user);
		template.getRoles().add(new PlayerModel(r.getId(), title, summary));
	}

	private void addScene(List<ScenarioModel> scenes, UserEntity user, Scenario scene) {
		if (!"neutral".equals(scene.getTitleKey())) {
			ScenarioModel template = initializeTemplate(scene, user);
			scene.getRoles().values().forEach(r -> addRole(template, user, r));
			scenes.add(template);
		}
	}

	public ScenarioModel getOpenGame(long gameId) {
		UserEntity user = uService.getUser();
		ScenarioModel template = initializeTemplate(gameService.getGame(gameId).getScenario(), user);
		gameService.getAvailableRoles(gameId).forEach(r -> addRole(template, user, r));
		return template;
	}

	public List<ScenarioModel> getAvailableScenarios() {
		UserEntity user = uService.getUser();
		List<ScenarioModel> scenes = new ArrayList<>();
		sceneRepo.findAll().forEach(scene -> addScene(scenes, user, scene));
		return scenes;
	}

	public Scenario getScenario(String key) {
		return sceneRepo.findOne(key);
	}
}
