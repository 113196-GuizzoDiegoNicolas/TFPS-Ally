package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.login.UserCreateDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import Ally.Scafolding.dtos.common.specialty.SpecialtyDTO;
import Ally.Scafolding.services.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for user management operations.
 * <p>
 *     Handles user CRUD operations with ModelMapper auto-conversion.
 * </p>
 */
@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "*")
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping
    public List<SpecialtyDTO> getAllUsers() {
        return specialtyService.findAll();
    }


}
