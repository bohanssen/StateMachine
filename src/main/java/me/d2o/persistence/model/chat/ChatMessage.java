package me.d2o.persistence.model.chat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.d2o.persistence.model.general.UserEntity;

/**
 * The Class ChatMessage.
 *
 * @Author: bo.hanssen
 * @since: Dec 21, 2016 1:05:39 PM
 */
@Entity
public class ChatMessage implements Comparable<ChatMessage> {

	@Id
	@GeneratedValue()
	private int id;

	@ManyToOne
	@JsonIgnore
	private Conversation conversation;

	@ManyToOne
	private UserEntity author;

	@ManyToOne
	private UserEntity receiver;

	private String message;
	private long timestamp;

	private boolean delivered;

	@PrePersist
	@PreUpdate
	private void updateTimestamp() {
		this.timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}

	public UserEntity getReceiver() {
		return receiver;
	}

	public void setReceiver(UserEntity receiver) {
		this.receiver = receiver;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
	}

	public String getMessage() {
		return new String(Base64.getDecoder().decode(message));
	}

	public void setMessage(String message) {
		this.message = Base64.getEncoder().encodeToString(message.getBytes());
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	@Override
	public int compareTo(ChatMessage message) {
		return message.getId() - this.id;
	}

}
