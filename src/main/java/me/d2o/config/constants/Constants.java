
package me.d2o.config.constants;

/**
 * Class: Constants
 *
 * @author bo.hanssen
 * @since Jan 10, 2017 4:29:10 PM
 *
 */
public class Constants {

	public static final String AUTHENTICATED = "authenticated";
	public static final String UTF8 = "UTF-8";
	public static final String DEFAULTLEVEL = "SUCCESS";
	public static final String NOTIFY_QUEUE = "/queue/notification";
	public static final String NOTIFY_TOPIC = "/topic/notification";

	// OneSignalFields
	public static final String ONESIGNAL_FIELD = "field";
	public static final String ONESIGNAL_PLAYER_IDS = "include_player_ids";
	public static final String ONESIGNAL_SEGMENTS = "included_segments";
	public static final String ONESIGNAL_FILTERS = "filters";
	public static final String ONESIGNAL_RELATION = "relation";
	public static final String ONESIGNAL_KEY = "key";
	public static final String ONESIGNAL_VALUE = "value";
	public static final String ONESIGNAL_MOCK = "Mocking";
	public static final String ONESIGNAL_CONTENTS = "contents";
	public static final String ONESIGNAL_ID = "app_id";
	public static final String ONESIGNAL_HEADINGS = "headings";
	public static final String ONESIGNAL_DATA = "data";
	public static final String ONESIGNAL_URL = "url";

	// Game
	public static final String USERSTATUS = "userstatus";
	public static final String USERID = "userid";
	public static final String ONLINE = "online";
	public static final String NICKNAME = "nickname";
	public static final String SCENARIOS = "scenarios";
	public static final String ROLES = "roles";
	public static final String AGGRESSOR = "aggressor";
	public static final String DEFENDOR = "defendor";
	public static final String USER_PREFIX = "user-";
	public static final String LOG_FILE_NAME = "logFileName";

	private Constants() {
		// Hide default constructor
	}
}
