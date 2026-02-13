package Ally.Scafolding.services.payment;


import Ally.Scafolding.dtos.common.provider.ProviderReportsDTO;

    public interface ProviderReportsService {
        ProviderReportsDTO getReports(Long prestadorId, String periodo);
    }

