package Ally.Scafolding.models;

public enum ServicioEstado {
    PENDIENTE,          // Recién solicitado
    ACEPTADO,           // Prestador aceptó
    EN_PROCESO,         // En atención/transporte
    COMPLETADO,         // Finalizado exitosamente
    CANCELADO,          // Cancelado antes de completar
    RECHAZADO,          // Prestador rechazó
    PAGO_PENDIENTE,     // Esperando pago
    PAGADO              // Servicio pagado
}
