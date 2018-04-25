/**
 *
 */
package me.d2o.web.api;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.d2o.service.general.UserService;
import me.d2o.utils.i18n.Dictionary;
import me.d2o.utils.i18n.Translate;

/**
 * Class: DictionaryApi
 *
 * @author bo.hanssen
 * @since Jan 29, 2017 3:18:09 PM
 *
 */
@RestController
@RequestMapping("/api")
public class DictionaryApi {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Dictionary dictionary;

	@Autowired
	private Translate translate;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/dictionary/", method = RequestMethod.GET, produces = "application/json")
	public Map<String, String> getDictionary() {
		logger.info("Load dictionary for user: [{}]", userService.getUser().getUsername());
		HashMap<String, String> dic = new HashMap<>();
		for (String key : dictionary.getKeys()) {
			dic.put(key, translate.getMessage(key, userService.getUser()));
		}
		return dic;
	}
}
