package me.d2o.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class AuthenticationResource.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:54 PM
 */
@Controller
@RequestMapping("/api/session")
public class AuthenticationResource {

	@Autowired
	AuthenticationManager authenticationManager;

	
	@RequestMapping(method = RequestMethod.POST)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}