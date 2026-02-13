package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.provider.ProviderReportsDTO;
import Ally.Scafolding.dtos.common.provider.ProviderDTO;
import Ally.Scafolding.models.Provider;
import Ally.Scafolding.services.ProviderService;
import Ally.Scafolding.services.payment.ProviderReportsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/prestadores")
@CrossOrigin(origins = "*")
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderReportsService providerReportsService;

    public ProviderController(ProviderService providerService,
                              ProviderReportsService providerReportsService) {
        this.providerService = providerService;
        this.providerReportsService = providerReportsService;
    }

    @GetMapping
    public List<ProviderDTO> getAllPrestadores() {
        return providerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDTO> getPrestadorById(@PathVariable Long id) {
        ProviderDTO prestador = providerService.findById(id);
        return prestador != null ? ResponseEntity.ok(prestador) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Provider> createPrestador(@RequestBody @Valid Provider prestadorCreate) {
        Provider providerSaved = providerService.create(prestadorCreate);
        if (Objects.isNull(providerSaved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ya existe registrado como Prestador");
        }
        return ResponseEntity.ok(providerSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDTO> updatePrestador(@PathVariable Long id,
                                                       @RequestBody ProviderDTO providerDTO) {
        try {
            ProviderDTO updated = providerService.update(id, providerDTO);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestador(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activation")
    public ResponseEntity<ProviderDTO> changeActivation(@PathVariable Long id,
                                                        @RequestParam Boolean activo) {
        ProviderDTO updated = providerService.changeActivation(id, activo);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/especialidad/{codigoEspecialidad}")
    public List<ProviderDTO> getPrestadoresByEspecialidad(@PathVariable String codigoEspecialidad) {
        return providerService.findByEspecialidad(codigoEspecialidad);
    }

    @GetMapping("/especialidad/{codigoEspecialidad}/activos")
    public List<ProviderDTO> getPrestadoresActivosByEspecialidad(@PathVariable String codigoEspecialidad) {
        return providerService.findActiveByEspecialidad(codigoEspecialidad);
    }

    @GetMapping("/activos")
    public List<ProviderDTO> getPrestadoresActivos() {
        return providerService.findByActivo(true);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ProviderDTO> getPrestadorByEmail(@PathVariable String email) {
        ProviderDTO prestador = providerService.findByEmail(email);
        return prestador != null ? ResponseEntity.ok(prestador) : ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ProviderDTO> getPrestadorByUsuarioId(@PathVariable Long usuarioId) {
        ProviderDTO prestador = providerService.findByUsuarioId(usuarioId);
        return prestador != null ? ResponseEntity.ok(prestador) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{prestadorId}/reportes")
    public ResponseEntity<ProviderReportsDTO> reportesPrestador(
            @PathVariable Long prestadorId,
            @RequestParam(defaultValue = "6M") String periodo
    ) {
        return ResponseEntity.ok(providerReportsService.getReports(prestadorId, periodo));
    }
}