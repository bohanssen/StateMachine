package me.d2o.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class HomeController.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:48 PM
 */
@Controller
public class HomeController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${redirectUrl}")
	private String redirectUrl;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		logger.debug("Opened main page");
		ModelAndView mv = new ModelAndView("home/home");
		mv.addObject("redirectUrl", redirectUrl);
		return mv;
	}
}
