/**
 *
 */
package me.d2o.web.api;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.d2o.service.scenario.RegionService;
import me.d2o.transfermodel.graphics.RegionClientModel;

/**
 * Class: RegionApi
 *
 * @author bo.hanssen
 * @since Jan 18, 2017 12:26:55 PM
 *
 */
@RestController
@RequestMapping("/api")
@Secured({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER", "ROLE_AI" })
public class RegionApi {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegionService regionService;

	@RequestMapping(value = "/region/{id}/", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public RegionClientModel getRegion(@PathVariable(value = "id") int id) {
		logger.info("Loading region.");
		return regionService.getRegionClientModel(id);
	}
}
