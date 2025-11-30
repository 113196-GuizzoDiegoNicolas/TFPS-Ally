package Ally.Scafolding.entities;

import Ally.Scafolding.models.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "metodos_pagos")
@Audited
public class MetodosPagosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_pago_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

}
