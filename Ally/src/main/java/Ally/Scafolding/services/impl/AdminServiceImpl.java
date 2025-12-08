package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.admin.AdminMetricsDTO;
import Ally.Scafolding.dtos.common.admin.AdminUserDTO;
import Ally.Scafolding.dtos.common.admin.PagosEspecialidadDTO;
import Ally.Scafolding.entities.UsersEntity;

import Ally.Scafolding.repositories.PaymentsRepository;
import Ally.Scafolding.repositories.ServiceRepository;


import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UsersRepository usersRepository;

    private final ServiceRepository serviceRepository;
    @Autowired
    private PaymentsRepository paymentsRepository;

    @Override
    public AdminMetricsDTO getMetrics(String fechaDesde, String fechaHasta) {
        LocalDateTime desde = (fechaDesde != null && !fechaDesde.isEmpty())
                ? LocalDate.parse(fechaDesde).atStartOfDay()
                : LocalDate.MIN.atStartOfDay();

        LocalDateTime hasta = (fechaHasta != null && !fechaHasta.isEmpty())
                ? LocalDate.parse(fechaHasta).atTime(23,59,59)
                : LocalDate.MAX.atTime(23,59,59);
        long pacientes = usersRepository.countByRol("PACIENTE");
        long prestadores = usersRepository.countByRol("PRESTADOR");
        long transportistas = usersRepository.countByRol("TRANSPORTISTA");
        long admins = usersRepository.countByRol("ADMIN");
        long solicitudesPendientes =
                serviceRepository.countByEstadoAndFechaBetween("PENDIENTE", desde, hasta);
        long serviciosAceptados =
                serviceRepository.countByEstadoAndFechaBetween("ACEPTADO", desde, hasta);

        long pagosProcesados = paymentsRepository.count();



        return new AdminMetricsDTO(
                pacientes,
                prestadores,
                transportistas,
                admins,
                solicitudesPendientes,
                serviciosAceptados,
                pagosProcesados
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
    @Override
    public List<PagosEspecialidadDTO> getPagosPorEspecialidad() {
        return serviceRepository.countPagosPorEspecialidad()
                .stream()
                .map(r -> new PagosEspecialidadDTO(
                        r[0].toString(),
                        (Long) r[1]
                ))
                .toList();
    }

}

