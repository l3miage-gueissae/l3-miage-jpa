package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubjectTest extends Base {

    SubjectRepository subjectRepository;

    @BeforeEach
    void before() {
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveSubject() {

        final var subject = Fixtures.createSubject();
        final var subject2 = Fixtures.createSubject();
        System.out.println(subject.getName());
        subject.setName("a");
        //subject2.setName("a"); Erreur car un nom de matiere est unique
        subject2.setName("b");
        entityManager.getTransaction().begin();
     
        
        subjectRepository.save(subject);
        subjectRepository.save(subject2);
        entityManager.getTransaction().commit();
        entityManager.detach(subject);

        var pSubject = subjectRepository.findById(subject.getId());
        assertThat(pSubject).isNotNull().isNotSameAs(subject);
        assertThat(pSubject.getName()).isEqualTo(subject.getName());

    }

    @Test
    void shouldFindTeachersForSubject() {
        // TODO
    	
    }

}
