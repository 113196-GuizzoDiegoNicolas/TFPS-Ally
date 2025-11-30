package Ally.Scafolding.entities;

import Ally.Scafolding.models.PagoEstado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id")
    private ServiceEntity servicio;

    private Double monto;

    @Enumerated(EnumType.STRING)
    private PagoEstado estado;

    private LocalDateTime fechaPago;
    private LocalDateTime fechaProcesamiento;

    private String idTransaccion; // ID de gateway de pago
    private String mensajeError;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodo_pago_id")
    private MetodosPagosEntity metodoPago;
}
