package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.ServiceEntity;
import Ally.Scafolding.entities.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    // Solicitudes de un paciente específico
    List<ServiceEntity> findByPacienteId(Long pacienteId);
    // Solicitudes de un servicio específico
    Optional<ServiceEntity> findById(Long id);

    //  Solicitudes asignadas a un prestador
    List<ServiceEntity> findByPrestadorId(Long prestadorId);

    //  Solicitudes por especialidad sin prestador asignado (disponibles)
    List<ServiceEntity> findByEspecialidadAndPrestadorIdIsNull(String especialidad);


    // Solicitudes por especialidad
    List<ServiceEntity> findByEspecialidad(String especialidad);

    //  NUEVOS MÉTODOS RECOMENDADOS para tu aplicación:


    //  Solicitudes aceptadas de un paciente (para pagos)
    @Query("SELECT s FROM ServiceEntity s WHERE s.pacienteId = :pacienteId and s.estado = :estado")
    List<ServiceEntity> findServiciosAceptadosPorPaciente(@Param("pacienteId") Long pacienteId,
                                                          @Param("estado") String estado);

    // Solicitudes pendientes de un prestador

    List<ServiceEntity> findByPrestadorIdAndEstado(Long prestadorId, String estado);

    //  Solicitudes por estado
    List<ServiceEntity> findByEstado(String estado);

    long countByEstadoIn(List<String> estados);

    long countByEstado(String estado);

    @Query("SELECT s.especialidad, COUNT(s) " +
            "FROM ServiceEntity s " +
            "WHERE s.estado = 'ACEPTADO' " +
            "GROUP BY s.especialidad")
    List<Object[]> countPagosPorEspecialidad();

    @Query("SELECT COUNT(s) FROM ServiceEntity s " +
            "WHERE s.estado = :estado " +
            "AND s.fechaSolicitud BETWEEN :desde AND :hasta")
    long countByEstadoAndFechaBetween(
            @Param("estado") String estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
    @Query("SELECT s.id, CONCAT(p.nombre, ' ', p.apellido), " +
            "CONCAT(pr.nombre, ' ', pr.apellido), s.especialidad, s.estado, s.fechaSolicitud " +
            "FROM ServiceEntity s " +
            "JOIN PatientsEntity p ON s.pacienteId = p.id " +
            "LEFT JOIN ProvidersEntity pr ON s.prestadorId = pr.id " +
            "WHERE s.estado = 'PAGO_PENDIENTE'")
    List<Object[]> findServiciosPendientesDetalle();

    @Query("SELECT s.id, CONCAT(p.nombre, ' ', p.apellido), " +
            "CONCAT(pr.nombre, ' ', pr.apellido), s.especialidad, s.estado, s.fechaSolicitud " +
            "FROM ServiceEntity s " +
            "JOIN PatientsEntity p ON s.pacienteId = p.id " +
            "LEFT JOIN ProvidersEntity pr ON s.prestadorId = pr.id " +
            "WHERE s.estado = 'ACEPTADO'")
    List<Object[]> findServiciosAceptadosDetalle();
    @Modifying
    @Transactional
    @Query("UPDATE ServiceEntity s SET s.estado = :estado WHERE s.id = :id")
    void updateEstadoServicio(@Param("id") Long id, @Param("estado") String estado);





    @Query("""
SELECT s.estado, COUNT(s)
FROM ServiceEntity s
WHERE s.pacienteId = :pacienteId
  AND s.fechaSolicitud >= :desde
GROUP BY s.estado
""")
    List<Object[]> countByEstadoPacienteDesde(@Param("pacienteId") Long pacienteId,
                                              @Param("desde") LocalDateTime desde);





    @Query("""
SELECT s.especialidad, COUNT(s)
FROM ServiceEntity s
WHERE s.pacienteId = :pacienteId
  AND s.fechaSolicitud >= :desde
GROUP BY s.especialidad
ORDER BY COUNT(s) DESC
""")
    List<Object[]> countByEspecialidadPacienteDesde(@Param("pacienteId") Long pacienteId,
                                                    @Param("desde") LocalDateTime desde);

    @Query("""
SELECT COUNT(s)
FROM ServiceEntity s
WHERE s.prestadorId = :prestadorId
  AND (cast(:desde as date) IS NULL OR s.fechaSolicitud >= :desde)
""")
    long countByPrestadorDesde(@Param("prestadorId") Long prestadorId,
                               @Param("desde") LocalDateTime desde);

    @Query("""
SELECT COUNT(s)
FROM ServiceEntity s
WHERE s.prestadorId = :prestadorId
  AND s.estado = :estado
  AND (cast(:desde as date) IS NULL OR s.fechaSolicitud >= :desde)
""")
    long countByPrestadorAndEstadoDesde(@Param("prestadorId") Long prestadorId,
                                        @Param("estado") String estado,
                                        @Param("desde") LocalDateTime desde);

    @Query("""
SELECT s.especialidad, COUNT(s)
FROM ServiceEntity s
WHERE s.prestadorId = :prestadorId
  AND s.estado = 'ACEPTADO'
  AND (cast(:desde as date) IS NULL OR s.fechaSolicitud >= :desde)
GROUP BY s.especialidad
ORDER BY COUNT(s) DESC
""")
    List<Object[]> countAceptadosPorEspecialidadPrestador(@Param("prestadorId") Long prestadorId,
                                                          @Param("desde") LocalDateTime desde);
}
