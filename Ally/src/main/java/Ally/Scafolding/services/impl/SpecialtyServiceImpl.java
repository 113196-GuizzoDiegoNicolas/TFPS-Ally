package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.specialty.SpecialtyDTO;
import Ally.Scafolding.entities.SpecialtyEntity; // Import corregido
import Ally.Scafolding.repositories.SpecialtyRepository;
import Ally.Scafolding.services.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyRepository repository;

    @Override
    public List<SpecialtyDTO> findAll() {
        return repository.findAll()  // Corregido de findByAll a findAll
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Método mapToDTO corregido con la entidad correcta
    private SpecialtyDTO mapToDTO(SpecialtyEntity specialty) {
        SpecialtyDTO dto = new SpecialtyDTO();

        dto.setId(specialty.getId());
        dto.setCodigo(specialty.getCodigo());
        dto.setNombre(specialty.getNombre());
        dto.setImporteConsulta(specialty.getImporteConsulta());

        return dto;
    }
}