package me.d2o.persistence.model.chat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import me.d2o.persistence.model.general.UserEntity;

/**
 * The Class Conversation.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:41 PM
 */
@Entity
public class Conversation {

	@Id
	private String id;

	@ManyToOne
	private UserEntity user1;

	@ManyToOne
	private UserEntity user2;

	@OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy
	List<ChatMessage> messages = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserEntity getUser1() {
		return user1;
	}

	public void setUser1(UserEntity user1) {
		this.user1 = user1;
	}

	public UserEntity getUser2() {
		return user2;
	}

	public void setUser2(UserEntity user2) {
		this.user2 = user2;
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessage> messages) {
		this.messages = messages;
	}

	public void addMessage(ChatMessage message) {
		message.setConversation(this);
		this.messages.add(message);
	}
}
