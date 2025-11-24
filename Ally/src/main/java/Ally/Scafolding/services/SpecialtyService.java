package Ally.Scafolding.services;


import Ally.Scafolding.dtos.common.specialty.SpecialtyDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for user management operations.
 * <p>
 *     Handles CRUD operations for user entities.
 * </p>
 */
@Service
public interface SpecialtyService {


    List<SpecialtyDTO> findAll();


}