package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;

import javax.persistence.EntityManager;
import java.util.List;

public class TeacherRepositoryImpl extends BaseRepositoryImpl implements TeacherRepository {

    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public TeacherRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public Teacher findHeadingGraduationClassByYearAndName(Integer year, String name) {
        
        return entityManager.createNamedQuery("heading-year-name", Teacher.class)
        		.setParameter("year", year)
        		.setParameter("name", name)
        		.getSingleResult();
    }

    @Override
    public void save(Teacher entity) {
        entityManager.persist(entity);

    }

    @Override
    public void delete(Teacher entity) {
        entityManager.remove(entity);

    }

    @Override
    public Teacher findById(Long id) {
        Teacher T;
        T = entityManager.find(Teacher.class, id);
        return T;
    } 

    @Override
    public List<Teacher> getAll() {
    	 List<Teacher> Res;
         Res = entityManager.createQuery("SELECT t from Teacher t", Teacher.class).getResultList();
         return Res;
    }
}
