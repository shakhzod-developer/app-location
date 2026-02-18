package sales.applocation.tracking.persistence.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sales.applocation.tracking.application.AdminCurrentOnlineUseCase;
import sales.applocation.tracking.application.dto.AdminMapOfEmployeeDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminCurrentOnlineUseCase useCase;

    public AdminController(AdminCurrentOnlineUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/getOnlineEmployee")
    public ResponseEntity<List<AdminMapOfEmployeeDto>> getOnlineEmployee() {
        return ResponseEntity.ok(useCase.mapOfOnlineEmloyees());
    }
}
