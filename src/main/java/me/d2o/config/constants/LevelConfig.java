/**
 *
 */
package me.d2o.config.constants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.d2o.utils.game.GameLevelTitles;

/**
 * Class: LevelConfig
 *
 * @author bo.hanssen
 * @since Jan 31, 2017 5:06:55 PM
 *
 */
@Configuration
public class LevelConfig {

	@Bean
	public GameLevelTitles gameLevelTitles() {
		GameLevelTitles level = new GameLevelTitles();
		level.addLevel(60, "title1");
		level.addLevel(80, "title2");
		level.addLevel(120, "title3");
		level.addLevel(160, "title4");
		level.addLevel(200, "title5");
		level.addLevel(240, "title6");
		level.addLevel(280, "title7");
		level.addLevel(320, "title8");
		level.addLevel(360, "title9");
		level.addLevel(400, "title10");
		level.addLevel(440, "title11");
		level.addLevel(480, "title12");
		level.addLevel(520, "title13");
		level.addLevel(560, "title14");
		level.addLevel(600, "title15");
		level.addLevel(640, "title16");
		level.addLevel(700, "title17");
		level.addLevel(800, "title18");
		return level;
	}
}
