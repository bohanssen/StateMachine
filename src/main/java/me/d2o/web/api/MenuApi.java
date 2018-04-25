
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
import me.d2o.transfermodel.menu.Contact;
import me.d2o.transfermodel.menu.ScenarioModel;

/**
 * Class: CreateGameApi
 *
 * @author bo.hanssen
 * @since Jan 15, 2017 10:52:03 AM
 *
 */
@RestController
@RequestMapping("/api")
@Secured({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER", "ROLE_AI" })
public class MenuApi {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SceneService sceneService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/contacts/", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public List<Contact> getContacts() {
		logger.debug("Contact list requested.");
		return userService.getContacts();
	}

	@RequestMapping(value = "/roles/{id}/", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ScenarioModel getAvailableRoles(@PathVariable(value = "id") long gameID) {
		return sceneService.getOpenGame(gameID);
	}
}
