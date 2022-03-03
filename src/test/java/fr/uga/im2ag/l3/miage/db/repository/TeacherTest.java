package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Student;
import fr.uga.im2ag.l3.miage.db.model.Teacher;
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
  	  // Création d'un subject
	final var subject = Fixtures.createSubject();
  	  // Création d'une classe
  	  final var graduationClass = Fixtures.createClass();
  	  // Création d'un student
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


        //Récupère le teacher 
        var pTeacher = teacherRepository.findById(teacher.getId());
        //Vérifie si il est pas null
        assertThat(pTeacher).isNotNull().isNotSameAs(teacher);
        // Vérifie si le nom est égal 
        assertThat(pTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        // vérifie sa la date et nom de la graduationClass sont égaux
        assertThat(pTeacher.getHeading().getName()).isEqualTo(teacher.getHeading().getName());
        assertThat(pTeacher.getHeading().getYear()).isEqualTo(teacher.getHeading().getYear());
        // Test si le nom du subject est égal 
        assertThat(pTeacher.getTeaching().getName()).isEqualTo(subject.getName());
        // Test d'un élève favori
        assertThat(pTeacher.getFavorites().get(0).getFirstName()).isEqualTo(teacher.getFavorites().get(0).getFirstName());
    }

    @Test
    void shouldFindHeadingGraduationClassByYearAndName() {
        // TODO
    	  final var graduationClass = Fixtures.createClass();
    	  graduationClass.setName("Miage");
    	  graduationClass.setYear(2022);
    	  final var subject = Fixtures.createSubject();
    	  final var teacher = Fixtures.createTeacher(subject, graduationClass);
    	  
    	  entityManager.getTransaction().begin();
    	  
    	  graduationClassRepository.save(graduationClass);
    	  subjectRepository.save(subject);
    	  teacherRepository.save(teacher);
    	  //System.out.println(teacher.getFirstName());
    	  entityManager.getTransaction().commit();
    	  entityManager.detach(teacher);
    	  entityManager.detach(subject);
    	  entityManager.detach(graduationClass);
    	  
    	  //test de getALL
    	  assertThat(teacherRepository.getAll().size()).isNotNull().isNotEqualTo(0);
    	  //Récupération du teacher par date et name
    	  Teacher t = teacherRepository.findHeadingGraduationClassByYearAndName(graduationClass.getYear(), graduationClass.getName());
    	  //System.out.println(t.getFirstName());
    	  assertThat(t).isNotNull().isNotSameAs(teacher);
    	  assertThat(t.getId()).isEqualTo(teacher.getId());
    	  assertThat(t.getFirstName()).isEqualTo(teacher.getFirstName());
    	  assertThat(t.getLastName()).isEqualTo(teacher.getLastName());
    	  
    	  

    }

}
