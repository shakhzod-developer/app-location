package sales.applocation.tracking.persistence.service;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import org.springframework.security.core.context.SecurityContextHolder;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.tracking.application.dto.AdminMapOfEmployeeDto;
import sales.applocation.tracking.domain.LocationPoint;
import sales.applocation.tracking.domain.TrackingPoint;
import sales.applocation.tracking.domain.TrackingRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


public class TrackingService implements TrackingRepository {

    private final RedisCommands<String, String> redisCommands;
    private final StringCodec codec = StringCodec.UTF8;
    private final ObjectMapper objectMapper;

    public TrackingService(StatefulRedisConnection<String, String> connection, ObjectMapper objectMapper) {
        this.redisCommands = connection.sync();
        this.objectMapper = objectMapper;
    }

    @Override
    public void delete(OrderId id) {
        redisCommands.dispatch(CommandType.DEL, new StatusOutput<>(codec),
                new CommandArgs<>(codec)
                        .add("deliveries")
                        .add(id.id().toString()));
    }

    @Override
    public void save(TrackingPoint trackingPoint) {
        try {
            // 1. Get the current logged-in Employee's name from Spring Security
            String employeeName = SecurityContextHolder.getContext()
                    .getAuthentication().getName();

            String id = trackingPoint.getOrderId().id().toString();
            String jsonHistory = objectMapper.writeValueAsString(trackingPoint.getPoints());
            LocationPoint latest = trackingPoint.getPoints().get(trackingPoint.getPoints().size() - 1);

            // 2. Real Tile38 Command with FIELDS
            // SET [collection] [id] POINT [lat] [lon] FIELD [key] [value] ...
            redisCommands.dispatch(CommandType.SET, new StatusOutput<>(codec),
                    new CommandArgs<>(codec)
                            .add("deliveries")      // Key 1: Collection
                            .add(id)                // Key 2: ID (OrderId)
                            .add("POINT")
                            .add(latest.lat())
                            .add(latest.lon())
                            .add("FIELD")
                            .add("employeeName")    // Metadata 1
                            .add(employeeName)
                            .add("FIELD")
                            .add("history")         // Metadata 2 (Your 6 points)
                            .add(jsonHistory));

        } catch (Exception e) {
            throw new RuntimeException("Tile38 Persistence Error", e);
        }
    }

    @Override
    public TrackingPoint findByOrderId(OrderId oId) {
        String id = oId.value().toString();
        String response = redisCommands.dispatch(CommandType.GET,
                new StatusOutput<>(codec),
                new CommandArgs<>(codec)
                        .add("deliveries")
                        .add(id)
                        .add("WITHFIELDS"));

        if (response == null || response.contains("nil") || !response.contains("{")) {
            return null;
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode historyNode = root.path("fields").path("history");

            if (historyNode.isMissingNode() || historyNode.asText().isEmpty()) {
                return null;
            }

            List<LocationPoint> points = objectMapper.readValue(
                    historyNode.asText(),
                    new TypeReference<List<LocationPoint>>() {}
            );

            return new TrackingPoint(oId, points);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing Tile38 JSON response", e);
        }
    }


    @Override
    public List<AdminMapOfEmployeeDto> findAllActiveLocations() {
        String response = redisCommands.dispatch(CommandType.SCAN, new StatusOutput<>(codec),
                new CommandArgs<>(codec).add("deliveries"));

        if (response == null || response.contains("nil") || !response.contains("{")) {
            return List.of();
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode objects = root.path("objects");
            List<AdminMapOfEmployeeDto> results = new ArrayList<>();

            for (JsonNode node : objects) {
                String orderId = node.path("id").asText();
                JsonNode fields = node.path("fields");

                // Tile38 stores coordinates in GeoJSON format [lon, lat]
                JsonNode coordinates = node.path("object").path("coordinates");

                results.add(new AdminMapOfEmployeeDto(
                        orderId,
                        fields.path("employeeName").asText(), // This was saved in the 'save' method
                        coordinates.get(1).asDouble(), // Latitude
                        coordinates.get(0).asDouble()  // Longitude
                ));
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Admin Map data", e);
        }
    }
}
