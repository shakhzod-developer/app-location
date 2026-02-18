package sales.applocation.tracking.persistence.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sales.applocation.tracking.application.AdminCurrentOnlineUseCase;
import sales.applocation.tracking.application.TrackingUseCase;
import sales.applocation.tracking.domain.TrackingRepository;
import sales.applocation.tracking.persistence.service.TrackingService;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class TrackingConfig {

    @Bean
    public TrackingRepository trackingRepository(StatefulRedisConnection<String, String> redisConnection, ObjectMapper objectMapper) {
        return new TrackingService(redisConnection, objectMapper);
    }

    @Bean
    public TrackingUseCase trackingUseCase(TrackingRepository trackingRepository) {
        return new TrackingUseCase(trackingRepository);
    }

    @Bean(destroyMethod = "shutdown")
    public RedisClient tile38Client() {
        return RedisClient.create("redis://localhost:9851"); // Tile38 port
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, String> tile38Connection(RedisClient tile38Client) {
        return tile38Client.connect();
    }

    @Bean
    public AdminCurrentOnlineUseCase adminCurrentOnlineUseCase(TrackingRepository trackingRepository) {
        return new AdminCurrentOnlineUseCase(trackingRepository);
    }
}
