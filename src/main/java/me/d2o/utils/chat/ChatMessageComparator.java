/**
 *
 */
package me.d2o.utils.chat;

import java.util.Comparator;

import me.d2o.persistence.model.chat.ChatMessage;

/**
 * Class: ChatMessageComparator
 *
 * @author bo.hanssen
 * @since Feb 10, 2017 10:43:39 AM
 *
 */
public class ChatMessageComparator implements Comparator<ChatMessage> {

	@Override
	public int compare(ChatMessage m1, ChatMessage m2) {
		return m1.compareTo(m2);
	}

}
