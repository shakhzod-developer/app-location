package sales.applocation.tracking.application;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sales.applocation.tracking.application.dto.AdminMapOfEmployeeDto;

import java.util.List;

@Service
public class TrackingBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;
    private final AdminCurrentOnlineUseCase adminUseCase;

    public TrackingBroadcastService(SimpMessagingTemplate messagingTemplate, AdminCurrentOnlineUseCase adminUseCase) {
        this.messagingTemplate = messagingTemplate;
        this.adminUseCase = adminUseCase;
    }


    public void broadcastOnlineEmployees() {
        List<AdminMapOfEmployeeDto> onlineEmployees = adminUseCase.mapOfOnlineEmloyees();
        messagingTemplate.convertAndSend("/topic/online-employees", onlineEmployees);
    }
}