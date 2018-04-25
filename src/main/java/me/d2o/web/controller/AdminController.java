package me.d2o.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import me.d2o.service.general.UserService;

/**
 * The Class AdminController.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:50 PM
 */
@RestController
@RequestMapping("/secure")
@Secured({ "ROLE_ADMIN" })
public class AdminController {

	@Autowired
	private UserService uservice;

	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView("secure/admin");
		mv.addObject("users", uservice.getAllUsers());
		return mv;
	}
}
