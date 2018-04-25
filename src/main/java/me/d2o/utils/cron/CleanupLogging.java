/*
 *
 * @Author: bo.hanssen
 * Created: Dec 21, 2016 11:22:55 AM
 */
package me.d2o.utils.cron;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The Class CleanupLogging.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:31 PM
 */
@Service
public class CleanupLogging {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${logging.path:}")
	private String path;

	private void checkExpiration(Path file, long timestamp) {
		try {
			File f = file.toFile();
			if (f.exists() && f.isFile()) {
				if (f.lastModified() < timestamp) {
					logger.info("Removed [{}] from system [{}]", f.getName(), f.delete());
				} else {
					logger.info("Skipped [{}] timestamp[{}] now[{}]", f.getName(), f.lastModified(), timestamp);
				}
			}
		} catch (Exception ex) {
			logger.error("Could not handle {}\n{}", file, ex);
		}
	}

	@Scheduled(cron = "${schedule.cleanup.log}")
	private void cleanupLogging() {
		MDC.put("logFileName", "cleanup/" + this.getClass().getSimpleName());
		if (path.isEmpty()) {
			logger.error("No logging path defined in the properties file!");
			return;
		}
		logger.info("Cleanup old log files:");
		long timestamp = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC) * 1000;
		try {
			Files.walk(Paths.get(path)).forEach(file -> checkExpiration(file, timestamp));
		} catch (IOException e) {
			logger.error("Logging cleanup failed {}", e);
		}
	}
}
