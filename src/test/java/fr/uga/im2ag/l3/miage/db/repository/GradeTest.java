package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Teacher;
import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GradeTest extends Base {

    GradeRepository gradeRepository;
    SubjectRepository subjectRepository;
    @BeforeEach
    void before() {
        gradeRepository = daoFactory.newGradeRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveGrade() {
        final var subject = Fixtures.createSubject();
        System.out.println(subject.getName());
        final var grade = Fixtures.createGrade(subject);
        System.out.println(grade.getValue());
     
       
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        
        entityManager.getTransaction().commit();
        entityManager.detach(grade);

        var pGrade = gradeRepository.findById(grade.getId());
        assertThat(pGrade).isNotNull().isNotSameAs(grade);
        assertThat(pGrade.getValue()).isNotNull().isNotSameAs(grade.getValue());

    }

    @Test
    void shouldFailUpgradeGrade() {
        // TODO, ici tester que la mise Ã  jour n'a pas eu lieu.
    	 
    }

    @Test
    void shouldFindHighestGrades() {
    	
    	final var subject = Fixtures.createSubject();
        //System.out.println(subject.getName());
        final var grade = Fixtures.createGrade(subject);
        /*System.out.println(grade.getWeight());
        System.out.println(grade.getValue());*/
        
        final float  limit = 3;
        final int limit_int = 3;
     
       
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        
        entityManager.getTransaction().commit();
        entityManager.detach(grade);
        entityManager.detach(subject);
        System.out.println("ouiiiieieieieeiieie 1 ");
        ArrayList<Grade> LesNotes = new ArrayList<Grade>(gradeRepository.findHighestGrades(limit_int)); // problem 
       
        System.out.println("ouiiiieieieieeiieie 2");
        // Vérifie si il y a bien 1 note
    	assertThat(LesNotes.size()).isEqualTo(1);
    	// On prend la note
    	Grade g = LesNotes.get(0);
    	System.out.println("ouiiiieieieieeiieie 3"+g.getValue());
    	// On vérifie si la note est plus grande que la limite
    	//assertThat(g.getValue()).isGreaterThan(limit);
    
    	// Vérification que getAll <grade> fonctionne 
    	System.out.println(gradeRepository.getAll().get(0).getValue());
    }

    @Test
    void shouldFindHighestGradesBySubject() {
        // TODO
    }

}
