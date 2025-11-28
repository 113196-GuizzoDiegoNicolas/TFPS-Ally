package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    // ✅ Solicitudes de un paciente específico
    List<ServiceEntity> findByPacienteId(Long pacienteId);



    // ✅ Solicitudes asignadas a un prestador
    List<ServiceEntity> findByPrestadorId(Long prestadorId);

    // ✅ Solicitudes por especialidad sin prestador asignado (disponibles)
    List<ServiceEntity> findByEspecialidadAndPrestadorIdIsNull(String especialidad);

    // ✅ Solicitudes por especialidad
    List<ServiceEntity> findByEspecialidad(String especialidad);

    // 🔥 NUEVOS MÉTODOS RECOMENDADOS para tu aplicación:

    // ✅ Solicitudes aceptadas de un paciente (para pagos)
    @Query("SELECT s FROM ServiceEntity s WHERE s.pacienteId = :pacienteId and s.estado = :estado")
    List<ServiceEntity> findServiciosAceptadosPorPaciente(@Param("pacienteId") Long pacienteId,
                                                          @Param("estado") String estado);
    // ✅ Solicitudes pendientes de un prestador
    List<ServiceEntity> findByPrestadorIdAndEstado(Long prestadorId, String estado);

    // ✅ Solicitudes por estado
    List<ServiceEntity> findByEstado(String estado);


}
