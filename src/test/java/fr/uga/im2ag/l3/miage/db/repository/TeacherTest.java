package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.GraduationClass;
import fr.uga.im2ag.l3.miage.db.model.Student;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;
import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
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
        entityManager.detach(subject);
        entityManager.detach(student1);
        entityManager.detach(student2);
        entityManager.detach(student3);
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
    
    @Test
    void shouldGetAllTeacher() {
            // Initialisation d'une liste N grade    	
    		ArrayList<Teacher> lesProfs = new ArrayList<Teacher>();
    		ArrayList<GraduationClass> lesClasses = new ArrayList<GraduationClass>();
    		Subject subject = Fixtures.createSubject();
            entityManager.getTransaction().begin();
            subjectRepository.save(subject);
            for(int i = 0; i < 10; i++) {
                final var classe = Fixtures.createClass();
                lesClasses.add(classe);
                graduationClassRepository.save(classe);

    		}
       
        	for(int i = 0; i < 10; i++) {
                final var teacher = Fixtures.createTeacher(subject,lesClasses.get(i));
                lesProfs.add(teacher);
                teacherRepository.save(teacher);

    		}

            entityManager.getTransaction().commit();
    		
    		for(int i = 0; i < lesProfs.size(); i++) {
                entityManager.detach(lesProfs.get(i));
    		}
    		
    		ArrayList<Teacher> pLesProfs = new ArrayList<Teacher>();
    		pLesProfs = new ArrayList(teacherRepository.getAll());
    		assertThat(pLesProfs).isNotEmpty();
    		assertThat(pLesProfs.size()).isEqualTo(lesProfs.size());
    		for(int i = 0; i < pLesProfs.size(); i++) {
    		
               assertThat(pLesProfs.get(i).getId()).isEqualTo(lesProfs.get(i).getId());
               assertThat(pLesProfs.get(i).getGender()).isEqualTo(lesProfs.get(i).getGender());
               assertThat(pLesProfs.get(i).getFirstName()).isEqualTo(lesProfs.get(i).getFirstName());
               assertThat(pLesProfs.get(i).getLastName()).isEqualTo(lesProfs.get(i).getLastName());

               assertThat(pLesProfs.get(i).getTeaching().getId()).isEqualTo(lesProfs.get(i).getTeaching().getId());

               assertThat(pLesProfs.get(i).getHeading().getId()).isEqualTo(lesProfs.get(i).getHeading().getId());

    		}
    		
    }
    
    @Test
    void shouldDeleteTeacher() {
    	Subject subject = Fixtures.createSubject();
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        
        GraduationClass classe = Fixtures.createClass();
        graduationClassRepository.save(classe);
        
        Teacher teacher = Fixtures.createTeacher(subject, classe);
        teacherRepository.save(teacher);
        entityManager.getTransaction().commit();
        teacherRepository.delete(teacher);
        
        Teacher pTeacher = teacherRepository.findById(teacher.getId());
        assertThat(pTeacher).isNull();
    	
    }

}
