package org.silsagusi.joonggaemoa.domain.consultation.contorller;

import lombok.RequiredArgsConstructor;
import org.silsagusi.joonggaemoa.domain.consultation.entity.ConsultationDTO;
import org.silsagusi.joonggaemoa.domain.consultation.service.ConsultationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agents/{agentId}/consultations")
@RequiredArgsConstructor
public class ConsultationApiController {

    private final ConsultationService consultationService;

    //상담 예약
    @PostMapping("")
    public ResponseEntity<ConsultationDTO> createConsultation(
            @PathVariable Long agentId,
            @RequestBody ConsultationDTO consultationDTO
    ){
        ConsultationDTO savedConsultation = consultationService.createConsultation(agentId,consultationDTO);
        return ResponseEntity.ok(savedConsultation);
    }
}
