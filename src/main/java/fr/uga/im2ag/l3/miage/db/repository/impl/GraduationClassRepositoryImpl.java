package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.model.GraduationClass;

import javax.persistence.EntityManager;
import java.util.List;

public class GraduationClassRepositoryImpl extends BaseRepositoryImpl implements GraduationClassRepository {

    public GraduationClassRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

   //A faire 
    @Override
    public GraduationClass findByYearAndName(Integer year, String name) {
    	GraduationClass Gc;
    	Gc = entityManager.find(GraduationClass.class,name );
        return Gc;
    }

    @Override
    public void save(GraduationClass entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(GraduationClass entity) {
        entityManager.remove(entity);
    }

    @Override
    public GraduationClass findById(Long id) {
    	GraduationClass Gc;
    	Gc = entityManager.find(GraduationClass.class,id );
        return Gc;
    }

    @Override
    public List<GraduationClass> getAll() {
        // TODO
        return null;
    }
}
