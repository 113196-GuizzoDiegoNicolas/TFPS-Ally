package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.login.UserCreateDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import Ally.Scafolding.services.UserService;
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
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        // Validate unique constraints
      /*  if (userService.usernameExists(userCreateDTO.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        if (userService.emailExists(userCreateDTO.getEmail())) {
            return ResponseEntity.badRequest().build();
        }*/

        UserDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updated = userService.update(id, userDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }


}



