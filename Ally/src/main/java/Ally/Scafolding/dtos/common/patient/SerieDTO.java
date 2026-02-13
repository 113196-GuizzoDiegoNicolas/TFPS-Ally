package Ally.Scafolding.dtos.common.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SerieDTO {
    private String label;
    private BigDecimal value;
}