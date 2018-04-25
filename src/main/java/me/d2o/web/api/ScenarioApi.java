/**
 *
 */
package me.d2o.web.api;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.d2o.service.general.UserService;
import me.d2o.service.scenario.SceneService;
import me.d2o.transfermodel.graphics.ScenarioClientModel;
import me.d2o.transfermodel.menu.ScenarioModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: ScenarioApi
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 11:04:10 PM
 *
 */
@RestController
@RequestMapping("/api")
@Secured({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER", "ROLE_AI" })
public class ScenarioApi {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SceneService sceneService;

	@Autowired
	private Translate translate;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/scenes/", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public List<ScenarioModel> getScenes() {
		logger.info("Scenario list requested.");
		return sceneService.getAvailableScenarios();
	}

	@RequestMapping(value = "/scenario/{id}/", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ScenarioClientModel getSscenario(@PathVariable(value = "id") String id) {
		logger.info("Scenario data requested.");
		ScenarioClientModel model = new ScenarioClientModel(sceneService.getScenario(id));
		model.setTitle(translate.getMessage(model.getTitle(), userService.getUser()));
		return model;
	}
}
