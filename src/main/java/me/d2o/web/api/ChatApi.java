/**
 *
 */
package me.d2o.web.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.d2o.persistence.model.chat.ChatMessage;
import me.d2o.persistence.model.general.UserEntity;
import me.d2o.service.general.ChatService;
import me.d2o.service.general.UserService;
import me.d2o.utils.chat.ChatMessageComparator;

/**
 * Class: ChatApi
 *
 * @author bo.hanssen
 * @since Jan 16, 2017 4:06:00 PM
 *
 */
@RestController
@RequestMapping("/api")
@Secured({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
public class ChatApi {

	@Autowired
	private ChatService chatService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/conversation/{id}/", method = RequestMethod.GET, produces = "application/json")
	public List<ChatMessage> getConversation(@PathVariable(value = "id") long id) {
		UserEntity requester = userService.getUser();
		UserEntity target = userService.getUser(id);
		List<ChatMessage> messages = chatService.getConversation(target, requester).getMessages();
		messages.sort(new ChatMessageComparator());
		return messages.stream().filter(m -> m.isDelivered()).collect(Collectors.toList());
	}
}
