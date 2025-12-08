package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequestEntity, Long> {

    List<ServiceRequestEntity> findByPrestador_Id(Long prestadorId);
    List<ServiceRequestEntity> findByPaciente_Id(Long pacienteId);

    List<ServiceRequestEntity> findByPaciente_IdAndActivoTrue(Long pacienteId);


}


