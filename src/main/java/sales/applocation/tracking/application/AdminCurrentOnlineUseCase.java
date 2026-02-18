package sales.applocation.tracking.application;

import sales.applocation.tracking.application.dto.AdminMapOfEmployeeDto;
import sales.applocation.tracking.domain.TrackingRepository;

import java.util.List;

public class AdminCurrentOnlineUseCase {

    private final TrackingRepository repo;

    public AdminCurrentOnlineUseCase(TrackingRepository repo) {
        this.repo = repo;
    }

    public List<AdminMapOfEmployeeDto> mapOfOnlineEmloyees() {
        List<AdminMapOfEmployeeDto> result = repo.findAllActiveLocations();
        return result;
    }
}
