/**
 *
 */
package me.d2o.utils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.service.general.UserService;

/**
 * Class: TestCode
 *
 * @author bo.hanssen
 * @since Jan 22, 2017 12:38:47 AM
 *
 */
@Service
@Transactional
public class StartupConfig {

	@Autowired
	private UserService userService;

	@EventListener
	public void executeTestCode(SpringApplicationEvent event) {
		// Mark client side data for reload
		userService.getAllUsers().forEach(u -> u.setReload(true));
	}

}
