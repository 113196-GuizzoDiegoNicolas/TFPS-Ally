package ar.edu.utn.frc.tup.lc.iv.specifications;

import ar.edu.utn.frc.tup.lc.iv.entities.EmailTemplateEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Provides specifications for querying EmailTemplateEntity instances.
 */
public class TemplateSpecifications {

    /**
     * Creates a Specification for filtering EmailTemplateEntity.
     * @param active           filters templates by their active status.
     * @param hasPlaceholders  filters templates by their placeholder status.
     * @param name             filters by a name containing this string.
     * @return A Specification that can be used to retrieve  objects
     *         matching the specified criteria.
     */
    public static Specification<EmailTemplateEntity> createSpecification(Boolean active, Boolean hasPlaceholders, String name) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (active != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("active"), active));
            }
            if (hasPlaceholders != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("hasPlaceholders"), hasPlaceholders));
            }
            if (name != null && !name.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            return predicates;
        };
    }

}
