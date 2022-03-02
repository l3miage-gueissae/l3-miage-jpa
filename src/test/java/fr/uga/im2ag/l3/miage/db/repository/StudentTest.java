package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest extends Base {

	SubjectRepository subjectRepository;
	GradeRepository gradeRepository;
	GraduationClassRepository graduationClassRepository;	
    StudentRepository studentRepository;

    @BeforeEach
    void before() {
    	subjectRepository = daoFactory.newSubjectRepository(entityManager);
    	gradeRepository = daoFactory.newGradeRepository(entityManager);
    	graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
        studentRepository = daoFactory.newStudentRepository(entityManager);
        
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveStudent() {
    	  // Cr�ation d'un subject
  	  	  final var subject = Fixtures.createSubject();
  	  	  // Cr�ation d'une grade
    	  final var grade = Fixtures.createGrade(subject);
    	  // Cr�ation d'une classe
    	  final var graduationClass = Fixtures.createClass();
    	  // Cr�ation d'un student
    	  final var student = Fixtures.createStudent(graduationClass);
    	  // Cr�ation d'une liste de Grade
    	  List<Grade> lesNotes = new ArrayList<Grade>();
    	  // Ajout d'une grade
    	  lesNotes.add(grade);
    	  // Liste de grade ajout� dans le student
    	  student.setGrades(lesNotes);
    	  
          System.out.println(student.getFirstName());
          
          entityManager.getTransaction().begin();
          // Persite dans la base
          subjectRepository.save(subject);
          gradeRepository.save(grade);
          graduationClassRepository.save(graduationClass);
          studentRepository.save(student);
          
          
          entityManager.getTransaction().commit();
          entityManager.detach(student);


          //R�cup�re l'�tudiant 
          var pStudent = studentRepository.findById(student.getId());
          //V�rifie si il est pas null
          assertThat(pStudent).isNotNull().isNotSameAs(student);
          // V�rifie si le nom est �gal 
          assertThat(pStudent.getFirstName()).isEqualTo(student.getFirstName());
          System.out.println("nom de class");
          System.out.println(pStudent.getBelongTo().getName());
          // Test si le nom du subject est �gal 
          assertThat(pStudent.getGrades().get(0).getSubject().getName()).isEqualTo(student.getGrades().get(0).getSubject().getName());

    }

    @Test
    void shouldFindStudentHavingGradeAverageAbove() {
        // TODO
    }

}
