package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraduationClassTest extends Base {

    GraduationClassRepository classRepository;

    @BeforeEach
    void before() {
        classRepository = daoFactory.newGraduationClassRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveClass() {
    	   final var Gc = Fixtures.createClass();
    	   final var testGc = Fixtures.createClass();
           System.out.println(Gc.getName());
          
           entityManager.getTransaction().begin();
//           Test de la contrainte unique name + year 
//           Gc.setName("a");
//           Gc.setYear(1);
//           testGc.setName("a");
//           testGc.setYear(1)
//           classRepository.save(testGc);
           classRepository.save(Gc);
          
           entityManager.getTransaction().commit();
           entityManager.detach(Gc); 

           var pGc = classRepository.findById(Gc.getId());
           assertThat(pGc).isNotNull().isNotSameAs(Gc);
           assertThat(pGc.getName()).isEqualTo(Gc.getName());
           assertThat(pGc.getYear()).isEqualTo(Gc.getYear());
    }


    @Test
    void shouldFindByYearAndName() {
        // TODO
    }

}
