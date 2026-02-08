package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.models.PagoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentsEntity, Long> {

    // Encontrar pagos por servicio
    List<PaymentsEntity> findByServicioId(Long servicioId);

    // Encontrar pagos por estado - CORREGIDO: usar el nombre correcto del campo
    List<PaymentsEntity> findByEstadoPago(PagoEstado estadoPago);

    // Encontrar pagos por paciente (a través del servicio)
    @Query("SELECT p FROM PaymentsEntity p WHERE p.servicio.pacienteId = :pacienteId")
    List<PaymentsEntity> findByPacienteId(@Param("pacienteId") Long pacienteId);

    // Encontrar pagos aceptados por paciente - CORREGIDO: usar estadoPago
    @Query("SELECT p FROM PaymentsEntity p WHERE p.servicio.pacienteId = :pacienteId AND p.estadoPago = 'COMPLETADO'")
    List<PaymentsEntity> findPagosAceptadosByPacienteId(@Param("pacienteId") Long pacienteId);

    // Encontrar pago por ID de transacción
    Optional<PaymentsEntity> findByIdTransaccion(String idTransaccion);
    List<PaymentsEntity> findAll();
    @Query(value = """
SELECT FORMATDATETIME(COALESCE(p.fecha_pago, p.fecha_creacion), 'yyyy-MM') AS mes,
       COALESCE(SUM(p.monto),0) AS total
FROM payments p
JOIN services s ON s.id = p.servicio_id
WHERE s.paciente_id = :pacienteId
  AND COALESCE(p.fecha_pago, p.fecha_creacion) >= :desde
  AND p.estado_pago = 'COMPLETADO'
GROUP BY FORMATDATETIME(COALESCE(p.fecha_pago, p.fecha_creacion), 'yyyy-MM')
ORDER BY mes
""", nativeQuery = true)
    List<Object[]> pagosPorMesPaciente(@Param("pacienteId") Long pacienteId,
                                       @Param("desde") LocalDateTime desde);


    @Query(value = """
SELECT COALESCE(SUM(p.monto),0)
FROM payments p
JOIN services s ON s.id = p.servicio_id
WHERE s.paciente_id = :pacienteId
  AND COALESCE(p.fecha_pago, p.fecha_creacion) >= :desde
  AND p.estado_pago = 'COMPLETADO'
""", nativeQuery = true)
    BigDecimal totalPagadoDesde(@Param("pacienteId") Long pacienteId,
                                @Param("desde") LocalDateTime desde);

    @Query(value = """
SELECT FORMATDATETIME(COALESCE(p.fecha_pago, p.fecha_creacion), 'yyyy-MM') AS mes,
       COALESCE(SUM(p.monto),0) AS total
FROM payments p
JOIN services s ON s.id = p.servicio_id
WHERE s.prestador_id = :prestadorId
  AND (:desde IS NULL OR COALESCE(p.fecha_pago, p.fecha_creacion) >= :desde)
  AND p.estado_pago = 'COMPLETADO'
GROUP BY FORMATDATETIME(COALESCE(p.fecha_pago, p.fecha_creacion), 'yyyy-MM')
ORDER BY mes
""", nativeQuery = true)
    List<Object[]> ingresosPorMesPrestador(@Param("prestadorId") Long prestadorId,
                                           @Param("desde") LocalDateTime desde);

    @Query(value = """
SELECT COALESCE(SUM(p.monto),0)
FROM payments p
JOIN services s ON s.id = p.servicio_id
WHERE s.prestador_id = :prestadorId
  AND (:desde IS NULL OR COALESCE(p.fecha_pago, p.fecha_creacion) >= :desde)
  AND p.estado_pago = 'COMPLETADO'
""", nativeQuery = true)
    BigDecimal totalIngresosPrestadorDesde(@Param("prestadorId") Long prestadorId,
                                           @Param("desde") LocalDateTime desde);

}