package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.GraduationClass;
import fr.uga.im2ag.l3.miage.db.model.Student;
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
    	  // Création d'un subject
  	  	  final var subject = Fixtures.createSubject();
  	  	  // Création d'une grade
    	  final var grade = Fixtures.createGrade(subject);
    	  // Création d'une classe
    	  final var graduationClass = Fixtures.createClass();
    	  // Création d'un student
    	  final var student = Fixtures.createStudent(graduationClass);
    	  // Création d'une liste de Grade
    	  List<Grade> lesNotes = new ArrayList<Grade>();
    	  // Ajout d'une grade
    	  lesNotes.add(grade);
    	  // Liste de grade ajouté dans le student
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


          //Récupère l'étudiant 
          var pStudent = studentRepository.findById(student.getId());
          //Vérifie si il est pas null
          assertThat(pStudent).isNotNull().isNotSameAs(student);
          // Vérifie si le nom est égal 
          assertThat(pStudent.getFirstName()).isEqualTo(student.getFirstName());
          System.out.println("nom de class");
          System.out.println(pStudent.getBelongTo().getName());
          // Test si le nom du subject est égal 
          assertThat(pStudent.getGrades().get(0).getSubject().getName()).isEqualTo(student.getGrades().get(0).getSubject().getName());

    }

    @Test
    void shouldFindStudentHavingGradeAverageAbove() {
    	
    	// Création d'un subject de nom BDD
    	final var subject = Fixtures.createSubject();
    	subject.setName("BDD");
    	
    	// Création de 3 grades
  	  	final var grade = Fixtures.createGrade(subject);
  	  	final var grade2 = Fixtures.createGrade(subject);
  	  	final var grade3 = Fixtures.createGrade(subject);
  	  	final var grade4 = Fixtures.createGrade(subject);
  	  	
  	  	// On affecte des valeurs et des poids à chacune des 3 notes
  	  	grade.setValue((float)10);
  	  	grade.setWeight((float)3);
  	  	
  	  	grade2.setValue((float)7);
  	  	grade2.setWeight((float)1);
  	  	
  	  	grade3.setValue((float)7);
	  	grade3.setWeight((float)1);
  	  	
  	  	grade4.setValue((float)4);
  	  	grade4.setWeight((float)3);
  	  	
	  	// Création d'une graduationClass
	  	final var graduationClass = Fixtures.createClass();
	  	
	  	// Création d'un student de nom Manu
	  	final var student_m = Fixtures.createStudent(graduationClass);
	  	student_m.setFirstName("Manu");
	  	
	  	// Création d'un student de nom Justin
	  	final var student_j = Fixtures.createStudent(graduationClass);
	  	student_j.setFirstName("Justin");
	  	
	  	// Création d'une liste de Grade pour Manu et Justin
	  	List<Grade> lesNotes_Manu = new ArrayList<Grade>();
	  	List<Grade> lesNotes_Justin = new ArrayList<Grade>();
	  	
	  	// Ajout de deux grades dans les notes de Manu
	  	lesNotes_Manu.add(grade);
	  	lesNotes_Manu.add(grade2);
	  	
	  	// Ajout de deux grades dans les notes de Justin
	  	lesNotes_Justin.add(grade3);
	  	lesNotes_Justin.add(grade4);
	  	
	  	// Liste de grade ajouté dans le student Manu
	  	student_m.setGrades(lesNotes_Manu);
	  	
	  	// Liste de grade ajouté dans le student Justin
	  	student_j.setGrades(lesNotes_Justin);
	  	
        //System.out.println(student.getFirstName());
        
        entityManager.getTransaction().begin();
        // Persite dans la base
        subjectRepository.save(subject);
        
        gradeRepository.save(grade);
        gradeRepository.save(grade2);
        gradeRepository.save(grade3);
        gradeRepository.save(grade4);
        
        graduationClassRepository.save(graduationClass);
        
        studentRepository.save(student_m);
        studentRepository.save(student_j);
        
        entityManager.getTransaction().commit();
        entityManager.detach(student_m);
        entityManager.detach(student_j);
        
        // on vérifie que l'objet existe
        assertThat(student_m).isNotNull();
        assertThat(student_j).isNotNull();
        
        ArrayList<Student> LesEleves = new ArrayList<Student>(studentRepository.findStudentHavingGradeAverageAbove(5));
        
        // Vérifie si il y a bien 1 seul student dans les élèves qui ont une moyenne au dessus de 5.
    	assertThat(LesEleves.size()).isEqualTo(1);
    	
    	// Le test ne doit pas passer avec cette valeur.
    	// assertThat(LesEleves.size()).isEqualTo(2);
    	
    	// On prend l'élève
    	Student st = LesEleves.get(0);
    	    	
    	// On vérifie que ce soit bien Manu ( seul student avec une moyenne au dessus de 5)
    	assertThat(st.getFirstName()).isEqualTo("Manu");
    	
    	// Vérification que getAll <GraduationClass> fonctionne 
    	System.out.println(studentRepository.getAll().get(0).getFirstName());
    }
    
    @Test
    void shouldDeleteStudent() {
  	  	
	  	// Création d'une classe
	  	final var graduationClass = Fixtures.createClass();
	  	
	  	// Création d'un student
	  	final var student = Fixtures.createStudent(graduationClass);
	  	student.setFirstName("Manu");
	  	
  	  
        System.out.println(student.getFirstName());
        
        entityManager.getTransaction().begin();
        
        // Persite dans la base
        graduationClassRepository.save(graduationClass);
        studentRepository.save(student);
        
        entityManager.getTransaction().commit();
        studentRepository.delete(student);

        //Récupère l'étudiant 
        var pStudent = studentRepository.findById(student.getId());
        //Vérifie si il est null
        assertThat(pStudent).isNull();
    }
    
    @Test
    void shouldGetAllStudents() {
            // Création d'une classe
            final var graduationClass = Fixtures.createClass();
              
            // Création d'un student
            final var student = Fixtures.createStudent(graduationClass);
            
            student.setFirstName("Manu");
            
            // Création d'un student
            final var student2 = Fixtures.createStudent(graduationClass);
            
            student2.setFirstName("Justin");
            
            // Création d'un student
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

            //Récupère l'étudiant 
            List<Student> LesStudents = studentRepository.getAll();
            
            //Vérifie si le prénom est bon pour le premier étudiant récupéré
            assertThat(LesStudents.get(0).getFirstName()).isEqualTo(student.getFirstName());
            //Vérifie si le prénom est bon pour le second étudiant récupéré
            assertThat(LesStudents.get(1).getFirstName()).isEqualTo(student2.getFirstName());
            //Vérifie si le prénom est bon pour le troisième étudiant récupéré
            assertThat(LesStudents.get(2).getFirstName()).isEqualTo(student3.getFirstName());
        }
}
