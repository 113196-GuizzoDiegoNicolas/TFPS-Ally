package Ally.Scafolding.entities;

import Ally.Scafolding.models.PagoEstado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private LocalDateTime fechaPago;
    private LocalDateTime fechaProcesamiento;
    private String idTransaccion; // ID de gateway de pago
    private String mensajeError;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodo_pago_id")
    private MetodosPagosEntity metodoPago;

    @Enumerated(EnumType.STRING)
    private PagoEstado estadoPago;
    private Double importe;

    // ============ NUEVOS CAMPOS NECESARIOS ============

    @Column(name = "codigo_autorizacion", length = 50)
    private String codigoAutorizacion;

    @Column(name = "referencia_externa", length = 100)
    private String referenciaExterna;

    @Column(name = "numero_autorizacion_os", length = 50)
    private String numeroAutorizacionOs;

    @Column(name = "monto_cubierto_os", precision = 10, scale = 2)
    private BigDecimal montoCubiertoOs;

    @Column(name = "monto_copago", precision = 10, scale = 2)
    private BigDecimal montoCopago;

    @Column(name = "comision", precision = 10, scale = 2)
    private BigDecimal comision;

    @Column(name = "monto_neto", precision = 10, scale = 2)
    private BigDecimal montoNeto;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "datos_adicionales", length = 2000)
    private String datosAdicionalesJson;

    @Version
    private Long version;

    // Método para calcular monto neto
    public void calcularMontoNeto() {
        if (this.monto != null) {
            BigDecimal montoBigDecimal = BigDecimal.valueOf(this.monto);
            BigDecimal comisionBigDecimal = this.comision != null ? this.comision : BigDecimal.ZERO;
            this.montoNeto = montoBigDecimal.subtract(comisionBigDecimal);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        calcularMontoNeto();
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        calcularMontoNeto();
    }
}