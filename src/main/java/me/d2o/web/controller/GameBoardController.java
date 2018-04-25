/**
 *
 */
package me.d2o.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import me.d2o.service.gameplay.GameService;

/**
 * Class: GameBoardController
 *
 * @author bo.hanssen
 * @since Jan 24, 2017 10:32:20 PM
 *
 */
@Controller
@RequestMapping("/secure")
public class GameBoardController {

	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/game/{id}/", method = RequestMethod.GET)
	public ModelAndView board(@PathVariable(value = "id") long id) {
		ModelAndView mv = new ModelAndView("secure/board");
		mv.addObject("gameId", id);
		mv.addObject("scenario", gameService.getGame(id).getScenario().getTitleKey());
		return mv;
	}
}
