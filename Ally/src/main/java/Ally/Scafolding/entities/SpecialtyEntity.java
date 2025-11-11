package Ally.Scafolding.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.envers.Audited;

/**
 * Represents a specialty in the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "specialties")
@Audited
public class SpecialtyEntity {

    /**
     * Unique identifier for the specialty.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the specialty.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;
}