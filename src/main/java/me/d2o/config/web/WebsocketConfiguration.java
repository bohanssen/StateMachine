
package me.d2o.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Class: WebsocketConfiguration
 *
 * @author bo.hanssen
 * @since Jan 4, 2017 11:44:17 AM
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

	@Value("${broker.external}")
	private boolean external;

	@Value("${broker.systemlogin:}")
	private String systemLogin;

	@Value("${broker.systempassword:}")
	private String systemPassword;

	@Value("${broker.clientlogin:}")
	private String clientLogin;

	@Value("${broker.clientpassword:}")
	private String clientPassword;

	@Value("${broker.host:}")
	private String host;

	@Value("${broker.port:0}")
	private int port;

	@Value("${redirectUrl}")
	private String origin;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		if (!external) {
			config.enableSimpleBroker("/topic", "/queue");
		} else {
			StompBrokerRelayRegistration stomp = config.enableStompBrokerRelay("/topic", "/queue");
			stomp.setAutoStartup(true);
			if (!systemLogin.isEmpty()) {
				stomp.setSystemLogin(systemLogin);
			}
			if (!systemPassword.isEmpty()) {
				stomp.setSystemPasscode(systemPassword);
			}
			if (!clientLogin.isEmpty()) {
				stomp.setClientLogin(clientLogin);
			}
			if (!clientPassword.isEmpty()) {
				stomp.setClientPasscode(clientPassword);
			}
			if (!host.isEmpty()) {
				stomp.setRelayHost(host);
			}
			if (port != 0) {
				stomp.setRelayPort(port);
			}
		}
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOrigins(origin).withSockJS();
	}
}
