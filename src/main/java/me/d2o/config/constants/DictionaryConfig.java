/**
 *
 */
package me.d2o.config.constants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.d2o.utils.i18n.Dictionary;

/**
 * Class: Dictionary
 *
 * @author bo.hanssen
 * @since Jan 29, 2017 3:11:26 PM
 *
 */
@Configuration
public class DictionaryConfig {

	@Bean
	public Dictionary dictionary() {
		Dictionary d = new Dictionary();
		d.add("menu.units");
		d.add("menu.unit.selected");
		d.add("menu.town.disclaimer");
		d.add("menu.town.error");
		d.add("menu.town.name");
		d.add("menu.town");
		d.add("menu.turn");
		d.add("menu.capitol");
		d.add("menu.close");
		d.add("menu.attacker");
		d.add("menu.defendor");
		d.add("menu.atackbuton");
		d.add("menu.skipbattle");
		d.add("menu.battleTitle");
		d.add("menu.battle.atack");
		d.add("menu.battle.available");
		d.add("menu.battle.resting");
		d.add("menu.battle.as");
		d.add("menu.defenceTitle");
		d.add("menu.battle.defence");
		d.add("menu.battle.ds");
		d.add("menu.mobi.src");
		d.add("menu.mobi.unit");
		d.add("menu.mobi.transfer");
		d.add("menu.mobi.reset");
		d.add("menu.mobi.submit");
		d.add("menu.mobi.clear");
		d.add("menu.battle.victoryTitle");
		d.add("menu.battle.victroysummary");
		return d;
	}
}
