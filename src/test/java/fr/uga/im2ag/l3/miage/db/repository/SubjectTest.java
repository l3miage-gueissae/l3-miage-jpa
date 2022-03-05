package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Student;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class SubjectTest extends Base {

    SubjectRepository subjectRepository;
    TeacherRepository teacherRepository;

    @BeforeEach
    void before() {
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
        
        teacherRepository = daoFactory.newTeacherRepository(entityManager);
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
        final var subject = Fixtures.createSubject();
    	final var teacher = Fixtures.createTeacher(subject, null, null);
    	final var teacher2 = Fixtures.createTeacher(subject, null, null);

    	entityManager.getTransaction().begin();
    	
    	subjectRepository.save(subject);
    	teacherRepository.save(teacher);
    	teacherRepository.save(teacher2);
    	
    	entityManager.getTransaction().commit();
    	entityManager.detach(teacher);
    	entityManager.detach(teacher2);
    	entityManager.detach(subject);
    	
    	ArrayList<Teacher> LesProfs = new ArrayList<Teacher>(subjectRepository.findTeachers(subject.getId()));
    	// Vérifie si il y a bien 2 teacher
    	assertThat(LesProfs.size()).isEqualTo(2);
    	// On prend le premier
    	Teacher t = LesProfs.get(0);
    	// On vérifie si le prenoms correspond
    	assertThat(t.getFirstName()).isEqualTo(teacher.getFirstName());
    
    	// Vérification que getAll <subject> fonctionne 
    	System.out.println(subjectRepository.getAll().get(0).getName());
    
    	
    }
    @Test
    void shouldDeleteSubject() {
  	  	
	  	// Création d'une classe
	  	final var subject = Fixtures.createSubject();
	  	        
        entityManager.getTransaction().begin();
        
        // Persite dans la base
        subjectRepository.save(subject);
        
        entityManager.getTransaction().commit();
        subjectRepository.delete(subject);

        //Récupère l'étudiant 
        Subject pSubject = subjectRepository.findById(subject.getId());
        //Vérifie si il est null
        assertThat(pSubject).isNull();
    }
    
    @Test
    void shouldGetAllSubject() {
            // Création d'un student
            final var subject = Fixtures.createSubject();
	  	
	  		subject.setName("BDD");
            
            // Création d'un student
            final var subject2 = Fixtures.createSubject();
	  	
	  		subject.setName("APO");
            
            // Création d'un student
            final var subject3 = Fixtures.createSubject();
	  	
	  		subject.setName("MSI");
              
            
            System.out.println(subject.getName());
            System.out.println(subject2.getName());
            System.out.println(subject3.getName());
            
            entityManager.getTransaction().begin();
            // Persite dans la base
            subjectRepository.save(subject);
            subjectRepository.save(subject2);
            subjectRepository.save(subject3);
            
            
            entityManager.getTransaction().commit();
            
            entityManager.detach(subject);
            entityManager.detach(subject2);
            entityManager.detach(subject3);

            //Récupère l'étudiant 
            List<Subject> LesSubjects = subjectRepository.getAll();
            
            assertThat(LesSubjects).isNotNull();
            
            //Vérifie si le nom de sujet est bon pour le premier sujet récupéré
            assertThat(LesSubjects.get(0).getName()).isEqualTo(subject.getName());
            //Vérifie si le prénom est bon pour le second étudiant récupéré
            assertThat(LesSubjects.get(1).getName()).isEqualTo(subject2.getName());
            //Vérifie si le prénom est bon pour le troisième étudiant récupéré
            assertThat(LesSubjects.get(2).getName()).isEqualTo(subject3.getName());
        }
}
