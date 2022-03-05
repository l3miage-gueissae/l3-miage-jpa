package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;

import javax.persistence.EntityManager;

import java.util.Collection;
import java.util.List;

public class GradeRepositoryImpl extends BaseRepositoryImpl implements GradeRepository {

    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public GradeRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Grade> findHighestGrades(int limit) {
    	List<Grade> Res;
        Res = entityManager.createNamedQuery("HighestGrade", Grade.class)
        		.setParameter("limit", (float)limit)
        		.getResultList();
        return Res;
    }

    @Override
    public List<Grade> findHighestGradesBySubject(int limit, Subject subject) {
    	List<Grade> Res;
        Res = entityManager.createNamedQuery("HighestGrade-Subject", Grade.class)
        		.setParameter("limit", (float)limit)
        		.setParameter("subjid", subject.getId())
        		.getResultList();
        return Res;
    }

    @Override
    public void save(Grade entity) {
       entityManager.persist(entity);
    }

    @Override
    public void delete(Grade entity) {
        entityManager.remove(entity);
    }

    @Override
    public Grade findById(Long id) {
    	Grade grd;
    	grd = super.entityManager.find(Grade.class, id);
        return grd;
    }
    
    // ajout d'un order by id pour sortir une liste trié par l'id (pratique pour le test)
    @Override
    public List<Grade> getAll() {
    	List<Grade> Res;
        Res = entityManager.createQuery("SELECT g from Grade g order by id", Grade.class).getResultList();
        return Res;
    }
}
