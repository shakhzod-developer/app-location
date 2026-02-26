package sales.applocation.tracking.persistence.web_socket;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefix for messages flowing FROM server TO client
        config.enableSimpleBroker("/topic");
        // Prefix for messages flowing FROM client TO server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The URL where the Admin UI connects
        registry.addEndpoint("/ws-tracking")
                .setAllowedOrigins("*"); // In production, specify your Admin URL
    }

}
