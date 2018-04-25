package me.d2o.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.d2o.persistence.model.chat.Conversation;
import me.d2o.persistence.model.general.UserEntity;

/**
 * The Interface ConversationRepository.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:39 PM
 */
public interface ConversationRepository extends JpaRepository<Conversation, String> {

	
	public Conversation findByUser1AndUser2(UserEntity u1, UserEntity u2);
}
