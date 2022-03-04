package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.model.Student;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;

import javax.persistence.EntityManager;

import java.util.Collection;
import java.util.List;

public class StudentRepositoryImpl extends BaseRepositoryImpl implements StudentRepository {


    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public StudentRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void save(Student entity) {
        entityManager.persist(entity);

    }

    @Override
    public void delete(Student entity) {
        entityManager.remove(entity);

    }

    @Override
    public Student findById(Long id) {
    	Student stu;
    	stu = entityManager.find(Student.class, id);
        return stu;
    }

    @Override
    public List<Student> getAll() {
    	 List<Student> Res;
         Res = entityManager.createQuery("SELECT s from Student s", Student.class).getResultList();
         return Res;
    }

    @Override
    public List<Student> findStudentHavingGradeAverageAbove(float minAverage) {
    	List<Student> Res;
        Res = entityManager.createNamedQuery("Student-AvgAbove", Student.class)
        		.setParameter("avg", (double)minAverage)
        		.getResultList();
        return Res;
    }
}
