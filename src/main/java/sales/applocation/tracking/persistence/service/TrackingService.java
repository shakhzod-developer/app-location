package sales.applocation.tracking.persistence.service;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import sales.applocation.employee.domain.EmployeeId;
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
import java.util.UUID;

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
            String orderId = trackingPoint.getOrderId().id().toString();
            String empId = trackingPoint.getEmployeeId().id().toString();

            TrackingPoint existing = findByOrderId(trackingPoint.getOrderId());
            List<LocationPoint> allPoints = new ArrayList<>();
            if (existing != null) {
                allPoints.addAll(existing.getPoints());
            }
            allPoints.addAll(trackingPoint.getPoints());

            String jsonHistory = objectMapper.writeValueAsString(allPoints);
            LocationPoint latest = allPoints.get(allPoints.size() - 1);

            redisCommands.dispatch(CommandType.SET, new StatusOutput<>(codec),
                    new CommandArgs<>(codec)
                            .add("deliveries")
                            .add(orderId)
                            .add("FIELD").add("employeeId").add(empId)
                            .add("FIELD").add("history").add(jsonHistory)
                            .add("EX").add(120)
                            .add("POINT")
                            .add(latest.lat())
                            .add(latest.lon()));
        } catch (Exception e) {
            throw new RuntimeException("Tile38 Save Error", e);
        }
    }

    @Override
    public List<AdminMapOfEmployeeDto> findAllActiveLocations() {
        try {
            String response = redisCommands.dispatch(CommandType.SCAN, new StatusOutput<>(codec),
                    new CommandArgs<>(codec).add("deliveries"));

            JsonNode root = objectMapper.readTree(response);
            List<AdminMapOfEmployeeDto> results = new ArrayList<>();

            for (JsonNode node : root.path("objects")) {
                String orderId = node.path("id").asText();
                JsonNode fields = node.path("fields");
                JsonNode coords = node.path("object").path("coordinates");

                results.add(new AdminMapOfEmployeeDto(
                        orderId,
                        fields.path("employeeId").asText(),
                        coords.get(1).asDouble(),
                        coords.get(0).asDouble()
                ));
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Failed to scan active locations", e);
        }
    }


    @Override
    public TrackingPoint findByOrderId(OrderId id) {
        try {
            String response = redisCommands.dispatch(CommandType.GET, new StatusOutput<>(codec),
                    new CommandArgs<>(codec).add("deliveries").add(id.id().toString()).add("WITHFIELDS"));

            if (response == null || response.contains("nil")) return null;

            JsonNode root = objectMapper.readTree(response);
            JsonNode fields = root.path("fields");

            String empId = fields.path("employeeId").asText();
            String historyJson = fields.path("history").asText();

            List<LocationPoint> points = objectMapper.readValue(historyJson,
                    new TypeReference<>() {
                    });

            return new TrackingPoint(id, new EmployeeId(UUID.fromString(empId)), points);
        } catch (Exception e) {
            return null;
        }
    }
}
