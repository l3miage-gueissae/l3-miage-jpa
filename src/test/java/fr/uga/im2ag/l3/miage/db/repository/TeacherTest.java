package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Student;
import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertDelegateTarget;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeacherTest extends Base {
	
	StudentRepository studentRepository;
	GraduationClassRepository graduationClassRepository;
	SubjectRepository subjectRepository;
    TeacherRepository teacherRepository;

    @BeforeEach
    void before() {
    	studentRepository = daoFactory.newStudentRepository(entityManager);
    	graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
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
    void shouldSaveTeacher()  {
  	  // Cr�ation d'un subject
	final var subject = Fixtures.createSubject();
  	  // Cr�ation d'une classe
  	  final var graduationClass = Fixtures.createClass();
  	  // Cr�ation d'un student
  	  final var student1 = Fixtures.createStudent(graduationClass);
  	  final var student2 = Fixtures.createStudent(graduationClass);
  	  final var student3 = Fixtures.createStudent(graduationClass);

  	  final var teacher = Fixtures.createTeacher(subject, graduationClass, student1,student2);
  
        System.out.println(teacher.getFirstName());
        
        entityManager.getTransaction().begin();
        // Persite dans la base
        subjectRepository.save(subject);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        graduationClassRepository.save(graduationClass);
        teacherRepository.save(teacher);
        
        
        entityManager.getTransaction().commit();
        entityManager.detach(teacher);


        //R�cup�re le teacher 
        var pTeacher = teacherRepository.findById(teacher.getId());
        //V�rifie si il est pas null
        assertThat(pTeacher).isNotNull().isNotSameAs(teacher);
        // V�rifie si le nom est �gal 
        assertThat(pTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        // v�rifie sa la date et nom de la graduationClass sont �gaux
        assertThat(pTeacher.getHeading().getName()).isEqualTo(teacher.getHeading().getName());
        assertThat(pTeacher.getHeading().getYear()).isEqualTo(teacher.getHeading().getYear());
        // Test si le nom du subject est �gal 
        assertThat(pTeacher.getTeaching().getName()).isEqualTo(subject.getName());
        // Test d'un �l�ve favori
        assertThat(pTeacher.getFavorites().get(0).getFirstName()).isEqualTo(teacher.getFavorites().get(0).getFirstName());
    }

    @Test
    void shouldFindHeadingGraduationClassByYearAndName() {
        // TODO
    }

}
