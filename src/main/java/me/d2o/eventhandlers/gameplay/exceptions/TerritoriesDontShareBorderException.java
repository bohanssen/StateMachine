/**
 *
 */
package me.d2o.eventhandlers.gameplay.exceptions;

import me.d2o.statemachine.TransitionException;

/**
 * Class: TerritoriesDontShareBorder
 *
 * @author bo.hanssen
 * @since Feb 3, 2017 10:03:19 AM
 *
 */
public class TerritoriesDontShareBorderException extends TransitionException {

	private static final long serialVersionUID = 8808823711050533903L;

	public TerritoriesDontShareBorderException() {
		super("no-border");
	}

}
