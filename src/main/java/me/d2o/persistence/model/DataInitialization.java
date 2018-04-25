package me.d2o.persistence.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.model.scenario.Region;
import me.d2o.persistence.model.scenario.Role;
import me.d2o.persistence.model.scenario.Scenario;
import me.d2o.persistence.model.scenario.Unit;
import me.d2o.persistence.repository.ScenarioRepository;
import me.d2o.service.general.UserService;

/**
 * The Class DataInitialization.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:38 PM
 */
@Component
public class DataInitialization implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ScenarioRepository scenarios;

	@Autowired
	private UserService userService;

	@Autowired
	private Environment env;

	private void createNeutral() {
		try {
			Scenario s = new Scenario();
			s.setTitleKey("neutral");
			Role r = new Role();
			r.setColor("#ddd");
			r.setKey("Neutral");
			Unit unit = new Unit();
			unit.setKey("neutraldefence");
			unit.setCost(0);
			unit.setDefence(3);
			unit.setOffence(0);
			r.addUnit(unit);
			s.addRole(r);
			scenarios.save(s);
		} catch (Exception ex) {
			logger.warn("Neutral force not added: {}", ex);
		}
	}

	private void editUnit(Scenario scenario, String[] kv) {
		String[] keys = kv[0].split("\\.");
		int index = keys.length - 1;
		String leading = keys[index];
		Unit unit;
		Role role;
		logger.info("Editing unit [{} -> {}]", kv[0], kv[1]);
		switch (leading) {
		case "unit":
			role = scenario.findRole(keys[0]);
			unit = new Unit();
			unit.setKey(kv[1]);
			role.addUnit(unit);
			break;
		case "offence":
			role = scenario.findRole(keys[0]);
			unit = role.findUnit(keys[2]);
			unit.setOffence(Integer.parseInt(kv[1]));
			break;
		case "defence":
			role = scenario.findRole(keys[0]);
			unit = role.findUnit(keys[2]);
			unit.setDefence(Integer.parseInt(kv[1]));
			break;
		case "cost":
			role = scenario.findRole(keys[0]);
			unit = role.findUnit(keys[2]);
			unit.setCost(Integer.parseInt(kv[1]));
			break;
		case "can_be_veteran":
			role = scenario.findRole(keys[0]);
			unit = role.findUnit(keys[2]);
			unit.setVeteran(Boolean.parseBoolean(kv[1]));
			break;
		case "start":
			role = scenario.findRole(keys[0]);
			unit = role.findUnit(keys[2]);
			unit.setStartQuantity(Integer.parseInt(kv[1]));
			break;
		default:
			break;
		}
	}

	private void addStoreItem(Scenario scenario, String[] kv) {
		String[] data = kv[0].split("\\.");
		scenario.findRole(data[0]).getItems().put(data[2], Integer.parseInt(kv[1]));
	}

	private void processSubTags(Scenario scenario, String[] kv) {
		Region region;
		if (kv[0].contains("bonus")) {
			String[] keys = kv[0].split("\\.");
			region = scenario.findRegion(keys[0]);
			if ("gold".equals(keys[2])) {
				region.setBonus(Integer.parseInt(kv[1]));
			}
			if ("unit".equals(keys[2])) {
				region.setBonusUnit(Integer.parseInt(kv[1]));
			}
			if ("advantage".equals(keys[2])) {
				region.setAdvantage(Integer.parseInt(kv[1]));
			}
		} else if (kv[0].contains("unit")) {
			editUnit(scenario, kv);
		} else if (kv[0].contains("store")) {
			addStoreItem(scenario, kv);
		} else if (kv[0].contains("vector")) {
			String regionkey = kv[0].split("\\.")[0];
			region = scenario.findRegion(regionkey);
			region.setVector(kv[1]);
			logger.info("Add vector mapping to: {}", regionkey);
		} else if (kv[0].contains("color")) {
			String key = kv[0].split("\\.")[0];
			scenario.findRole(key).setColor(kv[1]);
		}
	}

	private void addRegion(Scenario scenario, String[] kv) {
		Region region = new Region();
		region.setKey(kv[1].trim());
		scenario.addRegion(region);
		logger.info("Add region [{}] to scenario [{}]", kv[1], scenario.getTitleKey());
	}

	private void addBorder(Scenario scenario, String[] kv) {
		String[] border = kv[1].split("<->");
		Region r1 = scenario.findRegion(border[0]);
		Region r2 = scenario.findRegion(border[1]);
		logger.info("Create border [{} <-> {}]", r1.getKey(), r2.getKey());
		r1.addBorder(r2);
		r2.addBorder(r1);
	}

	private void addRole(Scenario scenario, String[] kv) {
		Role role = new Role();
		role.setKey(kv[1]);
		scenario.addRole(role);
	}

	private Scenario checkScenario(Scenario scenario, String title) {
		if (scenarios.exists(title)) {
			return scenarios.findOne(title);
		} else {
			return scenario;
		}
	}

	private Scenario processLine(String line, Scenario scenario) {
		String[] kv = line.split("=");
		switch (kv[0]) {
		case "title":
			scenario = checkScenario(scenario, kv[1]);
			scenario.setTitleKey(kv[1]);
			logger.info("Set titleKey [{}]", kv[1]);
			break;
		case "width":
			scenario.setWidth(Integer.parseInt(kv[1]));
			break;
		case "height":
			scenario.setHeight(Integer.parseInt(kv[1]));
			break;
		case "originx":
			scenario.setOriginx(Integer.parseInt(kv[1]));
			break;
		case "originy":
			scenario.setOriginy(Integer.parseInt(kv[1]));
			break;
		case "neutral":
			scenario.setNeutral(Double.parseDouble(kv[1]));
			break;
		case "neutral.quantity":
			scenario.setNeutralQuantity(Integer.parseInt(kv[1]));
			break;
		case "region":
			addRegion(scenario, kv);
			break;
		case "border":
			addBorder(scenario, kv);
			break;
		case "role":
			addRole(scenario, kv);
			break;
		default:
			processSubTags(scenario, kv);
			break;
		}
		return scenario;
	}

	private void processFile(Path file) throws IOException {
		Scenario scenario = new Scenario();
		for (String line : Files.readAllLines(file)) {
			try {
				if (line.contains("=")) {
					scenario = processLine(line, scenario);
				}
			} catch (Exception ex) {
				logger.warn("Data not correctly initialized: {}", ex);
			}
		}
		if ((scenario.getTitleKey() != null) && !scenario.getTitleKey().isEmpty()) {
			logger.info("Attempting to save [{}].", scenario.getTitleKey());
			scenarios.save(scenario);
			logger.info("Scenario [{}] saved.", scenario.getTitleKey());
		}
	}

	public void createUserAI() {
		for (int i : Arrays.asList(1,2)){
			String name = "AI_"+i;
			UserEntity ai = userService.getUser(name);
			if (ai == null) {
				ai = new UserEntity();
				ai.setLocale(Locale.ENGLISH);
				ai.setNickname(name);
				ai.setReload(false);
				ai.setRole("AI");
				ai.setUsername(name);
				ai.setUniqueUserId(name);
			}
			ai.setOnline(true);
			userService.saveUser(ai);
		}
	}

	public void initialize() throws IOException {
		try {
			createNeutral();
		} catch (Exception e) {
			logger.warn("Something went wrong.", e);
		}
		String base = env.getProperty("game.scenarios");
		Files.walk(Paths.get(base)).forEach(file -> {
			try {
				processFile(file);
			} catch (IOException e) {
				logger.warn("Scenario not added [{}] {}", file.toString(), e);
			} catch (Exception ex) {
				logger.warn("Scenario not added [{}]: {}", file.toString(), ex);
			}
		});

		try {
			createUserAI();
		} catch (Exception ex) {
			logger.warn("AI user not added [{}]", ex);
		}
	}

	@Override
	public void run() {
		try {
			MDC.put("logFileName", "DataInitialization");
			initialize();
		} catch (IOException e) {
			logger.error("Initialization failed. {}", e);
		}
	}
}
