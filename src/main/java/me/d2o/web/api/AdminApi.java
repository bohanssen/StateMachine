
package me.d2o.web.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.d2o.persistence.model.DataInitialization;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.UserService;
import me.d2o.transfermodel.admin.UpdateRequest;
import me.d2o.transfermodel.admin.UpdateResponse;

/**
 * Class: AdminApi
 *
 * @author bo.hanssen
 * @since Jan 9, 2017 8:36:19 AM
 *
 */
@RestController
@RequestMapping("/api")
@Secured({ "ROLE_ADMIN" })
public class AdminApi {

	@Autowired
	private UserService uservice;

	@Autowired
	private DataInitialization data;

	private String generateMessage(String name, String property) {
		return "Succesfully granted " + property + " status to user: " + name;
	}

	@Transactional
	public void updateRole(UserEntity user, UpdateResponse response, String role) {
		user.setRole(role.toUpperCase());
		response.setLevel("success");
		response.setMessage(generateMessage(user.getUsername(), role));
		response.setStatus(true);
	}

	@Transactional
	@RequestMapping(value = "/updateUserRights", method = RequestMethod.POST, produces = "application/json")
	public UpdateResponse update(@RequestBody UpdateRequest request, HttpSession session) {
		UpdateResponse response = new UpdateResponse(request);
		UserEntity target = uservice.getUser(request.getId());
		UserEntity requester = uservice.getUser();
		String role = request.getProperty();
		updateRole(target, response, role);
		if (target.equals(requester)) {
			session.invalidate();
			response.setAlert(
					"Congratulations you lost your admin rights, hava a nice day!\n\n\nps. Provide some candy for the team and I'll reset it for you ;-)");
		}
		return response;
	}

	@RequestMapping(value = "/loadDB", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public UpdateResponse loadDb() {
		uservice.getAllUsers().forEach(u -> u.setReload(true));
		UpdateResponse response = new UpdateResponse();
		response.setLevel("info");
		response.setMessage("Updating DB, this could take a while.");
		(new Thread(data)).start();
		return response;
	}
}
