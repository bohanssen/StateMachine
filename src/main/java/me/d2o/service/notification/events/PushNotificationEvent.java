
package me.d2o.service.notification.events;

import org.springframework.context.ApplicationEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import me.d2o.config.constants.Constants;
import me.d2o.persistence.model.general.UserEntity;

/**
 * Class: Notification
 *
 * @author bo.hanssen
 * @since Jan 12, 2017 11:36:44 AM
 *
 */
public class PushNotificationEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1584867935488943297L;
	private ObjectMapper mapper;
	private ObjectNode body;
	private ObjectNode contents;
	private ObjectNode titles;
	private ObjectNode data;
	private ArrayNode recipients;
	private ArrayNode segments;
	private ArrayNode filters;
	private static String appId;

	public PushNotificationEvent() {
		super("NotificationEvent");
		mapper = new ObjectMapper();
		body = mapper.createObjectNode();
	}

	public static void setAppId(String id) {
		appId = id;
	}

	public String getPayload(String onesignalId) throws JsonProcessingException {
		body.put(Constants.ONESIGNAL_ID, onesignalId);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
	}

	public PushNotificationEvent addContent(String locale, String message) {
		if (!body.has(Constants.ONESIGNAL_CONTENTS)) {
			contents = mapper.createObjectNode();
			body.putPOJO(Constants.ONESIGNAL_CONTENTS, contents);
		}
		contents.put(locale, message);
		return this;
	}

	public PushNotificationEvent addContent(String message) {
		return this.addContent("en", message);
	}

	public PushNotificationEvent addTitle(String locale, String title) {
		if (!body.has(Constants.ONESIGNAL_HEADINGS)) {
			titles = mapper.createObjectNode();
			body.putPOJO(Constants.ONESIGNAL_HEADINGS, titles);
		}
		titles.put(locale, title);
		return this;
	}

	public PushNotificationEvent addTitle(String title) {
		return this.addTitle("en", title);
	}

	/*
	 * device. The id string can be obtained client side, see: onesignal.js
	 *
	 * @param id the id
	 * 
	 * @return the push notification event
	 */
	public PushNotificationEvent addRecipient(String id) {
		if (!body.has(Constants.ONESIGNAL_PLAYER_IDS)) {
			recipients = mapper.createArrayNode();
			body.putPOJO(Constants.ONESIGNAL_PLAYER_IDS, recipients);
		}
		recipients.add(id);
		return this;
	}

	public PushNotificationEvent addSegment(String segment) {
		if (!body.has(Constants.ONESIGNAL_SEGMENTS)) {
			segments = mapper.createArrayNode();
			body.putPOJO(Constants.ONESIGNAL_SEGMENTS, segments);
		}
		segments.add(segment);
		return this;
	}

	public PushNotificationEvent put(String key, Object value) {
		if (!body.has(Constants.ONESIGNAL_DATA)) {
			data = mapper.createObjectNode();
			body.putPOJO(Constants.ONESIGNAL_DATA, data);
		}
		data.putPOJO(key, value);
		return this;
	}

	public PushNotificationEvent setUrl(String url) {
		body.put(Constants.ONESIGNAL_URL, url);
		return this;
	}

	private void ensureFilterNodeExists() {
		if (!body.has(Constants.ONESIGNAL_FILTERS)) {
			filters = mapper.createArrayNode();
			body.putPOJO(Constants.ONESIGNAL_FILTERS, filters);
		}
	}

	public PushNotificationEvent roleFilter(String role) {
		ensureFilterNodeExists();
		ObjectNode filter = mapper.createObjectNode();
		filter.put(Constants.ONESIGNAL_FIELD, "tag");
		filter.put(Constants.ONESIGNAL_RELATION, "=");
		filter.put(Constants.ONESIGNAL_KEY, appId + "Role");
		filter.put(Constants.ONESIGNAL_VALUE, role);
		filters.add(filter);
		return this;
	}

	public PushNotificationEvent or() {
		ensureFilterNodeExists();
		ObjectNode filter = mapper.createObjectNode();
		filter.put("operator", "OR");
		filters.add(filter);
		return this;
	}

	public PushNotificationEvent and() {
		ensureFilterNodeExists();
		ObjectNode filter = mapper.createObjectNode();
		filter.put("operator", "AND");
		filters.add(filter);
		return this;
	}

	public PushNotificationEvent sendToUser(UserEntity user) {
		ensureFilterNodeExists();
		ObjectNode filter = mapper.createObjectNode();
		filter.put(Constants.ONESIGNAL_FIELD, "tag");
		filter.put(Constants.ONESIGNAL_RELATION, "=");
		filter.put(Constants.ONESIGNAL_KEY, appId);
		filter.put(Constants.ONESIGNAL_VALUE, Long.toString(user.getId()));
		filters.add(filter);
		return this;
	}

	public PushNotificationEvent sendToUser(long userId) {
		ensureFilterNodeExists();
		ObjectNode filter = mapper.createObjectNode();
		filter.put(Constants.ONESIGNAL_FIELD, "tag");
		filter.put(Constants.ONESIGNAL_RELATION, "=");
		filter.put(Constants.ONESIGNAL_KEY, appId);
		filter.put(Constants.ONESIGNAL_VALUE, Long.toString(userId));
		filters.add(filter);
		return this;
	}

	private void clear() {
		body.remove(Constants.ONESIGNAL_FILTERS);
		body.remove(Constants.ONESIGNAL_SEGMENTS);
		body.remove(Constants.ONESIGNAL_PLAYER_IDS);
	}

	public ApplicationEvent broadcast() {
		clear();
		ensureFilterNodeExists();
		ObjectNode filter = mapper.createObjectNode();
		filter.put(Constants.ONESIGNAL_FIELD, "tag");
		filter.put(Constants.ONESIGNAL_RELATION, "=");
		filter.put(Constants.ONESIGNAL_KEY, "appId");
		filter.put(Constants.ONESIGNAL_VALUE, appId);
		filters.add(filter);
		return this;
	}

	public void enableMocking() {
		clear();
		this.addSegment(Constants.ONESIGNAL_MOCK);
	}
}
