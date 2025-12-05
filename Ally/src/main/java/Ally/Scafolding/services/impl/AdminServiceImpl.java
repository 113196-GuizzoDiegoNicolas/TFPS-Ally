package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.admin.AdminMetricsDTO;
import Ally.Scafolding.dtos.common.admin.AdminUserDTO;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.ServiceRequestRepository;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UsersRepository usersRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    @Override
    public AdminMetricsDTO getMetrics() {
        long pacientes = usersRepository.countByRol("PACIENTE");
        long prestadores = usersRepository.countByRol("PRESTADOR");
        long transportistas = usersRepository.countByRol("TRANSPORTISTA");
        long admins = usersRepository.countByRol("ADMIN");
        long solicitudesPendientes = serviceRequestRepository.countByEstado("PENDIENTE");
        long serviciosAceptados = serviceRequestRepository.countByEstado("ACEPTADO");
        return new AdminMetricsDTO(
                pacientes,
                prestadores,
                transportistas,
                admins,
                solicitudesPendientes,
                serviciosAceptados
        );
    }

    @Override
    public List<AdminUserDTO> getUsers() {
        return usersRepository.findAll().stream()
                .map(u -> new AdminUserDTO(
                        u.getId(),
                        u.getUsuario(),    // Nombre real del usuario
                        u.getRol().toString(),         // Ya es String
                        u.getActivo()       // Boolean
                ))
                .toList();
    }

    @Override
    public AdminUserDTO toggleUser(Long id) {

        UsersEntity user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setActivo(!user.getActivo());
        UsersEntity updated = usersRepository.save(user);

        return new AdminUserDTO(
                updated.getId(),
                updated.getUsuario(),
                updated.getRol(), //  ya es String, sin .name()
                updated.getActivo()
        );
    }
}

