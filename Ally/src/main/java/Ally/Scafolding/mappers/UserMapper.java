package Ally.Scafolding.mappers;

import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.models.User;

public class UserMapper {

    public static User toModel(UsersEntity entity) {
        if (entity == null) return null;

        return new User(
                entity.getId(),
                entity.getUsuario(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getRol(),
                entity.getActivo(),
                entity.getBloqueado(),
                entity.getFechaCreacion(),
                entity.getUltimoLogin(),
                entity.getIntentosFallidos()
        );
    }

    public static UsersEntity toEntity(User model) {
        if (model == null) return null;

        UsersEntity entity = new UsersEntity();
        entity.setId(model.getId()); // Importante si es update
        entity.setUsuario(model.getUsername());
        entity.setPassword(model.getPassword());
        entity.setEmail(model.getEmail());
        entity.setRol(model.getRole());
        entity.setActivo(model.isActive());
        entity.setBloqueado(model.isLocked());
        entity.setFechaCreacion(model.getCreatedAt());
        entity.setUltimoLogin(model.getLastLogin());
        entity.setIntentosFallidos(model.getFailedAttempts());
        return entity;
    }
}

