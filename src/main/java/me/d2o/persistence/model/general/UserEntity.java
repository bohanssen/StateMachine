package me.d2o.persistence.model.general;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class UserEntity.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:26:14 PM
 */
@Entity
@Table(name = "UserData")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -3928529318478645592L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@JsonIgnore
	private String username;

	@NotNull
	@JsonIgnore
	private String uniqueUserId;

	@JsonIgnore
	private String role;

	@JsonIgnore
	private boolean online;

	@JsonIgnore
	private Locale locale;

	private String nickname;

	private boolean reload;

	/*
	 * GETTERS & SETTERS
	 */

	@PrePersist
	@PreUpdate
	public void setNickname() {
		if (nickname == null || nickname.isEmpty()) {
			nickname = username;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUniqueUserId() {
		return uniqueUserId;
	}

	public void setUniqueUserId(String uniqueUserId) {
		this.uniqueUserId = uniqueUserId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}
}
