package ru.terra.server.engine;

import ru.terra.server.db.controllers.AbstractJpaController;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEngine<Entity, Dto> {

    protected AbstractJpaController<Entity> jpaController;

    public AbstractEngine(AbstractJpaController<Entity> jpaController) {
        this.jpaController = jpaController;
    }

    public List<Entity> listBeans(Boolean all, Integer page, Integer perPage) {
        try {
            return jpaController.list(all, page, perPage);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Entity getBean(Integer id) {
        try {
            return jpaController.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateBean(Entity bean) {
        try {
            jpaController.update(bean);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract boolean updateDTO(Dto dto);

    public Entity createBean(Entity bean) {
        try {
            jpaController.create(bean);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract Dto createDTO(Dto dto);

    public abstract List<Dto> listDtos(Boolean all, Integer page, Integer perPage);

    public abstract Dto getDto(Integer id);

    public boolean delete(Integer id) {
        try {
            jpaController.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract void dtoToEntity(Dto dto, Entity entity);

}
