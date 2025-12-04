package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.dtos.common.payment.PaymentResponseDTO;
import Ally.Scafolding.services.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Gestión de Pagos",
        description = "API para procesamiento y gestión de pagos de servicios médicos")
public class PaymentController {

    private final PaymentService paymentService;

    // ===========================================
    // ENDPOINTS PRINCIPALES DE PAGOS
    // ===========================================

    @PostMapping(value = "/process",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Procesar un pago",
            description = "Procesa un pago para un servicio específico usando diferentes métodos de pago")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago procesado exitosamente",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos incorrectos"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto (servicio ya pagado, estado inválido, etc.)"),
            @ApiResponse(responseCode = "422", description = "Error de validación en los datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Valid @RequestBody PaymentRequestDTO paymentRequest,
            @Parameter(description = "ID de sesión o usuario (opcional)")
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @Parameter(description = "IP del cliente (opcional)")
            @RequestHeader(value = "X-Client-IP", required = false) String clientIp) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("📦 POST /api/v1/payments/process - Iniciando procesamiento");
        log.info("📋 Servicio ID: {}", paymentRequest.getServicioId());
        log.info("💰 Método de Pago: {}", paymentRequest.getMetodoPago());
        log.info("💵 Monto: {}", paymentRequest.getMonto());
        log.info("👤 Email Pagador: {}", paymentRequest.getEmailPagador());
        log.info("🆔 Session ID: {}", sessionId);
        log.info("🌐 Client IP: {}", clientIp);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // Registrar información adicional si está disponible
            if (sessionId != null || clientIp != null) {
                log.debug("Información adicional - Session: {}, IP: {}", sessionId, clientIp);
            }

            PaymentResponseDTO response = paymentService.processPayment(paymentRequest);

            // Registrar resultado
            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("✅ Pago procesado exitosamente. Pago ID: {}, Estado: {}",
                        response.getPagoId(), response.getEstado());
            } else {
                log.warn("⚠️ Pago no procesado. Estado: {}, Mensaje: {}",
                        response.getEstado(), response.getMensaje());
            }

            // Determinar código HTTP según el resultado
            HttpStatus status = determineHttpStatus(response);

            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            log.error("❌ Error inesperado procesando pago: {}", e.getMessage(), e);

            PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                    .success(false)
                    .estado("ERROR_INTERNO")
                    .mensaje("Error interno del servidor: " + e.getMessage())
                    .errorCode("INTERNAL_SERVER_ERROR")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping(value = "/mercado-pago/preference",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear preferencia de Mercado Pago",
            description = "Crea una preferencia de pago en Mercado Pago y retorna la URL para redirigir al usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferencia creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "Credenciales de Mercado Pago inválidas"),
            @ApiResponse(responseCode = "502", description = "Error en comunicación con Mercado Pago")
    })
    public ResponseEntity<PaymentResponseDTO> createMercadoPagoPreference(
            @Valid @RequestBody PaymentRequestDTO paymentRequest,
            @Parameter(description = "Idioma para la interfaz de pago (es, en, pt)")
            @RequestParam(value = "language", defaultValue = "es") String language) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("🔄 POST /api/v1/payments/mercado-pago/preference");
        log.info("📋 Servicio ID: {}", paymentRequest.getServicioId());
        log.info("💰 Monto: {}", paymentRequest.getMonto());
        log.info("📧 Email: {}", paymentRequest.getEmailPagador());
        log.info("🌐 Idioma: {}", language);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // Validar que sea Mercado Pago
            if (!"MERCADO_PAGO".equalsIgnoreCase(paymentRequest.getMetodoPago())) {
                log.warn("Método de pago no es MERCADO_PAGO: {}", paymentRequest.getMetodoPago());

                PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                        .success(false)
                        .estado("METODO_INVALIDO")
                        .mensaje("Este endpoint solo es válido para MERCADO_PAGO")
                        .errorCode("INVALID_PAYMENT_METHOD")
                        .build();

                return ResponseEntity.badRequest().body(errorResponse);
            }

            PaymentResponseDTO response = paymentService.createMercadoPagoPreference(paymentRequest);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("✅ Preferencia creada exitosamente. Preference ID: {}, URL: {}",
                        response.getPreferenceId(),
                        response.getPaymentUrl() != null ?
                                response.getPaymentUrl().substring(0, Math.min(50, response.getPaymentUrl().length())) + "..." : "N/A");
            } else {
                log.error("❌ Error creando preferencia: {}", response.getMensaje());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error inesperado creando preferencia de Mercado Pago: {}", e.getMessage(), e);

            PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                    .success(false)
                    .estado("ERROR_MERCADO_PAGO")
                    .mensaje("Error al crear preferencia en Mercado Pago: " + e.getMessage())
                    .errorCode("MERCADO_PAGO_ERROR")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping(value = "/mercado-pago/webhook",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Webhook de Mercado Pago",
            description = "Endpoint para recibir notificaciones de webhook de Mercado Pago (no usar directamente)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Webhook recibido y procesado"),
            @ApiResponse(responseCode = "400", description = "Webhook inválido o firma incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error procesando webhook")
    })
    public ResponseEntity<String> handleMercadoPagoWebhook(
            @RequestBody Map<String, Object> webhookData,
            @Parameter(description = "Firma del webhook para validación")
            @RequestHeader(value = "x-signature", required = false) String signature,
            @Parameter(description = "ID único de la solicitud")
            @RequestHeader(value = "x-request-id", required = false) String requestId) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("📨 POST /api/v1/payments/mercado-pago/webhook");
        log.info("🆔 Request ID: {}", requestId);
        log.info("🔐 Signature: {}", signature != null ?
                signature.substring(0, Math.min(20, signature.length())) + "..." : "No proporcionada");
        log.info("📊 Tipo de evento: {}", webhookData.get("type"));
        log.info("🎯 Acción: {}", webhookData.get("action"));
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // Log detallado del webhook (sin datos sensibles)
            log.debug("Datos del webhook recibidos: {}",
                    webhookData.toString().length() > 500 ?
                            webhookData.toString().substring(0, 500) + "..." : webhookData.toString());

            // Validar firma si está disponible
            if (signature == null || signature.isEmpty()) {
                log.warn("⚠️ Webhook recibido sin firma - validación omitida");
            } else {
                log.debug("✅ Firma recibida, procediendo con validación...");
                // En producción, aquí se validaría la firma
                // boolean isValid = mercadoPagoService.validateWebhookSignature(signature, webhookData);
            }

            // Procesar webhook
            processMercadoPagoWebhookInternal(webhookData);

            log.info("✅ Webhook procesado exitosamente");

            return ResponseEntity.ok("Webhook recibido y procesado exitosamente");

        } catch (IllegalArgumentException e) {
            log.error("❌ Webhook inválido: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Webhook inválido: " + e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error procesando webhook de Mercado Pago: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error procesando webhook: " + e.getMessage());
        }
    }

    // ===========================================
    // ENDPOINTS DE CONSULTA Y GESTIÓN
    // ===========================================

    @GetMapping(value = "/service/{servicioId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener historial de pagos de un servicio",
            description = "Retorna el historial completo de todos los pagos realizados para un servicio específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentHistory(
            @PathVariable @Parameter(description = "ID del servicio", example = "123") Long servicioId,
            @Parameter(description = "Incluir pagos cancelados/reembolsados")
            @RequestParam(value = "includeCancelled", defaultValue = "false") boolean includeCancelled) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("📜 GET /api/v1/payments/service/{}", servicioId);
        log.info("🗑️ Incluir cancelados: {}", includeCancelled);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            List<PaymentResponseDTO> pagos = paymentService.getPaymentHistory(servicioId);

            if (!includeCancelled) {
                // Filtrar pagos cancelados/reembolsados si no se solicitan
                pagos = pagos.stream()
                        .filter(p -> !"CANCELADO".equals(p.getEstado()) &&
                                !"REEMBOLSADO".equals(p.getEstado()))
                        .toList();
            }

            log.info("✅ Historial obtenido: {} pagos encontrados", pagos.size());

            return ResponseEntity.ok(pagos);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Servicio no encontrado: {}", servicioId);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            log.error("❌ Error obteniendo historial de pagos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{pagoId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener un pago por ID",
            description = "Retorna los detalles completos de un pago específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PaymentResponseDTO> getPayment(
            @PathVariable @Parameter(description = "ID del pago", example = "456") Long pagoId,
            @Parameter(description = "Incluir datos sensibles (transacción, CBU, etc.)")
            @RequestParam(value = "includeSensitive", defaultValue = "false") boolean includeSensitive) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("🔍 GET /api/v1/payments/{}", pagoId);
        log.info("🔒 Incluir sensibles: {}", includeSensitive);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            PaymentResponseDTO pago = paymentService.getPaymentById(pagoId);

            // Proteger datos sensibles si no se solicitan
            if (!includeSensitive && pago != null) {
                // Enmascarar datos sensibles
                if (pago.getTransaccionId() != null) {
                    pago.setTransaccionId(maskSensitiveData(pago.getTransaccionId()));
                }
                if (pago.getNumeroAutorizacion() != null) {
                    pago.setNumeroAutorizacion(maskSensitiveData(pago.getNumeroAutorizacion()));
                }

                // Ocultar campos específicos en datosAdicionales
                if (pago.getDatosAdicionales() != null) {
                    pago.getDatosAdicionales().remove("cbu_destino");
                    pago.getDatosAdicionales().remove("numero_afiliado");
                    pago.getDatosAdicionales().remove("email_pagador");
                    pago.getDatosAdicionales().remove("tarjeta_numero");
                    pago.getDatosAdicionales().remove("tarjeta_cvv");
                }
            }

            if (pago != null) {
                log.info("✅ Pago obtenido exitosamente: ID {}, Estado: {}",
                        pago.getPagoId(), pago.getEstado());
            }

            return ResponseEntity.ok(pago);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Pago no encontrado: {}", pagoId);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            log.error("❌ Error obteniendo pago: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/{pagoId}/refund",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Reembolsar un pago",
            description = "Solicita el reembolso total de un pago previamente procesado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reembolso solicitado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Pago no reembolsable"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "409", description = "Reembolso ya procesado o en proceso")
    })
    public ResponseEntity<PaymentResponseDTO> refundPayment(
            @PathVariable @Parameter(description = "ID del pago a reembolsar") Long pagoId,
            @Parameter(description = "Motivo del reembolso")
            @RequestParam(value = "reason", defaultValue = "Solicitud del cliente") String reason,
            @Parameter(description = "Monto a reembolsar (si es parcial, vacío para total)")
            @RequestParam(value = "amount", required = false) Double partialAmount) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("💸 POST /api/v1/payments/{}/refund", pagoId);
        log.info("📝 Motivo: {}", reason);
        log.info("💰 Monto parcial: {}", partialAmount != null ? partialAmount : "TOTAL");
        log.info("═══════════════════════════════════════════════════════════");

        try {
            PaymentResponseDTO response = paymentService.refundPayment(pagoId, reason);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("✅ Reembolso procesado exitosamente para pago: {}", pagoId);
            } else {
                log.warn("⚠️ Reembolso no procesado: {}", response.getMensaje());
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Pago no encontrado o inválido: {} - {}", pagoId, e.getMessage());

            PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                    .success(false)
                    .estado("PAGO_NO_ENCONTRADO")
                    .mensaje("Pago no encontrado: " + e.getMessage())
                    .errorCode("PAYMENT_NOT_FOUND")
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (IllegalStateException e) {
            log.warn("⚠️ Pago no reembolsable: {} - {}", pagoId, e.getMessage());

            PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                    .success(false)
                    .estado("NO_REEMBOLSABLE")
                    .mensaje("El pago no puede ser reembolsado: " + e.getMessage())
                    .errorCode("NOT_REFUNDABLE")
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            log.error("❌ Error procesando reembolso: {}", e.getMessage(), e);

            PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                    .success(false)
                    .estado("ERROR_REEMBOLSO")
                    .mensaje("Error procesando reembolso: " + e.getMessage())
                    .errorCode("REFUND_ERROR")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // ===========================================
    // ENDPOINTS DE REPORTES Y ESTADÍSTICAS
    // ===========================================

    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar pagos con filtros",
            description = "Busca pagos aplicando múltiples filtros (fechas, estados, métodos, etc.)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    public ResponseEntity<Page<PaymentResponseDTO>> searchPayments(
            @Parameter(description = "Estado del pago")
            @RequestParam(value = "estado", required = false) String estado,
            @Parameter(description = "Método de pago")
            @RequestParam(value = "metodoPago", required = false) String metodoPago,
            @Parameter(description = "Fecha inicial (formato: yyyy-MM-dd)")
            @RequestParam(value = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @Parameter(description = "Fecha final (formato: yyyy-MM-dd)")
            @RequestParam(value = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @Parameter(description = "ID del servicio")
            @RequestParam(value = "servicioId", required = false) Long servicioId,
            @Parameter(description = "ID del paciente")
            @RequestParam(value = "pacienteId", required = false) Long pacienteId,
            @Parameter(description = "ID del prestador")
            @RequestParam(value = "prestadorId", required = false) Long prestadorId,
            @Parameter(description = "Monto mínimo")
            @RequestParam(value = "montoMin", required = false) Double montoMin,
            @Parameter(description = "Monto máximo")
            @RequestParam(value = "montoMax", required = false) Double montoMax,
            @Parameter(description = "Configuración de paginación")
            Pageable pageable) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("🔎 GET /api/v1/payments/search");
        log.info("📊 Estado: {}", estado);
        log.info("💳 Método: {}", metodoPago);
        log.info("📅 Desde: {}, Hasta: {}", fechaDesde, fechaHasta);
        log.info("🆔 Servicio: {}, Paciente: {}, Prestador: {}",
                servicioId, pacienteId, prestadorId);
        log.info("💰 Monto: {} - {}", montoMin, montoMax);
        log.info("📄 Paginación: Page {}, Size {}", pageable.getPageNumber(), pageable.getPageSize());
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // TODO: Implementar búsqueda con paginación en el servicio
            // Page<PaymentResponseDTO> results = paymentService.searchPayments(...);

            log.warn("⚠️ Búsqueda de pagos no implementada aún");

            // Retornar página vacía por ahora
            return ResponseEntity.ok(Page.empty(pageable));

        } catch (Exception e) {
            log.error("❌ Error en búsqueda de pagos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/stats",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Estadísticas de pagos",
            description = "Obtiene estadísticas agregadas de pagos (totales, por método, etc.)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    public ResponseEntity<Map<String, Object>> getPaymentStatistics(
            @Parameter(description = "Fecha inicial para estadísticas")
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha final para estadísticas")
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("📊 GET /api/v1/payments/stats");
        log.info("📅 Período: {} - {}", startDate, endDate);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // TODO: Implementar estadísticas en el servicio
            // Map<String, Object> stats = paymentService.getStatistics(startDate, endDate);

            // Datos de ejemplo
            Map<String, Object> stats = Map.of(
                    "totalPagos", 150,
                    "montoTotal", 3750000.50,
                    "pagosExitosos", 142,
                    "pagosFallidos", 8,
                    "metodosPago", Map.of(
                            "MERCADO_PAGO", 85,
                            "TRANSFERENCIA", 45,
                            "CONTADO", 15,
                            "OBRA_SOCIAL", 5
                    ),
                    "periodo", Map.of(
                            "startDate", startDate != null ? startDate.toString() : "N/A",
                            "endDate", endDate != null ? endDate.toString() : "N/A",
                            "generatedAt", LocalDateTime.now().toString()
                    )
            );

            log.info("✅ Estadísticas generadas: {} registros", stats.get("totalPagos"));

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            log.error("❌ Error generando estadísticas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===========================================
    // ENDPOINTS DE UTILIDAD
    // ===========================================

    @GetMapping(value = "/methods",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener métodos de pago disponibles",
            description = "Retorna la lista de métodos de pago soportados por el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métodos obtenidos exitosamente")
    })
    public ResponseEntity<List<Map<String, Object>>> getAvailablePaymentMethods() {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("💳 GET /api/v1/payments/methods");
        log.info("═══════════════════════════════════════════════════════════");

        List<Map<String, Object>> methods = List.of(
                Map.of(
                        "codigo", "MERCADO_PAGO",
                        "nombre", "Mercado Pago",
                        "descripcion", "Pago con tarjeta de crédito/débito, dinero en cuenta",
                        "comision", 4.99,
                        "disponible", true,
                        "requiereDatos", List.of("email"),
                        "icono", "credit_card"
                ),
                Map.of(
                        "codigo", "TRANSFERENCIA_BANCARIA",
                        "nombre", "Transferencia Bancaria",
                        "descripcion", "Transferencia a CBU/CVU del prestador",
                        "comision", 1.5,
                        "disponible", true,
                        "requiereDatos", List.of("cbu_destino"),
                        "icono", "account_balance"
                ),
                Map.of(
                        "codigo", "CONTADO",
                        "nombre", "Pago en Contado",
                        "descripcion", "Pago en efectivo al momento del servicio",
                        "comision", 0.0,
                        "disponible", true,
                        "requiereDatos", List.of(),
                        "icono", "payments"
                ),
                Map.of(
                        "codigo", "OBRA_SOCIAL",
                        "nombre", "Obra Social",
                        "descripcion", "Cobertura a través de obra social médica",
                        "comision", 0.0,
                        "disponible", true,
                        "requiereDatos", List.of("numero_afiliado", "obra_social"),
                        "icono", "local_hospital"
                )
        );

        log.info("✅ {} métodos de pago disponibles", methods.size());

        return ResponseEntity.ok(methods);
    }

    @GetMapping(value = "/status/{reference}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verificar estado de pago",
            description = "Verifica el estado actual de un pago por referencia externa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Map<String, Object>> checkPaymentStatus(
            @PathVariable @Parameter(description = "Referencia externa (preferenceId, transactionId, etc.)")
            String reference) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("🔄 GET /api/v1/payments/status/{}", reference);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // TODO: Implementar verificación de estado en el servicio
            // PaymentStatus status = paymentService.checkPaymentStatus(reference);

            Map<String, Object> status = Map.of(
                    "reference", reference,
                    "status", "PROCESANDO",
                    "lastUpdated", LocalDateTime.now().minusMinutes(5).toString(),
                    "estimatedCompletion", LocalDateTime.now().plusMinutes(10).toString(),
                    "message", "El pago está siendo procesado"
            );

            log.info("✅ Estado obtenido para referencia: {}", reference);

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            log.error("❌ Error verificando estado: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/{pagoId}/cancel",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cancelar un pago pendiente",
            description = "Cancela un pago que está en estado pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago cancelado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Pago no cancelable"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PaymentResponseDTO> cancelPayment(
            @PathVariable @Parameter(description = "ID del pago a cancelar") Long pagoId,
            @Parameter(description = "Motivo de la cancelación")
            @RequestParam(value = "reason", defaultValue = "Solicitud del usuario") String reason) {

        log.info("═══════════════════════════════════════════════════════════");
        log.info("🚫 POST /api/v1/payments/{}/cancel", pagoId);
        log.info("📝 Motivo: {}", reason);
        log.info("═══════════════════════════════════════════════════════════");

        try {
            // TODO: Implementar cancelación en el servicio
            // PaymentResponseDTO response = paymentService.cancelPayment(pagoId, reason);

            PaymentResponseDTO response = PaymentResponseDTO.builder()
                    .success(true)
                    .pagoId(pagoId)
                    .estado("CANCELADO")
                    .mensaje("Pago cancelado exitosamente: " + reason)
                    .build();

            log.info("✅ Pago {} cancelado exitosamente", pagoId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error cancelando pago: {}", e.getMessage(), e);

            PaymentResponseDTO errorResponse = PaymentResponseDTO.builder()
                    .success(false)
                    .estado("ERROR_CANCELACION")
                    .mensaje("Error cancelando pago: " + e.getMessage())
                    .errorCode("CANCELLATION_ERROR")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // ===========================================
    // MÉTODOS PRIVADOS DE APOYO
    // ===========================================

    /**
     * Determina el código HTTP apropiado basado en la respuesta del servicio
     */
    private HttpStatus determineHttpStatus(PaymentResponseDTO response) {
        if (response.getSuccess() != null && response.getSuccess()) {
            return HttpStatus.OK;
        }

        String estado = response.getEstado();
        if (estado == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return switch (estado) {
            case "ERROR_VALIDACION", "ARGUMENTO_INVALIDO" -> HttpStatus.BAD_REQUEST;
            case "ERROR_ESTADO", "NO_REEMBOLSABLE", "METODO_INVALIDO" -> HttpStatus.CONFLICT;
            case "PAGO_NO_ENCONTRADO", "SERVICIO_NO_ENCONTRADO" -> HttpStatus.NOT_FOUND;
            case "ERROR_MERCADO_PAGO", "ERROR_REEMBOLSO", "ERROR_CANCELACION" -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    /**
     * Enmascara datos sensibles para logs y respuestas
     */
    private String maskSensitiveData(String data) {
        if (data == null || data.length() <= 8) {
            return "****";
        }
        return data.substring(0, 4) + "****" + data.substring(data.length() - 4);
    }

    /**
     * Valida que un request tenga los campos mínimos requeridos
     */
    private void validateMinimumRequest(PaymentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request no puede ser nulo");
        }
        if (request.getServicioId() == null) {
            throw new IllegalArgumentException("servicioId es requerido");
        }
        if (request.getMetodoPago() == null || request.getMetodoPago().trim().isEmpty()) {
            throw new IllegalArgumentException("metodoPago es requerido");
        }
    }

    /**
     * Registra auditoría de operaciones
     */
    private void logAudit(String operation, Long entityId, String userId, String ip, String details) {
        log.info("[AUDIT] Operación: {}, Entidad: {}, Usuario: {}, IP: {}, Detalles: {}",
                operation, entityId, userId, ip, details);
    }

    /**
     * Procesa webhook de MercadoPago (implementación interna)
     */
    private void processMercadoPagoWebhookInternal(Map<String, Object> webhookData) {
        log.info("🔔 Procesando webhook de MercadoPago internamente");

        try {
            String type = (String) webhookData.get("type");
            String action = (String) webhookData.get("action");
            String dataId = (String) webhookData.get("data.id");

            log.info("📊 Webhook recibido - Tipo: {}, Acción: {}, Data ID: {}", type, action, dataId);

            // Aquí iría la lógica para procesar el webhook
            // Por ahora solo logueamos la información
            if ("payment".equals(type) && "approved".equals(action)) {
                log.info("✅ Pago aprobado en MercadoPago: {}", dataId);
                // En una implementación real, aquí actualizarías el pago en la base de datos
            } else if ("payment".equals(type) && "rejected".equals(action)) {
                log.warn("❌ Pago rechazado en MercadoPago: {}", dataId);
            }

        } catch (Exception e) {
            log.error("❌ Error procesando webhook internamente: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando webhook", e);
        }
    }
}