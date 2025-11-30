package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.MetodosPagosEntity;
import Ally.Scafolding.entities.PatientsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MetodosPagosRepository extends JpaRepository<MetodosPagosEntity, Long>,
        JpaSpecificationExecutor<MetodosPagosEntity> {

    Optional<MetodosPagosEntity> findById(Long aLong);
}
