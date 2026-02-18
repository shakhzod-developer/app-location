package sales.applocation.tracking.persistence.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sales.applocation.tracking.application.TrackingUseCase;
import sales.applocation.tracking.application.dto.SavingTrackDto;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingUseCase trackingUseCase;

    @PostMapping("/createTrack")
    public ResponseEntity<Void> savingTrack(@RequestBody SavingTrackDto dto){
        trackingUseCase.saveTrackPoints(
                dto.id(),
                dto.points()
        );
        return ResponseEntity.ok().build();
    }
}
