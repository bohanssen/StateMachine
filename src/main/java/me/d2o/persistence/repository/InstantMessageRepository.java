package me.d2o.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.chat.ChatMessage;
import me.d2o.persistence.model.general.UserEntity;

/**
 * The Interface InstantMessageRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:38 PM
 */
public interface InstantMessageRepository extends JpaRepository<ChatMessage, Integer> {

	public List<ChatMessage> findByReceiverAndDelivered(UserEntity receiver, boolean delivered);

	public List<ChatMessage> findByDeliveredAndTimestampLessThan(boolean delivered, long timestamp);
}
