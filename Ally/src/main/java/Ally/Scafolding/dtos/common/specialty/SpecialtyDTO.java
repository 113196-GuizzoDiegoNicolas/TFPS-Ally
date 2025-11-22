package Ally.Scafolding.dtos.common.specialty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyDTO {
    private Long id;

    private String codigo;
    private String nombre;

}
