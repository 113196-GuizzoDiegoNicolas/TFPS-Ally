package Ally.Scafolding.models;

public enum PagoEstado {
    PENDIENTE,      // Pago pendiente (mostrar en pagos-paciente)
    PROCESANDO,     // Pago en proceso
    COMPLETADO,     // Pago exitoso
    FALLIDO,        // Pago fallido
    REEMBOLSADO,    // Pago reembolsado
    CANCELADO       // Pago cancelado
}
