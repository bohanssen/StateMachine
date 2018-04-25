/**
 *
 */
package me.d2o.service.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.scenario.Region;
import me.d2o.persistence.repository.RegionRepository;
import me.d2o.service.general.UserService;
import me.d2o.transfermodel.graphics.RegionClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: RegionService
 *
 * @author bo.hanssen
 * @since Jan 22, 2017 11:57:37 PM
 *
 */
@Service
public class RegionService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private Translate translate;

	@Autowired
	private UserService userService;

	public RegionClientModel getRegionClientModel(int id) {
		logger.debug("User [{}] requested region [{}]", userService.getUser().getId(), id);
		Region region = regionRepository.findOne(id);
		RegionClientModel model = new RegionClientModel();
		model.setName(translate.getMessage(region.getScenario().getTitleKey() + "." + region.getKey(), region.getKey(),
				userService.getUser()));
		model.setId(region.getId());
		model.setVector(region.getVector());
		return model;
	}
}
