package me.d2o.service.general;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.config.security.AuthUtil;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.persistence.repository.UserRepository;
import me.d2o.statemachine.GameEvent;
import me.d2o.transfermodel.menu.Contact;

/**
 * The Class UserService.
 *
 * @Author: bo.hanssen
 * @since: Jan 10, 2017 4:25:50 PM
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<>();
		getAllUsers().forEach(u -> contacts.add(new Contact(u)));
		return contacts;
	}

	public UserEntity createIfNew(String username, String uniqueId) {
		UserEntity user = userRepository.findByUniqueUserId(uniqueId);
		if (user != null) {
			return user;
		}

		UserEntity newUser = new UserEntity();
		newUser.setUniqueUserId(uniqueId);
		newUser.setUsername(username);
		newUser.setRole("USER");

		userRepository.save(newUser);
		return newUser;
	}

	public void saveUser(UserEntity user) {
		userRepository.save(user);
	}

	public UserEntity getUser() {
		return getUser((Long) SecurityContextHolder.getContext().getAuthentication().getDetails());
	}

	public UserEntity getUser(long id) {
		return userRepository.findOne(id);
	}

	public UserEntity getUser(GameEvent event) {
		return getUser(event.getUserId());
	}

	public UserEntity getUser(Principal principal) {
		return userRepository.findByUsername(principal.getName());
	}

	public UserEntity getUser(String username) {
		return userRepository.findByUsername(username);
	}

	public void authenticate(Principal principal) {
		UserEntity user = getUser(principal);
		this.authenticate(user);
	}

	public void authenticate(UserEntity user) {
		AuthUtil.authenticate(user.getUsername(), user);
	}

	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	public List<UserEntity> getAllTeamMembers() {
		return userRepository.findByRoleNotLike("USER");
	}

	public List<UserEntity> getOnlineUsers() {
		return userRepository.getUserByOnline(true).stream().filter(u -> !"AI".equals(u.getRole()))
				.collect(Collectors.toList());
	}
}
