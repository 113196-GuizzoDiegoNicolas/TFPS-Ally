package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findByPacienteId(Long pacienteId);
    List<ServiceEntity> findByPrestadorId(Long prestadorId);
    List<ServiceEntity> findByEspecialidadAndPrestadorIdIsNull(String especialidad);
    List<ServiceEntity> findByEspecialidad(String especialidad);
}
