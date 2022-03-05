package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.GraduationClass;

import javax.persistence.EntityManager;
import java.util.List;

public class GraduationClassRepositoryImpl extends BaseRepositoryImpl implements GraduationClassRepository {

    public GraduationClassRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public GraduationClass findByYearAndName(Integer year, String name) {
    	GraduationClass Res = entityManager.createNamedQuery("Year-Name", GraduationClass.class)
        		.setParameter("annee", year)
        		.setParameter("nom", name)
        		.getSingleResult();
        return Res;
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
    
    // ajout d'un order by id pour sortir une liste trié par l'id (pratique pour le test)
    @Override
    public List<GraduationClass> getAll() {
    	List<GraduationClass> Res;
        Res = entityManager.createQuery("SELECT g from GraduationClass g order by id", GraduationClass.class).getResultList();
        return Res;
    }
}
