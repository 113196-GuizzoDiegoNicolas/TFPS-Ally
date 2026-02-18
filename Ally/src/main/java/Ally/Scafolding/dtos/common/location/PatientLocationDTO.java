package Ally.Scafolding.dtos.common.location;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientLocationDTO {
    private Long pacienteId;
    private Double lat;
    private Double lng;
    private String addressText;
    private LocalDateTime updatedAt;
}
