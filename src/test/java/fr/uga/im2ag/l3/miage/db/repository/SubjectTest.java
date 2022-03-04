package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Student;
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
    	// V�rifie si il y a bien 2 teacher
    	assertThat(LesProfs.size()).isEqualTo(2);
    	// On prend le premier
    	Teacher t = LesProfs.get(0);
    	// On v�rifie si le prenoms correspond
    	assertThat(t.getFirstName()).isEqualTo(teacher.getFirstName());
    
    	// V�rification que getAll <subject> fonctionne 
    	System.out.println(subjectRepository.getAll().get(0).getName());
    
    	
    }
    
    void shouldDeleteSubject() {/*
  	  	
	  	// Cr�ation d'une classe
	  	final var subject = Fixtures.createSubject();
	  	
	  	subject.setName("BDD");
        
        entityManager.getTransaction().begin();
        
        // Persite dans la base
        graduationClassRepository.save(graduationClass);
        studentRepository.save(student);
        
        entityManager.getTransaction().commit();
        studentRepository.delete(student);

        //R�cup�re l'�tudiant 
        var pStudent = studentRepository.findById(student.getId());
        //V�rifie si il est null
        assertThat(pStudent).isNull();*/
    }
    
    @Test
    void shouldGetAllSubject() {/*
            // Cr�ation d'une classe
            final var graduationClass = Fixtures.createClass();
              
            // Cr�ation d'un student
            final var student = Fixtures.createStudent(graduationClass);
            
            student.setFirstName("Manu");
            
            // Cr�ation d'un student
            final var student2 = Fixtures.createStudent(graduationClass);
            
            student2.setFirstName("Justin");
            
            // Cr�ation d'un student
            final var student3 = Fixtures.createStudent(graduationClass);
            
            student2.setFirstName("Quentin");
              
            
            System.out.println(student.getFirstName());
            System.out.println(student2.getFirstName());
            System.out.println(student3.getFirstName());
            
            entityManager.getTransaction().begin();
            // Persite dans la base
            graduationClassRepository.save(graduationClass);
            studentRepository.save(student);
            studentRepository.save(student2);
            studentRepository.save(student3);
            
            
            entityManager.getTransaction().commit();
            
            entityManager.detach(student);
            entityManager.detach(student2);
            entityManager.detach(student3);

            //R�cup�re l'�tudiant 
            List<Student> LesStudents = studentRepository.getAll();
            
            //V�rifie si le pr�nom est bon pour le premier �tudiant r�cup�r�
            assertThat(LesStudents.get(0).getFirstName()).isEqualTo(student.getFirstName());
            //V�rifie si le pr�nom est bon pour le second �tudiant r�cup�r�
            assertThat(LesStudents.get(1).getFirstName()).isEqualTo(student2.getFirstName());
            //V�rifie si le pr�nom est bon pour le troisi�me �tudiant r�cup�r�
            assertThat(LesStudents.get(2).getFirstName()).isEqualTo(student3.getFirstName());*/
        }
}
