/**
 *
 */
package me.d2o.service.notification.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import me.d2o.config.constants.Constants;
import me.d2o.service.notification.events.PushNotificationEvent;

/**
 * Class: NotificationService
 *
 * @author bo.hanssen
 * @since Jan 8, 2017 7:47:43 PM
 *
 */
@Service
public class PushNotificationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean devEnv;

	@Autowired
	private Environment env;

	@Value("${onesignal.key}")
	private String onesignalKey;

	@Value("${onesignal.id}")
	private String onesignalId;

	@Value("${D2O.id}")
	private String appId;

	@PostConstruct
	private void checkEnvironment() {
		this.devEnv = Arrays.asList(env.getActiveProfiles()).contains("development");
		PushNotificationEvent.setAppId(appId);
	}

	/**
	 * Send.
	 *
	 * @param event
	 *            the event
	 * @throws JsonProcessingException
	 */
	@EventListener
	public void send(PushNotificationEvent event) throws JsonProcessingException {
		if (devEnv) {
			logger.info("Detected development environment, use mocking to prevent spamming users.");
			logger.info("Original body: " + event.getPayload(onesignalId));
			event.enableMocking();
		}
		try {
			String jsonResponse;
			URL url = new URL("https://onesignal.com/api/v1/notifications");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Authorization", "Basic " + onesignalKey);
			con.setRequestMethod("POST");

			String strJsonBody = event.getPayload(onesignalId);

			logger.info("strJsonBody: " + strJsonBody);
			byte[] sendBytes = strJsonBody.getBytes(Constants.UTF8);
			con.setFixedLengthStreamingMode(sendBytes.length);

			OutputStream outputStream = con.getOutputStream();
			outputStream.write(sendBytes);

			int httpResponse = con.getResponseCode();
			logger.info("httpResponse: " + httpResponse);
			if (httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
				Scanner scanner = new Scanner(con.getInputStream(), Constants.UTF8);
				jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
				scanner.close();
			} else {
				Scanner scanner = new Scanner(con.getErrorStream(), Constants.UTF8);
				jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
				scanner.close();
			}
			logger.info("jsonResponse: " + jsonResponse);
		} catch (Exception ex) {
			logger.warn("Notification failed: {}", ex);
		}
	}
}
