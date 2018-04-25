/*
 *
 * @Author: bo.hanssen
 * Created: Dec 21, 2016 11:23:05 AM
 */
package me.d2o.utils.cron;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import me.d2o.persistence.model.chat.ChatMessage;
import me.d2o.persistence.repository.InstantMessageRepository;

/**
 * The Class CleanupIM.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:39 PM
 */
@Service
public class CleanupIM {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InstantMessageRepository messages;

	@Scheduled(cron = "${schedule.cleanup.im}")
	private void cleanup() {
		MDC.put("logFileName", "cleanup/" + this.getClass().getSimpleName());
		logger.info("Cleanup old chat messages:");
		long timestamp = LocalDateTime.now().minusHours(168).toEpochSecond(ZoneOffset.UTC);
		for (ChatMessage message : messages.findByDeliveredAndTimestampLessThan(true, timestamp)) {
			message.getConversation().getMessages().remove(message);
			logger.info("Remove message [{}]", message.getId());
			messages.delete(message);
		}
	}
}
