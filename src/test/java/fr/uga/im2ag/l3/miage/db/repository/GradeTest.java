package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;
import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    	final var subject = Fixtures.createSubject();   	
        final var grade = Fixtures.createGrade(subject);
        grade.setValue((float)5);
        
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        System.out.println("1" + grade.getId());

        // persiste grade avec value = 5 
        entityManager.getTransaction().commit();
        entityManager.detach(grade);
        Grade gradeUpdate = gradeRepository.findById(grade.getId());

        entityManager.getTransaction().begin();
        System.out.println("1" + gradeUpdate.getId());
        // modifie la value a 7
        gradeUpdate.setValue((float)7);
        gradeRepository.save(gradeUpdate);
        entityManager.getTransaction().commit();

        //la value est quand meme update
        Grade gradeUpdated = gradeRepository.findById(gradeUpdate.getId());
        System.out.println("2" + gradeUpdated.getId());
        
        System.out.println(gradeUpdated.getValue());
        // tente de mettre a jour   - ne met pas la valeur a jour car value n'est pas modifiable
        

//        entityManager.detach(subject);
//         entityManager.detach(grade);
//        Grade pGrade = gradeRepository.findById(grade.getId());
//        assertThat(pGrade.getValue()).isEqualTo(5);
//     
        
        // TODO, ici tester que la mise Ã  jour n'a pas eu lieu.
    	 
    }

    @Test
    void shouldFindHighestGrades() {
    	
    	final float  limit = 3;
        final int limit_int = 3;
    	final var subject = Fixtures.createSubject();
    	
        //System.out.println(subject.getName());
    	
        final var grade = Fixtures.createGrade(subject);
        grade.setValue((float)4.4);
        
        /*System.out.println(grade.getWeight());
        System.out.println(grade.getValue());*/
        
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        
        entityManager.getTransaction().commit();
        entityManager.detach(grade);
        entityManager.detach(subject);
        ArrayList<Grade> LesNotes = new ArrayList<Grade>(gradeRepository.findHighestGrades(limit_int)); // problem 
               
        // Vérifie si il y a bien 1 note
    	assertThat(LesNotes.size()).isEqualTo(1);
    	
    	// On prend la note
    	Grade g = LesNotes.get(0);
    	    	
    	// On vérifie si la note est plus grande que la limite
    	assertThat(g.getValue()).isGreaterThan(limit);
    
    	// Vérification que getAll <grade> fonctionne 
    	System.out.println(gradeRepository.getAll().get(0).getValue());
    }

    @Test
    void shouldFindHighestGradesBySubject() {
        // TODO
        final var subject = Fixtures.createSubject();
        subject.setName("Physics");
        subject.setHours((float) 5.0);
        
        // Create a date object
        Date d = new Date(2022,3,3);
        subject.setStart(d);
        subject.setPoints(5);
        subject.setEnd(d);
        
        final var grade_subject = Fixtures.createGrade(subject);
        grade_subject.setValue((float)4.4);
        
        
        final var subject_2 = Fixtures.createSubject();
        subject.setName("APO");
        subject.setHours((float) 2.0);
        
        // Create a date object
        Date data = new Date(2022,2,28);
        subject.setStart(data);
        subject.setPoints(5);
        subject.setEnd(data);  
        
        final var grade_subject_2 = Fixtures.createGrade(subject_2);
        grade_subject_2.setValue((float)4.8);
        
        
        entityManager.getTransaction().begin();
        
        subjectRepository.save(subject);
        subjectRepository.save(subject_2);
        
        gradeRepository.save(grade_subject);
        gradeRepository.save(grade_subject_2);
        
        entityManager.getTransaction().commit();
        entityManager.detach(grade_subject);
        entityManager.detach(grade_subject_2);
        entityManager.detach(subject);
        entityManager.detach(subject_2);
        
        ArrayList<Grade> LesNotes_subject = new ArrayList<Grade>(gradeRepository.findHighestGradesBySubject(4, subject));
        ArrayList<Grade> LesNotes_subject_2 = new ArrayList<Grade>(gradeRepository.findHighestGradesBySubject(4, subject_2));
           
        // Vérifie si il y a bien 1 note pour chaque sujet
    	assertThat(LesNotes_subject.size()).isEqualTo(1);
    	assertThat(LesNotes_subject_2.size()).isEqualTo(1);
    	
    	// On prend la note de chaque sujet
    	Grade g_subject = LesNotes_subject.get(0);
    	Grade g_subject_2 = LesNotes_subject_2.get(0);
    	
    	
    	// On vérifie si la note est plus grande que la limite
    	assertThat(g_subject.getValue()).isGreaterThan(4);
    	assertThat(g_subject_2.getValue()).isGreaterThan(4);
    
    	// Vérification que getAll <grade> fonctionne 
    	System.out.println(gradeRepository.getAll().get(0).getValue());
    }
    
    @Test
    void shouldGetAllGrade() {
            // Initialisation d'une liste N grade    	
    		ArrayList<Grade> lesGrades = new ArrayList<Grade>();
            final var subject = Fixtures.createSubject();
            entityManager.getTransaction().begin();
            //save un subject
            subjectRepository.save(subject);
    		for(int i = 0; i < 10; i++) {
                final var grade = Fixtures.createGrade(subject);
                lesGrades.add(grade);
                gradeRepository.save(grade);

    		}
    		entityManager.getTransaction().commit();
    		for(int i = 0; i < lesGrades.size(); i++) {
                entityManager.detach(lesGrades.get(i));
    		}
    		
    		ArrayList<Grade> plesGrades = new ArrayList<Grade>();
    		plesGrades = new ArrayList(gradeRepository.getAll());
    		assertThat(plesGrades).isNotEmpty();
    		assertThat(plesGrades.size()).isEqualTo(lesGrades.size());
    		for(int i = 0; i < plesGrades.size(); i++) {
               assertThat(plesGrades.get(i).getId()).isEqualTo(lesGrades.get(i).getId());
               assertThat(plesGrades.get(i).getValue()).isEqualTo(lesGrades.get(i).getValue());
               assertThat(plesGrades.get(i).getSubject().getId()).isEqualTo(subject.getId());
    		}
    		
    }
    
    @Test
    void shouldDeleteGrade() {
    	  final var subject = Fixtures.createSubject();
    	  Grade grade = Fixtures.createGrade(subject);
          entityManager.getTransaction().begin();
          //save un subject
          subjectRepository.save(subject);
          gradeRepository.save(grade);
          entityManager.getTransaction().commit();
          
          gradeRepository.delete(grade);
          
          Grade pGrade = gradeRepository.findById(grade.getId());
          assertThat(pGrade).isNull();
          
    }
    

}
