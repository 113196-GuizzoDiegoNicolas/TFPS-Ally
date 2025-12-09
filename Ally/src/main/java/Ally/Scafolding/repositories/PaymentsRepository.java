package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.models.PagoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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


}