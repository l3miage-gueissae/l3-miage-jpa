package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;

import static org.assertj.core.api.Assertions.assertThat;

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
        // TODO, ici tester que la mise à jour n'a pas eu lieu.
    }

    @Test
    void shouldFindHighestGrades() {
        // TODO
    }

    @Test
    void shouldFindHighestGradesBySubject() {
        // TODO
    }

}
